

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

	public class Server implements ServerOperation {

	
	private Registry registry;
	private HashMap<String,HashMap<String,Integer>> record=new HashMap<String,HashMap<String,Integer>>();
	private String Location;
	private LinkedList<String[]> userSchedule;
	
    public static void main(String args[]) {

    	Server server = new Server();
		server.start();
		ServerTemplate serverOTA = new ServerTemplate("OTA",2003);
		server.start();
		ServerTemplate serverMTA = new ServerTemplate("MTL",2002);
		server.start();
    }

    private Server() {
    	recordSetup(record);
    	Location="MTL";
    }

    public void start() {
	
        try {
        	Server obj = new Server();
        	Server obj_customer = new Server();
         
        	ServerOperation stub_Manager = (ServerOperation) UnicastRemoteObject.exportObject(obj, 0);
           // CustomerOperation stub_customer = (CustomerOperation) UnicastRemoteObject.exportObject(obj_customer, 2);

            // Bind the remote object's stub in the registry
          
            registry = LocateRegistry.getRegistry(1099);
            //System.setProperty("java.rmi.server.hostname","192.168.1.2");
            //registry.bind("rmi://localhost:1099/Hello", stub);
           // registry.bind("Hello", stub);
           
          
            registry.rebind("ManagerOperation", stub_Manager);
            //registry.rebind("CustomerOperation", stub_customer);
            
            
            System.err.println("Server ready");
            
            
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
        }
        
    	
    
	@Override
	public boolean removeEvent(String eventID, String eventType)  throws RemoteException{
		// TODO Auto-generated method stub
		HashMap<String,Integer> temp=new HashMap<String,Integer>();
		temp.put(eventID,record.get(eventType).get(eventID));
				
		record.remove(eventID, temp);
		
		System.out.println(record.get(eventType).get(eventID));
		return false;
	}


	@Override
	public boolean addEvent(String eventID,String eventType,Integer bookingCapacity) throws RemoteException {
		
		HashMap<String,Integer> rec=new HashMap<String,Integer>();
		rec.put(eventID, bookingCapacity);
				
		//record.put(eventType, rec);
		
		record.get(eventType).put(eventID, bookingCapacity);
		
		System.out.println(record.get(eventType).get(eventID));
		
		return false;
	}
	
	@Override
	public LinkedList<String> listEventAvailability(String eventType) throws RemoteException {
		
				
		LinkedList<String> res= new LinkedList<String>();
		
		Map<String,Integer> temp=record.get(eventType);
		
		for (Map.Entry<String,Integer> entry : temp.entrySet()) {
			 
		    System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
		    res.add(entry.getKey()+" "+entry.getValue());
		}
	
		
		return res;
	}

	public boolean bookEvent(String customerID, String eventID, String eventType) throws RemoteException {
		
		
		int newCapacity=record.get(eventType).get(eventID)+1;
		record.get(eventType).put(eventID, newCapacity);
		
		//userSchedule.addAll(customerID, eventID);
		String[] userTemp= {customerID, eventID};
		userSchedule.add(userTemp);
		
		return true;
	}


	public boolean cancelEvent(String eventID, String customerID) throws RemoteException {
		
		for(String[] o: userSchedule) {
			
			if(o[0].equalsIgnoreCase(eventID)&o[1].equalsIgnoreCase(customerID)) {
				userSchedule.remove(o);
				return true;
			}
		
		}
		
		
		
		return false;
	}


	public LinkedList<String[]> getBookingSchedule(String customerID, String eventID) throws RemoteException {
		
		
		return userSchedule;
	}
	
    public String sayHello2() {
        return "Welcome to MTL Server!";
    }
    public String sayHello() {
        return "Hello, world!";
    }
    


	
	private void recordSetup(HashMap<String,HashMap<String,Integer>> record){
		 
		
			HashMap<String,Integer>value=new HashMap<String,Integer>();
			
			value.put("111", 222);
		
		    record.put("Conference", value);
		
		
	
	}
	
	public HashMap<String,HashMap<String,Integer>> getRecord(){
		return record;
	}


	
}

