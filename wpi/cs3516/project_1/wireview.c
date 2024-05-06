/****************
 * wireview - Programming Project 1
 * by Zen Dignan <zen@dignan.dev>
 * 
 * Regarding Operating System Requirements:
 * I made sure this does run on the provided UTM VM image.
 * However, it was initially coded for my Apple Silicon MacBook Pro.
 * This is why there is an ifdef for __APPLE__, the ether_ntoa() function
 * is included in <net/ethernet.h> on OSX, but is inside <netinet/ether.h>
 * on Unix, this file does not exist on OSX.
 * For some reason, on the VM, __UNIX__ is not defined. This means I have to do
 * this ugly ifdef else exclusion to make it import only when not on OSX. Sorry.
 * 
 * Also no idea if this works on Windows, I don't have a Windows device I care
 * enough to fiddle with to figure out how to compile, nevermind get this running.
 * 
 * Finally, I know this file is a little sparse on comments, but I tried to name
 * my variables in a verbose manner. It should be readable without comments.
 * 
 * REGARDING THREAD SAFETY
 * 
 * This code is NOT thread safe! If you want to be thread safe:
 * The global variables used here are BAD for thread safety. Implement mutex locks.
*****************/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <pcap.h>
#include <time.h>
#include <net/ethernet.h>
#include <netinet/ip.h>
#include <netinet/if_ether.h>
#include <netinet/udp.h>
#ifdef __APPLE__
#else // ONLY import if NOT on Apple
#include <netinet/ether.h>
#endif
#define MAX_IP_ADDR_LEN 16
#define MAX_MAC_ADDR_LEN 18
#define LOCAL_MAX_ADDR_LEN 18 // weird name to avoid definition collision on linux

// All general functions and global variables
void packet_parser(u_char*, const struct pcap_pkthdr*, const u_char*);
int packet_count;
long start_sec;
long start_ns;
int max_size;
int min_size;
float avg_size;

// All packet_list related functions and global variables.
typedef struct packet_list {
    char addr_str[LOCAL_MAX_ADDR_LEN];
    int count;
    struct packet_list* next;
} packet_list;

void log_address(char*, packet_list**);
packet_list* store_addr(char*, packet_list*);
packet_list* craft_packet_list_entry(char*);
packet_list* ip_sources;
packet_list* ip_destinations;
packet_list* mac_sources;
packet_list* mac_destinations;

// All port_list related functions and global variables
typedef struct port_list {
    int port;
    int count;
    struct port_list* next;
} port_list;

void log_port(int, port_list**);
port_list* store_port(int, port_list*);
port_list* craft_port_list_entry(int);
port_list* udp_sources;
port_list* udp_destinations;

// All arp_map related functions and global variables
typedef struct arp_map {
    char ip[MAX_IP_ADDR_LEN];
    char hardware[MAX_MAC_ADDR_LEN];
    struct arp_map* next;
} arp_map;

void log_arp(char*, char*, arp_map**);
arp_map* store_arp(char*, char*, arp_map*);
arp_map* craft_arp_map_entry(char*, char*);
arp_map* arp_participants;

