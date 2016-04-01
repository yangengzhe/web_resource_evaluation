package main.thread;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import com.system.runtime.pojo.systemStatusPojo;
import com.system.runtime.service.systemStatusService;

import main.common.Global;

public class systemStatusRunnable implements Runnable {
private Global global;
    
    public systemStatusRunnable(Global global){
        this.global = global;
    }
    @Override
    public void run() {
        if(global.system_run || !global.jmeter_run) return;
        final String  ipadress = global.ipadress;
        synchronized (global) {
            global.system_run = true;
        }
        try {
            systemStatusService rhello =(systemStatusService)Naming.lookup("rmi://"+ipadress+":8828/systemstatus");
            long mem_avg=0,mem_max=0;
            double cpu_avg=0,cpu_max=0,rx_avg=0,rx_max=0,tx_avg=0,tx_max=0;
            int times=0;
            long time = System.currentTimeMillis();
            while(global.jmeter_run){
                systemStatusPojo ssp = rhello.getRuntime();
                mem_avg+=ssp.getMemory_uesd();
                mem_max = mem_max >ssp.getMemory_uesd()?mem_max:ssp.getMemory_uesd();
                cpu_avg+=ssp.getCpu_combined();
                cpu_max = cpu_max>ssp.getCpu_combined()?cpu_max:ssp.getCpu_combined();
                rx_avg+=ssp.getRx_speed();
                rx_max = rx_max>ssp.getRx_speed()?rx_max:ssp.getRx_speed();
                tx_avg+=ssp.getTx_speed();
                tx_max = tx_max>ssp.getTx_speed()?tx_max:ssp.getTx_speed();
                times++;
                try {
                    Thread.sleep(10*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            time = System.currentTimeMillis()-time-3000;
            int threads=global.threads;
            System.out.println("System：共测量次数："+times+"次，当"+threads+"个用户并发时，平均内存="+mem_avg/times+"M，最大内存="+mem_max+"M ，平均CPU:"+cpu_avg/times+"，最大CPU："+cpu_max+"，平均速率："+rx_avg/times+"kb/s（下载），"+tx_avg/times+"kb/s（上传），最大速率："+rx_max+"kb/s（下载），"+tx_max+"kb/s（上传）");
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
        synchronized (global) {
            global.system_run = false;
        }
    }

}
