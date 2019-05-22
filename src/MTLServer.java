import java.rmi.RemoteException;
import java.util.HashMap;

public class MTLServer  {


//MTL2002;OTA2003;TOR2004

public static void main(String args[]) {


	
	
	HashMap<String,HashMap<String,Integer>> MTLrecord=new HashMap<String,HashMap<String,Integer>>();
		
	HashMap<String,Integer>value=new HashMap<String,Integer>();
	
	value.put("111", 222);

	MTLrecord.put("Conference", value);
	MTLrecord.put("Trade shows", value);
	MTLrecord.put("Seminars", value);
	
	Server server = new Server("MTL",2002,MTLrecord);
	server.start();
	server.recordSetup(MTLrecord);
	
	try {
		server.listEventAvailability("Conference");
	} catch (RemoteException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	try {
		
		//server.addEvent("12121", "Seminars", 50);
		//server.addEvent("11", "Trade shows", 500);
		
		server.listEventAvailability("Conference");
	} catch (RemoteException e) {
	
		e.printStackTrace();
	}


}

}

