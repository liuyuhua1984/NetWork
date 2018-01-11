package net.tthy.netty.handler;

import io.netty.channel.Channel;
import net.tthy.netty.codec.ProxyXmlBean;
import net.tthy.netty.session.IConnect;

public abstract interface INetApdapterLisener<T> {
	public abstract void doProxy(IConnect paramNetSession, T msg);

	public abstract void doProxy(IConnect paramNetSession, T msg, ProxyXmlBean paramProxyXmlBean);

	/** 
	 * sessionClose:(). <br/> 
	 * TODO().<br/> 
	 * 会话关闭时处理
	 * @author lyh 
	 * @param session 
	 */  
	public abstract void sessionClose(IConnect session);

	/** 
	 * sessionOpen:(). <br/> 
	 * TODO().<br/> 
	 * sesssion session连接打开
	 * @author lyh 
	 * @param paramNetSession 
	 */  
	public abstract void sessionOpen(Channel paramNetSession);
	
	public abstract void haveNoProxyError(IConnect paramNetSession);

	public abstract void decodeProxyError(IConnect paramNetSession);

	public abstract void sessionTimeOut(IConnect paramNetSession);
}