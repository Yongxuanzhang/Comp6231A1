import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.LinkedList;

public class ManagerClient {
	private int host;
	private String bindobj;
	
    private ManagerClient(int host,String bindobj) {
    	
    	this.host=host;
    	this.bindobj=bindobj;
    }
    

    
    public void start() {
    	
    	  //String host = (args.length < 1) ? null : args[0];
          try {
              Registry registry = LocateRegistry.getRegistry(host);
              
              
              ServerOperation stub = (ServerOperation) registry.lookup(bindobj);
            
              
              String response = stub.sayHello();
              System.out.println("response: " + response);
              
   
              String response2 = stub.sayHello2();
              System.out.println("response: " + response2);
              System.out.println(stub.addEvent("21312","Conference",33));
              System.out.println(stub.addEvent("2122","Conference",323));
              System.out.println(stub.addEvent("222","Conference",43));
              System.out.println(stub.addEvent("21322","Conference",413));
              
              
              System.out.println(stub.removeEvent("21312","Conference"));
              
              System.out.println(stub.listEventAvailability("Conference"));
              
              LinkedList<String> res= new LinkedList<String>();
              res = stub.listEventAvailability("Conference");
              
              for(String obj : res) {
            	  System.out.println("The res are:"+obj);
              }
              
              
          } catch (Exception e) {
              System.err.println("Client exception: " + e.toString());
              e.printStackTrace();
          }
     
    	
    	
    }
    public static void main(String[] args) {

        String host = (args.length < 1) ? null : args[0];
        
 
        ManagerClient client2 = new ManagerClient(2002,"ManagerOperation");
        client2.start();
        

    }
}
