# Project 4: Scheduling Policies

# Automated Tests Status

| Test | Passing | Test Description |
| :--- | :-----: | :--- |
| 1  | ✅ | FIFO policy with one job |
| 2  | ✅ | FIFO policy with 10 jobs |
| 3  | ✅ | FIFO policy with longest to shortest jobs |
| 4  | ✅ | SJF policy with one job |
| 5  | ✅ | SJF policy with 10 jobs in decreasing lengths |
| 6  | ✅ | SJF policy with 20 jobs with random length and tie breaker |
| 7  | ✅ | RR policy with one job and a small time slice |
| 8  | ✅ | RR policy with one job and a non-divisible time slice |
| 9  | ✅ | RR policy with one job and a larger time slice |
| 10 | ✅ | RR with 10 random length jobs with time slice = 5 |
| 11 | ✅ | RR policy with 100 random length jobs, time slice = 3 |
| 12 | ✅ | FIFO analysis with 1 job |
| 13 | ✅ | FIFO analysis with multiple jobs |
| 14 | ✅ | SJF analysis with one job |
| 15 | ✅ | SJF analysis with multiple jobs |
| 16 | ✅ | RR analysis with one job with time slice 4 |
| 17 | ✅ | RR analysis with multiple jobs with time slice 5 |

# Instructions
In this directory, you should write the program `scheduler.c` and compile it into the binary `scheduler`
(e.g., `gcc -o scheduler scheduler.c -Wall -Werror`). You should do it inside the Ubuntu machine you have setup,
as grading will be done so inside the Ubuntu.

After doing so, you can run the tests from this directory by running the `./run_tests.sh` script.
If all goes well, you will see standard output that indicate that all tests are passed.

Passing all tests are not sufficient to obtain all points for this assignment. As an example, any assignment
in which hardcodes the expected output to pass the test case will lead to point deductions.

# Final Submission
Explain your project, each novel workload, and anything that you feel the teaching staff should know when
grading the project. In particular, describe the data structure and algorithm you used to complete both
the policy implementation and analysis parts. Only plaintext write-ups are accepted.