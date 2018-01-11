
package net.tthy.netty;

import java.net.InetSocketAddress;


import io.netty.channel.Channel;

/** 
 * ClassName:IClient <br/> 
 * Function: TODO (客户端连接接口). <br/> 
 * Reason:   TODO (). <br/> 
 * Date:     2015-7-2 下午3:39:06 <br/> 
 * @author   lyh 
 * @version   
 * @see       
 */
public interface IClient {
	 Channel connect(String ip,int port) throws Exception;
	 Channel connect(InetSocketAddress socketAddress) throws Exception;
}
  