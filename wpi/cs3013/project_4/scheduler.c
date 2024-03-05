#include <stdio.h>
#include <stdlib.h>
#include <string.h>

enum Policy {
    FIFO, // first in first out
    SJF, // shortest job first
    RR // round robin
};

typedef struct job {
    int id;
    int length;
    // other meta data
    // time_t startTime;
    // time_t completionTime;
    struct job *next;
} job;

typedef struct job_data {
    int id;
    int response_time;
    int turnaround_time;
    int wait_time;
} job_data;

void execute_job(job*, job_data*);
int execute_for(job*, job_data*, int);
int length_compare(const void*, const void*);
void fifo(job*, job_data*);
void sjf(job*, job_data*, int);
void rr(job*, job_data*, int, int);

int time = 0;

int main(int argc, char **argv) {

    if (argc < 4) {
        puts("Not enough arguments supplied.");
        return 1;
    }

    char* policy_input = argv[1]; // String representing what policy should be used for a workload
    FILE* file = fopen(argv[2], "r"); // Workload file containing job info
    int duration = atoi(argv[3]); // Duration of a timeslice

    int policy;

    if (strcmp(policy_input, "FIFO") == 0) {
        if (duration != 0) {
            puts("Invalid timeslice duration for FIFO.");
            return 1;
        }
        policy = FIFO;
    } else if (strcmp(policy_input, "SJF") == 0) {
        if (duration != 0) {
            puts("Invalid timeslice duration for SJF.");
            return 1;
        }
        policy = SJF;
    } else if (strcmp(policy_input, "RR") == 0) {
        if (duration == 0) {
            puts("Invalid timeslice duration for RR.");
            return 1;
        }
        policy = RR;
    } else {
        puts("Invalid scheduling policy.");
        return 1;
    }

    /**
    * We now have validated, non-string forms of all input.
    * 
    * policy: Enumerated form of the scheduling policy
    * file: File pointer to the input file we need to process.
    * duration: Integer representation of duration for RR.
    * 
    */
    int id = 0;
    int length;
    job *prior; // past created job
    job *first; // first/current job executing

    while (fscanf(file, "%d", &length) > 0) {
        job* j = (job *) malloc(sizeof(job)); // the new job
        /* set metadata of job */
        j->id = id;
        j->length = length;

        /* configure first/next jobs*/
        if (id == 0) {
            first = j;
        } else {
            prior->next = j;
        }

        /*update construction metadata */
        prior = j;
        id++;
    }

    job_data analysis[id];

    for (int i = 0; i < id; i++) {
        analysis[i].id = -1;
        analysis[i].response_time = -1;
        analysis[i].turnaround_time = -1;
        analysis[i].wait_time = -1;
    }

    switch(policy) {
        case FIFO:
            fifo(first, analysis);
            break;
        case SJF:
            sjf(first, analysis, id);
            break;
        case RR:
            rr(first, analysis, id, duration);
            break;
    }
    float response_avg = 0;
    float turnaround_avg = 0;
    float wait_avg = 0;
    printf("Begin analyzing %s:\n", policy_input);
    for (int job = 0; job < id; job++) {
        job_data data = analysis[job];
        printf("Job %d -- Response time: %d  Turnaround: %d  Wait: %d\n", data.id, data.response_time, data.turnaround_time, data.wait_time);
        response_avg += data.response_time;
        turnaround_avg += data.turnaround_time;
        wait_avg += data.wait_time;
    }
    response_avg /= id;
    turnaround_avg /= id;
    wait_avg /= id;
    printf("Average -- Response: %.2f  Turnaround: %.2f  Wait: %.2f\n", response_avg, turnaround_avg, wait_avg);
    printf("End analyzing %s.\n", policy_input);
    fclose(file);
    return 0;
}

/**
 * Prework:
 * Helpers to create linked-list and lined-list helpers
    * Add fields to your LL fields that will make your life easier
    * Manage LL according to a given policy
    * E.g., make an enque() function that adds a job and returns the list and a deque() function that removes a job and returns it.
*/

