package object;

import java.util.HashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class TaskResult{

	private final ReentrantLock lock = new ReentrantLock();  
    private final Condition con = lock.newCondition();
    private HashMap<String,String> results = new HashMap<String,String>();
    private String waitTaskResult = null;
    
    public String getRtMid(String mid){
    	lock.lock();  
    	String rtmid = null;
        try {
        	if(!results.containsKey(mid)){
				waitTaskResult = mid;
				con.await();
			}
			rtmid = results.remove(mid);
        }catch (Exception e) {  
            e.printStackTrace();  
        }  
        finally{  
            lock.unlock();  
        }
        return rtmid;
    }
    
    public void addResult(Pair pair){
    	lock.lock();  
        try {
        	results.put(pair.getMid(), pair.getRtMid());
			if(lock.hasWaiters(con) && waitTaskResult != null && waitTaskResult.equals(pair.getMid())){
				waitTaskResult = null;
				con.signalAll();
			}
        }catch (Exception e) {  
            e.printStackTrace();  
        }  
        finally{  
            lock.unlock();  
        }
    }
    
}