all: prep server client
prep:
	mkdir -p bin
server: prep src/server/qr_logic.c src/server/server.c src/common/common.c
	gcc -o bin/server src/server/qr_logic.c src/server/server.c src/common/common.c
client: prep src/client/client.c src/common/common.c
	gcc -o bin/client src/client/client.c src/common/common.c
cleanup:
	rm -r bin
