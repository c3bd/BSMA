package object;

import java.util.TreeSet;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import timelineGenerator.Parameter;

public class Task {
	private final ReentrantLock lock = new ReentrantLock();
	private final Condition con1 = lock.newCondition();
	private final Condition con2 = lock.newCondition();
	private TreeSet<Tweet> tasks = new TreeSet<Tweet>(
			new ComparatorByTimeOrder());

	public void trySingalTaskCon() {
		lock.lock();
		try {
			if (lock.hasWaiters(con1)
					&& (Parameter.pool.getFollowingPool().isEmpty() || 
							tasks.first().getTime() <= Parameter.pool.getFirstFollowingTime())) {
				con1.signalAll();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
	
	public void trySingalTaskEmpty() {
		lock.lock();
		try {
			if (lock.hasWaiters(con2)) {
				con2.signalAll();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	public void addTask(Tweet m) {
		lock.lock();
		try {
			tasks.add(m);
			if (lock.hasWaiters(con1)
					&& tasks.first().getTime() <= Parameter.pool
							.getFirstFollowingTime()) {
				con1.signalAll();
			}
			if (lock.hasWaiters(con2)) {
				con2.signalAll();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	public Tweet getFirstTask() {
		lock.lock();
		Tweet result = null;
		try {
			if (tasks.isEmpty()) {
				if (!Parameter.ReceiveTaskEnd) {
					//System.out.println("con2.await");
					con2.await();
					//System.out.println("con2.await out");
				}
			}else if ((Parameter.pool.getFollowingPool().isEmpty() 
					&& Parameter.pool.getCandidatePool().isEmpty())
					|| (!Parameter.pool.getFollowingPool().isEmpty() 
							&& tasks.first().getTime() > Parameter.pool.getFirstFollowingTime())) {

				//System.out.println("con1.await");
				con1.await();
				//System.out.println("con1.await out");
			}
			
			if(!tasks.isEmpty()){
				result = tasks.pollFirst();
			}
			

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
		return result;
	}
}