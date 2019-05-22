

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
	private int port;
	private String location;
	//private LinkedList<String[]> userSchedule;
	private HashMap<String,LinkedList<String>> userSchedule=new HashMap<String,LinkedList<String>>();
	
    public static void main(String args[]) {

  
    }


    
    public Server(String location,int port,HashMap<String,HashMap<String,Integer>> newRecord) {
    	//recordSetup(record);
    	//Location="MTL";
    	this.location=location;
    	this.port=port;
    	this.record=newRecord;
    }

    public void start() {
	
        try {
           	Server obj = new Server(location,port,record);     
        	ServerOperation stub = (ServerOperation) UnicastRemoteObject.exportObject(obj, 0);
            registry = LocateRegistry.getRegistry(port);                     
            registry.rebind(location+"ManagerOperation", stub);
                       
            System.err.println(location+"ManagerOperation"+" has blinded");                       
            System.err.println("Server "+location+" ready");
            
            
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
		
		return true;
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
		
		
		
		//userSchedule.addAll(customerID, eventID);
		//String[] userTemp= {customerID, eventID};
		//userSchedule.add(userTemp);
		System.out.println(customerID);
		System.out.println(userSchedule.containsKey(customerID)+" -- ");
		if(userSchedule.containsKey(customerID)&&userSchedule.get(customerID).contains(eventID)) return false;
		
		int newCapacity=record.get(eventType).get(eventID)+1;
		record.get(eventType).put(eventID, newCapacity);
		
		if(userSchedule.containsKey(customerID)) {
			userSchedule.get(customerID).add(eventID);
			
		}else {
			LinkedList<String> usTemp= new LinkedList<String>();
			usTemp.add(eventID);		
			userSchedule.put(customerID, usTemp);
		}
		
		
		return true;
	}


	public boolean cancelEvent(String eventID, String customerID) throws RemoteException {
		/*
		for(String[] o: userSchedule) {
			
			if(o[0].equalsIgnoreCase(eventID)&o[1].equalsIgnoreCase(customerID)) {
				userSchedule.remove(o);
				return true;
			}
		
		}
		*/
		
		if(userSchedule.containsKey(customerID)) {
			userSchedule.get(customerID).remove(eventID);
			
			
			return true;
		}else {
			return false;
		}
		
		
	}

	@Override
	public LinkedList<String> getBookingSchedule(String customerID) throws RemoteException {
		// TODO Auto-generated method stub
		return userSchedule.get(customerID);
	}


    public String sayHello2() {
        return "Welcome to "+location+" Server!";
    }
    public String sayHello() {
        return "Hello, world!";
    }
    


	
	public void recordSetup(HashMap<String,HashMap<String,Integer>> record){
		 
		
			HashMap<String,Integer>value=new HashMap<String,Integer>();
			
			value.put("111", 222);
		
		    record.put("Conference", value);
		
			this.record=record;
	
	}
	
	public HashMap<String,HashMap<String,Integer>> getRecord(){
		return record;
	}





	
}

