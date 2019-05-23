
	
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

	
	/**
	 * The server class
	 * @author z_yongxu
	 *
	 */
	
public class Server implements ServerOperation {

	
	private Registry registry;
	private HashMap<String,HashMap<String,Integer>> record=new HashMap<String,HashMap<String,Integer>>();
	private int port;
	private int receivePort1;
	private int receivePort2;
	private String targetStub1;
	private String targetStub2;
	private int targetPort1;
	private int targetPort2;
	private DatagramSocket aSocket1;
	private DatagramSocket aSocket2;
	private String location;
	
	private Registry registry1;    
	private Registry registry2;

	private ServerOperation stub1;
	private ServerOperation stub2;
	
	//private LinkedList<String[]> userSchedule;
	private HashMap<String,LinkedList<String>> userSchedule=new HashMap<String,LinkedList<String>>();
	private DatagramSocket aSocket = null;
	
    public static void main(String args[]) {

  
    }


    
    public Server(String location,int port,HashMap<String,HashMap<String,Integer>> newRecord) {
    	//recordSetup(record);
    	//Location="MTL";
    	this.location=location;
    	this.port=port;
    	
    	this.record=newRecord;
    	receivePort1=port+3000;
    	receivePort2=port+3005;
    	
    	
    	switch(port){
 
    	case 2002:
    		this.targetStub1="OTAManagerOperation";
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
    		this.targetStub1="OTAManagerOperation";
    		this.targetStub2="MTLManagerOperation";
    		targetPort1=2003;
    		targetPort2=2002;
    		break;
    		
    	
    		
    	}

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
        
    	
    
	@Override
	public synchronized boolean  removeEvent(String eventID, String eventType)  throws RemoteException{
		// TODO Auto-generated method stub
		HashMap<String,Integer> temp=new HashMap<String,Integer>();
		temp.put(eventID,record.get(eventType).get(eventID));
				
		record.remove(eventID, temp);
		
		System.out.println(record.get(eventType).get(eventID));
		return false;
	}


	@Override
	public synchronized boolean addEvent(String eventID,String eventType,Integer bookingCapacity) throws RemoteException {
		
		HashMap<String,Integer> rec=new HashMap<String,Integer>();
		rec.put(eventID, bookingCapacity);
				
		//record.put(eventType, rec);
		
		record.get(eventType).put(eventID, bookingCapacity);
		
		System.out.println(record.get(eventType).get(eventID));
		
		return true;
	}
	
	@Override
	public LinkedList<String> listEventAvailability(String eventType) throws RemoteException {
		
				
		
		Thread t = new Thread(new Runnable(){	//running thread which will publish record counts using UDP/sockets 
			public void run(){
				try {
					requestData(eventType);
				} catch (NotBoundException | RemoteException e) {
					e.printStackTrace();
				}
			}
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
		
		
		//multi threading


		
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
    	
    	bf.append("this is from"+location);
    	for(String s:data) {
    		bf.append(s);
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
			  byte[] d2=recevPacket2.getData();
			  int dlen2 = recevPacket2.getLength();
			 String info2 = new String(d2,0,dlen2,"UTF-8");
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
	
	public synchronized boolean bookEvent(String customerID, String eventID, String eventType) throws RemoteException {
		
		
		
		//userSchedule.addAll(customerID, eventID);
		//String[] userTemp= {customerID, eventID};
		//userSchedule.add(userTemp);
		
		
		System.out.println(customerID);
		System.out.println(userSchedule.containsKey(customerID)+" -- ");
		if(userSchedule.containsKey(customerID)&&userSchedule.get(customerID).contains(eventID)) return false;
		
		int newCapacity=record.get(eventType).get(eventID)+1;
		record.get(eventType).put(eventID, newCapacity);
		
		if(userSchedule.containsKey(customerID)) {
			userSchedule.get(customerID).add(eventID);
			
		}else {
			LinkedList<String> usTemp= new LinkedList<String>();
			usTemp.add(eventID);		
			userSchedule.put(customerID, usTemp);
		}
		
		
		return true;
	}


	public synchronized boolean cancelEvent(String eventID, String customerID) throws RemoteException {
		/*
		for(String[] o: userSchedule) {
			
			if(o[0].equalsIgnoreCase(eventID)&o[1].equalsIgnoreCase(customerID)) {
				userSchedule.remove(o);
				return true;
			}
		
		}
		*/
		
		if(userSchedule.containsKey(customerID)) {
			userSchedule.get(customerID).remove(eventID);
			
			
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

