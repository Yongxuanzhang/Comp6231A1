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
    private String location;
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
      operateM(input);
     // System.out.println(operate(input)); 
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
      operateC(input);
      //System.out.println(operate(input)); 
    }
 
    public boolean operateC(String input) throws RemoteException {
    	Scanner sct;
    	String eventID;
    	String eventType;
    	String customerID;
    	int bookingCapacity;
        switch(input) {
          case "1":          	
        	  sct = new Scanner(System.in);
        	  //System.out.println("Please Enter Customer ID:");         	  
	          customerID = this.ID; 
	          System.out.println("Please Enter Event ID:"); 
	          eventID = sct.nextLine(); 
	          System.out.println("Please Enter Event Type:"); 
	          eventType = sct.nextLine(); 

	          int bookRes=stub.bookEvent(customerID, eventID, eventType);
	          
	          //System.out.println("res is "+bookRes); 
              
	          if(bookRes==1) {
	              System.out.println("Booked successfully."); 
	              
	              return true;
	          }else if(bookRes==-3) {
	          	 System.out.println("Capacity is full."); 
	             
	          	 return false;
	          }else if(bookRes==-2) {
	          	 System.out.println("Customer "+ customerID+" Already booked this event."); 
	             
	          	 return false;
	          }else if(bookRes==-5) {
		          	 System.out.println("Event doesn't exist"); 
		             
		          	 return false;
	          }else if(bookRes==-6) {
		          	 System.out.println("Wrong type"); 
		             
		          	 return false;
		          }
	          else {
	        	  System.out.println("Wrong Input.");
	        	  return false;
	          }
	     
          case "2":
        	  sct = new Scanner(System.in);
        	 // System.out.println("Please Enter Customer ID:");         	  
	         // customerID = sct.nextLine(); 

	          customerID = this.ID; 
	          if(stub.getBookingSchedule(customerID)!=null) {
	              //System.out.println(stub.getBookingSchedule(customerID)); 
	              
	              for(String o:stub.getBookingSchedule(customerID)) {
	            	     System.out.println(o); 
	              }
	              
	              return true;
	          }else{
	          	 System.out.println("Failure."); 
	             
	          	 return false;
	          }
        	  
          case "3":
        	  sct = new Scanner(System.in);
        	  //System.out.println("Please Enter Customer ID:");         	  
        	  customerID = this.ID; 
	          System.out.println("Please Enter Event ID:"); 
	          eventID = sct.nextLine(); 
              System.out.println("Please Enter Event Type:"); 
              eventType = sct.nextLine(); 	
	          if(stub.cancelEvent(eventID,eventType,customerID)) {
	              System.out.println("Canceled successfully."); 
	              
	              return true;
	          }else{
	          	 System.out.println("Failure."); 
	             
	          	 return false;
	          }

          case "E":
        	  opFlag=false;
        	  break;
          case "e":
        	  opFlag=false;
        	  break;
        }
        return false;
        }
    
    public boolean operateM(String input) throws RemoteException {
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

	          String eloc=eventID.substring(0, 3);
	          String uloc=customerID.substring(0, 3);
	          if(eloc.equals("MTL")||eloc.equals("OTW")||eloc.equals("TOR")||uloc.equals("MTL")||uloc.equals("OTW")||
	              uloc.equals("TOR")||eventType.equals("Conference")||eventType.equals("Seminars")||eventType.equals("Trade shows")) {
	            int bookRes=stub.bookEvent(customerID, eventID, eventType);
	          
	            //System.out.println("res is "+bookRes); 
	              
	              if(bookRes==1) {
	                  System.out.println("Booked successfully."); 
	                  
	                  return true;
	              }else if(bookRes==-3) {
	                 System.out.println("Capacity is full."); 
	                 
	                 return false;
	              }else if(bookRes==-2) {
	                 System.out.println("Customer "+ customerID+" Already booked this event."); 
	                 
	                 return false;
	              }else if(bookRes==-5) {
	                     System.out.println("Event doesn't exist"); 
	                     
	                     return false;
	                  }
	          
	          
	          }
	          else {
	        	  System.out.println("Wrong Input.");
	        	  return false;
	          }
	     
          case "2":
        	  sct = new Scanner(System.in);
        	  System.out.println("Please Enter Customer ID:");         	  
	          customerID = sct.nextLine(); 


	          if(stub.getBookingSchedule(customerID)!=null) {
	              //System.out.println(stub.getBookingSchedule(customerID)); 
	              
	              for(String o:stub.getBookingSchedule(customerID)) {
	            	     System.out.println(o); 
	              }
	              
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
              System.out.println("Please Enter Event Type:"); 
              eventType = sct.nextLine(); 	
              
	          if(stub.cancelEvent(eventID,eventType,customerID)) {
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
            
	            if(!eventID.substring(0, 3).equals(location)) {
	            	 //System.out.println(eventID.substring(0, 3)+" "+location); 
	            	  System.out.println("Wrong Event ID Format."); 
	            	  return false;
	            }
	            
	            if(stub.addEvent(ID, eventID, eventType, bookingCapacity)) {
	                System.out.println("Added successfully."); 
	                
	                return true;
	            }else {
	            	 System.out.println("You can not add event to other cities."); 
	                 
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
	              LinkedList<String> res=stub.listEventAvailability(ID,eventType);
	              if(res!=null) {
	            	  System.out.println(eventType+res);
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
      location = ID.substring(0, 3);
      
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
