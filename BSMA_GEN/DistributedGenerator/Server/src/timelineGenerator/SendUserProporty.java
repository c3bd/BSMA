package timelineGenerator;

import generatorServer.IO;

import java.util.List;



public class SendUserProporty {

	IO io ;
	Integer clientID;
	public SendUserProporty(IO io,Integer clientID){
		this.io = io;
		this.clientID = clientID;
	}
	
	public void send() {
		// TODO Auto-generated method stub
		System.out.println("send UserProporty start...");
		List<Integer> userIds = ParameterGen.cluster.get(clientID);
		
		try {
			for(Integer id:userIds){
				double iden= 0;
				iden = ParameterGen.getUserProportyIden(id);
				io.writeUserProportyIden(iden);
			}
			io.writeUserFeedSize(ParameterGen.userFeedSizeInClient.get(clientID));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("send to"+clientID+" UserProporty end..." +userIds.size() );
	}
	
}