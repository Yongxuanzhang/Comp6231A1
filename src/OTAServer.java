import java.io.IOException;
import java.util.HashMap;

public class OTAServer {

public static void main(String args[]) throws SecurityException, IOException {

	HashMap<String,HashMap<String,Integer>> OTArecord=new HashMap<String,HashMap<String,Integer>>();
	
	HashMap<String,Integer>value=new HashMap<String,Integer>();
	
	value.put("OTWA110619", 22);

	OTArecord.put("Conference", value);
	OTArecord.put("Trade shows", value);
	OTArecord.put("Seminars", value);
	
	Server server = new Server("OTA",2003,OTArecord);
	server.start();
	//server.regist();

	}

}

