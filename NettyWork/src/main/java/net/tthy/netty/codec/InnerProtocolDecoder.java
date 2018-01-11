package net.tthy.netty.codec;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lyh.protocol.BaseMessage.InnerMessage;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import net.tthy.netty.utils.NetConst;
/**
 * 
 * ClassName: InnerProtocolDecoder <br/> 
 * Function: TODO (). <br/> 
 * Reason: TODO (). <br/> 
 * date: 2015-6-30 下午4:16:05 <br/> 
 * 内部协议解析类
 * @author lyh 
 * @version
 */
public class InnerProtocolDecoder extends ByteToMessageDecoder {
	private Logger logger = LoggerFactory.getLogger(InnerProtocolDecoder.class);
	
	private ProtocolCodec codec;
	
	public InnerProtocolDecoder(ProtocolCodec codec) {
		
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
			in.markReaderIndex();
			int size = in.readInt();
			// log.error("包长::"+size);
			if (size == 1952937823) {
				// in.skip(8);
				// log.error("d::"+d+"::v::"+v);
				 readQQProtocol(in);
				return;
			} else {
				if (size > in.readableBytes()) {
					in.resetReaderIndex();
					return ;
				}
				
				byte data[] = new byte[size];
				in.readBytes(data);
				InnerMessage msg = null;
				try {
					msg = codec.decode(data);
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