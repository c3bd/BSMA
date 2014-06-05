package object;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class TweetsInClients{

	private final ReentrantLock lock = new ReentrantLock();  
    private final Condition con = lock.newCondition();
    private HashMap<Integer,LinkedList<Tweet>> tweets = new HashMap<Integer,LinkedList<Tweet>>();//<clientID,tweets>
    
    public TweetsInClients(Integer clientNum){
    	for(int i=0;i<clientNum;i++){
    		tweets.put(i, new LinkedList<Tweet>()); 
		}
    }
    
    public void addTweet(Tweet m){
    	lock.lock();  
        try {
        	tweets.get(m.getClientID()).add(m);
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
    
    public Tweet getFirstTask(Integer clientID){
    	lock.lock();  
    	Tweet result = null;
        try {
        	while(tweets.get(clientID).isEmpty()){
        		con.await();
        	}
        	result = tweets.get(clientID).poll();
        }catch (Exception e) {  
            e.printStackTrace();  
        }  
        finally{  
            lock.unlock();  
        }
        return result;
    }
    
    public Integer size(Integer clientID){
    	lock.lock();  
    	Integer result = 0;
        try {
        	result = tweets.get(clientID).size();
        }catch (Exception e) {  
            e.printStackTrace();  
        }  
        finally{  
            lock.unlock();  
        }
        return result;
    }
}