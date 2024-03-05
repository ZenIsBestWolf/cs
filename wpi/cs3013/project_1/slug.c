#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>

int rng (int, int);

int main(int argc, char* args[]) {
    int choice = atoi(args[1]);
    if (choice < 1 || choice > 4) {
        printf("[ERROR] INVLAID ARGUMENT PROVIDED: %d\n", choice);
        exit(1);
    }
    char path[16];
    snprintf(path, 16, "seed_slug_%d.txt", choice);
    FILE* seedFile = fopen(path, "r");
    int seed;
    fscanf(seedFile, "%d", &seed);
    fclose(seedFile);
    srand(seed);
    int mypid = (int) getpid();
    printf("[Slug PID: %d] Read seed value (converted to integer) %d\n", mypid, seed);

    int seconds = rng(2, 6);
    int coin = rng(1, 2) % 2;

    printf("[Slug PID: %d] Delay is %d, coin is %d\n", mypid, seconds, coin);

    sleep(seconds);

    if (coin) {
        printf("[Slug PID: %d] Running last command...\n", mypid);
        char* args[4] = {"last", "-i", "-x", NULL};
        execvp("last", args);
        exit(0);
    } else {
        printf("[Slug PID: %d] Running id command...\n", mypid);
        char* args[3] = {"id", "--group", NULL};
        execvp("id", args);
        exit(0);
    }
    return 0;
}

/**
 * Generates a random number 
*/
int rng (int lower, int upper) {
    return (rand() % (upper - lower + 1)) + lower;
}