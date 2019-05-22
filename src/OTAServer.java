import java.util.HashMap;

public class OTAServer {

public static void main(String args[]) {

	HashMap<String,HashMap<String,Integer>> OTArecord=new HashMap<String,HashMap<String,Integer>>();
	
	HashMap<String,Integer>value=new HashMap<String,Integer>();
	
	value.put("111", 222);

	OTArecord.put("Conference", value);
	OTArecord.put("Trade shows", value);
	OTArecord.put("Seminars", value);
	
	Server server = new Server("OTA",2003,OTArecord);
	server.start();

	}

}

