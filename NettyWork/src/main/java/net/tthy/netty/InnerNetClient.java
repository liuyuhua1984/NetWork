package net.tthy.netty;

import java.net.InetSocketAddress;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lyh.net.executor.OrderedQueuePoolExecutor;
import com.lyh.net.executor.TasksQueue;
import com.lyh.net.mina.codec.InnerCodecFactoryImpl;
import com.lyh.net.mina.codec.ProtocolCodecFactoryImpl;
import com.lyh.net.mina.codec.ProxySet;
import com.lyh.net.mina.codec.ProxyXmlBean;
import com.lyh.net.mina.handler.INetApdapterLisener;
import com.lyh.net.mina.handler.InnerHandlerAdapter;
import com.lyh.net.mina.handler.MinaHandlerAdapter;
/**
 * 
 * ClassName: NetClient <br/> 
 * Function: TODO (). <br/> 
 * Reason: TODO (). <br/> 
 * date: 2015-6-30 下午4:19:06 <br/> 
 * mina客户端
 * @author lyh 
 * @version
 */
public class InnerNetClient implements IClient{
	private IoConnector connector;
	protected InnerHandlerAdapter adapter;
	public static Map<String, ProxyXmlBean> proxyList;
	protected InnerCodecFactoryImpl codecFactory;
	public static Map<String,Integer> protocolMap;
	protected Logger log = LoggerFactory.getLogger(this.getClass());
	protected OrderedQueuePoolExecutor<Long> queuePool = null;;
	//private static final ExecutorService FILTER_EXECUTOR = Executors.newCachedThreadPool();
					//new OrderedThreadPoolExecutor(10,1000);
	public void addLisener(INetApdapterLisener net) {
		if (this.adapter != null)
			this.adapter.setProxyDispatch(net);
		else
			System.exit(1);
	}

	protected void initProxy(String fileName) {
		ProxySet.proxy_initialize(fileName);
		this.proxyList = ProxySet.getProxyList();
		protocolMap = ProxySet.getProtocolmap();
		this.adapter = new InnerHandlerAdapter(this.proxyList,queuePool);
	}

	protected void initialize(String ip, int port,String fileName,int corePoolSize)  throws Exception{
		try {
			 queuePool =new OrderedQueuePoolExecutor<Long>("InnerNetClientQueuePool", corePoolSize);
			initProxy(fileName);
			this.connector = new NioSocketConnector();
			this.codecFactory = new InnerCodecFactoryImpl(this.protocolMap);
			this.connector.setConnectTimeoutMillis(0L);
			this.connector.getFilterChain().addLast("codec",
					new ProtocolCodecFilter(this.codecFactory));
			//connector.getFilterChain().addLast("threadPool", new ExecutorFilter(FILTER_EXECUTOR));
			this.connector.setHandler(this.adapter);
			
		} catch (RuntimeIoException e) {
			e.printStackTrace();
			throw new Exception(e.toString());
		}
	}

	/** 
	 * connect:(). <br/> 
	 * TODO().<br/> 
	 * 连接服务器地址
	 * @author lyh 
	 * @param ip
	 * @param port
	 * @return
	 * @throws Exception 
	 */  
	public IoSession connect(String ip,int port) throws Exception{
		InetSocketAddress _socketAddress = new InetSocketAddress(ip, port);
		
		IoSession session = connect(_socketAddress);
		return session;
	}
	
	public IoSession connect(InetSocketAddress socketAddress) throws Exception{
		ConnectFuture connectf = this.connector.connect(socketAddress);
		IoSession session = connectf.awaitUninterruptibly().getSession();
		return session;
	}
	
	/**
	 * @param key
	 * 删除消息队列
	 * @return
	 */
	public TasksQueue removeQueuePoolFromMap(Long key){
		if (queuePool != null){
			return queuePool.removeFromPool(key);
		}
		return null;
	}
	
	public void close() {
		this.connector.dispose();
		if (queuePool != null){
			queuePool.shutdown();
		}
	}

	
	
}