int main(int argc, char* argv[]) {
    if (argc < 2) {
        puts("Please specify a capture file.\nUsage: wireview ./path/to/capture.pcap");
        return 1;
    }
    puts("Welcome to Wire View! Let\'s process some packets!");
    char* path = argv[1];

    printf("Processing capture located at: %s\n", path);

    char error_buff[PCAP_ERRBUF_SIZE];
    pcap_t* capture = pcap_open_offline(path, error_buff);
    if (capture == NULL) {
        puts("pcap_open_offline returned null, error buffer:");
        puts(error_buff);
        return -1;
    }

    int link_type = pcap_datalink(capture);
    if (link_type != DLT_EN10MB && link_type != DLT_EN3MB) {
        printf("capture %s is not from ethernet", argv[1]);
        return -1;
    }

    u_char buff[BUFSIZ];
    packet_count = 0;
    min_size = INT32_MAX;
    max_size = 0;
    avg_size = 0;

    pcap_loop(capture, 0, &packet_parser, buff);
    pcap_close(capture);

    printf("Total of %d packets found\n", packet_count);
    avg_size /= packet_count;
    printf("Statistics -> MIN: %d MAX: %d AVERAGE: %.2f\n", min_size, max_size, avg_size);


    if (ip_sources != NULL) puts("\nIP Source Tree");;
    while (ip_sources != NULL) {
        printf("%s = %d\n", ip_sources->addr_str, ip_sources->count);
        ip_sources = ip_sources->next;
    }

    if (ip_destinations != NULL) puts("\nIP Destination Tree");
    while (ip_destinations != NULL) {
        printf("%s = %d\n", ip_destinations->addr_str, ip_destinations->count);
        ip_destinations = ip_destinations->next;
    }

    if (mac_sources != NULL) puts("\nMAC Source Tree");
    while (mac_sources != NULL) {
        printf("%s = %d\n", mac_sources->addr_str, mac_sources->count);
        mac_sources = mac_sources->next;
    }

    if (mac_destinations != NULL) puts("\nMAC Destination Tree");
    while (mac_destinations != NULL) {
        printf("%s = %d\n", mac_destinations->addr_str, mac_destinations->count);
        mac_destinations = mac_destinations->next;
    }

    if (udp_sources != NULL) puts("\nUDP Source Tree");;
    while (udp_sources != NULL) {
        printf("%d = %d\n", udp_sources->port, udp_sources->count);
        udp_sources = udp_sources->next;
    }

    if (udp_destinations != NULL) puts("\nUDP Destination Tree");
    while (udp_destinations != NULL) {
        printf("%d = %d\n", udp_destinations->port, udp_destinations->count);
        udp_destinations = udp_destinations->next;
    }

    if (arp_participants != NULL) puts("\nARP Participant Tree");
    while (arp_participants != NULL) {
        printf("MAC Address %s mapped to IP address %s\n", arp_participants->hardware, arp_participants->ip);
        arp_participants = arp_participants->next;
    }
    return 0;
}

void packet_parser(u_char* buffer, const struct pcap_pkthdr* header, const u_char* packet) {
    packet_count++;
    time_t packet_timestamp = header->ts.tv_sec;
    time_t packet_nanoseconds = header->ts.tv_usec;
    struct tm timestamp;
    char time_buf[80];

    if (packet_count == 1) {
        start_sec = header->ts.tv_sec;
        start_ns = header->ts.tv_usec;
    }
    long sec_dif = header->ts.tv_sec - start_sec;
    long ns_dif = header->ts.tv_usec - start_ns;

    if (header->ts.tv_usec < start_ns) {
        sec_dif -= 1;
        ns_dif += 1000000;
    }

    if (header->caplen > max_size) {
        max_size = header->caplen;
    } else if (header->caplen < min_size) {
        min_size = header->caplen;
    }
    avg_size += header->caplen;

    printf("%d ", packet_count);
    timestamp = *localtime(&packet_timestamp);
    strftime(time_buf, sizeof(time_buf), "%m-%d-%Y %H:%M:%S", &timestamp);
    printf("%s.%ld %ld.%.6ld %d\n", time_buf, packet_nanoseconds, sec_dif, ns_dif, header->caplen);

    struct ether_header *ethernet_header = (struct ether_header *) packet;

    log_address(ether_ntoa((struct ether_addr*)ethernet_header->ether_shost), &mac_sources);
    log_address(ether_ntoa((struct ether_addr*)ethernet_header->ether_dhost), &mac_destinations);

    if (ntohs(ethernet_header->ether_type) == ETHERTYPE_IP) {
        struct ip *ip_header = (struct ip *) (packet + ETHER_HDR_LEN);

        log_address(inet_ntoa(ip_header->ip_src), &ip_sources);
        log_address(inet_ntoa(ip_header->ip_dst), &ip_destinations);

        if (ip_header->ip_p == IPPROTO_UDP) {
            struct udphdr *udp_header = (struct udphdr *) (packet + ETHER_HDR_LEN + (ip_header->ip_hl * 4));

            log_port(ntohs(udp_header->uh_sport), &udp_sources);
            log_port(ntohs(udp_header->uh_dport), &udp_destinations);
        }
    } else if (ntohs(ethernet_header->ether_type) == ETHERTYPE_ARP) {
        struct ether_arp *arp_header = (struct ether_arp *) (packet + ETHER_HDR_LEN);

        in_addr_t* arp_src_raw = (in_addr_t*) arp_header->arp_spa;
        struct in_addr arp_src;
        arp_src.s_addr = *arp_src_raw;
        
        log_arp(ether_ntoa((struct ether_addr*)arp_header->arp_sha), inet_ntoa(arp_src), &arp_participants);

        in_addr_t* arp_dst_raw = (in_addr_t*) arp_header->arp_tpa;
        struct in_addr arp_dst;
        arp_dst.s_addr = *arp_dst_raw;

        log_arp(ether_ntoa((struct ether_addr*)arp_header->arp_tha), inet_ntoa(arp_dst), &arp_participants);
    } else {
        printf("ether_type %x is not suppported, ignoring\n", ntohs(ethernet_header->ether_type));
    }
}

