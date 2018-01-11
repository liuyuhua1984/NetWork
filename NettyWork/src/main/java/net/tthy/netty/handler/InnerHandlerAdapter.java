package net.tthy.netty.handler;

import java.util.Map;

import com.lyh.protocol.BaseMessage.InnerMessage;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import net.tthy.executor.AbstractWork;
import net.tthy.executor.OrderedQueuePoolExecutor;
import net.tthy.netty.codec.ProxyXmlBean;
import net.tthy.netty.session.IConnect;
import net.tthy.netty.utils.NetConst;

/** ClassName: NetHandlerAdapter <br/>
 * Function: TODO (). <br/>
 * Reason: TODO (). <br/>
 * date: 2015-6-30 下午4:20:05 <br/>
 * mina业务处理
 * 
 * @author lyh
 * @version */
public class InnerHandlerAdapter extends NetHandlerAdapter {
	
	private OrderedQueuePoolExecutor<Long> queuePool = null;
	
	
	/** Creates a new instance of InnerHandlerAdapter.
	 * 
	 * @param proxyList */
	public InnerHandlerAdapter(Map<String, ProxyXmlBean> proxyList, OrderedQueuePoolExecutor<Long> queuePool) {
		super(proxyList);
		// TODO Auto-generated constructor stub
		this.queuePool = queuePool;
	}
	

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object message) throws Exception {
		// TODO Auto-generated method stub
		InnerMessage msg = (InnerMessage) message;
		try {
			
			IConnect net = (IConnect)	ctx.channel().attr(AttributeKey.valueOf(NetConst.NET_SESSION)).get();
			if (net == null) {
				return;
			}
			
			// ProxyXmlBean xml = (ProxyXmlBean)
			// this._proxyList.get(String.format("%1$0#6x",
			// new Object[] { Integer.valueOf(po.code) }).toUpperCase());
			ProxyXmlBean xml = (ProxyXmlBean) this._proxyList.get(msg.getCommand());
			if ((XMLNetApdapterLisener<InnerMessage>) this.netDispatch != null) {
				if (queuePool != null && msg.getClientSessionId() > 0) {
					// 对于自己id是有顺序的
					final IConnect cNet = net;
					final InnerMessage im = msg;
					final ProxyXmlBean pxb = xml;
					queuePool.addTask(msg.getClientSessionId(), new AbstractWork() {
						
						@Override
						public void doWork() {
							// TODO Auto-generated method stub
							netDispatch.doProxy(cNet, im, pxb);
						}
					});
				} else {
					netDispatch.doProxy(net, msg, xml);
				}
			} else this.netDispatch.doProxy(net, msg);
			
			message = null;
		} catch (Exception e) {
			log.error("错误是::::" + Integer.toHexString(msg.getCommand()), e);
			// e.printStackTrace();
		}
	}


//	@SuppressWarnings("unchecked")
//	public void messageReceived(IoSession session, Object message) {
//		InnerMessage msg = (InnerMessage) message;
//		try {
//			
//			IConnect net = (IConnect)	ctx.channel().attr(AttributeKey.valueOf(NetConst.NET_SESSION)).get();
//			if (net == null) {
//				message = null;
//				return;
//			}
//			
//			// ProxyXmlBean xml = (ProxyXmlBean)
//			// this._proxyList.get(String.format("%1$0#6x",
//			// new Object[] { Integer.valueOf(po.code) }).toUpperCase());
//			ProxyXmlBean xml = (ProxyXmlBean) this._proxyList.get(msg.getCommand());
//			if ((XMLNetApdapterLisener<InnerMessage>) this.netDispatch != null) {
//				if (queuePool != null && msg.getClientSessionId() > 0) {
//					// 对于自己id是有顺序的
//					final IConnect cNet = net;
//					final InnerMessage im = msg;
//					final ProxyXmlBean pxb = xml;
//					queuePool.addTask(msg.getClientSessionId(), new AbstractWork() {
//						
//						@Override
//						public void doWork() {
//							// TODO Auto-generated method stub
//							netDispatch.doProxy(cNet, im, pxb);
//						}
//					});
//				} else {
//					netDispatch.doProxy(net, msg, xml);
//				}
//			} else this.netDispatch.doProxy(net, msg);
//			
//			message = null;
//		} catch (Exception e) {
//			log.error("错误是::::" + Integer.toHexString(msg.getCommand()), e);
//			// e.printStackTrace();
//		}
//	}
	

}