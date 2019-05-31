import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

public class ClientUI{
    private int host;
    private String ID;
    private String bindobj;
    private Registry registry;
    private ServerOperation stub;
    private Log userLog;
    private HashMap<String,String> userInfo=new HashMap<String,String>();
	private boolean opFlag=true;
	private Scanner sc = new Scanner(System.in); 
    
    private ClientUI() throws SecurityException, IOException {       
        userLog=new Log(ID+"-userLog.txt");
        this.userInfoStart();
    }
    
    private void userInfoStart() {
      //User data in client side.
      userInfo.put("MTLM2345", "123");
      userInfo.put("MTLC2345", "123");
      userInfo.put("TORM2345", "123");
      userInfo.put("OTWM2345", "123");
      userInfo.put("OTWC2345", "123");
    }

    
    public void run() {
        
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
            
        }else {
            System.out.println("Wrong ID");
        }
        
          
    
    }
    

    
    
    public void callServerBookEvent(String customerID,String eventID,String eventType) throws RemoteException {
        
        int res=stub.bookEvent(customerID, eventID, eventType);
        System.out.println("booked "+res);
         if(res==1){
              System.out.println("booked successfully!");
              
         }
    }
    
    public void managerOperation() {
          //String response = stub.sayHello();
        //System.out.println("response: " + response);
        
        try {
        String response2 = stub.sayHello2();
        System.out.println("response: " + response2);
        
        if(stub.addEvent(ID,"OTWA100619", "Conference", 50)) {
            userLog.logger.info(ID+" has added"+" OTWA100619-"+ "Conference-"+ 50);
        }else {
            
        }
        
        System.out.println(stub.addEvent(ID,"OTWA100619", "Conference", 50));
        System.out.println(stub.addEvent(ID,"TORM100719","Conference",323));
        System.out.println(stub.addEvent(ID,"OTWA101519","Seminars",43));
        System.out.println(stub.addEvent(ID,"MTLA110519","Conference",413));
     
 
        //System.out.println(stub.bookEvent("TORM2345", "OTWA110519", "Conference"));
        //System.out.println(stub.bookEvent("TORM2545", "OTWA101519", "Seminars"));
        System.out.println(stub.listEventAvailability(ID,"Conference"));
        //System.out.println(stub.listEventAvailability("Seminars"));
        
        
        callServerBookEvent("MTLM2345", "MTLA110519", "Conference");
        
        if(stub.bookEvent("MTLM2345", "MTLA110519", "Conference")==1) {
          System.out.println("booked successfully!");
          
        }
        
        stub.getBookingSchedule("MTLM2345");
        
        for(String o : stub.getBookingSchedule("MTLM2345")) {
          System.out.println("MTLM2345--"+o);
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
    
    private boolean checkUser(String ID,String pw) {
      
      
      
      if(userInfo.containsKey(ID)) {
        if(userInfo.get(ID).equals(pw)) {
          return true;
        }
       
      }
      return false;
    }
    
    
    public void mOperation() throws RemoteException {
      
      System.out.println("**********************************"); 
      System.out.println("Please Select One Manager Operation:");       
      System.out.println("1. Book Event"); 
      System.out.println("2. Get BookingSchedule"); 
      System.out.println("3. Cancel Event"); 
      System.out.println("4. Add Event"); 
      System.out.println("5. Remove Event"); 
      System.out.println("6. List Event Availability"); 
      System.out.println("E for Exit"); 
      System.out.println("**********************************"); 
      Scanner sc = new Scanner(System.in); 
      String input = sc.nextLine();
      
      System.out.println(operate(input)); 
    }
    
    public void cOperation() throws RemoteException {
      System.out.println("**********************************"); 
      System.out.println("Please Select One Customer Operation:"); 
      System.out.println("1. Book Event"); 
      System.out.println("2. Get BookingSchedule"); 
      System.out.println("3. Cancel Event"); 
      System.out.println("E for Exit"); 
      System.out.println("**********************************"); 
      Scanner sc = new Scanner(System.in); 
      String input = sc.nextLine();
     
      System.out.println(operate(input)); 
    }
 
    public boolean operate(String input) throws RemoteException {
    	Scanner sct;
    	String eventID;
    	String eventType;
    	String customerID;
    	int bookingCapacity;
        switch(input) {
          case "1":          	
        	  sct = new Scanner(System.in);
        	  System.out.println("Please Enter Customer ID:");         	  
	          customerID = sct.nextLine(); 
	          System.out.println("Please Enter Event ID:"); 
	          eventID = sct.nextLine(); 
	          System.out.println("Please Enter Event Type:"); 
	          eventType = sct.nextLine(); 

	          int bookRes=stub.bookEvent(customerID, eventID, eventType);
	          
	
	          if(bookRes==1) {
	              System.out.println("Booked successfully."); 
	              
	              return true;
	          }else if(bookRes==-3) {
	          	 System.out.println("Capacity is full."); 
	             
	          	 return false;
	          }else if(bookRes==-2) {
	          	 System.out.println("Customer "+ customerID+" Already booked this event."); 
	             
	          	 return false;
	          }
	     
          case "2":
        	  sct = new Scanner(System.in);
        	  System.out.println("Please Enter Customer ID:");         	  
	          customerID = sct.nextLine(); 
	          System.out.println("Please Enter Event ID:"); 
	          eventID = sct.nextLine(); 

	          if(stub.cancelEvent(eventID, customerID)) {
	              System.out.println("Canceled successfully."); 
	              
	              return true;
	          }else{
	          	 System.out.println("Failure."); 
	             
	          	 return false;
	          }
        	  
          case "3":
        	  sct = new Scanner(System.in);
        	  System.out.println("Please Enter Customer ID:");         	  
	          customerID = sct.nextLine(); 
	          System.out.println("Please Enter Event ID:"); 
	          eventID = sct.nextLine(); 

	          if(stub.cancelEvent(eventID, customerID)) {
	              System.out.println("Canceled successfully."); 
	              
	              return true;
	          }else{
	          	 System.out.println("Failure."); 
	             
	          	 return false;
	          }
          case "4":

	        	sct = new Scanner(System.in);
	            System.out.println("Please Enter Event ID:"); 
	            eventID = sct.nextLine(); 
	            System.out.println("Please Enter Event Type:"); 
	            eventType = sct.nextLine(); 
	            System.out.println("Please Enter Booking Capacity:"); 
	            bookingCapacity = sct.nextInt(); 
            
	            if(stub.addEvent(ID, eventID, eventType, bookingCapacity)) {
	                System.out.println("Booked successfully."); 
	                
	                return true;
	            }else {
	            	 System.out.println("Wrong Input."); 
	                 
	            	 return false;
	            }
	            
          case "5":            
	          	  sct = new Scanner(System.in); 
	              System.out.println("Please Enter Event ID:"); 
	              eventID = sct.nextLine(); 
	              System.out.println("Please Enter Event Type:"); 
	              eventType = sct.nextLine(); 	             	              
	              if(stub.removeEvent(ID, eventID, eventType)) {
	                  System.out.println("Removed successfully."); 
	                  
	                  return true;
	              }else {
	              	 System.out.println("Wrong Input."); 
	                 
	              	 return false;
	              }   
	              
          case "6":
	        	  sct = new Scanner(System.in); 
	              System.out.println("Please Enter Event Type:"); 
	              eventType = sct.nextLine(); 
	              
	              if(stub.listEventAvailability(ID,eventType)!=null) {
	            	  System.out.println(eventType+stub.listEventAvailability(ID,eventType));
	              }else {
	               	 System.out.println("Wrong Input."); 
	                 
	               	 return false;
	              }            	
	              
	            return true;
          case "E":
        	  opFlag=false;
        	  break;
          case "e":
        	  opFlag=false;
        	  break;
          
        }
        
        
        return false;
    }
    
    private void login() {
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
        
    }
    
    
    public void demo() throws RemoteException {
      
      boolean inRun=true;
      
      while(inRun){
        Scanner sc = new Scanner(System.in); 
        System.out.println("Please Enter Your ID:"); 
        String userID = sc.nextLine(); 
        System.out.println("Please Enter Your Passward:"); 
        String password = sc.nextLine(); 
        
        if(this.checkUser(userID, password)==true) {
          System.out.println("Sucessfully Login in:"); 
          System.out.println("Name:"+userID+"\n");
          inRun=false;
          
          this.ID=userID;
          this.login();
          String userType=userID.substring(3, 4);

          while(opFlag) {
              if(userType.equals("M"))this.mOperation();
              if(userType.equals("C"))this.cOperation();
          }

          
          
        }else {
          System.out.println("Information Error. Please Try Again."); 
          
        }
                        
      } 
 


    }
    public static void main(String[] args) throws SecurityException, IOException {

  
        ClientUI client;

            client = new ClientUI();
            client.demo();


    }
}
