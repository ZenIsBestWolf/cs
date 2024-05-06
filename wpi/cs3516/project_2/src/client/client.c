/****************
 * client.c
 * by Zen Dignan <zen@dignan.dev>
*****************/

#include "../common/common.h"
#define RCVBUFSIZE 32                  /* Size of receive buffer */

int main(int argc, char* argv[]) {
    if (argc < 3) {
        printf("Error: 2 arguments required (IP, port) but only got %d.\n", argc - 1);
        puts("Usage: client <Server IP> <Server Port>");
        exit(1);
    }
    shout("127.0.0.1", "Parsing arguments...\n");
    unsigned short port = (unsigned short) atoi(argv[2]);
    struct sockaddr_in server_address;
    char* formatted_ip = argv[1];
    int client_socket = socket(PF_INET, SOCK_STREAM, IPPROTO_TCP);
    shout("127.0.0.1", "Arguments parsed. Creating socket...\n");
    if (client_socket < 0) DieWithError("Socket failed.");
    shout("127.0.0.1", "Created socket.\n");
    memset(&server_address, 0, sizeof(server_address));
    server_address.sin_family = AF_INET;
    server_address.sin_addr.s_addr = inet_addr(formatted_ip);
    server_address.sin_port = htons(port);
    shout("127.0.0.1", "Connecting to server...\n");
    if (connect(client_socket, (struct sockaddr *) &server_address, sizeof(server_address)) < 0) DieWithError("Connection failed.");
    shout("127.0.0.1", "Successfully connected.\n");

    FILE* qr_file;
    char* qr_buffer;
    unsigned int qr_size;

    qr_file = fopen("qr.png", "r");
    fseek(qr_file, 0, SEEK_END);
    qr_size = ftell(qr_file);
    rewind(qr_file);
    unsigned long qr_buff_size = qr_size * sizeof(char) + 1;
    qr_buffer = (char *) malloc(qr_buff_size);
    int byte;
    int i = 0;
    while ((byte = fgetc(qr_file)) != EOF) {
        qr_buffer[i] = (char) byte;
        i++;
    }
    qr_buffer[i] = '\0';
    fclose(qr_file);

    shout("127.0.0.1", "Sending size...\n");
    char* size_buffer = (char *) malloc(12 * sizeof(char));
    snprintf(size_buffer, 11, "%010d", qr_size);
    if (send(client_socket, size_buffer, strlen(size_buffer), 0) != strlen(size_buffer)) {
        shout("127.0.0.1", "Failed to send size.\n");
        printf("send() returned a different number of bytes than expected.\n");
        close(client_socket);
        exit(1);
    }
    shout("127.0.0.1", "Sending image with size ");
    printf("%u...\n", qr_size);
    if (send(client_socket, qr_buffer, qr_buff_size, 0) != qr_buff_size) {
        shout("127.0.0.1", "Failed to send buffer.\n");
        printf("send() returned a different number of bytes than expected.\n");
        close(client_socket);
        exit(1);
    }
    shout("127.0.0.1", "Data sent.\n");
    free(size_buffer);
    free(qr_buffer);
    sleep(1);
    shout("127.0.0.1", "Listening for reply...\n");
    char return_code[4];
    char url[1024];
    int message_size = 1;
    int cursor = 0;
    int code = -1;
    int invalid = 0;
    char input_buffer[RCVBUFSIZE];

    while (message_size > 0 && cursor < 1024) {
        if (code == -1) {
            if ((message_size = recv(client_socket, return_code, 1, 0)) <= 0) {
                close(client_socket);
                DieWithError("recv() failed or connection closed prematurely");
            }
            return_code[1] = '\0';
            code = atoi(return_code);
            if (code) {
                invalid = 1;
                break;
            }
            shout(formatted_ip, "Code 0: Success\n");
        } else {
            for (int i = 0; i < message_size; i++) {
                url[cursor] = input_buffer[i];
                cursor++;
            }
        }
        if ((message_size = recv(client_socket, input_buffer, RCVBUFSIZE, 0)) < 0) DieWithError("recv() failed?");
    }

    if (invalid) {
        if (code == 1) shout(formatted_ip, "Code 1: Payload Invalud (Too Large or No QR)\n");
        if (code == 2) shout(formatted_ip, "Code 2: Timed out\n");
        if (code == 3) shout (formatted_ip, "Code 3: Too Many Requests\n");
    } else {
        url[cursor] = '\0';
        shout(formatted_ip, "URL:");
        printf(" \"%s\"\n", url);
    }

    shout("127.0.0.1", "Closing connection. Goodbye!\n");
    close(client_socket);
    return 0;
}
