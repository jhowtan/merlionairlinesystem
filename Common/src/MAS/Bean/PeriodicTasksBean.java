package MAS.Bean;

import MAS.Common.Utils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

@Singleton
@Startup
public class PeriodicTasksBean {
    @EJB
    FFPBean ffpBean;

    @PostConstruct
    public void init() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                System.out.println("Running daily scheduled task...");

                // Handles status expiry and status qualification expiry
                ffpBean.periodicUpdateFFP();
            }
        };
        Timer timer = new Timer();
        long everyDay = 1000 * 60 * 60 * 24;
        Date midnight = Utils.addTimeToDate(Utils.hoursFromNow(24), "00:00");
        // Executes every midnight
        timer.scheduleAtFixedRate(task, midnight, everyDay);
    }

}
