/************************************************************************************

    main.c
    Worcester Gompei Park

    Written by Zen Dignan <zen@dignan.dev>

    "I don't know why, I don't want to know why, I shouldn't have to wonder why"
    -- Anonymous Valve Game Developer, Team Fortress 2 Source Code Leak

*************************************************************************************/

#define NUM_BASEBALL 36
#define BASEBALL_SIZE 18
#define NUM_FOOTBALL 44
#define FOOTBALL_SIZE 22
#define NUM_RUGBY 60
#define RUGBY_SIZE 2
#define MAX_FIELD 30
#define PLAYER_TURNS 2
#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <unistd.h>
#include <assert.h>
#include <string.h>

// baseball functions
void *baseball_player(void *args);
void baseball(int id);
int join_queue_bb();
void leave_queue_bb();

// football functions
void *football_player(void *args);
void football(int id);
int join_queue_fb();
void leave_queue_fb();

// rugby functions
void *rugby_player(void *args);
void rugby(int id);
void leave_active_rb();
int join_active_rb();
void pair_up();

// generic functions
int random_integer(int hi, int lo);
int generate_id();
void acquire_field();
void release_field();

int bb_counter = 0;
int bb_full = 0;
int bb_play = 0;

int fb_counter = 0;
int fb_full = 0;
int fb_play = 0;

int rb_ready_counter = 0;
int rb_active_counter = 0;
int rb_even = 0;
int rb_trip = 0;
int rb_time;


int id_counter = 1;
int game_time;

pthread_mutex_t bb_queue = PTHREAD_MUTEX_INITIALIZER;
pthread_mutex_t bb_count_lock = PTHREAD_MUTEX_INITIALIZER;

pthread_mutex_t fb_queue = PTHREAD_MUTEX_INITIALIZER;
pthread_mutex_t fb_count_lock = PTHREAD_MUTEX_INITIALIZER;

pthread_mutex_t rb_setup = PTHREAD_MUTEX_INITIALIZER;
pthread_mutex_t rb_ready_lock = PTHREAD_MUTEX_INITIALIZER;
pthread_mutex_t rb_active_counter_lock = PTHREAD_MUTEX_INITIALIZER;
pthread_mutex_t rb_trip_lock = PTHREAD_MUTEX_INITIALIZER;

pthread_cond_t rb_ready_signal = PTHREAD_COND_INITIALIZER;
pthread_cond_t rb_active_emptied = PTHREAD_COND_INITIALIZER;

pthread_mutex_t field_open = PTHREAD_MUTEX_INITIALIZER;
pthread_mutex_t id_lock = PTHREAD_MUTEX_INITIALIZER;

int main(int argc, char* argv[]) {
    puts("[Main] Welcome to Worcester Gompei Park!");
    puts("[Main] Initializing field...");
    if (argc > 2 && strcmp(argv[1], "--seed") == 0) {
        int seed = atoi(argv[2]);
        srand(seed);
        printf("[Main] A custom seed was set as %d.\n", seed);
    } else {
        srand(time(NULL)); // otherwise, become truly random
    }
    int total_threads = NUM_BASEBALL + NUM_FOOTBALL + NUM_RUGBY;
    pthread_t tid[total_threads];
    int ticker = 0;

    puts("[Main] Initialization complete. Summoning players...");

    for (int player = 0; player < NUM_BASEBALL; player++) {
        pthread_create(&tid[ticker], NULL, baseball_player, NULL);
        ticker++;
    }

    for (int player = 0; player < NUM_FOOTBALL; player++) {
        pthread_create(&tid[ticker], NULL, football_player, NULL);
        ticker++;
    }

    for (int player = 0; player < NUM_RUGBY; player++) {
        pthread_create(&tid[ticker], NULL, rugby_player, NULL);
        ticker++;
    }

    puts("[Main] Finished player creation. Now waiting on all players...");

    for (int id = 0; id < total_threads; id++) {
        pthread_join(tid[id], NULL);
    }

    puts("[Main] All players have completed.");

    return 0;
}

/************************************************************************************

BASEBALL & FOOTBALL

The baseball and football functions are identical due to functioning the same but
with different counter variables, queues, and print statements.

First threads start in a "player" function that serves to simply allow them to
acquire an ID, unique sleep time, and handle their turn-count. They loop between
sleeping, playing, and decrementing their turn until their remaining turns are 0.

While in the playing funciton, both sports use a queue-up system where once the last
player joins, it will try to acquire the field, determine a game-time, and start a
game. When the game is done and all other players are gone, the last one cleans up.

*************************************************************************************/

void *baseball_player(void *args) {
    int my_turns = PLAYER_TURNS;
    int my_sleep_timer = random_integer(2, 5);
    int id = generate_id();

    do {
        sleep(my_sleep_timer);
        baseball(id);
        my_turns--;
    } while (my_turns > 0);
    return 0;
}

