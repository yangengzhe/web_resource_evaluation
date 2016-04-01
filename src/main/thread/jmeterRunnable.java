package main.thread;

import com.jmeter.jmx.pojo.summaryPojo;
import com.jmeter.jmx.util.execLinux;
import com.jmeter.jmx.util.parseReport;

import main.common.Global;

public class jmeterRunnable implements Runnable {
    private Global global;
    
    public jmeterRunnable(Global global){
        this.global = global;
    }
    @Override
    public void run() {
        if(global.jmeter_run)    return;
        int threads=0;
        final String  ipadress = global.ipadress;
        synchronized (global) {
            global.jmeter_run = true;
            threads = global.threads;
        }
        long time = System.currentTimeMillis();
        summaryPojo summarys = null;
        String cmd = "cd /usr/local/apache-jmeter-2.13/bin/ ;sh jmeter -n -t /Users/yangengzhe/Documents/云平台/物流_游客查询.jmx -Jthread="+threads+" -Jipadress="+ipadress;
        summarys = parseReport.parsePojo(execLinux.exec(cmd).toString());
        time = System.currentTimeMillis() - time;
        System.out.println("JMeter：当"+threads+"个用户并发时，吞吐率="+summarys.getThroughput()+"，错误率："+summarys.getError()+"%，用时："+time/1000f);
        
        synchronized (global) {
            global.jmeter_run = false;
        }
    }

}
