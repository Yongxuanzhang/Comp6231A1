import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;

public class MTLServer  {


//MTL2002;OTA2003;TOR2004

public static void main(String args[]) throws SecurityException, IOException {


	
	
	HashMap<String,HashMap<String,Integer>> MTLrecord=new HashMap<String,HashMap<String,Integer>>();
		
	HashMap<String,Integer>value=new HashMap<String,Integer>();
	
	value.put("111", 222);

	MTLrecord.put("Conference", value);
	MTLrecord.put("Trade shows", value);
	MTLrecord.put("Seminars", value);
	
	Server server = new Server("MTL",2002,MTLrecord);
	server.start();
	server.recordSetup(MTLrecord);
	




}

}

