// Starting code version 1.0

#include <assert.h>
#include <string.h>

#include "memsim.h"

/* Private Internals: */

// Currently 'boolean' array (bitmap would be better for scale and perf)
short freePages[NUM_PAGES];

// The simulated physical memory array (in bytes), aka physical R.A.M.
char physmem[PHYSICAL_SIZE];

/*
 * Performs sanity checks based on the simulations defined constants.
 * If these checks fail, the simulation is not valid and should not proceed.
 * Asserts are used to halt the program immediately if these checks fail.
 */
void MemsimConfigSanityChecks() { // you should not modify this function
	assert(PHYSICAL_SIZE % PAGE_SIZE == 0); // physical size in bytes must be a multiple of page size bytes
	assert(VIRTUAL_SIZE == PHYSICAL_SIZE); // standard implementation gives whole PA space to each process (virtually)
    assert(MAX_VA == MAX_PA);
    assert(NUM_PAGES == NUM_FRAMES);
}

/*
 *  Public Interface: 
 */

/* 
 * Zeroes out the simulated physical memory array.
 */
void Memsim_Init() { // zero free pages list 
    MemsimConfigSanityChecks();
    memset(physmem, 0, sizeof(physmem)); // zero out physical memory
	memset(freePages, 0, sizeof(freePages)); // zero implies free / FALSE / not used
}

 /* Gets current shared reference to start of simulated physical memory. */
char* Memsim_GetPhysMem() {
    return physmem;
}

/*
 * Searches through the free page list to find the first free page in memory. 
 * It claims the page, marking it, and returns the physical address of the beginning 
 * of the page. If there are no free pages, returns -1;
 */
int Memsim_FirstFreePFN() {
    // make this evict a page itself to then return?
    // might not be the best idea because there are so many interacting pieces
    // would have to update the page table, but I think evict already does that?
    for(int i = 0; i < NUM_PAGES; i++) {
        if (freePages[i] == 0) {
            freePages[i] = 1;
            return PAGE_START(i);
        }
    }

	return -1;
}

/**
 * Uses the provided page number to release the page, unmarking it.
 */
void Memsim_MarkPageFree(int pageNum) {
    if (pageNum < 0 || pageNum >= NUM_PAGES) {
		printf("Invalid page number provided. Value must be 0-%d.\n", (NUM_PAGES - 1)); 
		return 1;
	}

    freePages[pageNum] = 0;
}