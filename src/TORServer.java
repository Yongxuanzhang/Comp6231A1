

	import java.rmi.registry.Registry;
	import java.rmi.registry.LocateRegistry;
	import java.io.IOException;
	import java.rmi.AccessException;
	import java.rmi.NotBoundException;
	import java.rmi.RemoteException;
	import java.rmi.server.UnicastRemoteObject;
	import java.util.HashMap;
import java.util.LinkedList;

	public class TORServer implements ServerOperation, CustomerOperation {

	
	private Registry registry;
	private HashMap<String,HashMap<String,Integer>> torrecord=new HashMap<String,HashMap<String,Integer>>();
	private String Location;
	
    public static void main(String args[]) {

    	TORServer server = new TORServer();
		server.start();

    }

    private TORServer() {
    	recordSetup(torrecord);
    	Location="TOR";
    }

    public void start() {
	
        try {
        	TORServer obj = new TORServer();
        	TORServer obj_customer = new TORServer();
         
        	ServerOperation stub_Manager = (ServerOperation) UnicastRemoteObject.exportObject(obj, 0);
            CustomerOperation stub_customer = (CustomerOperation) UnicastRemoteObject.exportObject(obj_customer, 1);

            // Bind the remote object's stub in the registry
          
            registry = LocateRegistry.getRegistry(2004);
            //System.setProperty("java.rmi.server.hostname","192.168.1.2");
            //registry.bind("rmi://localhost:1099/Hello", stub);
           // registry.bind("Hello", stub);
           
          
            registry.rebind("TORManagerOperation", stub_Manager);
            registry.rebind("TORCustomerOperation", stub_customer);
            
            
            System.err.println("TORServer ready");
            
            
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
        }
        
    	
    
	@Override
	public boolean removeEvent(String eventID, String eventType) {
		// TODO Auto-generated method stub
		HashMap<String,Integer> temp=new HashMap<String,Integer>();
		temp.put(eventID,torrecord.get(eventType).get(eventID));
				
		torrecord.remove(eventID, temp);
		
		System.out.println(torrecord.get(eventType).get(eventID));
		return false;
	}


	@Override
	public boolean addEvent(String eventID,String eventType,Integer bookingCapacity) throws RemoteException {
		
		HashMap<String,Integer> rec=new HashMap<String,Integer>();
				rec.put(eventID, bookingCapacity);
				
		torrecord.put(eventType, rec);
		
		System.out.println(torrecord.get(eventType).get(eventID));
		
		return false;
	}
	
    public String sayHello2() {
        return "Hello, TOR!";
    }
    public String sayHello() {
        return "Hello, world!";
    }
    

	@Override
	public boolean bookEvent(String customerID, String eventID, String eventType) throws RemoteException {
		System.out.println("In TOR");
		return false;
	}

	@Override
	public boolean cancelEvent(String eventID, String eventType) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getBookingSchedule(String customerID, String eventID) {
		// TODO Auto-generated method stub
		return false;
	}
	
	private void recordSetup(HashMap<String,HashMap<String,Integer>> mtlrecord){
		
	
	}
	
	public HashMap<String,HashMap<String,Integer>> getRecord(){
		return torrecord;
	}

	@Override
	public LinkedList<String> listEventAvailability(String eventType) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}
	
}

