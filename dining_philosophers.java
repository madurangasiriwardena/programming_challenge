class ChopStick{
    private Thread holder;
    private int index;

    public ChopStick(int in) {
        holder = null;
        index= in;
    }

    synchronized void grab(Philosopher current)throws InterruptedException{
        while(holder != null){
            System.out.println("Philosopher " + current.sendIndex() + " waiting for stick" + index);
            wait();
        }
            holder = Thread.currentThread();
            System.out.println("Philosopher " + current.sendIndex() + " grabed a stick " + index);
        
    }

    synchronized void release(Philosopher current){
        holder = null;
        System.out.println("Philosopher " + current.sendIndex() + " released a stick" + index);
        notify();
    }

    boolean isHolding(){
        if(holder == null)
            return false;
        else
            return true;
    }
}

class Bowl{
    private boolean lidClosed;
    private int amount;

    public Bowl() {
        lidClosed = true;
        amount = 100;
    }

    synchronized void serve(Philosopher current, int amount){
        lidClosed = false;
        System.out.println("Philosopher " + current.sendIndex() + " is serving");

        if(this.amount>amount){
            this.amount -= amount;

        }else{
            this.amount = 0;
            System.out.println("Noodles finished ");
        }
        
        System.out.println("Philosopher " + current.sendIndex() + " is finished serving");

        lidClosed = true;
    }

    boolean isEmpty(){
        if(amount != 0){
            return false;
        }else{
            return true;
        }

    }

    boolean isclosed(){
        return lidClosed;
    }

}

class Philosopher implements Runnable{
    private ChopStick left;
    private ChopStick right;
    private int index;
    private Bowl bowlRef;

    public Philosopher(ChopStick left, ChopStick right, int index, Bowl bowlRef) {
        this.left = left;
        this.right = right;
        this.index = index;
        this.bowlRef = bowlRef;
    }

    int sendIndex(){
        return index;
    }

    public void run(){
        try{
            while(!bowlRef.isEmpty()){
                right.grab(this);
                if(left.isHolding()){
                    right.release(this);
                    continue;
                }
                left.grab(this);

                if(bowlRef.isclosed() && !bowlRef.isEmpty()){
                    bowlRef.serve(this, 10);
                }else{
                    System.out.println("Philosopher " + index + " is eating");
                }

                right.release(this);
                left.release(this);

                System.out.println("Philosopher " + index + " is thinking");
            }

        }catch(InterruptedException e){
        }
    }
}

public class dining_philosophers{
    public static void main(String[] args) {

        Bowl noodleBowl = new Bowl();

        ChopStick stick[] = new ChopStick[5];
        for(int i=0; i<5; i++){
            stick[i] = new ChopStick(i+1);
        }


        Philosopher phil[] = new Philosopher[5];
        for(int i=0; i<4; i++){
            phil[i] = new Philosopher(stick[i], stick[i+1], i+1, noodleBowl);
        }
        phil[4] = new Philosopher(stick[4], stick[0], 5, noodleBowl);
        

        Thread t[] = new Thread[5];
        for(int i=0; i<5; i++){
            t[i] = new Thread(phil[i]);
        }

        for(int i=0; i<5; i++){
            t[i].start();
        }
        
    }

}
