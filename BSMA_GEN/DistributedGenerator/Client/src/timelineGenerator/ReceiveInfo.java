package timelineGenerator;



public class ReceiveInfo{
	public void receive() {
		System.out.println("receive userInfo start...");
		// TODO Auto-generated method stub
		boolean receivedSFI = false;
		boolean receivedUI = false;
		while(true){
			String Handler="";
			try {
				Handler =Parameter.io.readHandler();
				if(Handler!=null){
					if(Handler.equals("SubFollowInfo")){
						Parameter.io.readSubFollowInfo();
						receivedSFI = true;
					}
					if(Handler.equals("UserInfo")){
						Parameter.io.readUserInfo();
						receivedUI = true;
					}
					if(receivedSFI && receivedUI){
						break;
					}
				}
		
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.err.println("receive userInfo:"+e);
			}
		}	
		
		System.out.println("receive userInfo end...");
			
		
		
	}
}