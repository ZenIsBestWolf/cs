// Starting code version 1.1

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdint.h>
#include <sys/types.h>
#include <unistd.h>
#include <assert.h>

#include "mmu.h"
#include "pagetable.h"
#include "memsim.h"

int pageToEvict = 1; // if using round robin eviction, this can be used to keep track of the next page to evict

// Page table root pointer register values (one stored per process, would be "swapped in" to MMU with process, when scheduled)
typedef struct{
	int ptStartPhysAddr; // The physical address at which the page table starts in physical memory
	int present; // Tracks whether the page table is currently stored in physical memory (1) or not (0)
	int ptStartDiskAddr; // The physical address at which the page table starts on disk
} ptRegister;

// Page table root pointer register values
// One stored for each process, swapped in to MMU when process is scheduled to run
ptRegister pageTableRegisters[NUM_PROCESSES]; // simulated registers that enable us to access the page table

/* Struct representing a singular page table entry (PTE)
 * Whole struct uses 2 bytes of memory
 */
typedef struct pte
{
    // using bit fields allows the compiler to align each member of the struct into a as few bytes as possible
    // declaring these as uint8_t lets the compiler know to squeeze them together into a single byte if possible
    uint8_t present : 1;
    uint8_t vpn : 2;
    uint8_t pfn : 2;
	uint8_t rw : 1;

	/**
	 * can use 2 most-significant bits as a counter to implement least recently used
     * only has to go up to 3 because of the number of pages we have
     * increment each page upon a page access except for the page being accessed
	*/
    uint8_t counter : 2;
    uint8_t dirty : 1;
    uint8_t accessed : 1;
} pte;

/*
 * Public Interface:
 */

/*
 * Sets the Page Table Entry (PTE) for the given process and VPN.
 * The PTE contains the PFN, valid bit, protection bit, present bit, and referenced bit.
 */
void PT_SetPTE(int pid, int VPN, int PFN, int valid, int protection, int present, int referenced) {
	char* physmem = Memsim_GetPhysMem();
	assert(PT_PageTableExists(pid)); // page table should exist if this is being called

	/* compute the address of the target PTE by retrieving the physical address of the start of the page table
	 * and indexing based on the page number
	 */
	int address = PT_GetRootPtrRegVal(pid) + (VPN * sizeof(pte));

	pte target = {.present = present, .vpn = VPN, .pfn = PFN, .rw = protection, .counter = 0, .dirty = 1, .accessed = referenced};

	memcpy(&physmem[address], &target, sizeof(target));
}

/* 
 * Initializes the page table for the process, and sets the starting physical address for the page table.
 * 
 * After initialization, get the next free page in physical memory.
 * If there are no free pages, evict a page to get a newly freed physical address.
 * Finally, return the physical address of the next free page.
 */
int PT_PageTableInit(int pid, int pa){
	char* physmem = Memsim_GetPhysMem();

	// zero out the page we are about to use for the page table 
	memset(&physmem[pa], 0, sizeof(physmem[pa])); // zero implies free / FALSE / not used

	// set the page table's root pointer register value
	pageTableRegisters[pid].ptStartPhysAddr = pa; // Do NOT need to call PT_GetRootPtrRegVal here since only initializing
	pageTableRegisters[pid].present = TRUE;

	// return the PA of the next free page
	int nextFreePA = Memsim_FirstFreePFN();

	// If there were no free pages, evict one and use the new space.
	if (nextFreePA == -1) {
		int targetPFN;
		int diskFrameNum;
		PT_EvictPageCommon(&targetPFN, &diskFrameNum, VERBOSE_MODE);
		nextFreePA = Memsim_FirstFreePFN();
	}
	
	return nextFreePA;
 }

/* 
 * Check the page table registers to see if there is a valid starting physical address for the given PID's page table.
 * Returns true (non-zero) or false (zero).
 */
int PT_PageTableExists(int pid) {
	if (PT_GetRootPtrRegVal(pid) != -1)
	{
		return TRUE;
	}

	return FALSE;
}

/* 
 * Returns the starting physical address of the page table for the given PID.
 * If the page table does not exist, returns -1.
 * If the page table is not in memory, first swaps it in to physical memory.
 * Finally, returns the starting physical address of the page table.
 */
int PT_GetRootPtrRegVal(int pid) {
	// If the page table does not exist (i.e. has a ptStartPhysAddr of -1), return -1
	if (pageTableRegisters[pid].ptStartPhysAddr == -1) {
		return -1;
	} // If the page table is not in memory, swap it in
	else if (pageTableRegisters[pid].present == FALSE) {
		// swap in
	}
	// Return the starting physical address of the page table
	return pageTableRegisters[pid].ptStartPhysAddr;
}

