Project 2: Worcester Gompei Park
by Zen Dignan <zen@dignan.dev>

NOTICE: This implementation of Worcester Gompei Park is known to cause anxiety in players
due to the stressful nature of needing to sit and watch painfully as other players have fun
and play a game. If you or another thread you know is at risk for anxiety, talk to your doctor.
(Threads busy-wait in this implementation. Sorry.)

Compilation:

Just run "make", assuming your system is configured with a C compiler and has make.
A file called "wgp" will be produced. This file is compiled with "-g" and can be used
with GDB if you so choose.

Execution:

Run "./wgp" in your terminal to run the simulation as default.
To set a specific seed, add "--seed" argument and then your seed.
For example, to run with seed "3013" run: "./wgp --seed 3013"
Only integer seeds are accepted. An invalid entry will be treated as 0.

Files:

- main.c: Contains all major code for the project
- Makefile: Simplifies compilation
- problem_explanation.txt: My explanation of how I solved the WGP problem
- plan.txt: My breakdown of the project assignment
- output_02152004.txt: Output generated with a seed made from my birthday.
- output_3013.txt: Output generated with a seed from the course number.
- output_3621.txt: Output generated with the seed number I usually test with.
- README.txt: This very document

Technical Details:

DEAR GRADER:

The author of this code finds it important for you to note the following:

- There is a consistent and descriptive naming patthen for all locks and conditions.
- All thread-critical resources are modified behind mutex locks.
- The prevention of starvation is entirely localized within the rugby players themselves.
    It was noted that sports automatically "queue" by waiting to acquire the field_open lock.
    Therefore, forcing rugby to have some respect caused the sports to take turns properly.

Thank you,
Zen

These descriptions are also found as comments within the code.
I will provide them here too for accessibility.

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