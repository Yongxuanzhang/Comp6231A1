import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.LinkedList;

public class ManagerClient {
	private int host;
	private String bindobj;
	private Registry registry;
	private ServerOperation stub;
    private ManagerClient(int host,String bindobj) {
    	
    	this.host=host;
    	this.bindobj=bindobj;
    }
    

    
    public void start() {
    	
    	  //String host = (args.length < 1) ? null : args[0];
          try {
              registry = LocateRegistry.getRegistry(host);              
              
              stub = (ServerOperation) registry.lookup(bindobj);
                      
          } catch (Exception e) {
              System.err.println("Client exception: " + e.toString());
              e.printStackTrace();
          }
 	
    }
    
    public void demo() {
    	  //String response = stub.sayHello();
        //System.out.println("response: " + response);
        
    	try {
        String response2 = stub.sayHello2();
        System.out.println("response: " + response2);
        
        
        System.out.println(stub.addEvent("12121", "Conference", 50));
        System.out.println(stub.addEvent("2122","Conference",323));
        System.out.println(stub.addEvent("222","Conference",43));
        System.out.println(stub.addEvent("21312","Conference",413));
     
        //System.out.println(stub.listEventAvailability("Conference"));
        //System.out.println(stub.listEventAvailability("Seminars"));
        System.out.println(stub.bookEvent("TORM2345", "21312", "Conference"));
        System.out.println(stub.bookEvent("TORM2545", "111", "Seminars"));
        System.out.println(stub.listEventAvailability("Conference"));
        //System.out.println(stub.listEventAvailability("Seminars"));
        
      
        if(stub.bookEvent("TORM2345", "21312", "Conference")) {
      	  System.out.println("booked successfully!");
      	  
        }
        
        stub.getBookingSchedule("TORM2345");
        
        for(String o : stub.getBookingSchedule("TORM2345")) {
      	  System.out.println("TORM2345--"+o);
        }

        
        //System.out.println(stub.removeEvent("21312","Conference"));
        
       // System.out.println(stub.listEventAvailability("Conference"));
        
        /*
        LinkedList<String> res= new LinkedList<String>();
        res = stub.listEventAvailability("Conference");
        
        for(String obj : res) {
      	  System.out.println("The res are:"+obj);
        }*/
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
	
    }
    
    
    public static void main(String[] args) {

        String host = (args.length < 1) ? null : args[0];
        
 

        
        ManagerClient client1 = new ManagerClient(2003,"OTAManagerOperation");
        client1.start();
        client1.demo();
        ManagerClient client2 = new ManagerClient(2002,"MTLManagerOperation");
        client2.start();
        client2.demo();

    }
}
