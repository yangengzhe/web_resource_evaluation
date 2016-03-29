package main;

import main.common.Global;
import main.thread.jmeterRunnable;
import main.thread.managerRunnable;

public class performance {
    public static void main(String args[]) throws InterruptedException{
        Global global = new Global();
        jmeterRunnable jr = new jmeterRunnable(global);
        managerRunnable mr = new managerRunnable(global);
        long time;
        int[] threads = {1,2,3,4,5,6,7,8,9,10,50,100,200,300,400,500};
        for(int i = 0 ;i<threads.length;i++){
            time = System.currentTimeMillis();
            global.threads=threads[i];
            new Thread(jr).start();//同一线程不能run两次
            Thread.sleep(1000);
            new Thread(mr).start();
            while(global.jmeter_run || global.manager_run)
                Thread.sleep(1000);
            time = System.currentTimeMillis() - time;
            System.out.println("----完成当"+threads[i]+"个用户并发测试，共用时："+time/1000f);
        }
        
        
    }
}
