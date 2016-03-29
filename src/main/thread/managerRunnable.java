package main.thread;

import com.monitor.status;

import main.common.Global;

public class managerRunnable implements Runnable {
    private Global global;
    
    public managerRunnable(Global global){
        this.global = global;
    }
    @Override
    public void run() {
        if(global.manager_run || !global.jmeter_run) return;
        final String  ipadress = global.ipadress;
        synchronized (global) {
            global.manager_run = true;
        }
        float[] t_avgTime={0f,0f};
        int times=0;
        long time = System.currentTimeMillis();
        while(global.jmeter_run){
            float[] avgTime =status.getThoughput("http://"+ipadress+":8080/manager/status?XML=true","8080", "admin", "123456");
            if(avgTime[0]>=0){//<0是无工作
                t_avgTime[0] += avgTime[0];
                t_avgTime[1] += avgTime[1];
                times++;
            }
            try {
                Thread.sleep(40*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        time = System.currentTimeMillis()-time-3000;
        int threads=global.threads;
        System.out.println("Manager：共测量次数："+times+"次，当"+threads+"个用户并发时，平均响应时间="+t_avgTime[0]/times+" ，本地响应时间"+t_avgTime[1]/times+"，用时："+time/1000f);
        
        synchronized (global) {
            global.manager_run = false;
        }
    }

}
