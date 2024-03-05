#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>

int rng (int, int);

int main(int argc, char* argv[]) {
    FILE* seedFile = fopen("seed.txt", "r");
    int seed;
    fscanf(seedFile, "%d", &seed);
    fclose(seedFile);
    srand(seed);

    printf("Read seed value (converted to an integer): %d\n", seed);

    int randomNumber = rng(8, 13);
    printf("Spawning %d children...\n", randomNumber);
    for (int i = 0; i < randomNumber; i++) {
        int child = fork();
        int mypid = (int) getpid();
        if (child < 0) {
            puts("Illegal child! Get out!");
            exit(1);
        }
        int sleepTime = rng(1, 3);
        int exitCode = rng(1, 50);
        if (child == 0) {
            printf("[Child, PID: %d] Hi there, I am child! I\'m taking a nap for %d seconds, and exiting with %d. Goodnight...\n", mypid, sleepTime, exitCode);
            sleep(sleepTime);
            printf("[Child, PID: %d] Good morning! I'm all done now. Exiting...\n", mypid);
            exit(exitCode);
        } else {
            printf("[Parent] I am a parent waiting on PID %d\n", child);
            int childCode;
            waitpid(child, &childCode, 0);
            printf("[Parent] Child %d exited with code %d.\n", child, WEXITSTATUS(childCode));
        }
    }
    return 0;
}

int rng (int lower, int upper) {
    return (rand() % (upper - lower + 1)) + lower;
}