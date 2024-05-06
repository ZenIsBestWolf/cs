#include "common.h"

void shout(char* ip_addr, char* action) {
    time_t now;
    time(&now);
    char time_buf[80];
    struct tm now_struct = *localtime(&now);
    strftime(time_buf, sizeof(time_buf), "%m-%d-%Y %H:%M:%S", &now_struct);

    printf("%s %s:\t%s", time_buf, ip_addr, action);
}

void DieWithError(char* error) {
    printf("%s\n", error);
    exit(1);
}
