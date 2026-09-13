package os_task1;

public class ProducerConsumer {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Buffer buffer = new Buffer(5);

        Producer producer = new Producer(buffer);
        Consumer consumer = new Consumer(buffer);

        producer.start();
        consumer.start();

        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Production and consumption completed.");

	}

}
