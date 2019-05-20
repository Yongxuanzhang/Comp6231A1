

	import java.rmi.registry.Registry;
	import java.rmi.registry.LocateRegistry;
	import java.io.IOException;
	import java.rmi.AccessException;
	import java.rmi.NotBoundException;
	import java.rmi.RemoteException;
	import java.rmi.server.UnicastRemoteObject;
	import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

	public class MTLServer extends Server implements ServerOperation, CustomerOperation {

	
	private Registry registry;
	private HashMap<String,HashMap<String,String>> mtlrecord=new HashMap<String,HashMap<String,String>>();
	private String Location;
	
    public static void main(String args[]) {

    	MTLServer server = new MTLServer();
		server.start();

    }

    private MTLServer() {
    	recordSetup(mtlrecord);
    	Location="MTL";
    }

    public void start() {
	
        try {
        	MTLServer obj = new MTLServer();
        	MTLServer obj_customer = new MTLServer();
         
        	ServerOperation stub_Manager = (ServerOperation) UnicastRemoteObject.exportObject(obj, 0);
            CustomerOperation stub_customer = (CustomerOperation) UnicastRemoteObject.exportObject(obj_customer, 2);

            // Bind the remote object's stub in the registry
          
            registry = LocateRegistry.getRegistry(2002);
            //System.setProperty("java.rmi.server.hostname","192.168.1.2");
            //registry.bind("rmi://localhost:1099/Hello", stub);
           // registry.bind("Hello", stub);
           
          
            registry.rebind("ManagerOperation", stub_Manager);
            registry.rebind("CustomerOperation", stub_customer);
            
            
            System.err.println("MTLServer ready");
            
            
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
        }
        
    	
    
	@Override
	public boolean removeEvent(String eventID, String eventType) {
		// TODO Auto-generated method stub
		HashMap<String,String> temp=new HashMap<String,String>();
		temp.put(eventID,mtlrecord.get(eventType).get(eventID));
				
		mtlrecord.remove(eventID, temp);
		
		System.out.println(mtlrecord.get(eventType).get(eventID));
		return false;
	}


	@Override
	public boolean addEvent(String eventID,String eventType,String bookingCapacity) throws RemoteException {
		
		HashMap<String,String> rec=new HashMap<String,String>();
		rec.put(eventID, bookingCapacity);
				
		//mtlrecord.put(eventType, rec);
		
		mtlrecord.get(eventType).put(eventID, bookingCapacity);
		
		System.out.println(mtlrecord.get(eventType).get(eventID));
		
		return false;
	}
	
	@Override
	public LinkedList<String> listEventAvailability(String eventType) {
		
				
		LinkedList<String> res= new LinkedList<String>();
		
		Map<String,String> temp=mtlrecord.get(eventType);
		
		for (Map.Entry<String,String> entry : temp.entrySet()) {
			 
		    System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
		    res.add(entry.getKey()+" "+entry.getValue());
		}
	
		
		return res;
	}
	
	
    public String sayHello2() {
        return "Welcome to MTL Server!";
    }
    public String sayHello() {
        return "Hello, world!";
    }
    

	@Override
	public boolean bookEvent(String customerID, String eventID, String eventType) throws RemoteException {
		// TODO Auto-generated method stub
		
		
		
		System.out.println("MTL book");
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
	
	private void recordSetup(HashMap<String,HashMap<String,String>> mtlrecord){
		 
		
			HashMap<String,String>value=new HashMap<String,String>();
			
			value.put("111", "222");
		
		    mtlrecord.put("Conference", value);
		
		
	
	}
	
	public HashMap<String,HashMap<String,String>> getRecord(){
		return mtlrecord;
	}
	
}

