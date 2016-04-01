package jmeter.rmi;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import com.system.runtime.pojo.systemStatusPojo;
import com.system.runtime.service.systemStatusService;


public class Client {
    public static void main(String args[]){
        try {
            systemStatusService rhello =(systemStatusService)Naming.lookup("rmi://172.29.131.78:8828/systemstatus");
            systemStatusPojo ssp = rhello.getRuntime();
            ssp = rhello.getRuntime();
            System.out.println(ssp.getMemory_uesd());
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NotBoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
