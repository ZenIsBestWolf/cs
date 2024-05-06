# Project 2: Quick Response Codes
by Zen Dignan <zen@dignan.dev>

QR Code server and client!

First, build this project with `make` and grab the `server` and `client` binaries from the `bin/` folder. Also get the two `.jar` files from `jar/`.

Designate one host as your server. Create a folder (I go for `server/`) and place the following items.

```
server/
    - server
    - core.jar
    - javase.jar
```

The server is ready to go and can be run with `./server` and will host by default on port `2012`.
Arugments for the server are: `./server [port] [request rate] [rate time] [maximum clients] [timeout]`

Designate one or more other hosts as your clients. Create a folder like before (I go with `client/`) and place the binary and a QR code to send called `qr.png` (if you don't have one, get a sample one from the `qrs/` folder).

```
client/
    - client
    - qr.png
```

The client requires arguments in the format of `./client <SERVER IP> <SERVER PORT>`
