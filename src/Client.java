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
    

    
    public void callServerBookEvent(String customerID,String eventID,String eventType) throws RemoteException {
    	
    	int res=stub.bookEvent(customerID, eventID, eventType);
    	//System.out.println("booked "+res);
    	 if(res==1){
         	  System.out.println(customerID+" booked "+eventID+" successfully!");
         	  userLog.logger.info(customerID+" has booked "+eventID+ " successfully!");
         }else if(res==-2) {
        	  System.out.println(customerID+" has already booked"+eventID);
        	  userLog.logger.info(customerID+" has already booked"+eventID);
         }
         else if(res==-3) {
       	  System.out.println(ID+" :The capacity of "+eventID+" is full");
       	 userLog.logger.info(customerID+" cannot book "+eventID);
        }
    	 else {
           System.out.println("Failure Code:"+res);
         }
    }
    
    
    public void callAddEvent(String customerID,String eventID,String eventType,int Capacity) throws RemoteException {
      
      if(!customerID.substring(0, 3).equals(eventID.substring(0, 3))) {
        System.out.println(customerID+" cannot add "+eventID+" from other cities");
        userLog.logger.info(customerID+" cannot add "+eventID);
      }else {
        boolean res=stub.addEvent(customerID, eventID, eventType, Capacity);
        if(res) {
          System.out.println(customerID+" has added "+eventID+" successfully!");
          userLog.logger.info(customerID+" has added "+eventID+ " successfully!");
        }else {
          userLog.logger.info(customerID+" cannot add "+eventID);
        }
      }
      

    }
    
    
    public void managerOperation() {

    	try {
        String response2 = stub.sayHello2();
        System.out.println("response from server:" + response2);
        
        
        System.out.println(stub.listEventAvailability(ID,"Conference"));  
        
        
        callAddEvent(ID,"OTWA090619", "Conference", 50);
        callAddEvent(ID,"TORM100719","Conference",323);
        callAddEvent(ID,"OTWA101519","Seminars",43);
        callAddEvent(ID,"MTLA110519","Conference",2);
        
        

       
        callServerBookEvent(ID, "MTLA100619", "Conference");
        
          
        stub.getBookingSchedule(ID);
        
        if(stub.getBookingSchedule(ID)!=null) {
          for(String o : stub.getBookingSchedule(ID)) {
            System.out.println(ID+o);
          }

        }
        //System.out.println(stub.listEventAvailability(ID,"Conference"));

        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
	
    }
    
    public static void demoevent() {
      
    }
    
    public static void demo1(){
      try {
        Client client1 = new Client("OTWM2345");
        client1.start();
        Client client2 = new Client("MTLM2344");
        client2.start();
        Client client5 = new Client("TORM2346");
        client5.start();
        
        
        
    } catch (SecurityException | IOException e) {
        e.printStackTrace();
    }

    }
    
    public static void main(String[] args) {

          demo1();
		

    }
}
