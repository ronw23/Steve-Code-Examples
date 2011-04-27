/* This is an example of shared mutability and the use of
   java.util.concurrent */

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.Random;
import java.lang.InterruptedException;
   
public class ConcurrentExample {

	private final ReadWriteLock monitor = new ReentrantReadWriteLock(); 
	private int counter = 20;
	
	public void incrementCounter() {
		monitor.writeLock().lock();
		try {
			counter++;
			System.out.println("Incremented to " + counter);
		} finally {
			monitor.writeLock().unlock();
		}
	}
	
	public void decrementCounter() {
		monitor.writeLock().lock();
		try {
			counter--;
			System.out.println("Decremented to " + counter);
		} finally {
			monitor.writeLock().unlock();
		}
	}
	
	public void readCounter() {
		monitor.readLock().lock();
		try {
			System.out.println("Read as " + counter);
		} finally {
			monitor.readLock().unlock();
		}
	}
	
	private static Thread getIncThread(final ConcurrentExample example) {
		return new Thread(new Runnable() {
			private final Random rand = new Random();
			public void run() {
				for (int i=0; i<10; i++)
				{
					example.incrementCounter();
					try {
						Thread.sleep(rand.nextInt(3));
					} catch(InterruptedException e) {}
				}
			}
		});
	}

	private static Thread getDecThread(final ConcurrentExample example) {
		return new Thread(new Runnable() {		
			private final Random rand = new Random();
			public void run() {
				for (int i=0; i<10; i++)
				{
					example.decrementCounter();
					try {
						Thread.sleep(rand.nextInt(3));
					} catch(InterruptedException e) {}
				}
			}
		});
	}

	private static Thread getReadThread(final ConcurrentExample example) {
		return new Thread(new Runnable() {		
			private final Random rand = new Random();
			public void run() {
				for (int i=0; i<10; i++)
				{
					example.readCounter();
					try {
						Thread.sleep(rand.nextInt(3));
					} catch(InterruptedException e) {}
				}
			}
		});
	}
	
	public static void main (final String[] args) {
		final ConcurrentExample example = new ConcurrentExample();
		
		// Make threads
		Thread incThrd = getIncThread(example);
		Thread decThrd = getDecThread(example);
		Thread readThrd = getReadThread(example);
		
		// Start threads
		readThrd.start();
		incThrd.start();
		decThrd.start();
	}
}
