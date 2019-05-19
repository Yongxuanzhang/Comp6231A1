

	import java.rmi.registry.Registry;
	import java.rmi.registry.LocateRegistry;
	import java.io.IOException;
	import java.rmi.AccessException;
	import java.rmi.NotBoundException;
	import java.rmi.RemoteException;
	import java.rmi.server.UnicastRemoteObject;
	import java.util.HashMap;

		public class MTLServer extends Server implements ServerOperation, CustomerOperation {

		
		private Registry registry;
		private HashMap<String,HashMap<String,String>> mtlrecord=new HashMap<String,HashMap<String,String>>();
		
	    public static void main(String args[]) {

	    	MTLServer server = new MTLServer();
    		server.start();

	    }

	    public MTLServer() {}

	    public void start() {
	    	
	    	
	        try {
	        	MTLServer obj = new MTLServer();
	        	MTLServer obj_customer = new MTLServer();
	            ServerOperation stub = (ServerOperation) UnicastRemoteObject.exportObject(obj, 0);
	            CustomerOperation stub_customer = (CustomerOperation) UnicastRemoteObject.exportObject(obj_customer, 2);

	            // Bind the remote object's stub in the registry
	          
	            registry = LocateRegistry.getRegistry(2002);
	            //System.setProperty("java.rmi.server.hostname","192.168.1.2");
	            //registry.bind("rmi://localhost:1099/Hello", stub);
	           // registry.bind("Hello", stub);
	           
	          
	            registry.rebind("ServerOperation3", stub);
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
					
			mtlrecord.put(eventType, rec);
			
			System.out.println(mtlrecord.get(eventType).get(eventID));
			
			return false;
		}
		
	    public String sayHello2() {
	        return "Hello, MTL!";
	    }
	    public String sayHello() {
	        return "Hello, world!";
	    }
	    

		@Override
		public boolean bookEvent(String customerID, String eventID, String eventType) throws RemoteException {
			// TODO Auto-generated method stub
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
	}

