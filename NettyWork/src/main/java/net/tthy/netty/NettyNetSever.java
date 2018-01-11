package net.tthy.netty;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.tthy.netty.codec.NetProtocolCodecFactoryImpl;
import net.tthy.netty.codec.ProxyXmlBean;


/**
 * 
 * ClassName: MinaNetSever <br/> 
 * Function: TODO (). <br/> 
 * Reason: TODO (). <br/> 
 * date: 2015-6-30 下午4:18:21 <br/> 
 * mina服务器
 * @author lyh 
 * @version
 */
public class NettyNetSever {
	
	protected Logger log = LoggerFactory.getLogger(this.getClass());
	protected NetHandlerAdapter adapter;
	private boolean singleThread = false;
	public static Map<String, ProxyXmlBean> proxyList;
	public static Map<String, Integer> protocolMap;
	protected NetProtocolCodecFactoryImpl codcFactory;
	private NioSocketAcceptor acceptor;
//	private static final String THREAD_NAME = "Socket线程池";
//	private static final ThreadGroup THREAD_GROUP = new ThreadGroup("SocketGroup");
//
//	private static final NamedThreadFactory THREAD_FACTORY = new NamedThreadFactory(THREAD_GROUP, "SocketPoolName");

	private static final ExecutorService FILTER_EXECUTOR = //Executors.newCachedThreadPool();
					new OrderedThreadPoolExecutor(10,1000);

	public NetHandlerAdapter getAdapter() {
		return this.adapter;
	}

	public NetProtocolCodecFactoryImpl getCodcFactory() {
		return this.codcFactory;
	}

	public void addLisener(INetApdapterLisener net) {
		if (this.adapter != null) {
			this.adapter.setProxyDispatch(net);
		} else {
			log.error("addLisener is not NetApdapterLisener");
			System.exit(1);
		}
	}

	public void setSingleThread(boolean singleThread) {
		this.singleThread = singleThread;
	}

	protected void initProxy(String fileName) {
		log.error("======协议初始化======");
		ProxySet.proxy_initialize(fileName);
		this.proxyList = ProxySet.getProxyList();
		protocolMap = ProxySet.getProtocolmap();
		this.adapter = new NetHandlerAdapter(this.proxyList);
		log.error("======协议初始化成功======");
	}

	protected void initialize(int port,String fileName) throws Exception {
		try {
			if (!bindPort(port)) {
				throw new Exception("======监听端口:" + port + "被占用  ======");
			}
			
			initProxy(fileName);

			NioSocketAcceptor acceptor = new NioSocketAcceptor(Runtime.getRuntime().availableProcessors()+1);
			//LoggingFilter logFilter = new LoggingFilter();
			DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();
//			logFilter.setMessageReceivedLogLevel(LogLevel.NONE);
//			logFilter.setMessageSentLogLevel(LogLevel.NONE);
//			logFilter.setSessionClosedLogLevel(LogLevel.NONE);
//			logFilter.setSessionOpenedLogLevel(LogLevel.NONE);
//			logFilter.setExceptionCaughtLogLevel(LogLevel.NONE);
//
//			chain.addLast("mdcFilter", new MdcInjectionFilter());


			//chain.addLast("logger", logFilter);
			this.codcFactory = new NetProtocolCodecFactoryImpl(this.protocolMap);
			chain.addLast("codec", new ProtocolCodecFilter(this.codcFactory));
		
			if (!this.singleThread) {
				chain.addLast("threadPool", new ExecutorFilter(FILTER_EXECUTOR));
			} else {
				chain.addLast("threadPool", new ExecutorFilter(Executors.newSingleThreadExecutor()));
			}
			
			acceptor.setHandler(this.adapter);
			acceptor.getSessionConfig().setSoLinger(0);
			//acceptor.getSessionConfig().setKeepAlive(true);
			acceptor.getSessionConfig().setReuseAddress(true);
			acceptor.setReuseAddress(true);
			acceptor.getSessionConfig().setReceiveBufferSize(1024*8);
			acceptor.getSessionConfig().setSendBufferSize(1024*32);
			acceptor.getSessionConfig().setReadBufferSize(1024*8);
			acceptor.getSessionConfig().setWriteTimeout(1000 * 20);
			acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 60*1000);
			//是否延时
			acceptor.getSessionConfig().setTcpNoDelay(true);
			
			acceptor.bind(new InetSocketAddress(port));
			IoBuffer.setUseDirectBuffer(false);
			IoBuffer.setAllocator(new SimpleBufferAllocator());
			log.error("======监听端口:" + port + "成功======");
		} catch (IOException e) {
			log.error("======网络创建失败======");
			log.error(e.toString());
			throw new Exception(e.toString());
		}
	}

	private static boolean bindPort(int port) {
		try {
			Socket s = new Socket();
			s.bind(new InetSocketAddress(port));
			s.close();
			return true;
		} catch (Exception e) {
		}
		return false;
	}

	protected void stop() {
		if (this.acceptor != null) {
			this.acceptor.unbind();
			this.acceptor.dispose();
			this.acceptor = null;
		}

		if (FILTER_EXECUTOR != null) {
			FILTER_EXECUTOR.shutdown();
			try {
				FILTER_EXECUTOR.awaitTermination(5000L, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
			     
				log.error("停服抛出了异常", e);
			}
		}
	}

	static class NamedThreadFactory implements ThreadFactory {
		final ThreadGroup group;
		final AtomicInteger threadNumber = new AtomicInteger(1);
		final String namePrefix;

		public NamedThreadFactory(ThreadGroup group, String name) {
			this.group = group;
			this.namePrefix = (group.getName() + ":" + name);
		}

		public Thread newThread(Runnable r) {
			Thread thread = new Thread(this.group, r, this.namePrefix + this.threadNumber.getAndIncrement(), 0L);
			return thread;
		}
	}
}