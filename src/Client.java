import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RemoteException;
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
    	userLog=new Log("Logs."+ID+"-userLog.txt");
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
      	case"OTW":
    		host=2003;
    		bindobj="OTWManagerOperation";
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
      		//TODO
      	}else {
      		System.out.println("Wrong ID");
      	}
      	
          
 	
    }
    
   // public void demo() {
    	

   // }
    
    
    public void callServerBookEvent(String customerID,String eventID,String eventType) throws RemoteException {
    	
    	int res=stub.bookEvent(customerID, eventID, eventType);
    	//System.out.println("booked "+res);
    	 if(res==1){
         	  System.out.println(customerID+"booked "+eventID+" successfully!");
          	  
         }else if(res==-2) {
        	  System.out.println(customerID+" has already booked"+eventID);
         }
         else if(res==-3) {
       	  System.out.println("The capacity of "+eventID+" is full");
        }
    	 else {
           System.out.println("Failure Code:"+res);
         }
    }
    
    public void managerOperation() {
    	  //String response = stub.sayHello();
        //System.out.println("response: " + response);
        
    	try {
        String response2 = stub.sayHello2();
        System.out.println("response from server:" + response2);
        
        /*
        if(stub.addEvent(ID,"OTWA100619", "Conference", 50)) {
        	userLog.logger.info(ID+" has added"+" OTWA100619-"+ "Conference-"+ 50);
        }else {
        	
        }
        */
        System.out.println(stub.addEvent(ID,"OTWA090619", "Conference", 50));
        System.out.println(stub.addEvent(ID,"TORM100719","Conference",323));
        System.out.println(stub.addEvent(ID,"OTWA101519","Seminars",43));
        System.out.println(stub.addEvent(ID,"MTLA110519","Conference",2));
     
 
        //System.out.println(stub.bookEvent("TORM2345", "OTWA110519", "Conference"));
        //System.out.println(stub.bookEvent("TORM2545", "OTWA101519", "Seminars"));
        System.out.println(stub.listEventAvailability(ID,"Conference"));  
        callServerBookEvent(ID, "MTLA100619", "Conference");
        callServerBookEvent(ID, "OTWA100619", "Conference");
        System.out.println(stub.listEventAvailability(ID,"Conference"));

        //System.out.println("BookingSchedule of "+ID+" is:");
        stub.getBookingSchedule(ID);
        
        if(stub.getBookingSchedule(ID)!=null) {
          for(String o : stub.getBookingSchedule(ID)) {
            System.out.println(ID+o);
          }

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
			client1 = new Client("OTWM2345");
	        client1.start();
	        Client client2 = new Client("MTLM2344");
	        client2.start();
	        Client client3 = new Client("MTLM2345");
	        client3.start();
            Client client4 = new Client("MTLM2346");
            client4.start();
			
			
			
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}


    }
}
