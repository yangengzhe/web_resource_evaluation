package main;

import main.common.Global;
import main.thread.jmeterRunnable;
import main.thread.managerRunnable;
import main.thread.systemStatusRunnable;

public class performance {
    public static void main(String args[]) throws InterruptedException{
        Global global = new Global();
        jmeterRunnable jr = new jmeterRunnable(global);
        managerRunnable mr = new managerRunnable(global);
        systemStatusRunnable ssr = new systemStatusRunnable(global);
        long time;
        int[] threads = {450};
        for(int i = 0 ;i<threads.length;i++){
            time = System.currentTimeMillis();
            global.threads=threads[i];
            new Thread(jr).start();//同一线程不能run两次
            Thread.sleep(1000);
            new Thread(mr).start();
            new Thread(ssr).start();
            while(global.jmeter_run || global.manager_run || global.system_run)
                Thread.sleep(1000);
            time = System.currentTimeMillis() - time;
            System.out.println("----完成当"+threads[i]+"个用户并发测试，共用时："+time/1000f);
        }
        
        
    }
}
