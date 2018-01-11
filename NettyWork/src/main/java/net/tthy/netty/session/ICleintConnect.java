package net.tthy.netty.session;

/** 与客户端的连接 */
public interface ICleintConnect extends IConnect {
	/** 得到所在游戏服的id */
	public int getGameId();
	
//	/** 设定连接的 id,并将连接id也设为此值 */
//	public void setId(int userId);
	
	/** 设定本次id */
	public void setSessId(int gId);
	
	/** 得到本次会话id */
	public int getSessId();
	
	/** 得到所在游戏服的id */
	public void setGameId(int gameID);
	
	/** 得到玩家唯一id */
	public long getPlayerId();
	
	/** 设定玩家唯一id */
	public void setPlayerId(long pid);
	
	/** 得到用户名称 */
	public String getUserName();
	
	/** 设定用户名称 */
	public void setUserName(String userName);
	
	/**
	 * 请求频率验证，返回false,表示请求频率太高
	 * 
	 * @param backId TODO
	 * @param cmd TODO
	 */
	public int requestFreqCheck(long mill, int backId, int cmd);
	
	/** 是否在游戏状态 */
	public boolean isPlaying();
	
	/** 设置为游戏状态 */
	public void play();
	
	/** 是否在登录状态 */
	public boolean isLogin();
	
	/** 设置为登录状态 */
	public void login();
	
	/** 得到所属的虚拟服务器 */
	public int getBelongServer();
	
	/** 设定所属的虚拟服务器 */
	public void setBelongServer(int belongServer);
	
}