void baseball(int id) {

    printf("[Baseball Player ID: %d] I\'m ready to play!\n", id);

    while (bb_full || bb_play); // spin while the queue is full or while a game is playing

    pthread_mutex_lock(&bb_queue); // lock into queuing
    int slot = join_queue_bb(); // join the queue
    
    if (slot == BASEBALL_SIZE) { // if this fills the queue, coordinate the other threads
        bb_full = 1; // Fire off that the queue is full, don't allow anyone else in

        acquire_field(1); // lock the field

        game_time = random_integer(5, 10); // set the game time
        printf("[Baseball Player ID: %d] I decided we are going to start playing now! Activating the game...\n", id);
        bb_play = 1; // activate other threads
        printf("[Baseball Player ID: %d] I\'m playing in slot %d! (Sleeping for %d seconds...)\n", id, slot, game_time);
        sleep(game_time); // play
        printf("[Baseball Player ID: %d] I\'m all done playing! Leaving...\n", id);
        leave_queue_bb(); // leave the queue and mark self as done
        while (bb_counter > 0); // spin until everything else is done
        printf("[Baseball Player ID: %d] The game should be over now.\n", id);

        /** Clean up the field and get ready for the next game or sport. **/

        bb_full = 0; // the queue is no longer full
        bb_play = 0; // playing is done
        release_field(1); // unlock the field
        pthread_mutex_unlock(&bb_queue); // unlock the queue
    } else { // otherwise, wait for the queue to fill up
        pthread_mutex_unlock(&bb_queue); // Queue handling is done
        while (!bb_play); // spin until someone activates the game
        printf("[Baseball Player ID: %d] I\'m playing in slot %d! (Sleeping for %d seconds...)\n", id, slot, game_time);
        sleep(game_time); // play
        printf("[Baseball Player ID: %d] I\'m all done playing! Leaving...\n", id);
        leave_queue_bb(); // dequeue
    }
}

void *football_player(void *args) {
    int my_turns = PLAYER_TURNS;
    int my_sleep_timer = random_integer(2, 5);
    int id = generate_id();

    do {
        sleep(my_sleep_timer);
        football(id);
        my_turns--;
    } while (my_turns > 0);
    return 0;
}

void football(int id) {
    printf("[Football Player ID: %d] I\'m ready to play!\n", id);

    while (fb_full || fb_play); // spin while the queue is full or while a game is playing

    pthread_mutex_lock(&fb_queue); // lock into queuing
    int slot = join_queue_fb(); // enqueue

    if (slot == FOOTBALL_SIZE) { // if this fills the queue, coordinate the other threads
        fb_full = 1; // Fire off that the queue is full, don't allow anyone else in

        acquire_field(2); // lock the field

        game_time = random_integer(5, 10); // set the game time
        printf("[Football Player ID: %d] I decided we are going to start playing now! Activating the game...\n", id);
        fb_play = 1; // activate other threads
        printf("[Football Player ID: %d] I\'m playing in slot %d! (Sleeping for %d seconds...)\n", id, slot, game_time);
        sleep(game_time); // play
        printf("[Football Player ID: %d] I\'m all done playing! Leaving...\n", id);
        leave_queue_fb();
        while (fb_counter > 0); // spin until everything else is done
        printf("[Football Player ID: %d] The game should be over now.\n", id);


        /** Clean up the field and get ready for the next game or sport. **/

        fb_full = 0; // the queue is no longer full
        fb_play = 0; // playing is done
        release_field(2); // unlock the field
        pthread_mutex_unlock(&fb_queue); // unlock the queue
    } else { // otherwise, wait for the queue to fill up
        pthread_mutex_unlock(&fb_queue); // Queue handling is done
        while (!fb_play); // spin until someone activates the game
        printf("[Football Player ID: %d] I\'m playing in slot %d! (Sleeping for %d seconds...)\n", id, slot, game_time);
        sleep(game_time); // play
        printf("[Football Player ID: %d] I\'m all done playing! Leaving...\n", id);
        leave_queue_fb(); // dequeue
    }
}

/************************************************************************************

RUGBY

Rugby initially was the most complex, and yet through refactoring, became the
simplest! I'm quite proud of this frankly.

Rugby players start in a player function that is exactly like the baseball and
football player threads, refer to the comment for them for more info.

Within the rugby function, players will set their own gametime, partner up,
and then start to play as soon as they can. Once done, they leave the field.

If a player is first, they will claim the field via a condition variable. This
is because if they were to use a lock, it would be complex to pass this lock to
another rugby thread without loosing it to another sport. By using a variable,
all rugby threads would continue to set the variable to "true", and only had to
worry about the lock from other sports.

If a player is last, they will broadcast that the rugby team is empty (allowing
for further queueing) and then release the field for the next sport, which is part
of the fairness mechanism.

*************************************************************************************/

