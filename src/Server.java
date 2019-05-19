

	import java.rmi.registry.Registry;
	import java.rmi.registry.LocateRegistry;
	import java.io.IOException;
	import java.rmi.RemoteException;
	import java.rmi.server.UnicastRemoteObject;

	public class Server implements Hello {

	    public Server() {}

	    public String sayHello() {
	        return "Hello, world!";
	    }

	    public static void main(String args[]) {

	        try {
	            Server obj = new Server();
	            Hello stub = (Hello) UnicastRemoteObject.exportObject(obj, 0);

	            // Bind the remote object's stub in the registry
	          
	            Registry registry = LocateRegistry.getRegistry(2001);
	            //System.setProperty("java.rmi.server.hostname","192.168.1.2");
	            //registry.bind("rmi://localhost:1099/Hello", stub);
	            registry.bind("Hello", stub);
	            System.err.println("Server ready");
	        } catch (Exception e) {
	            System.err.println("Server exception: " + e.toString());
	            e.printStackTrace();
	        }
	        
	        try {
				System.in.read();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	}

