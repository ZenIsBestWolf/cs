#include <stdio.h>                     /* for printf() and fprintf() */
#include <sys/socket.h>                /* for socket(), bind(), and connect() */
#include <arpa/inet.h>                 /* for sockaddr_in and inet_ntoa() */
#include <stdlib.h>                    /* for atoi() and exit() */
#include <string.h>                    /* for memset() */
#include <unistd.h>                    /* for close() */
#include <time.h>                      /* for time() */
#include <sys/types.h>
#include <errno.h>
void shout(char*, char*);              /* for custom logging */
void DieWithError(char*);              /* for erroring */
