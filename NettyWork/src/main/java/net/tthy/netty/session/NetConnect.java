package net.tthy.netty.session;

import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.Message;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;


/**
 * @preserve private
 */
public class NetConnect implements IConnect {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	/** 对mina IoSession的引用 */
	protected Channel session;
	
	/** 远端连接地址 */
	private InetSocketAddress remouteAddress;
	
	/** 远端的服务类型(网关,游戏服...) */
	private int remoteType;
	
	/** 远端的服务名字(网关游戏服1) */
	private String remoteName;
	
	/** 远端的服务id (每个游戏服务器性一id)*/
	private int remoteSid;
	
	/** 远端的服务组 */
	private String remoteGroup;
	
	/** 上一次的ping值 */
	private long ping;
	
	/**角色id**/  
	private long roleId;

	/**游戏类型**/  
	private int gameType;
	
	
	/** 相同时间戳，接收请求的次数 */
	public int msgFreq;
	
	/** 接收到同一消息的时间 */
	public long lastSameMsgTime;
	
	public int lastMsg;
	
	/** 
	 * checkCmd:(). <br/> 
	 * TODO().<br/> 
	 * 防止ddos攻击
	 * @author lyh 
	 * @param nowMill
	 * @param cmd
	 * @return 
	 */  
	public int checkCmd(long nowMill, int cmd) {
		int msg = cmd;
		if (msg == lastMsg) {
			if (lastSameMsgTime == 0) {
				lastSameMsgTime = nowMill;
			}
			if (nowMill - lastSameMsgTime < 1000) {
				msgFreq++;
			} else {
				lastSameMsgTime = nowMill;
				msgFreq = 0;
			}
			if (msgFreq >= 50) {
				//Log.system.fatal("请求频率过快   时间:" + (nowMill - lastSameMsgTime) + " msg:" + msg + " 频率:" + msgFreq + " short:" + backId + " cmd:" + cmd + " uid:" + userId + " pid:" + playerId + " name:" + getRemoteName());
				lastSameMsgTime = nowMill;
				msgFreq = 0;
				return -1;
			}
		} else {
			lastMsg = msg;
			msgFreq = 0;
			lastSameMsgTime = 0;
		}
		return cmd;
	}
	
	/** 得到远端的服务名 */
	public String getRemoteName() {
		return remoteName;
	}
	
	/** 设定远端的服务名 */
	public void setRemoteName(String remoteName) {
		this.remoteName = remoteName;
	}
	
	/** 是否连接中 */
	private boolean connected;
	
	/** 连接id */
	private String id =" -1";
	
	public NetConnect() {
		ping(System.currentTimeMillis());
	}
	
	public NetConnect(Channel session) {
		this.session = session;
	}
	
	/** 得到远端连接地址 */
	public InetSocketAddress getRemouteAdress() {
		
		return this.remouteAddress;
	}
	
	/** 得到远端连接地址 */
	public String getRemouteIp() {
		if (remouteAddress == null && session != null) {
			this.remouteAddress = (InetSocketAddress) session.remoteAddress();
		}
		if (remouteAddress == null) {
			return null;
		}
		return this.remouteAddress.getAddress().getHostAddress();
	}
	
	/** 设定远端连接地址 */
	public void setRemouteAdress(InetSocketAddress soketAddress) {
		this.remouteAddress = soketAddress;
	}
	
	/** 得到远端的服务组 */
	public String getRemoteGroup() {
		return remoteGroup;
	}
	
	/** 设置远端的服务组 */
	public void setRemoteGroup(String remoteGroup) {
		this.remoteGroup = remoteGroup;
	}
	
	/** 得到远端的服务id */
	public int getRemoteSTypeid() {
		return remoteSid;
	}
	
	/** 设置远端的服务类型 * */
	public void setRemoteSTypeid(int remoteSid) {
		this.remoteSid = remoteSid;
	}
	
	/** 得到远端的服务类型 */
	public int getRemoteSType() {
		return remoteType;
	}
	
	/** 设置远端的服务类型 */
	public void setRemoteSType(int type) {
		this.remoteType = type;
	}
	
	/** 是否正在连接 */
	public boolean isConnected() {
		return connected;
	}
	
	/** 是否正在连接中 */
	public boolean isConnect() {
		return connected;
	}
	
	/** 设定连接状态 */
	public void setConnected(boolean connected) {
		this.connected = connected;
	}
	
	public void send(ByteBuf buf) {
		if (session == null) {
			// Log.system.error(" session is not exsist!! ,send msg error");
			return;
		}
		if (isConnect() && session.isActive()) {
			session.write(buf);
		} else {
			// Log.system.error(" connect is lost !! ,send msg error");
		}
	}
	

	
	public void send(Message protobufMsg) {
		if (session == null) {
			// Log.system.error(" session is not exsist!! ,send msg error");
			return;
		}
		if (isConnect() && session.isActive()) {
			session.write(protobufMsg);
//			doWrite(bm);
		} else {
			// Log.system.error(" connect is lost !! ,send msg error");
		}
	}

	
	/** 设定连接绑定的对象 */
	public void setAttachment(Object obj) {
		if (obj == null) {
			this.session = null;
		} else if (obj instanceof Channel) {
			this.session = (Channel) obj;
			this.setId(session.id().asLongText());
		} else {
			throw new IllegalArgumentException("minaConnect ,obj is not IoSession ,obj=" + obj.getClass().getName());
		}
	}
	
	/** 设定连接 id */
	public void setId(String id) {
		this.id = id;
	}
	
	/** 获得连接绑定的对象 */
	public Object getAttachment() {
		return session;
	}
	
	/** 得到连接id */
	public String getId() {
		return this.id;
	}
	
	public void closeNow() {
		if (this.session != null) {
			this.session.close();
			logger.info("连接断开  connId=" + id + " address=" + getRemouteIp() + " " + " " + getRemoteName());
			// Thread.dumpStack();
		}
	}
	
	public void closeNetClient(){
		if (this.session != null) {
			this.session.close();
			//logger.info("连接断开  connId=" + id + " address=" + getRemouteIp() + " " + " " + getRemoteName());
			// Thread.dumpStack();
		}
	}
	
	public void ping(long time) {
		this.ping = time;
	}
	
	public long getPing() {
		return this.ping;
	}
	
	

	@Override
    public long getRoleId() {
	    // TODO Auto-generated method stub
	    return roleId;
    }

	@Override
    public void setRoleId(long roleId) {
	    // TODO Auto-generated method stub
		this.roleId = roleId;
    }

	@Override
	public void closeOnFlush() {
		// TODO Auto-generated method stub
		if (session != null){
			session.flush();
			session.close();
		}
	}

	@Override
	public int getGameType() {
		// TODO Auto-generated method stub
		return gameType;
	}

	@Override
	public void setGameType(int gameType) {
		// TODO Auto-generated method stub
		this.gameType = gameType;
	}




}
