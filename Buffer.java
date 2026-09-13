package os_task1;

public class Buffer {
	    private final int[] buffer;
	    private int count = 0;
	    private int in = 0;
	    private int out = 0;

	    Buffer(int size) {
	        buffer = new int[size];
	    }

	    public synchronized void produce(int item) throws InterruptedException {
	        while (count == buffer.length) {
	            wait(); 
	        }

	        buffer[in] = item;
	        in = (in + 1) % buffer.length;
	        count++;

	        System.out.println("Produced: " + item);

	        notifyAll();
	    }

	    public synchronized int consume() throws InterruptedException {
	        while (count == 0) {
	            wait(); 
	        }

	        int item = buffer[out];
	        out = (out + 1) % buffer.length;
	        count--;

	        System.out.println("Consumed: " + item);

	        notifyAll(); 

	        return item;
	    }

}
