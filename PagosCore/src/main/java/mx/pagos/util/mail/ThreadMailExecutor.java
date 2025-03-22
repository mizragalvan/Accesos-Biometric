package mx.pagos.util.mail;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

@Service("ThreadMailExecutor")
public class ThreadMailExecutor {
    private static final int KEEP_ALIVE_TIME = 24;
    private static final int MAXIMUM_POOL_SIZE = 10;
    private static final int CORE_POOL_SIZE = 10;
    private final ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(100);
    private ThreadPoolExecutor threadPool = null;
	    
    public ThreadMailExecutor() {
        this.threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.HOURS,
                this.queue);
	}
	    
    public ThreadMailExecutor(final Integer corePoolSize, final Integer maxPoolSize, final Long keepAliveTime) {
        this.threadPool = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, TimeUnit.HOURS, this.queue);
    }
	    
    public final void runTask(final Runnable task) {
        this.threadPool.execute(task);
    }
 
    public final ArrayBlockingQueue<Runnable> getQueue() {
		return this.queue;
	}
    
    public final ThreadPoolExecutor getThreadPool() {
		return this.threadPool;
	}
    
    public final void shutDown() {
        this.threadPool.shutdown();
    }	    
}
