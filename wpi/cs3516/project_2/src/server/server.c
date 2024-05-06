/****************
 * server.c
 * by Zen Dignan <zen@dignan.dev>
*****************/

#include "../common/common.h"
#include <pthread.h>
void HandleQR(int, char*);
void* HandleThread(void*);
pthread_mutex_t user_lock = PTHREAD_MUTEX_INITIALIZER;

typedef struct {
    int* client_socket;
    char* client_addr;
    int* uid;
} qr_argument_struct;

int main(int argc, char* argv[]) {
    shout("127.0.0.1", "Parsing arguments...\n");
    int port = 2012;        /* port number */
    int rate_requests = 3;  /* number of requests */
    int rate_time = 60;     /* seconds for rate window */
    int max_users = 3;      /* max connections */
    int timeout = 80;       /* seconds to wait for timeout */
    int current_users = 0;  /* current connections and slot */
    if (argc > 1) {
        port = atoi(argv[1]);
    }
    if (argc > 2) {
        rate_requests = atoi(argv[2]);
    }
    if (argc > 3) {
        rate_time = atoi(argv[3]);
    }
    if (argc > 4) {
        max_users = atoi(argv[4]);
    }
    if (argc > 5) {
        timeout = atoi(argv[5]);
    }
    printf("port: %d, rate_requests: %d, rate_time: %d, max_users: %d, timeout: %d\n", port, rate_requests, rate_time, max_users, timeout);
    pthread_t clients[max_users];
    shout("127.0.0.1", "Arguments parsed. Creating socket...\n");
    int server_socket = socket(PF_INET, SOCK_STREAM, IPPROTO_TCP);
    if (server_socket < 0) DieWithError("Socket failed to open.\n");
    shout("127.0.0.1", "Created socket.\n");
    struct sockaddr_in server_addr;
    struct sockaddr_in client_addr;
    memset(&server_addr, 0, sizeof(server_addr));
    memset(&client_addr, 0, sizeof(client_addr));
    server_addr.sin_family = AF_INET;
    server_addr.sin_addr.s_addr = htonl(INADDR_ANY);
    server_addr.sin_port = htons(port);

    shout("127.0.0.1", "Binding...\n");
    if (bind(server_socket, (struct sockaddr *) &server_addr, sizeof(server_addr)) < 0) DieWithError("Failed to bind!");
    shout("127.0.0.1", "Bound to port.\n");
    shout("127.0.0.1", "Listening for incoming connections...\n");
    if (listen(server_socket, max_users) < 0) {
        shout("127.0.0.1", "Failed to listen.\n");
        printf("Got non-zero value when expected 0.\n");
        exit(1);
    }

    for (;;) {
        unsigned int client_length = sizeof(client_addr);
        int client_socket = accept(server_socket, (struct sockaddr *) &client_addr, &client_length);
        if (client_socket < 0) DieWithError("Failed to accept.");
        int uid;

        pthread_mutex_lock(&user_lock);
        shout(inet_ntoa(client_addr.sin_addr), "Connected.\n");
        qr_argument_struct *args = malloc(sizeof *args);
        args->client_socket = &client_socket;
        args->client_addr = inet_ntoa(client_addr.sin_addr);
        uid = current_users;
        args->uid = &uid;
        current_users++;
        pthread_mutex_unlock(&user_lock);
        pthread_create(&clients[uid], NULL, HandleThread, args);
    }
    return 0;
}
