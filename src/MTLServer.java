import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;

public class MTLServer  {


//MTL2002;OTA2003;TOR2004

public static void main(String args[]) throws SecurityException, IOException {


	
	
	HashMap<String,HashMap<String,Integer>> MTLrecord=new HashMap<String,HashMap<String,Integer>>();
		
	HashMap<String,Integer>value=new HashMap<String,Integer>();
	HashMap<String,Integer>value1=new HashMap<String,Integer>();
	HashMap<String,Integer>value2=new HashMap<String,Integer>();
	HashMap<String,Integer>value3=new HashMap<String,Integer>();
	value.put("MTLA100619", 2);
	value2.put("MTLA100617", 3);
	value3.put("MTLA100618", 1);
	//value.put("MTLA100619", 2);
	
	MTLrecord.put("Conference", value);
	MTLrecord.put("Trade shows", value2);
	MTLrecord.put("Seminars", value3);
	
	Server server = new Server("MTL",2002,MTLrecord);
	server.start();
	server.recordSetup(MTLrecord);
	




}

}

