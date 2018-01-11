package net.tthy.netty.handler;

import java.io.IOException;
import java.util.Map;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lyh.protocol.BaseMessage.NetMessage;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import net.tthy.netty.codec.ProxyXmlBean;
import net.tthy.netty.session.IConnect;
import net.tthy.netty.utils.NetConst;

/**
 * 
 * ClassName: NetHandlerAdapter <br/>
 * Function: TODO (). <br/>
 * Reason: TODO (). <br/>
 * date: 2015-6-30 下午4:20:05 <br/>
 * mina业务处理
 * 
 * @author lyh
 * @version
 */
public class NetHandlerAdapter extends ChannelInboundHandlerAdapter {
	protected Logger log = LoggerFactory.getLogger(this.getClass());
	
	protected Map<String, ProxyXmlBean> _proxyList;
	
	private boolean _isLongConn = false;
	
	@SuppressWarnings("rawtypes")
	protected INetApdapterLisener netDispatch;
	
	public NetHandlerAdapter(Map<String, ProxyXmlBean> proxyList) {
		this._proxyList = proxyList;
		this._isLongConn = false;
	}
	
	public NetHandlerAdapter(Map<String, ProxyXmlBean> proxyList, Boolean isLongConn) {
		this._proxyList = proxyList;
		this._isLongConn = isLongConn.booleanValue();
	}
	
	@SuppressWarnings("rawtypes")
	public void setProxyDispatch(INetApdapterLisener net) {
		if (net != null)
			this.netDispatch = net;
	}
	
//	public void sessionCreated(IoSession session) throws Exception {
//		log.info("sessionCreated......" + session.getRemoteAddress());
//	}
//	
//	public void sessionOpened(IoSession session) throws Exception {
//		log.info("sessionOpened......"+session.getId());
//		
//		netDispatch.sessionOpen(session);
//		log.info(System.currentTimeMillis() + " : sessionCreated : " + session + " : " + session.getCreationTime());
//	}
//	
//	public void sessionClosed(IoSession session) throws Exception {
//		log.info(session.getLocalAddress() + "[close]");
//		IConnect net = (IConnect) session.getAttribute(NetConst.NET_SESSION);
//		if (net != null) {
//			session.removeAttribute(NetConst.NET_SESSION);
//			this.netDispatch.sessionClose(net);
//			net.closeNetClient();
//		}
//	}
//	
//	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
//		if (!this._isLongConn && session != null) {
//			Integer in = (Integer) session.getAttribute("idle");
//			int idle = 0;
//			if (in != null) {
//				idle = ((Integer) session.getAttribute("idle")).intValue();
//				if (idle++ > NetConst.SYSTEM_SOCKET_MAX_IDLE_TIMES) {
//					log.info("达到系统最大闲置次数");
//					IConnect net = (IConnect) session.getAttribute(NetConst.NET_SESSION);
//					if (net != null) {
//						this.netDispatch.sessionTimeOut(net);
//					}
//					session.closeNow();
//				} else {
//					if (idle == 5) {
//						idle = 1;
//					}
//					session.setAttribute("idle", Integer.valueOf(idle));
//				}
//			} else {
//				session.setAttribute("idle", Integer.valueOf(idle));
//			}
//		}
//	}
//	
//	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
//		IConnect net = (IConnect) session.getAttribute(NetConst.NET_SESSION);
//
//		if (net == null)
//			return;
//
//		if (cause.getMessage() == null || (!cause.getMessage().contains("远程主机强迫关闭了一个现有的连接")) && (!cause.getMessage().contains("Connection")) && (!cause.getClass().equals(IOException.class))) {
//			
//			log.error("消息错误----", cause);
//
//		}
//		log.debug("消息异常错误----", cause);
//		session.closeNow();
//	}
//	
//	@SuppressWarnings("unchecked")
//    public void messageReceived(IoSession session, Object message) {
//		NetMessage msg = (NetMessage) message;
//		try {
//			
//			IConnect net = (IConnect) session.getAttribute(NetConst.NET_SESSION);
//			if (net == null) {
//				log.error("没有connect::::" + Integer.toHexString(msg.getCommand()));
//				message = null;
//				return;
//			}
//			
//			// ProxyXmlBean xml = (ProxyXmlBean) this._proxyList.get(String.format("%1$0#6x",
//			// new Object[] { Integer.valueOf(po.code) }).toUpperCase());
//			ProxyXmlBean xml = (ProxyXmlBean) this._proxyList.get(msg.getCommand());
//			
//			if ((XMLNetApdapterLisener<NetMessage>) this.netDispatch != null) {
//				this.netDispatch.doProxy(net, msg, xml);
//			}
//			
//			message = null;
//		} catch (Exception e) {
//			log.error("错误是::::" + Integer.toHexString(msg.getCommand()), e);
//			// e.printStackTrace();
//		}
//	}
//	
//	public void messageSent(IoSession session, Object message) throws Exception {
//	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelRegistered(ctx);
		log.info("channelRegistered......" + ctx.channel().remoteAddress());
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelUnregistered(ctx);
		log.info("channelUnregistered......" + ctx.channel().remoteAddress());
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelActive(ctx);
		log.info("channelActive......"+ctx.channel().id().asLongText());
		
		netDispatch.sessionOpen(ctx.channel());
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelInactive(ctx);
		log.info("channelInactive......"+ctx.channel().id().asLongText());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object message) throws Exception {
		// TODO Auto-generated method stub
		
		NetMessage msg = (NetMessage) message;
		try {
			IConnect net = (IConnect)	ctx.channel().attr(AttributeKey.valueOf(NetConst.NET_SESSION)).get();
			if (net == null) {
				log.error("没有connect::::" + Integer.toHexString(msg.getCommand()));
				message = null;
				return;
			}
			
			// ProxyXmlBean xml = (ProxyXmlBean) this._proxyList.get(String.format("%1$0#6x",
			// new Object[] { Integer.valueOf(po.code) }).toUpperCase());
			ProxyXmlBean xml = (ProxyXmlBean) this._proxyList.get(msg.getCommand());
			
			if ((XMLNetApdapterLisener<NetMessage>) this.netDispatch != null) {
				this.netDispatch.doProxy(net, msg, xml);
			}
			
			message = null;
		} catch (Exception e) {
			log.error("错误是::::" + Integer.toHexString(msg.getCommand()), e);
			// e.printStackTrace();
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelReadComplete(ctx);
		log.info("channelReadComplete......"+ctx.channel().id().asLongText());
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		// TODO Auto-generated method stub
		super.userEventTriggered(ctx, evt);
		log.info("userEventTriggered......"+ctx.channel().id().asLongText());
	}

	@Override
	public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelWritabilityChanged(ctx);
		log.info("channelWritabilityChanged......"+ctx.channel().id().asLongText());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// TODO Auto-generated method stub
		IConnect net = (IConnect)	ctx.channel().attr(AttributeKey.valueOf(NetConst.NET_SESSION)).get();

		if (net == null)
			return;

		if (cause.getMessage() == null || (!cause.getMessage().contains("远程主机强迫关闭了一个现有的连接")) && (!cause.getMessage().contains("Connection")) && (!cause.getClass().equals(IOException.class))) {
			
			log.error("消息错误----", cause);

		}
		log.debug("消息异常错误----", cause);
		
		log.info(ctx.channel().localAddress() + "[close]");
		
		if (net != null) {
			ctx.close();
			ctx.channel().attr(AttributeKey.valueOf(NetConst.NET_SESSION)).set(null);
			this.netDispatch.sessionClose(net);
			net.closeNetClient();
		}
	}
}