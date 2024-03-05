#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>

int rng (int, int);

int main(void) {
    FILE* seedFile = fopen("seed.txt", "r");
    int seed;
    fscanf(seedFile, "%d", &seed);
    fclose(seedFile);
    srand(seed);

   char* paths[] = {"/home", "/proc", "/proc/sys", "/usr", "/usr/bin", "/bin"};
   int mypid = (int) getpid();

   for (int i = 0; i < 5; i++) {
    int child = fork();
    if (child < 0) {
        puts("Illegal child, aborting...");
        exit(1);
    }
    int selection = rng(1, 6) % 6;
    if (child == 0) {
        char* path = paths[selection];
        printf("[Child, PID %d] Selected: %s\n", mypid, path);
        chdir(path);
        char* args[] = {"ls", "-tr", NULL};
        char buffer[256];
        getcwd(buffer, 256);
        printf("actual: %s\n", buffer);
        printf("[Child, PID %d] Located: %s\n", mypid, buffer);
        printf("[Child, PID %d] Executing ls -tr command...", mypid);
        execvp("ls", args);
        exit(0);
    } else {
        printf("[Parent] I\'m waiting for PID %d to finish.\n", child);
        int childCode;
        waitpid(child, &childCode, 0);
        printf("[Parent] Child %d finished with code %d.\n", child, WEXITSTATUS(childCode));
        continue;
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