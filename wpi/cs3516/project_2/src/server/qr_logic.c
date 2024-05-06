#include "../common/common.h"
#include <pthread.h>
#define RCVBUFSIZE 32                  /* Size of receive buffer */
#define MAX_IMAGE_SIZE 10000          /* maximum bytes for image, currently set to 10KB */
void HandleQR(int, char*, char*);
typedef struct {
    int* client_socket;
    char* client_addr;
    int* uid;
} qr_argument_struct;

void* HandleThread(void* arguments) {
    qr_argument_struct *args = arguments;
    char uid[2];
    snprintf(uid, 2, "%d", *(args->uid));
    char command[30];
    snprintf(command, 30, "mkdir -p %s", uid);
    system(command);
    HandleQR(*(args->client_socket), args->client_addr, uid);
    char removal[30];
    snprintf(removal, 30, "rm -r %s", uid);
    system(removal);
    fflush(stdout);
    return 0;
}

void HandleQR(int client_socket, char* client_addr, char* uid) {
    char input_buffer[RCVBUFSIZE]; /* Buffer for echo string */
    int message_size;              /* Size of received message */
    u_int32_t image_size = -1;     /* size of image */
    int cursor = -2;
    int invalid = 0;
    /* Receive message from client */
    char* byte_stream;
    if ((message_size = recv(client_socket, input_buffer, 10, 0)) < 0) DieWithError("recv() failed?"); // read 10 bytes only
    /* Send received string and receive again until end of transmission */
    while (message_size > 0 && cursor < image_size) { /* zero indicates end of transmission */
        if (image_size == -1) {
            char* size_buffer = (char *) malloc(11 * sizeof(char));
            for (int i = 0; i < 10; i++) {
                size_buffer[i] = input_buffer[i];
            };
            image_size = atoi(size_buffer);
            cursor = 0;
            shout(client_addr, "Reported image size: ");
            printf("%u\n", image_size);
            if (image_size > MAX_IMAGE_SIZE) {
                invalid = 1;
                break;
            }
            byte_stream = (char *) malloc(image_size * sizeof(char));
            message_size = RCVBUFSIZE;
        } else {
            for (int i = 0; i < RCVBUFSIZE; i++) {
                byte_stream[cursor] = input_buffer[i];
                cursor++;
                if (cursor == image_size) break; // get out if max has been read
            }
        }
        /* toss old data: */
        memset(&input_buffer, 0, sizeof(input_buffer));
        /* See if there is more data to receive */
        if (message_size < RCVBUFSIZE) break; // all done reading data!
        if ((message_size = recv(client_socket, input_buffer, RCVBUFSIZE, 0)) < 0) DieWithError("recv() failed?");
    }

    if (invalid) {
        shout("127.0.0.1", "Rejecting image from ");
        printf("%s because size %d is too large.\n", client_addr, image_size);
        char* return_code = "1";
        shout("127.0.0.1", "Sending code");
        printf(" %s...\n", return_code);
        if (send(client_socket, return_code, strlen(return_code), 0) != strlen(return_code)) {
            shout("127.0.0.1", "Failed to send buffer.\n");
            printf("send() returned a different number of bytes than expected.\n");
            close(client_socket);
            exit(1);
        }
        shout("127.0.0.1", "Done. Closing socket.\n");
        close(client_socket);
        return;
    }

    char img_path[13];
    snprintf(img_path, 13, "%s/output.png", uid);
    char result_path[13];
    snprintf(result_path, 13, "%s/result.txt", uid);
    FILE* streamed = fmemopen(byte_stream, image_size, "rb");
    FILE* out_png = fopen(img_path, "w");
    int byte;
    while ((byte = fgetc(streamed)) != EOF) {
        fputc(byte, out_png);
    }
    fclose(streamed);
    fclose(out_png);
    char command[1024];
    snprintf(command, 1024, "java -cp javase.jar:core.jar com.google.zxing.client.j2se.CommandLineRunner %s > %s", img_path, result_path);
    system(command);
    FILE* result_file = fopen(result_path, "r");
    char portion[1024];
    char url[1024];
    int no_qr = 1;
    while (fgets(portion, 1024, result_file) > 0) {
        if (strcmp("Parsed result:\n", portion) != 0) continue;
        fscanf(result_file, "%s", url);
        no_qr = 0;
        break;
    }
    fclose(result_file);
    char* return_code = "0";
    if (no_qr) {
        return_code = "1";
        shout("127.0.0.1", "Error: No QR code found.\n");
    }
    shout("127.0.0.1", "Sending code");
    printf(" %s...\n", return_code);
    if (send(client_socket, return_code, strlen(return_code), 0) != strlen(return_code)) {
        shout("127.0.0.1", "Failed to send buffer.\n");
        printf("send() returned a different number of bytes than expected.\n");
        close(client_socket);
        exit(1);
    }
    if (no_qr) {
        shout("127.0.0.1", "Done. Closing socket.\n");
        close(client_socket);
        return;
    }
    shout("127.0.0.1", "Done. Sending URL");
    printf(" \"%s\"...\n", url);
    if (send(client_socket, url, strlen(url), 0) != strlen(url)) {
        shout("127.0.0.1", "Failed to send buffer.\n");
        printf("send() returned a different number of bytes than expected.\n");
        close(client_socket);
        exit(1);
    }
    shout("127.0.0.1", "Done. Closing socket.\n");
    close(client_socket); /* Close client socket */
}
