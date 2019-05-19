import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CustomerOperation extends Remote{

    boolean bookEvent(String customerID,String eventID,String eventType) throws RemoteException;
    boolean cancelEvent (String eventID, String eventType)throws RemoteException;
    boolean getBookingSchedule (String customerID,String eventID)throws RemoteException;
}
