package Part1;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;


public class ServantsWork extends Thread
{
    int NUMBEROFPRESENTS = 500_000;
    int NUMBEROFTHREADS = 4;
    ReentrantLock lock = new ReentrantLock();

    //Atomics
    public AtomicInteger thxCards = new AtomicInteger();
    public LFLinkedList chain = new LFLinkedList();

    //data structure
    public Stack<Integer> giftsStack = new Stack<>();
    public ArrayList<Integer> giftsArray = new ArrayList<>();
    public static ArrayList<SingleServant> threadsArray = new ArrayList<>();

    int nPresents;
    private int numberOfThreads;

    //Constructor
    ServantsWork()
    {
        this.nPresents = NUMBEROFPRESENTS;
        this.numberOfThreads = NUMBEROFTHREADS;
    }

    void runServants(ServantsWork mainThread) throws InterruptedException
    {
        // create threads, insert them into the arraylist, then run them.
        for (int i = 1; i <= this.numberOfThreads; i++)
            threadsArray.add(new SingleServant(i, mainThread));

        for (int i = 0; i < mainThread.numberOfThreads; i++)
            threadsArray.get(i).start();

        for (int i = 0; i < mainThread.numberOfThreads; i++)
            threadsArray.get(i).join();

        System.out.println(thxCards + " Work done: thank you notes were written.");

    }

    public static void main(String args[]) throws InterruptedException
    {
        //Initialization of an instance of ServantsWork
        ServantsWork servantsWorkThread = new ServantsWork();

        //putting all the gifts in the stack
        for (int i = 1; i <= servantsWorkThread.nPresents; i++)
            servantsWorkThread.giftsStack.push(i);

        //Randomly reorder the stack, to simulate the unordered gifts.
        Collections.shuffle(servantsWorkThread.giftsStack);

        //initiate start-time.
        final long startTime = System.currentTimeMillis();

        //
        servantsWorkThread.runServants(servantsWorkThread);

        final long endTime = System.currentTimeMillis();
        final long computeTime = endTime - startTime;
        System.out.println("Execution time: " + computeTime + " ms");
    }
}

// This class simulates a servant.
class SingleServant extends Thread
{
    int threadID;
    ServantsWork work;

    SingleServant(int threadNum, ServantsWork mainThread)
    {
        this.threadID = threadNum;
        this.work = mainThread;
    }

    private boolean isBagEmpty()
    {
        return this.work.giftsStack.empty();
    }

    private boolean isChainEmpty()
    {
        if (this.work.giftsArray.size()==0){
            return true;
        }else
            return false;

    }

    @Override
    public void run()
    {
        while (work.thxCards.get() < work.nPresents)
        {
            //randomly select an action.
            int action = (int) (Math.random() * 3 + 1);

            int gift;

            switch (action)
            {
                case 1:
                    //Action 1: delete gift from bag and add it to array.

                    work.lock.lock();
                    try {
                        if (isBagEmpty())
                            break;
                        gift = work.giftsStack.pop();
                        work.giftsArray.add(gift);
                    } finally {
                        work.lock.unlock();
                    }

                    work.chain.add(gift);
                    break;
                case 2:
                    //Action 2: Write thank you note  and remove from array.
                    work.lock.lock();
                    try {
                        if (isChainEmpty())
                            break;
                        int randIndex = (int)(Math.random() * work.giftsArray.size());
                        gift = work.giftsArray.get(randIndex);
                        work.giftsArray.remove(randIndex);
                    } finally {
                        work.lock.unlock();
                    }
                    work.chain.remove(gift);
                    work.thxCards.getAndIncrement();
                    break;
                case 3:
                    //Action 3: check present is on chain
                    int randomGift = (int)(Math.random() * work.nPresents + 1);

                    work.chain.contains(randomGift);
            }
        }
    }
}

