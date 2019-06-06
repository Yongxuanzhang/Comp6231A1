import java.io.IOException;
import java.util.HashMap;

public class TORServer{


public static void main(String args[]) throws SecurityException, IOException {

	
	HashMap<String,HashMap<String,Integer>> TORrecord=new HashMap<String,HashMap<String,Integer>>();
	
	HashMap<String,Integer>value=new HashMap<String,Integer>();
	HashMap<String,Integer>value1=new HashMap<String,Integer>();
	HashMap<String,Integer>value2=new HashMap<String,Integer>();
	HashMap<String,Integer>value3=new HashMap<String,Integer>();

	value.put("TORA100519", 211);
	value1.put("TORA100519", 111);
	value2.put("TORA100519", 311);

	TORrecord.put("Conference", value);
	TORrecord.put("Trade shows", value1);
	TORrecord.put("Seminars", value2);
	
	Server server = new Server("TOR",2004,TORrecord);
	server.start();

	}

}

