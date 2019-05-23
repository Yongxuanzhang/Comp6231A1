import java.io.IOException;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.LinkedList;

public class Client extends Thread {
	private int host;
	private String ID;
	private String bindobj;
	private Registry registry;
	private ServerOperation stub;
	private Log userLog;
	
    private Client(String ID) throws SecurityException, IOException {
    	this.ID=ID;
    	userLog=new Log(ID+"-userLog.txt");
    	//this.host=host;
    	//this.bindobj=bindobj;
    }
    

    
    public void run() {
    	
    	  //String host = (args.length < 1) ? null : args[0];
    	String location = ID.substring(0, 3);
    	
    	switch(location) {
    	
    	case"MTL":
    		host=2002;
    		bindobj="MTLManagerOperation";
    		break;
      	case"OTA":
    		host=2003;
    		bindobj="OTAManagerOperation";
    		break;
      	case"TOR":
    		host=2004;
    		bindobj="TORManagerOperation";
    		break;
    	default:
    		System.out.println("Wrong ID");
  	
    	}
    	
    	
          try {
              registry = LocateRegistry.getRegistry(host);              
              
              stub = (ServerOperation) registry.lookup(bindobj);
                      
          } catch (Exception e) {
              System.err.println("Client exception: " + e.toString());
              e.printStackTrace();
          }
          
          
      	String type = ID.substring(3, 4);
    	
      	if(type.equals("M")) {
      		managerOperation();
      	}
      	else if(type.equals("C")) {
      		
      	}else {
      		System.out.println("Wrong ID");
      	}
      	
          
 	
    }
    
   // public void demo() {
    	

   // }
    
    
    public void managerOperation() {
    	  //String response = stub.sayHello();
        //System.out.println("response: " + response);
        
    	try {
        String response2 = stub.sayHello2();
        System.out.println("response: " + response2);
        
        if(stub.addEvent("OTWA100619", "Conference", 50)) {
        	userLog.logger.info(ID+" has added"+" OTWA100619-"+ "Conference-"+ 50);
        }else {
        	
        }
        
        System.out.println(stub.addEvent("OTWA100619", "Conference", 50));
        System.out.println(stub.addEvent("TORM100719","Conference",323));
        System.out.println(stub.addEvent("OTWA101519","Seminars",43));
        System.out.println(stub.addEvent("OTWA110519","Conference",413));
     
 
        System.out.println(stub.bookEvent("TORM2345", "OTWA110519", "Conference"));
        System.out.println(stub.bookEvent("TORM2545", "OTWA101519", "Seminars"));
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

        //String host = (args.length < 1) ? null : args[0];
        
        
        
 

        
    	Client client1;
		try {
			client1 = new Client("OTAC2345");
	        client1.start();
	        // client1.demo();
	         Client client2 = new Client("MTLM2345");
	         client2.start();
	         Client client3 = new Client("MTLM2345");
	         client3.start();
	        // client2.demo();
			
			
			
		} catch (SecurityException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


    }
}
