// Starting code version 1.0

#ifndef MEMSIM_H
#define MEMSIM_H

#include <assert.h>

/*
 * Public Interface:
 */

#define PAGE_SIZE 16

#define PHYSICAL_SIZE 64
#define VIRTUAL_SIZE 64

#define MAX_VA 255
#define MAX_PA 255

#define NUM_PAGES (PHYSICAL_SIZE / PAGE_SIZE)
#define NUM_FRAMES NUM_PAGES

// Address getters
#define PAGE_START(i) (i * PAGE_SIZE)
#define PAGE_OFFSET(addr) (addr % PAGE_SIZE) // will successively eliminate the upper bits, leaving only the lower bits as the remainder of division
#define VPN(addr) (addr / PAGE_SIZE) // will eliminate the lower bits, leaving the upper bits as the quotient
#define PFN(addr) (addr / PAGE_SIZE)
#define PAGE_NUM(addr) (addr / PAGE_SIZE)

// Public functions 
void Memsim_Init();
char* Memsim_GetPhysMem();
int Memsim_FirstFreePFN();
void Memsim_MarkPageFree(int pageNum);

#endif // MEMSIM_H