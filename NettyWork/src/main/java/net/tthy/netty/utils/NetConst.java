package net.tthy.netty.utils;

public final class NetConst {
	public static final String SYSTEM_ENCODE = new String("UTF-8");

	/**编码长度**/  
	public static final int CODE_LENGTH = 4;
	
	/**会话常量**/  
	public static final String NET_SESSION= "netsession";
	public static final short SYSTEM_SOCKET_MAX_IDLE_TIMES = Short.parseShort("10");

	
	/**压缩标志**/  
	public static final int ZLIB_FLAG = 1 << 31;
}