/*
 * The FIFO (first-in-first-out) policy states that jobs are scheduled in order of their arrival. Further, each job runs to completion.
 * In other words, there is no preemption for this FIFO policy.
 */
void fifo(job* job_list, job_data* analysis_list) {
    job *fifo_previous; // pointer to job that just executed (for freeing) in FIFO
    int job = 0;

    puts("Execution trace with FIFO:");
    while (job_list != NULL) { // While the job to execute exists
        execute_job(job_list, &analysis_list[job]); // execute it
        fifo_previous = job_list; // Save the "previously executed job" pointer
        job_list = job_list->next; // Overwrite the "current" job to the next one
        free(fifo_previous); // Free the job that just finished
        job++;
    }
    puts("End of execution with FIFO.");
}

/*
 * The SJF (shortest job first) policy requires that the scheduler always pick the job with the shortest runtime to run next.
 * If two jobs need the same amount of time to execute, the SJF policy breaks the tie by favoring the job that arrived earlier.
 */
void sjf(job* job_list, job_data* analysis_list, int id) {
    job *sjf_current; // current inspection job for list compilation in SJF
    job sjf_job_list[id]; // list of jobs in SJF
    job *sjf_unsorted[id]; // lsit of jobs to free in SJF

    puts("Execution trace with SJF:");
    int ticker = 0;
    sjf_current = job_list;

    while (sjf_current != NULL) {
        sjf_job_list[ticker] = *sjf_current;
        sjf_unsorted[ticker] = sjf_current;
        sjf_current = sjf_current->next;
        ticker++;
    }

    qsort(sjf_job_list, id, sizeof(job), length_compare);

    for (int j = 0; j < id; j++) {
        job* sjf_todo = &sjf_job_list[j];
        execute_job(sjf_todo, &analysis_list[j]);
    }
    for (int j = 0; j < id; j++) {
        free(sjf_unsorted[j]);
    }
    puts("End of execution with SJF.");
}

/*
 * The RR (round-robin) policy schedules jobs in the order of arrival and dictates that each job will only be run for a fixed duration of time,
 * called a time slice, before another job is scheduled. If the remaining duration of job exceeds the length of the time slice,
 * than that job must be preempted.
 */
void rr(job* job_list, job_data* analysis_list, int id, int slice) {
    // Iterate through the job list, subtracting time slice from 
    puts("Execution trace with RR:");

    int remaining = id;

    job* head = job_list;

    while (remaining > 0) {
        int ticker = 0;
        while (job_list != NULL) {
           remaining -= execute_for(job_list, &analysis_list[ticker], slice);
           job_list = job_list->next;
           ticker++;
        }
        job_list = head;
    }

    while (job_list != NULL) {
        head = job_list;
        job_list = job_list->next;
        free(head);
    }
    puts("End of execution with RR.");
}

/* basic execution for FIFO and SJF */
void execute_job(job* j, job_data* a) {
    a->id = j->id;
    a->response_time = time;
    a->wait_time = time;
    printf("Job %d ran for: %d\n", j->id, j->length);
    time += j->length;
    a->turnaround_time = time;
}

/* complex execution for RR */
/* returns true (1) if complete, false (0) otherwise */
int execute_for(job* j, job_data* a, int amount) {
    int amount_ran = amount > j->length ? j->length : amount; // if the timeslice is greater than the remaining length of the job
    if (amount_ran == 0) return 0;
    a->id = j->id;
    if (a->wait_time == -1) {
        a->response_time = time;
        a->wait_time = time;
    } else {
        a->wait_time += (time - a->turnaround_time);
    }
    j->length -= amount_ran;
    printf("Job %d ran for: %d\n", j->id, amount_ran);
    time += amount_ran;
    a->turnaround_time = time;
    if (j->length == 0) {
        return 1;
    }
    return 0;
}

/* adapted from the link below */
// https://stackoverflow.com/questions/1787996/c-library-function-to-perform-sort
int length_compare(const void *a,const void *b) {
    job *x = (job *) a;
    job *y = (job *) b;
    return x->length - y->length;
}