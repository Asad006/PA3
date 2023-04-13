# Programming Assignment 3.
This project is my answer to the two problems asked in assignment 3. 

### Execution Instructions
This is an IntelliJ project. it can easily clone to a new project from IntelliJ,
by clicking on Open from CVS then copy the clone link of the project. This project
can be also executed by downloading the project then open it by any IDE.
On a Terminal, get into the problem 1 in part 1 folder and problem 2 part 2 folder  where **Part1.ServantsWork.java** and **part2.AtmosphericTemperatureModule.java** is located, then execute the following commands
**javac ServantsWork.java** or **javac AtmosphericTemperatureModule.java** followed with **java Problem1** to run problem 1 and **javac AtmosphericTemperatureModule** to run problem2.

## Problem1

### Correctness and Efficiency
My solution for this problem uses a stack to simulate unordered bag of gifts. The total number of gifts was pushed into
 the stack, then I used shuffle method for this stack to have a random order. In my implementation of a liked list, I
used a Lock-free Linked List in the class LFLinkedList and followed the architecture discussed in chapter 9 in class to store the elements in chain. 
To keep track of the number of gifts especially after the threads remove them, I used an atomic variable which enables 
to have the correct tracking among the threads. The threads after they are ran, they will get randomly one of the three
actions listed in the problem. I simulated the first action by taking a  gift from the unordered bag and put it in the free
lock linked list, and the second task is writing a thank you note then remove the gift from the liked list. The third action
is to check if the liked list contains the gift.
### Experimental Evaluation & Efficiency
The solution can be improved to avoid threads waiting and starvation as add and remove operation involve locking.
## Problem2

### Correctness and Efficiency
My implementation of the solution of problem 2 simulates the temperature reading for the number of hours entered.
The temperatures reading in the solution was done in every 3 ms, and by generating a random number between -100F and
170F. I used a max-heap and a min-heap to store the top 5 temperature reading and minimum 5 on temperature reading,
these data structures were shared memory, and the data get refreshed every hour. The data get also refreshed every 10
min and assesses the temperature difference to get the largest temperature  change and its related time interval. I used 
this time a semaphore lock for temperature updates and max heap  to manage the shared memory and to ensure the correctness
of tracking among thread.

### Experimental Evaluation & Efficiency
I experimented using semaphore in this problem to ensure that the 8 threads can properly access the shared memory.
The results were good as the semaphore properly handles the access to shared memory as indicated above.

