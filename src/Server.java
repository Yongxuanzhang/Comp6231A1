

	import java.rmi.registry.Registry;
	import java.rmi.registry.LocateRegistry;
	import java.io.IOException;
	import java.rmi.AccessException;
	import java.rmi.NotBoundException;
	import java.rmi.RemoteException;
	import java.rmi.server.UnicastRemoteObject;
	import java.util.HashMap;
import java.util.LinkedList;
	
	public class Server implements ServerOperation {

	    public static void main(String args[]) {

    		Server server = new Server();
    		server.start();

	    }
	    
		private Registry registry;
		private HashMap<String,HashMap<String,String>> record=new HashMap<String,HashMap<String,String>>();
		
	    public Server() {}

	    public void start() {
	        try {
	            Server obj = new Server();
	            ServerOperation stub = (ServerOperation) UnicastRemoteObject.exportObject(obj, 0);

	            // Bind the remote object's stub in the registry
	          
	            registry = LocateRegistry.getRegistry(1099);
	            //System.setProperty("java.rmi.server.hostname","192.168.1.2");
	            //registry.bind("rmi://localhost:1099/Hello", stub);
	           // registry.bind("Hello", stub);
	           
	          
	            registry.rebind("ServerOperation", stub);
	          
	            
	            
	            System.err.println("Server ready1");
	            
	            
	        } catch (Exception e) {
	            System.err.println("Server exception: " + e.toString());
	            e.printStackTrace();
	        }finally {
	        	 try {
					registry.unbind("Hello");
				} catch (AccessException e) {
				} catch (RemoteException e) {
				} catch (NotBoundException e) {
				}
	        }
	        
	    	
	    }



		@Override
		public boolean addEvent(String eventID,String eventType,String bookingCapacity) throws RemoteException {
			
			HashMap<String,String> rec=new HashMap<String,String>();
					rec.put(eventID, bookingCapacity);
					
			record.put(eventType, rec);
			
			System.out.println(record.get(eventType).get(eventID));
			
			return false;
		}
		
	    public String sayHello2() {
	        return "Hello, world3!";
	    }
	    public String sayHello() {
	        return "Hello, world!";
	    }
	    


		@Override
		public boolean removeEvent(String eventID, String eventType) {
			// TODO Auto-generated method stub
			HashMap<String,String> temp=new HashMap<String,String>();
			temp.put(eventID,record.get(eventType).get(eventID));
					
			record.remove(eventType, temp);
			
			System.out.println(record.get(eventType).get(eventID));
			//System.out.println(record.containsKey(eventType));
			
			return false;
		}

		@Override
		public LinkedList<String> listEventAvailability(String eventType) {
			// TODO Auto-generated method stub
			return null;
		}
	}

