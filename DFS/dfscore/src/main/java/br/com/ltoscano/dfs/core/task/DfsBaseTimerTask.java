package br.com.ltoscano.dfs.core.task;

import br.com.ltoscano.dfs.core.random.DfsRandomHelper;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author ltosc
 */
public abstract class DfsBaseTimerTask extends DfsBaseTask implements DfsITask
{
    private final ScheduledExecutorService scheduledExecutor;
    private final TimerTask timerTask;
    private final int minDelay;
    private final int maxDelay;
    
    public DfsBaseTimerTask(Class derivedClass, String taskName, int delay)
    {
        this(derivedClass, taskName, delay, delay);
    }
    
    public DfsBaseTimerTask(Class derivedClass, String taskName, int minDelay, int maxDelay)
    {
        super(derivedClass, taskName);
        
        this.scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
        
        this.timerTask = new TimerTask() 
        {
            @Override
            public void run() 
            {
                try 
                {
                    performTask();
                }
                catch (Exception ex) 
                {
                    handleError(ex);
                }
                finally
                {
                    scheduledExecutor.schedule(timerTask, DfsRandomHelper.randomInteger(minDelay, maxDelay), TimeUnit.MILLISECONDS);
                }
            }
        };
        
        this.minDelay = minDelay;
        this.maxDelay = maxDelay;
    }
    
    @Override
    public void start()
    {
        scheduledExecutor.schedule(timerTask, DfsRandomHelper.randomInteger(minDelay, maxDelay), TimeUnit.MILLISECONDS);
    }
    
    @Override
    public void stop()
    {
        scheduledExecutor.shutdownNow();
    }
}
