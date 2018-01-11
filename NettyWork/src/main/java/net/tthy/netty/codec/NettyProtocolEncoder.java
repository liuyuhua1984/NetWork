package net.tthy.netty.codec;

import java.util.Map;

import com.google.protobuf.Message;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyProtocolEncoder extends MessageToByteEncoder<Object> {
	private NetProtocolCodec _codec;
	private Map<String, Integer> map;
	private Logger logger = LoggerFactory.getLogger(NettyProtocolEncoder.class);
	public NettyProtocolEncoder(NetProtocolCodec codec, Map<String, Integer> _map) {
		this._codec = codec;
		map = _map;
	}
	
	public void encode(ChannelHandlerContext ctx, Object obj, ByteBuf out) throws Exception {
		if ((obj instanceof Message)) {
			Message msg = (Message) obj;
			ByteBuf io = _codec.encode(msg, map);
			if (io != null){
				out.writeBytes(io);
				logger.info(io.array().length +"::send msg ok !!!!!!!!!!"+msg.getDescriptorForType().getName());
			}
		} else {
			ByteBuf buf = (ByteBuf)obj;
			out.writeBytes(buf);
			logger.info("out msg ok !!!!!!!!!!");
		}
		
	}
}