// Starting code version 1.1

/*
 * Public Interface:
 */

#define NUM_PROCESSES 4
#define VERBOSE_MODE 1

/*
 * Public Interface:
 */

void PT_SetPTE(int process_id, int VPN, int PFN, int valid, int protection, int present, int referenced);

int PT_PageTableInit(int process_id, int pageAddress);

int PT_PageTableExists(int process_id);

// Add functionality for part 2
int PT_GetRootPtrRegVal(int process_id);

int PT_NextEvictionRR();

// Add functionality for part 2
int PT_EvictPageCommon(int* targPFN, int* diskFrameNum, int verbose);

// double-check validation logic
int PT_VPNtoPA(int process_id, int VPN);

// check if Nick properly validated in the case of a pte not being found
int PT_PIDHasWritePerm(int process_id, int VPN);

void PT_Init();