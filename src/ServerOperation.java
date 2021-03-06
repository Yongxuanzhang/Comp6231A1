
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.LinkedList;

public interface ServerOperation extends Remote {
    String sayHello() throws RemoteException;
    String sayHello2() throws RemoteException;
    
    public void sendData(String eventType,int targetPort) throws RemoteException;
    boolean addEvent(String managerID,String eventID,String eventType,Integer bookingCapacity) throws RemoteException;
    boolean removeEvent (String ID,String eventID, String eventType)throws RemoteException;
    LinkedList<String> listEventAvailability(String managerID,String eventType)throws RemoteException ;
    
    int bookEvent(String customerID,String eventID,String eventType) throws RemoteException;
    boolean cancelEvent (String eventID,String eventType, String customerID)throws RemoteException;
    LinkedList<String>  getBookingSchedule (String customerID)throws RemoteException;
    
}