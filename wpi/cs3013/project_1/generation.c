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

    int lifespan = rng(5, 12);
    int mypid = (int) getpid();
    printf("[Parent, PID: %d] Starting lifespan at %d\n", mypid, lifespan);

    while (lifespan > 0) {
        int child = fork();
        int childCode;
        mypid = (int) getpid();
        if (child == 0) {
            printf("[Child, PID: %d] Called with lifespan %d, and will decrement lifespan.\n", mypid, lifespan);
            lifespan--;
            if (lifespan < 0) {
                printf("[Child, PID: %d] Lifespan is over! Exiting...", mypid);
                exit(0);
            }
        } else {
            printf("[Parent, PID: %d] I am a parent waiting on PID %d\n", mypid, child);
            waitpid(child, &childCode, 0);
            int childExitCode = WEXITSTATUS(childCode);
            printf("[Parent, PID: %d] Child %d exited with code %d. Exiting with code %d...\n", mypid, child, childExitCode, childExitCode + 1);
            exit(childExitCode + 1);
        }
    }

    return 0;
}
/**
 * Generates a random number 
*/
int rng (int lower, int upper) {
    return (rand() % (upper - lower + 1)) + lower;
}