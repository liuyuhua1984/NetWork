package net.tthy.netty.codec;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.Message;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
/**
 * 
 * ClassName: InnerProtocolEncoder <br/> 
 * Function: TODO (). <br/> 
 * Reason: TODO (). <br/> 
 * date: 2015-6-30 下午4:16:30 <br/> 
 * 内部协议编码类
 * @author lyh 
 * @version
 */
public class InnerProtocolEncoder  extends MessageToByteEncoder<Object> {
	private ProtocolCodec _codec;
	private Map<String, Integer> map;
	private Logger logger = LoggerFactory.getLogger(InnerProtocolEncoder.class);
	public InnerProtocolEncoder(ProtocolCodec codec, Map<String, Integer> _map) {
		this._codec = codec;
		map = _map;
	}
	

	@Override
	protected void encode(ChannelHandlerContext ctx, Object obj, ByteBuf out) throws Exception {
		// TODO Auto-generated method stub
		if ((obj instanceof Message)) {
			Message msg = (Message) obj;
			ByteBuf io = _codec.encode(msg, map);
			if (io != null){
				out.writeBytes(io);
				logger.info(io.array().length +"::send msg ok Inner !!!!!!!!!!"+msg.getDescriptorForType().getName());
			}
		} else {
			ByteBuf buf = (ByteBuf)obj;
			out.writeBytes(buf);
			logger.info("out msg ok Inner !!!!!!!!!!");
		}
	}
}