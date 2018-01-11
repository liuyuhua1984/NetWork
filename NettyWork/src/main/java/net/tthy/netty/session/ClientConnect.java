package net.tthy.netty.session;


/**
 * 
 * ClassName: ClientConnect <br/> 
 * Function: TODO (). <br/> 
 * Reason: TODO (). <br/> 
 * date: 2015-6-30 下午4:51:30 <br/> 
 * 客户端连接
 * @author lyh 
 * @version
 */
public class ClientConnect extends NetConnect implements ICleintConnect {
	
	/** 所在游戏服 */
	private int gameId;
	
	/** 会话id */
	private int sessId;
	
	/** 玩家id */
	private long playerId;
	
	/** 用户名 */
	private String userName;
	
	/** 相同时间戳，接收请求的次数 */
	public int msgFreq;
	
	/** 接收到同一消息的时间 */
	public long lastSameMsgTime;
	
	public int lastMsg;
	
	/** 当前状态 */
	private int state;
	
	/** 因为有模拟服务器链接的情况，设置所属的服务器链接 */
	private int belongServer;
	
	public int requestFreqCheck(long nowMill, int backId, int cmd) {
		int msg = (backId << 16) + cmd;
		if (msg == lastMsg) {
			if (lastSameMsgTime == 0) {
				lastSameMsgTime = nowMill;
			}
			if (nowMill - lastSameMsgTime < 5 * 1000) {
				msgFreq++;
			} else {
				lastSameMsgTime = nowMill;
				msgFreq = 0;
			}
			if (msgFreq >= 100) {
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
		return 1;
	}
	
	public long getPlayerId() {
		return playerId;
	}
	
	public void setPlayerId(long onlyId) {
		this.playerId = onlyId;
	}
	
	public int getSessId() {
		return sessId;
	}
	
	public void setSessId(int sessId) {
		this.sessId = sessId;
	}
	
	public int getGameId() {
		return this.gameId;
	}
	
	public void setGameId(int gameId) {
		this.gameId = gameId;
	}
	
	public boolean isPlaying() {
		return this.state == 2;
	}
	
	public void play() {
		this.state = 2;
	}
	
	public boolean isLogin() {
		return this.state == 1;
	}
	
	public void login() {
		this.state = 1;
	}
	

	public void setBelongServer(int belongServer) {
		this.belongServer = belongServer;
	}

	@Override
    public int getBelongServer() {
	    // TODO Auto-generated method stub
	    return belongServer;
    }

	@Override
    public String getUserName() {
	    // TODO Auto-generated method stub
	    return userName;
    }

	@Override
    public void setUserName(String userName) {
	    // TODO Auto-generated method stub
	    this.userName = userName;
    }

	
	
}