void *rugby_player(void *args) {
    int my_turns = PLAYER_TURNS;
    int my_sleep_timer = random_integer(2, 5);
    int id = generate_id();
    
    do {
        sleep(my_sleep_timer);
        rugby(id);
        my_turns--;
    } while (my_turns > 0);
    return 0;
}

void rugby(int id) {
    int is_even;
    int my_play_time;

    pthread_mutex_lock(&rb_setup);
    is_even = rb_even;
    if (!is_even) {
        rb_time = random_integer(2, 12);
    }
    my_play_time = rb_time; // localize the time
    rb_even = !rb_even; // flip partner bit for timing
    int slot = join_active_rb(); // enqueue
    pthread_mutex_unlock(&rb_setup);

    pair_up(); // get players paired up
    printf("[Rugby Player ID: %d] I\'m playing in slot %d! (Sleeping for %d seconds...)\n", id, slot, my_play_time);
    sleep(my_play_time); // play the game
    printf("[Rugby Player ID: %d] I\'m all done playing! Leaving...\n", id);
    leave_active_rb(); // dequeue
}

// ID generation handler (was also helpful for grasping how locking worked)
int generate_id() {
    int id;
    pthread_mutex_lock (&id_lock);
    id = id_counter;
    id_counter++;
    pthread_mutex_unlock (&id_lock);
    return id;
}

// helper function to generate random numbers
int random_integer(int lo, int hi) {
    return (rand() % (hi - lo + 1)) + lo;
}

// increment queued baseball players and return their slot
int join_queue_bb() {
    int slot;
    pthread_mutex_lock(&bb_count_lock);
    bb_counter++;
    slot = bb_counter;
    pthread_mutex_unlock(&bb_count_lock);
    return slot;
}
// decrement queued baseball players
void leave_queue_bb() {
    pthread_mutex_lock(&bb_count_lock);
    bb_counter--;
    pthread_mutex_unlock(&bb_count_lock);
}

// increment queued football players and return their slot
int join_queue_fb() {
    int slot;
    pthread_mutex_lock(&fb_count_lock);
    fb_counter++;
    slot = fb_counter;
    pthread_mutex_unlock(&fb_count_lock);
    return slot;
}

// decrement queued football players
void leave_queue_fb() {
    pthread_mutex_lock(&fb_count_lock);
    fb_counter--;
    pthread_mutex_unlock(&fb_count_lock);
}

// handle pairing up two rugby players
void pair_up() {
    pthread_mutex_lock(&rb_ready_lock);
    rb_ready_counter++;
    assert (rb_ready_counter <= 2);
    assert (rb_ready_counter >= 0);
    if (rb_ready_counter < 2) {
        pthread_cond_wait(&rb_ready_signal, &rb_ready_lock);
    } else if (rb_ready_counter == 2) {
        pthread_cond_signal(&rb_ready_signal);
        rb_ready_counter = 0;
    }
    pthread_mutex_unlock(&rb_ready_lock);
}

// increment active rugby players and return their slot
int join_active_rb() {
    int slot;
    pthread_mutex_lock(&rb_active_counter_lock);
    if (rb_active_counter >= MAX_FIELD) {
        pthread_cond_wait(&rb_active_emptied, &rb_active_counter_lock);
    }
    rb_active_counter++;
    slot = rb_active_counter;
    if (slot == 1) {
        acquire_field(3);
    }
    pthread_mutex_unlock(&rb_active_counter_lock);
    return slot;
}
// decrement active rugby players and handle emptying the field
void leave_active_rb() {
    pthread_mutex_lock(&rb_active_counter_lock);
    assert(rb_active_counter > 0);
    rb_active_counter--;
    if (rb_active_counter == 0) {
        pthread_cond_broadcast(&rb_active_emptied);
        release_field(3);
    }
    pthread_mutex_unlock(&rb_active_counter_lock);
}

// lock the field (complicated to do for rugby)
void acquire_field(int sport) {
    pthread_mutex_lock(&field_open);
    if (sport == 3) {
        pthread_mutex_lock(&rb_trip_lock);
        rb_trip = 1;
        pthread_mutex_unlock(&rb_trip_lock);
        pthread_mutex_unlock(&field_open);
    } else {
        while (rb_trip);
    }
}

// unlock the field (complicated to do for rugby)
void release_field(int sport) {
    if (sport == 3) {
        pthread_mutex_lock(&rb_trip_lock);
        rb_trip = 0;
        pthread_mutex_unlock(&rb_trip_lock);
    } else {
        pthread_mutex_unlock(&field_open);
    }
}