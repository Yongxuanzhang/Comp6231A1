
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerOperation extends Remote {
    String sayHello() throws RemoteException;
    String sayHello2() throws RemoteException;
    boolean addEvent(String eventID,String eventType,String bookingCapacity) throws RemoteException;
    boolean removeEvent (String eventID, String eventType)throws RemoteException;
    
    
}