/**
 * @brief Log an address into it's respective linked list
 * 
 * @param keyword the address
 * @param header pointer to the desired list
 */
void log_address(char* keyword, packet_list** header) {
    packet_list* item = store_addr(keyword, *header);
    if (item != NULL && strcmp(keyword, item->addr_str) == 0) return; // successfully stored already
    packet_list* entry = craft_packet_list_entry(keyword);
    if (*header == NULL) {
        *header = entry;
    } else {
        item->next = entry;
    }
}

/**
 * @brief Increment the count of the matching address, or return the end of the list
 * 
 * @param keyword the address
 * @param head pointer to head of list
 * @return packet_list* matching entry, or the end of the list
 */
packet_list* store_addr(char* keyword, packet_list* head) {
    packet_list* item = head;
    packet_list* tail = head;

    while (item != NULL) {
        if (strcmp(keyword, item->addr_str) == 0) {
            item->count++;
            return item;
        }
        tail = item;
        item = item->next;
    }
    return tail;
};

/**
 * @brief Creates a brand new packet_list object
 * 
 * @param addr_str The address
 * @return packet_list* new packet_list entry
 */
packet_list* craft_packet_list_entry(char* addr_str) {
    packet_list* entry = (packet_list *) malloc(sizeof(packet_list));
    strncpy(entry->addr_str, addr_str, 18);
    entry->count = 1;
    entry->next = NULL;
    return entry;
}

/**
 * @brief Log a UDP port into it's respective linked list
 * 
 * @param port the UDP port
 * @param header pointer to the desired list
 */
void log_port(int port, port_list** header) {
    port_list* item = store_port(port, *header);
    if (item != NULL && port == item->port) return;
    port_list* entry = craft_port_list_entry(port);
    if (*header == NULL) {
        *header = entry;
    } else {
        item->next = entry;
    }
}

/**
 * @brief Increment the count of the matching port, or return the end of the list
 * 
 * @param port the UDP port
 * @param head pointer to head of list
 * @return port_list* matching entry, or the end of the list
 */
port_list* store_port(int port, port_list* head) {
    port_list* item = head;
    port_list* tail = head;

    while (item != NULL) {
        if (port == item->port) {
            item->count++;
            return item;
        }
        tail = item;
        item = item->next;
    }
    return tail;
}

/**
 * @brief Creates a brand new port_list object
 * 
 * @param port The UDP port
 * @return port_list* new port_list entry
 */
port_list* craft_port_list_entry(int port) {
    port_list* entry = (port_list *) malloc(sizeof(port_list));
    entry->port = port;
    entry->count = 1;
    entry->next = NULL;
    return entry;
}

/**
 * @brief Log an address into it's respective linked list
 * 
 * @param hardware The hardware address
 * @param ip The IP address
 * @param header pointer to the desired list
 */
void log_arp(char* hardware, char* ip, arp_map** header) {
    if (strcmp(hardware, "0:0:0:0:0:0") == 0 || strcmp(hardware, "ff:ff:ff:ff:ff:ff") == 0) return; // ignore broadcasts
    arp_map* item = store_arp(hardware, ip, *header);
    if (item != NULL && strcmp(hardware, item->hardware) == 0) return; // successfully updated already
    arp_map* entry = craft_arp_map_entry(hardware, ip);
    if (*header == NULL) {
        *header = entry;
    } else {
        item->next = entry;
    }
}

/**
 * @brief Update the IP at the matching entry, or return the end of the list
 * 
 * @param hardware The hardware address
 * @param ip The IP address
 * @param head pointer to head of list
 * @return arp_map* 
 */
arp_map* store_arp(char* hardware, char* ip, arp_map* head) {
    arp_map* item = head;
    arp_map* tail = head;

    while (item != NULL) {
        if (strcmp(hardware, item->hardware) == 0) {
            strncpy(item->ip, ip, 16);
            return item;
        }
        tail = item;
        item = item->next;
    }
    return tail;
}

/**
 * @brief Creates a brand new arp_map object
 * 
 * @param hardware The hardware address
 * @param ip The IP address
 * @return arp_map* new arp_map entry
 */
arp_map* craft_arp_map_entry(char* hardware, char* ip) {
    arp_map* entry = (arp_map *) malloc(sizeof(arp_map));
    strncpy(entry->hardware, hardware, 18);
    strncpy(entry->ip, ip, 16);
    entry->next = NULL;
    return entry;
};
