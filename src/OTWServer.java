import java.io.IOException;
import java.util.HashMap;

public class OTWServer {

public static void main(String args[]) throws SecurityException, IOException {

	HashMap<String,HashMap<String,Integer>> OTWrecord=new HashMap<String,HashMap<String,Integer>>();
	
	HashMap<String,Integer>value1=new HashMap<String,Integer>();
	HashMap<String,Integer>value2=new HashMap<String,Integer>();
	HashMap<String,Integer>value3=new HashMap<String,Integer>();
	
	value1.put("OTWA100619", 22);
	value2.put("OTWM110419", 23);
	value3.put("OTWE090519", 26);
	OTWrecord.put("Conference", value1);
	OTWrecord.put("Trade shows", value2);
	OTWrecord.put("Seminars", value3);
	
	Server server = new Server("OTW",2003,OTWrecord);
	server.start();
	//server.regist();

	}

}

