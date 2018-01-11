package net.tthy.executor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * ClassName: OrderedQueuePool <br/>
 * Function: TODO (). <br/>
 * Reason: TODO (). <br/>
 * date: 2015-6-26 下午1:54:44 <br/>
 * 有序池
 * 
 * @author lyh
 * @version @param <K>
 * @version @param <V>
 */

public class OrderedQueuePool<K> {
	
	/** 因为用的同步所以用hmap **/
	Map<K, TasksQueue> map = new ConcurrentHashMap<K, TasksQueue>();
	
	/**
	 * 获得任务队列
	 * 
	 * @param key
	 * @return
	 */
	public TasksQueue getTasksQueue(K key) {
		TasksQueue queue = map.get(key);
		if (queue == null) {
			queue = new TasksQueue();
			TasksQueue oldQueue = map.putIfAbsent(key, queue);
			if (oldQueue != null) {
				queue = oldQueue;
			}
		}
		return queue;
		
	}
	
	/**
	 * 获得全部任务队列
	 * 
	 * @param key
	 * @return
	 */
	public Map<K, TasksQueue> getTasksQueues() {
		return map;
	}
	
	/**
	 * 移除任务队列
	 * 
	 * @param key
	 * @return
	 */
	public TasksQueue removeTasksQueue(K key) {
		return map.remove(key);
		
	}
}
