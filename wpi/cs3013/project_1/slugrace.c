#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>
#include <time.h>
// #include <linux/time.h> // make vscode happy

int rng (int, int);

int main(int argc, char* argv[]) {
    FILE* seedFile = fopen("seed.txt", "r");
    int seed;
    fscanf(seedFile, "%d", &seed);
    fclose(seedFile);
    srand(seed);

    int children[4];

    for (int i = 0; i < 4; i++) {
        int child = fork();
        int mypid = (int) getpid();
        if (child < 0) {
            puts("Illegal child! Get out!");
            exit(1);
        }
        if (child == 0) {
            char choice[sizeof(int)];
            snprintf(choice, sizeof(int), "%d", i + 1);
            char* args[] = {"./slug", choice, NULL};
            printf("[Child, PID %d] Spawning slug %d\n", mypid, i + 1);
            execvp("./slug", args);
            exit(0);
        } else {
            children[i] = child;
        }
    }

        struct timespec start, stop;
        double ticker;
        clock_gettime(CLOCK_REALTIME, &start);

        int remainingRacers = 4;

        while (remainingRacers > 0) {
            remainingRacers = 0;
            int code;
            int finished = 0;
            int waiter = 1;
            while (waiter > 0) {
                waiter = waitpid(-1, &code, WNOHANG);
                for (int i = 0; i < 4; i++) {
                    if (children[i] != waiter) continue;
                    if (children[i] == -1) continue;
                    children[i] = -1; // all done
                    clock_gettime(CLOCK_REALTIME, &stop);
                    ticker = stop.tv_sec - start.tv_sec + (stop.tv_nsec - start.tv_nsec)/1e9;
                    printf("Child %d has finished taking %f seconds.\n", waiter, ticker);
                    finished = 1;
                }
            }
            if (!finished) {
                usleep(330000);
            }
            char* racingChildren;
            printf("The race is ongoing. The following children are still racing:");
            for (int i = 0; i < 4; i++) {
                if (children[i] == -1) continue;
                printf(" %d", children[i]);
                remainingRacers++;
            }
            printf("\n");
        }
        clock_gettime(CLOCK_REALTIME, &stop);
        ticker = stop.tv_sec - start.tv_sec + (stop.tv_nsec - start.tv_nsec)/1e9;
        printf("The race has concluded, and took %f seconds.\n", ticker);
    return 0;
}

int rng (int lower, int upper) {
    return (rand() % (upper - lower + 1)) + lower;
}