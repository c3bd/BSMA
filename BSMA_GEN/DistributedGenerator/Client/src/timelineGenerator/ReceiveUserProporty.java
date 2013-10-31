package timelineGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import object.FContent;

public class ReceiveUserProporty{
	public void receive() throws Exception{
		System.out.println(" Receive UserProporty start...");
		List<Integer> uids = Parameter.cluster.get(Parameter.clientID);
		int index =0;
		while(true){
			String Handler = Parameter.io.readHandler();
			if(Handler!=null){
				if(Handler.equals("UserProportyIden")){
					double pr = Parameter.io.readUserProportyIden();
					//System.out.println("UserProportyIden"+pr);
					Integer uid = uids.get(index);
					index++;
					Parameter.userProporty.put(uid, pr);
					Parameter.random_userPost.put(uid, new Random());
					Parameter.random_userRetweet.put(uid, new Random());
					
					Long time = Util.nextTime(Parameter.startTime,uid);
					if(time!=null){
						Parameter.pool.getFollowingPool().add(new FContent(time,uid));
					}
				}
				if(Handler.equals("UserFeedSize")){
					Parameter.userFeedSize = Parameter.io.readUserFeedSize();
				//	System.out.println("userFeedSize");
					break;
				}
			}
		}

		System.out.println(" Receive UserFeedSize and UserProporty end..."+index);
		
	}
	

	
	
	
	
	
	
	
	
}