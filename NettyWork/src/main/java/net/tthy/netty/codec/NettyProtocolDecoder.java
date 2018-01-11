package net.tthy.netty.codec;
import com.lyh.protocol.BaseMessage.NetMessage;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import net.tthy.netty.utils.NetConst;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyProtocolDecoder extends ByteToMessageDecoder {
	private Logger logger = LoggerFactory.getLogger(NettyProtocolDecoder.class);
	
	private NetProtocolCodec codec;
	
	public NettyProtocolDecoder(NetProtocolCodec codec) {
		
		this.codec = codec;
	}
	

	
	/** 读取QQ协议 */
	private boolean readQQProtocol(ByteBuf in) {
		int matchCount = 0;
		// tgw_l7_forward\r\nHost: app00659110.qzoneapp.com:80\r\n\r\n"
		// .getBytes("utf-8")
		while (in.isReadable()) {
			byte b = in.readByte();
			if (b == '\r') {
				byte next = in.readByte();
				if (next == '\n') {
					matchCount++;
					if (matchCount >= 3) {
						return true;
					}
				}
			}
		}
		return false;
		
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		// TODO Auto-generated method stub
		if (in.readableBytes() > NetConst.CODE_LENGTH) {
			 in.markReaderIndex();      ////我们标记一下当前的readIndex的位置 
			int size = in.readInt();
			// log.error("包长::"+size);
			if (size == 1952937823) {
				// in.skip(8);
				// log.error("d::"+d+"::v::"+v);
				 readQQProtocol(in);
				 return;
			} else {
				if (size > in.readableBytes()) {
					 in.resetReaderIndex(); //读到的消息体长度如果小于我们传送过来的消息长度，则resetReaderIndex. 这个配合markReaderIndex使用的。把readIndex重置到mark的地方
					return;
				}
				
				byte data[] = new byte[size];
				in.readBytes(data);
				
				NetMessage msg = null;
				try {
					msg = codec.decode(data);
					logger.info("Net接收消息::"+Integer.toHexString(msg.getCommand()));
				} catch (Exception e) {
					logger.error("decode error::", e);
				}
				if (msg != null) {
					out.add(msg);
				} else {
				
				}
			}
			}
	}
}