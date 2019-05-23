import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedList;

public interface CustomerOperation extends Remote{

    boolean bookEvent(String customerID,String eventID,String eventType) throws RemoteException;
    boolean cancelEvent (String eventID, String customerID)throws RemoteException;
    LinkedList<String>  getBookingSchedule (String customerID)throws RemoteException;
}
