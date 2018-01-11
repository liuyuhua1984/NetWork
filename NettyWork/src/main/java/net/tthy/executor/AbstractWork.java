package net.tthy.executor;


/**
 * ClassName: AbstractWork <br/>
 * Function: TODO (). <br/>
 * Reason: TODO (). <br/>
 * date: 2015-6-26 上午11:36:30 <br/>
 * 每一个对象的管道类
 * 
 * @author lyh
 * @version
 */
public abstract class AbstractWork implements IWorker {
	
	private TasksQueue tasksQueue;
	
	/** 管道在工作 **/
	private boolean isWorking;
	
	public TasksQueue getTasksQueue() {
		return tasksQueue;
	}
	
	public void setTasksQueue(TasksQueue tasksQueue) {
		this.tasksQueue = tasksQueue;
	}
	
	public boolean isWorking() {
		return isWorking;
	}
	
	public void setWorking(boolean isWorking) {
		this.isWorking = isWorking;
	}
}
