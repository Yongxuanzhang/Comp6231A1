import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class CustomerClient {
	private int host;
	private String bindobj;
	private String clientID;
	
    private CustomerClient (String clientID,int host,String bindobj) {
    	
    	this.host=host;
    	this.bindobj=bindobj;
    	this.clientID=clientID;
    }
    

    
    public void start() {
    	
    	  //String host = (args.length < 1) ? null : args[0];
          try {
              Registry registry = LocateRegistry.getRegistry(host);
              
              //The customer can only access CustomerOperation on remote server.
              CustomerOperation stub = (CustomerOperation) registry.lookup(bindobj);
              
              System.out.println(stub.bookEvent("TOR1212","1212", "Conference"));
              
              
              
              
              /*
              ServerOperation stub = (ServerOperation) registry.lookup(bindobj);
            
              
              String response = stub.sayHello();
              System.out.println("response: " + response);
              
   
              String response2 = stub.sayHello2();
              System.out.println("response: " + response2);
              System.out.println(stub.addEvent("21312","Conference","33"));
              System.out.println(stub.removeEvent("21312","Conference"));
              */
              
              
          } catch (Exception e) {
              System.err.println("Client exception: " + e.toString());
              e.printStackTrace();
          }
     
    	
    	
    }
    public static void main(String[] args) {

        //String host = (args.length < 1) ? null : args[0];
        
        CustomerClient  client = new CustomerClient ("TORM2345",2003,"OTACustomerOperation");
        client.start();
        CustomerClient  client2 = new CustomerClient ("TORM2345",2002,"CustomerOperation");
        client2.start();
        CustomerClient  client3 = new CustomerClient ("TORM2345",2004,"TORCustomerOperation");
        client3.start();
        

    }

}
