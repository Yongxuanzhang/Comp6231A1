
	
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

	
	/**
	 * The server class
	 * @author z_yongxu
	 *
	 */
	
public class Server implements ServerOperation {

	
	
	private HashMap<String,HashMap<String,Integer>> record=new HashMap<String,HashMap<String,Integer>>();
	private HashMap<String,LinkedList<String>> userSchedule=new HashMap<String,LinkedList<String>>();
	private int port;
	private int receivePort1;
	private int receivePort2;
	private int targetPort1;
	private int targetPort2;
	private String targetStub1;
	private String targetStub2;
	private DatagramSocket aSocket1;
	private DatagramSocket aSocket2;
	private String location;
	private Registry registry;
	private Registry registry1;    
	private Registry registry2;
	private Log serverLog;
	private ServerOperation stub1;
	private ServerOperation stub2;
	private String[] receiveList1=null;
	private String[] receiveList2=null;
	private boolean binded=false;


	private DatagramSocket aSocket = null;
	private FileOutputStream fileOutputStream = null;
	private File serverFile;
	
    public static void main(String args[]) {

  
    }


    
    public Server(String location,int port,HashMap<String,HashMap<String,Integer>> newRecord) throws SecurityException, IOException {
    	//recordSetup(record);
    	//Location="MTL";
    	this.location=location;
    	this.port=port;
    	this.serverLog= new Log(location+"serverOperation.txt");
    	serverFile= new File(location+"-serverOperation.txt");
    	 if(!serverFile.exists()){
    		 serverFile.createNewFile();
         }
    	this.record=newRecord;
    	receivePort1=port+3000;
    	receivePort2=port+3005;
    	
    	
    	switch(port){
 
    	case 2002:
    		this.targetStub1="OTWManagerOperation";
    		this.targetStub2="TORManagerOperation"; 
    		targetPort1=2003;
    		targetPort2=2004;
    		break;
    	case 2003:
    		this.targetStub1="MTLManagerOperation";
    		this.targetStub2="TORManagerOperation";
    		targetPort1=2002;
    		targetPort2=2004;
    		break;	
    	case 2004:
    		this.targetStub1="OTWManagerOperation";
    		this.targetStub2="MTLManagerOperation";
    		targetPort1=2003;
    		targetPort2=2002;
    		break;
    		
    	
    		
    	}
/*
		try {
			registry1 = LocateRegistry.getRegistry(targetPort1);    
			registry2 = LocateRegistry.getRegistry(targetPort2);

			stub1 = (ServerOperation) registry1.lookup(targetStub1);
			stub2 = (ServerOperation) registry2.lookup(targetStub2);
			
		} catch (Exception e) {

			e.printStackTrace();
		}    
*/
    }

    public void regist() {
    	
    	
		try {
			registry1 = LocateRegistry.getRegistry(targetPort1);    
			registry2 = LocateRegistry.getRegistry(targetPort2);

			stub1 = (ServerOperation) registry1.lookup(targetStub1);
			stub2 = (ServerOperation) registry2.lookup(targetStub2);
			
		} catch (Exception e) {

			e.printStackTrace();
		}  
    }
    
