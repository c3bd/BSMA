package object;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import timelineGenerator.Parameter;

public class Task{
	private final ReentrantLock lock = new ReentrantLock();  
    private final Condition con = lock.newCondition();
    private LinkedList<Tweet> tasks = new LinkedList<Tweet>();
    
    public void addTask(Tweet rt){
    	lock.lock();  
        try {
        	tasks.add(rt);
        	if(lock.hasWaiters(con)){
        		con.signalAll();
        	}
        }catch (Exception e) {  
            e.printStackTrace();  
        }  
        finally{  
            lock.unlock();  
        }
    }
    
    public Tweet getFirstTask(){
    	lock.lock();  
    	Tweet result = null;
        try {
        	while(tasks.isEmpty() ){
        		if(Parameter.SendTweetEndClients.size() != Parameter.clientNum){
            		con.await();
        		}else{
        			return null;
        		}
        	}
        	result = tasks.pollFirst();
        }catch (Exception e) {  
            e.printStackTrace();  
        }  
        finally{  
            lock.unlock();  
        }
        return result;
    }
    
    
    public void trySingalTask(){
    	lock.lock();  
        try {
        	if(lock.hasWaiters(con) &&
        			Parameter.SendTweetEndClients.size() == Parameter.clientNum){
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