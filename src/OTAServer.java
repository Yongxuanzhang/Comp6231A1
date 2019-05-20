

	import java.rmi.registry.Registry;
	import java.rmi.registry.LocateRegistry;
	import java.io.IOException;
	import java.rmi.AccessException;
	import java.rmi.NotBoundException;
	import java.rmi.RemoteException;
	import java.rmi.server.UnicastRemoteObject;
	import java.util.HashMap;

		public class OTAServer extends Server implements ServerOperation, CustomerOperation {

		
		private Registry registry;
		private HashMap<String,HashMap<String,String>> otarecord=new HashMap<String,HashMap<String,String>>();
		private String Location;
		
	    public static void main(String args[]) {

	    	OTAServer server = new OTAServer();
    		server.start();

	    }

	    private OTAServer() {
	    	recordSetup(otarecord);
	    	Location="OTA";
	    }

	    public void start() {
    	
	        try {
	        	OTAServer obj = new OTAServer();
	        	OTAServer obj_customer = new OTAServer();
	         
	        	ServerOperation stub_Manager = (ServerOperation) UnicastRemoteObject.exportObject(obj, 0);
	            CustomerOperation stub_customer = (CustomerOperation) UnicastRemoteObject.exportObject(obj_customer, 3);

	            // Bind the remote object's stub in the registry
	          
	            registry = LocateRegistry.getRegistry(2003);
	            //System.setProperty("java.rmi.server.hostname","192.168.1.2");
	            //registry.bind("rmi://localhost:1099/Hello", stub);
	           // registry.bind("Hello", stub);
	           
	          
	            registry.rebind("OTAManagerOperation", stub_Manager);
	            registry.rebind("OTACustomerOperation", stub_customer);
	            
	            
	            System.err.println("OTAServer ready");
	            
	            
	        } catch (Exception e) {
	            System.err.println("Server exception: " + e.toString());
	            e.printStackTrace();
	        }
	        }
	        
	    	
	    
		@Override
		public boolean removeEvent(String eventID, String eventType) {
			// TODO Auto-generated method stub
			HashMap<String,String> temp=new HashMap<String,String>();
			temp.put(eventID,otarecord.get(eventType).get(eventID));
					
			otarecord.remove(eventID, temp);
			
			System.out.println(otarecord.get(eventType).get(eventID));
			return false;
		}


		@Override
		public boolean addEvent(String eventID,String eventType,String bookingCapacity) throws RemoteException {
			
			HashMap<String,String> rec=new HashMap<String,String>();
					rec.put(eventID, bookingCapacity);
					
			otarecord.put(eventType, rec);
			
			System.out.println(otarecord.get(eventType).get(eventID));
			
			return false;
		}
		
	    public String sayHello2() {
	        return "Hello, OTA!";
	    }
	    public String sayHello() {
	        return "Hello, world!";
	    }
	    

		@Override
		public boolean bookEvent(String customerID, String eventID, String eventType) throws RemoteException {
			System.out.println("OTA book");
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
			
		
		}
		
		public HashMap<String,HashMap<String,String>> getRecord(){
			return otarecord;
		}
		
	}
