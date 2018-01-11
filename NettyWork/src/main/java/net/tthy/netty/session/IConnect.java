package net.tthy.netty.session;

import java.net.InetSocketAddress;


import com.google.protobuf.Message;

import io.netty.buffer.ByteBuf;


/** 
 * ClassName: IConnect <br/> 
 * Function: TODO (). <br/> 
 * Reason: TODO (). <br/> 
 * date: 2015-6-30 下午4:36:14 <br/> 
 * 连接接口
 * @author lyh 
 * @version  
 */ 
public interface IConnect {
	
	/** 得到远端连接地址 */
	public InetSocketAddress getRemouteAdress();
	
	/** 得到远端的服务类型id (每一个服务器惟一id) */
	public int getRemoteSTypeid();
	
	/** 设定远端的服务id */
	public void setRemoteSTypeid(int sid);
	
	/** 得到远端的服务类型 (如网关,游戏服务,世界服)* */
	public int getRemoteSType();
	
	/** 得到远端的服务组 */
	public String getRemoteGroup();
	
	/** 
	 * getGameType:(). <br/> 
	 * TODO().<br/> 
	 * 游戏类型
	 * @author lyh 
	 * @return 
	 */  
	public int getGameType();
	public void setGameType(int gameType);
	/** 得到远端的服务名字(服务名字) */
	public String getRemoteName();
	
	/** ping值 */
	public void ping(long time);
	
	/** 得到上一次的ping值 */
	public long getPing();
	
	/** 是否在连接中 */
	public void setConnected(boolean connected);
	
	/** 是否在连接中 */
	public boolean isConnect();
	
//	public void sendBlock(ByteBuffer packet);
	
	/** 发送消息 */
	public void send(ByteBuf ioBuffer);
		
	/** 发送消息 */
	public void send(Message protobufMsg);

	
	/** 设定连接绑定的对象(这个对象可能是角色和用户) */
	public void setAttachment(Object obj);
	
	/** 获得连接绑定的对象 */
	public Object getAttachment();
	
	/** 得到连接sessionid */
	public String getId();
	
	/** 
	 * getRoleId:(). <br/> 
	 * TODO().<br/> 
	 * 获得角色id
	 * @author lyh 
	 * @return 
	 */  
	public long getRoleId();

	/** 
	 * setId:(). <br/> 
	 * TODO().<br/> 
	 * 设置连接id
	 * @author lyh 
	 * @param id 
	 */  
	public void setId(String id);
	/** 立即关闭连接 */
	public void closeNow();
	
	public void closeNetClient();
	

	/** 
	 * closeOnFlush:(). <br/> 
	 * TODO().<br/> 
	 *
	 * Closes this session after all queued write requests are flushed.  This operation 
	 * is asynchronous.  Wait for the returned {@link CloseFuture} if you want to wait 
	 * for the session actually closed.
	 * @author lyh  
	 */  
	public void closeOnFlush();
	/** 远端的服务类型 */
	public void setRemoteSType(int type);
	
	/** 远端的服务名字 */
	public void setRemoteName(String rsName);
	
	/** 远端的服务组 */
	public void setRemoteGroup(String group);
	
	/** 远端的ip地址 */
	public void setRemouteAdress(InetSocketAddress address);
	
	/** 得到远端的ip地址 */
	public String getRemouteIp();
	
//	/** 得到写数据的队列长度 */
//	public int getWriteSize();
//	
//	
//	/******计数器********/
//	public int getCount();
	
	/** 
	 * checkCmd:(). <br/> 
	 * TODO().<br/> 
	 * 防止ddos攻击
	 * @author lyh 
	 * @param nowMill
	 * @param cmd
	 * @return 
	 */  
	public int checkCmd(long nowMill, int cmd);
	/** 
	 * setRoleId:(). <br/> 
	 * TODO().<br/> 
	 * 设置角色id
	 * @author lyh 
	 * @param roleId 
	 */  
	public void setRoleId(long roleId);
//	
//	/** 得到连接服务器id */
//	public int getSId();
//	
//	/** 设定连接服务器的id */
//	public void setSId(int sid);
	
//	/** 镜像服务连接 */
//	public boolean isRemoutDup();
//	
//	/** 镜像服务连接 */
//	public void setRemoutDup(boolean dup);
}