/* Returns next page selected for eviction, based on a simple Round Robin policy. */
int PT_NextEvictionRR() {
	int nextPgToEvict = pageToEvict;

	pageToEvict++;
	pageToEvict %= NUM_PAGES; //wraps around to first page if it goes over

	return nextPgToEvict;
}

/* Returns next page selected for eviction, based on a simple Least Recently Used policy. */
/* int PT_NextEvictionLRU() {
	// Could make this use a LRU Aging policy
	
	int evicteePageNum;
	
	for (int pid = 0; pid < NUM_PROCESSES; pid++)
	{
		PT_GetRootPtrRegVal
	}
	

	return evicteePageNum;
} */

/*
 * Selects the next physical frame to evict, and writes it's data to the disk.
 * Returns the physical frame number of the evicted page, and the disk frame number it was written to.
 */
int PT_EvictPageCommon(int* targPFN, int* diskFrameNum, int verbose) {
	char* physmem = Memsim_GetPhysMem();
	FILE* swapFile = MMU_GetSwapFileHandle();
	int fd = fileno(swapFile);

	// For LRU, either loop and check counter or sort such that top frame is always the MOST recently used and the bottom frame is the LEAST recently used

	/**
	 * You may want to check the stored process register values to know in the case the page being evicted is a page table.
	 * Your implementation might also do something different if you choose depending on how your mapping and swapping logic works.
	 * Either way, when mappings change, PTEs need to be updated and stay accurate.
	 * There are different solutions to keeping things coherent so if/when it is needed again by the process, the logic can swap it back in.
	*/

	if (Disk_Flush(fd, swapFile) == -1) {
		return -1;
	} else { 
		*targPFN = PT_NextEvictionRR(); // call the RR policy decider

		int frameAddress = physmem[PAGE_START(*targPFN)];
		// Check if something is a page table
		if (frameAddress == pageTableRegisters[*targPFN]) {
			pageTableRegisters[pid].present = FALSE;
		} else {
			// If not a page table, iterate through page table, mark all PTEs as not present in memory

			// Update info in page table entry; affects present, counter (?), dirty, & accessed
		}

		// Unmark page in freePages to release it
		Memsim_MarkPageFree(*targPFN);
		
		// Write page to disk from physical memory
		*diskFrameNum = Disk_Write(swapFile, *targPFN, verbose);
	}
}

/**
 * PTE swap
 * get physmem and swapfile
 * ensure swapfile ready
 * determine PTE to swap with eviction policy
 * Find location to read data from swapfile
 * Find location to write data in physmem
 * update PTE for swapping out
 * update incoming PTE: mark as present and update physical address based on page table register
*/

/*
 * Searches through the process's page table. If an entry is found containing the specified VPN, 
 * return the address of the start of the corresponding physical page frame in physical memory. 
 *
 * If the physical page is not present, first swaps in the phyical page from the physical disk,
 * and returns the physical address.
 * 
 * Otherwise, returns -1.
 */
int PT_VPNtoPA(int pid, int VPN){
	char *physmem = Memsim_GetPhysMem();

	// does call to PT_GetRootPtrRegVal do the validation?
	int page_table_start = PT_GetRootPtrRegVal(pid);
	if (page_table_start == -1) return -1;

	/* Index into the page table to find the appropriate page table entry */
	pte entry;
	int entry_address = page_table_start + (VPN * sizeof(pte));
	memcpy(&entry, &physmem[entry_address], sizeof(entry));

	if (entry.vpn != VPN) return -1;

	return entry.pfn << 4; // could swap out constant for NUM_PAGES or a similar macro with a different name
}

/*
 * Finds the page table entry corresponding to the VPN, and checks
 * to see if the protection bit is set to 1 (readable and writable).
 * If it is 1, it returns TRUE, and FALSE if it is not found or is 0.
 */
int PT_PIDHasWritePerm(int pid, int VPN) {
	char* physmem = Memsim_GetPhysMem();
	assert(PT_PageTableExists(pid)); // page table should exist if this is being called

	pte entry; // the page table entry
	int entry_address = PT_GetRootPtrRegVal(pid) + (VPN * sizeof(pte)); // calculate address to grab
	assert(entry_address != -1); // validate that an address was found
	// should actually check if a pte was found at this address?

	memcpy(&entry, &physmem[entry_address], sizeof(entry)); // copy the PTE into the stack

	return entry.rw; // return the bit
}

/* 
 * Initialize the register values for each page table location (per process).
 * For example, -1 for the starting physical address of the page table, and FALSE for present.
 */
void PT_Init() {
	for (int pid = 0; pid < NUM_PROCESSES; pid++) {
		pageTableRegisters[pid].present = FALSE;
		pageTableRegisters[pid].ptStartPhysAddr = -1;  // Do NOT need to call PT_GetRootPtrRegVal here since only initializing
	}
	return;
}