    public void start() {
	
        try {
           	Server obj = new Server(location,port,record);     
        	ServerOperation stub = (ServerOperation) UnicastRemoteObject.exportObject(obj, 0);
           	
           	registry = LocateRegistry.getRegistry(port);                     
            registry.rebind(location+"ManagerOperation", stub);
                       
            System.err.println(location+"ManagerOperation"+" has blinded");                       
            System.err.println("Server "+location+" ready");
            
            
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
        
        }
        
    public void writeFile(String info) throws IOException {
		 fileOutputStream = new FileOutputStream(serverFile);
		 Date date = new Date();	 
	     fileOutputStream.write(info.getBytes());
	     fileOutputStream.write(date.toString().getBytes());
	     fileOutputStream.flush();
	     fileOutputStream.close();
    }
    
	@Override
	public boolean addEvent(String managerID, String eventID, String eventType, Integer bookingCapacity)throws RemoteException {
		HashMap<String,Integer> rec=new HashMap<String,Integer>();
		
		
		if(!record.containsKey(eventType))return false;
		else {
			
			rec.put(eventID, bookingCapacity);
			
			//record.put(eventType, rec);		
			record.get(eventType).put(eventID, bookingCapacity);
			
			System.out.println(record.get(eventType).get(eventID));
			//serverLog.logger.info("test");
			//serverLog.logger.info(managerID+" has added "+eventType+eventID+" of "+location+"Server.  ");
			
			 try {
				 String tempWrite=managerID+" has added "+eventType+eventID+" of "+location+"Server";
				 writeFile(tempWrite);
			} catch (Exception e) {

				e.printStackTrace();
			}
			
			return true;
		}
		
		

	}


    
	@Override
	public synchronized boolean removeEvent(String ID,String eventID, String eventType)  throws RemoteException{
		if(!record.containsKey(eventType)||!record.get(eventType).containsKey(eventID))return false;
		//if(record.get(eventType).containsKey(eventID)!=true)return false;
		
		
		HashMap<String,Integer> temp=new HashMap<String,Integer>();
		temp.put(eventID,record.get(eventType).get(eventID));
		
		record.remove(eventID, temp);
		//for(HashMap<String,LinkedList<String>> o:userSchedule)
		for (Map.Entry<String,LinkedList<String>> entry : userSchedule.entrySet()) {
			 for(String o:userSchedule.get(entry)) {
				if(eventID.equals(o)) {
					userSchedule.get(entry).remove(o);
				}
			 }
		    //System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
		    //res.add(entry.getKey()+" "+entry.getValue());
		}
		
		System.out.println(record.get(eventType).get(eventID));
		
		serverLog.logger.info(ID+" has removed "+eventType+eventID+" of "+location+"Server");
		return true;
	}


	
	@Override
	public synchronized LinkedList<String> listEventAvailability(String managerID,String eventType) throws RemoteException {
		
		if(!binded) {
			this.regist();
			binded=true;
		}
		
		if(!record.containsKey(eventType))return null;
		
		Thread t = new Thread(new Runnable(){	//running thread which will publish record counts using UDP/sockets 
			public void run(){
				//while(true) {
				try {
					requestData(eventType);
				} catch (NotBoundException | RemoteException e) {
					e.printStackTrace();
				}
			}
				//}
		});
		t.start();
		
		LinkedList<String> res= new LinkedList<String>();
		
		Map<String,Integer> temp=record.get(eventType);
		
		for (Map.Entry<String,Integer> entry : temp.entrySet()) {
			 
		    System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
		    res.add(entry.getKey()+" "+entry.getValue());
		}
	
		//this.sendData(res);
		System.out.println("Before receive data in "+location);
		this.receiveData();
		
		//String[] receiveList1;
		if(receiveList1.length>0) {
			for(String ts:receiveList1) {
				res.add(ts);
			}
		}

		if(receiveList2.length>0) {
			for(String ts:receiveList2) {
				res.add(ts);
			}
		}


		serverLog.logger.info(managerID+" has listed "+eventType+" of "+location+"Server");
		return res;
	}

	public LinkedList<String> thisEventList(String eventType) throws RemoteException {
		
		
		LinkedList<String> res= new LinkedList<String>();
		
		Map<String,Integer> temp=record.get(eventType);
		
		for (Map.Entry<String,Integer> entry : temp.entrySet()) {
			 
		    System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
		    res.add(entry.getKey()+" "+entry.getValue());
		}
	

		return res;
	}
	
	public void requestData(String eventType) throws AccessException, RemoteException, NotBoundException {
		
		//System.out.println("targetStub1 "+targetStub1);
		//Registry registry1 = LocateRegistry.getRegistry(targetPort1);    
		//Registry registry2 = LocateRegistry.getRegistry(targetPort2);

		//ServerOperation stub1 = (ServerOperation) registry1.lookup(targetStub1);
		//ServerOperation stub2 = (ServerOperation) registry2.lookup(targetStub2);
		stub1.sendData(eventType, receivePort1);
		stub2.sendData(eventType, receivePort2);
		
		
	}
	
	@Override
    public void sendData(String eventType,int targetPort) throws RemoteException {
    	StringBuffer bf=new StringBuffer();
    	LinkedList<String> data=this.thisEventList(eventType);
    	
    	//bf.append("this is from"+location);
    	for(String s:data) {
    		bf.append(s+".");
    	}
    	
    	try {
			aSocket = new DatagramSocket();
			
			byte[] sData=bf.toString().getBytes();
			
			InetAddress address = InetAddress.getByName("localhost");
			//int port=8088;
			DatagramPacket sendPacket=new DatagramPacket(sData,sData.length,address,targetPort);
			//DatagramPacket sendPacket2=new DatagramPacket(sData,sData.length,address,targetPort2);
			aSocket.send(sendPacket);
			//aSocket.send(sendPacket2);
			System.out.println("message is from"+location);
			System.out.println(bf.toString());
			aSocket.close();
		} catch (Exception e) {			
			e.printStackTrace();
		}finally {if(aSocket != null) aSocket.close();}
    	
    }
    public void receiveData() {
    	try {
    		  //int receivePort=port+5000;
    		  aSocket1 = new DatagramSocket(receivePort1);
    		  aSocket2 = new DatagramSocket(receivePort2);
			  byte[] data1= new byte[2000];
			  byte[] data2= new byte[2000];
		      DatagramPacket recevPacket1 = new DatagramPacket(data1,data1.length);
		      DatagramPacket recevPacket2 = new DatagramPacket(data2,data2.length);
			  aSocket1.receive(recevPacket1);
			  aSocket2.receive(recevPacket2);
			  
			  byte[] d1=recevPacket1.getData();
			  int dlen1 = recevPacket1.getLength();
			  String info1 = new String(d1,0,dlen1,"UTF-8");
			  receiveList1=info1.split("\\.");
		
			  
			  byte[] d2=recevPacket2.getData();
			  int dlen2 = recevPacket2.getLength();
			  String info2 = new String(d2,0,dlen2,"UTF-8");
			  receiveList2=info2.split("\\.");
			   System.out.println("message is "+info1);
			   System.out.println("message is "+info2);
			   aSocket1.close();
			   aSocket2.close();
		} catch (Exception e) {			
			e.printStackTrace();
		}finally {
			if(aSocket1 != null) aSocket1.close();
			if(aSocket2 != null) aSocket2.close();
		}
    	
    	
    }
	
	public synchronized int bookEvent(String customerID, String eventID, String eventType) throws RemoteException {
		
		
		/**
		 * use error code for return:
		 * -1:Book more than 3 events in 1 month from other servers;
		 * -2:Same eventID and eventType;
		 * -3:The capacity is full;
		 * 
		 */
		//userSchedule.addAll(customerID, eventID);
		//String[] userTemp= {customerID, eventID};
		//userSchedule.add(userTemp);
		
		
		System.out.println(customerID);
		System.out.println(userSchedule.containsKey(customerID)+" -- ");
		
		
	
		String eventLoc= eventID.substring(0, 3);
		
		if(eventLoc.equals(location)!=true) {
			if(eventLoc.equals(targetStub1.substring(0, 3))) {
				return  stub1.bookEvent(customerID, eventID, eventType);
				
			}else if(eventLoc.equals(targetStub2.substring(0, 3))) {
				return stub2.bookEvent(customerID, eventID, eventType);
				 
			}
			else return -4;
		}
		
		int checkTimes=0;
		
		if(userSchedule.get(customerID)!=null) {
		for(String o:userSchedule.get(customerID)) {
			checkTimes++;
			}
		}
		if(checkTimes>=3)return -1;
	
		//if(userSchedule.containsKey(customerID)&&userSchedule.get(customerID).contains(eventID)) return -1;
		//if(record.get(eventType).containsKey(eventID))return -2;

		
	      if(userSchedule.containsKey(customerID)) {
	            
	            for(String s:userSchedule.get(customerID)) {
	                
	                if(s.equals(eventType+" "+eventID)) {
	                    return -2;
	                }
	            }

	        }
		
		
		//check capacity	
		int newCapacity=record.get(eventType).get(eventID);
		if(newCapacity==0)return -3;
		else {
			newCapacity--;
			record.get(eventType).put(eventID, newCapacity);
		}
		

		
		if(userSchedule.containsKey(customerID)) {
			userSchedule.get(customerID).add(eventID);
			
		}else {
			LinkedList<String> usTemp= new LinkedList<String>();
			usTemp.add(eventType+" "+eventID);		
			userSchedule.put(customerID, usTemp);
		}
		
		
		return 1;
	}


	public synchronized boolean cancelEvent(String eventID, String customerID) throws RemoteException {

		boolean result=false;//else return false;
		System.out.println("value of uc "+userSchedule.containsKey(customerID));
		
		if(userSchedule.containsKey(customerID)) {
			userSchedule.get(customerID).remove(eventID);
			
			for (Map.Entry<String,HashMap<String,Integer>> entry : record.entrySet()) {
				
				for(Map.Entry<String,Integer> entry2 : entry.getValue().entrySet()) {
					System.out.println("value of e2 "+(entry2.getKey()));
					
					if(entry2.getKey().equals(eventID)) {
						
						record.get(entry.getKey()).remove(eventID, entry2.getValue());
						int tc=entry2.getValue()+1;
						record.get(entry.getKey()).put(eventID, tc);
						result=true;
					}
				}
			}
			
			if(!result)return false;
			
			return true;
		}else {
			return false;
		}
		
		
	}

	@Override
	public LinkedList<String> getBookingSchedule(String customerID) throws RemoteException {
		// TODO Auto-generated method stub
		return userSchedule.get(customerID);
	}


    public String sayHello2() {
        return "Welcome to "+location+" Server!";
    }
    public String sayHello() {
        return "Hello, world!";
    }
    

	
    
	public void recordSetup(HashMap<String,HashMap<String,Integer>> record){
		 
		
			//HashMap<String,Integer>value=new HashMap<String,Integer>();
			
			//value.put("111", 222);
		
		    //record.put("Conference", value);
		
			this.record=record;
	
	}
	
	public HashMap<String,HashMap<String,Integer>> getRecord(){
		return record;
	}







	
}

