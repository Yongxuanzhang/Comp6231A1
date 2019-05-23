import java.util.HashMap;

public class TORServer{


public static void main(String args[]) {

	
	HashMap<String,HashMap<String,Integer>> TORrecord=new HashMap<String,HashMap<String,Integer>>();
	
	HashMap<String,Integer>value=new HashMap<String,Integer>();
	
	value.put("111", 222);

	TORrecord.put("Conference", value);
	TORrecord.put("Trade shows", value);
	TORrecord.put("Seminars", value);
	
	Server server = new Server("TOR",2004,TORrecord);
	//server.start();

	}

}

