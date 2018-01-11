package net.tthy.executor;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** ClassName:OrderedQueuePoolExecutor <br/>
 * Function: TODO (对象顺序连接池). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-6-26 上午10:11:11 <br/>
 * 
 * @author lyh
 * @version
 * @see */
public class OrderedQueuePoolExecutor<K> extends ThreadPoolExecutor  {
	
	
	private Logger logger = LoggerFactory.getLogger(OrderedQueuePoolExecutor.class);
	
	/** A default value for the KeepAlive delay */
	private static final int DEFAULT_KEEP_ALIVE = 30;
	
	/** 每一个key,对应着一个相应的列表 **/
	private OrderedQueuePool<K> pool = new OrderedQueuePool<K>();
	
	/** 线程池名称 **/
	private String name;
	
	/** 核心线程池 **/
	private int corePoolSize;
	
	/** 线程池最大队列 **/
	private int maxQueueSize;
	
	//private Thread idleThread = null;
	
	private boolean isRunning = true;
	
	private int INTERAL_TIME = 300;
	
	/** 300秒 **/
	private int DEL_TIME = 60;
	//LinkedBlockingQueue
	
	public OrderedQueuePoolExecutor(String name, int corePoolSize, int maxQueueSize) {
		super(corePoolSize, maxQueueSize, DEFAULT_KEEP_ALIVE, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
		this.name = name;
		this.corePoolSize = corePoolSize;
		this.maxQueueSize = maxQueueSize;
//		idleThread = new Thread(this, "删除有序队列");
//		idleThread.start();
	}
	
	public OrderedQueuePoolExecutor(String name, int corePoolSize) {
		this(name, corePoolSize, Integer.MAX_VALUE);
	}
	
	/** 增加执行任务
	 * 
	 * @param key
	 * @param value
	 * @return */
	public boolean addTask(K key, IWorker task) {
		// key = key % corePoolSize;
		TasksQueue queue = pool.getTasksQueue(key);
		if (queue.isCloseQueue()){
			pool.removeTasksQueue(key);
			logger.error("删除消息队列::"+key+"::"+queue.size());
			return false;
		}
		queue.setLastTime(System.currentTimeMillis());
		
		boolean run = false;
		boolean result = false;
		synchronized (queue.tasksQueue) {
			
			result = queue.tasksQueue.offer(task);
			if (result) {
				
				if (queue.isProcessingCompleted()) {
					queue.setProcessingCompleted(false);
					run = true;
				}
				
			} else {
				logger.error("消息队列添加任务失败");
			}
		}
		
		if (run) {
			execute(queue);
		}
		return result;
	}
	
	/** removeFromPool:(). <br/>
	 * TODO().<br/>
	 * 删除队列
	 * 
	 * @author lyh
	 * @param key */
	public TasksQueue removeFromPool(K key) {
		// TasksQueue
		TasksQueue queue = pool.removeTasksQueue(key);
		if (queue != null) {
			queue.setCloseQueue(true);
		} else {
			// log.error("没有删除消息队列::" + key);
		}
		return queue;
	}
	
	
	/** 
	 * getTaskQueue:(). <br/> 
	 * TODO().<br/> 
	 * 获取TasksQueue
	 * @author lyh 
	 * @param key
	 * @return 
	 */  
	public TasksQueue getTaskQueue(K key){
		return pool.getTasksQueue(key);
	}
//	@Override
//	public void run() {
//		// TODO Auto-generated method stub
//		while (isRunning) {
//			long curTime = System.currentTimeMillis();
//			for (Entry<K, TasksQueue> entry : pool.map.entrySet()) {
//				TasksQueue queue = entry.getValue();
//				if ((curTime - queue.getLastTime()) / 1000 >= DEL_TIME) {
//					try {
//						synchronized (queue) {
//							pool.removeTasksQueue(entry.getKey());
//						}
//						
//					} catch (Exception e) {
//						log.error("删除有错误:" + entry.getKey(), e);
//					}
//				}
//			}
//			
//			long endTime = INTERAL_TIME - (System.currentTimeMillis() - curTime);
//			if (endTime < 5) {
//				endTime = 5;
//			}
//			try {
//				Thread.sleep(endTime);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
//	}
	
	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		super.shutdown();
		pool.map.clear();
		isRunning = false;
		
	}
}
