

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

	public class ServerTemplate implements ServerOperation, CustomerOperation {

	
	private Registry registry;
	private HashMap<String,HashMap<String,Integer>> localRecord=new HashMap<String,HashMap<String,Integer>>();
	private String location;
	private int port;
	
    public static void main(String args[]) {

    	ServerTemplate server = new ServerTemplate("MTL",2005);
		server.start();

    }

    public ServerTemplate(String location,int port) {
    	//recordSetup(localRecord);
    	//Location="MTL";
    	this.location=location;
    	this.port=port;
    	
    }

    public void start() {
	
        try {
        	ServerTemplate obj = new ServerTemplate(location,port);
        	ServerTemplate obj_customer = new ServerTemplate(location,port);
         
        	ServerOperation stub_Manager = (ServerOperation) UnicastRemoteObject.exportObject(obj, 0);
            CustomerOperation stub_customer = (CustomerOperation) UnicastRemoteObject.exportObject(obj_customer, 0);
            
            // Bind the remote object's stub in the registry
          
            registry = LocateRegistry.getRegistry(port);

            registry.rebind(location+"ManagerOperation", stub_Manager);
            registry.rebind(location+"CustomerOperation", stub_customer);
            
            System.err.println(location+"ManagerOperation"+" has blinded");
            
            
            System.err.println("Server "+location+" ready");
            
            
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
        }
        
    	
    
	@Override
	public boolean removeEvent(String eventID, String eventType) {
		// TODO Auto-generated method stub
		HashMap<String,Integer> temp=new HashMap<String,Integer>();
		temp.put(eventID,localRecord.get(eventType).get(eventID));
				
		localRecord.remove(eventID, temp);
		
		System.out.println(localRecord.get(eventType).get(eventID));
		return false;
	}


	@Override
	public boolean addEvent(String eventID,String eventType,Integer bookingCapacity) throws RemoteException {
		
		HashMap<String,Integer> rec=new HashMap<String,Integer>();
		rec.put(eventID, bookingCapacity);
				
		//localRecord.put(eventType, rec);
		
		localRecord.get(eventType).put(eventID, bookingCapacity);
		
		System.out.println(localRecord.get(eventType).get(eventID));
		
		return false;
	}
	
	@Override
	public LinkedList<String> listEventAvailability(String eventType) {
		
				
		LinkedList<String> res= new LinkedList<String>();
		
		Map<String,Integer> temp=localRecord.get(eventType);
		
		for (Map.Entry<String,Integer> entry : temp.entrySet()) {
			 
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
	
	private void recordSetup(HashMap<String,HashMap<String,Integer>> localRecord){
		 
		
			HashMap<String,Integer>value=new HashMap<String,Integer>();
			
			value.put("111", 222);
		
		    localRecord.put("Conference", value);
		
		
	
	}
	
	public HashMap<String,HashMap<String,Integer>> getRecord(){
		return localRecord;
	}
	
}

