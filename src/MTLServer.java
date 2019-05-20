import java.rmi.RemoteException;

public class MTLServer  {


//MTL2002;OTA2003;TOR2004

public static void main(String args[]) {

	ServerTemplate server = new ServerTemplate("MTL",2002);
	server.start();
	
	try {
		server.addEvent("12121", "Conference", 50);
	} catch (RemoteException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}


}

}

