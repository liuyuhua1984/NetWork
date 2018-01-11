package net.tthy.netty.codec;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.Message;
import com.lyh.protocol.BaseMessage.InnerMessage;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import net.tthy.netty.utils.NetConst;


public class ProtocolCodec implements ICodec<String, Integer> {
	private Logger log = LoggerFactory.getLogger(ProtocolCodec.class);
	
	public ByteBuf encode(Message msg, Map<String, Integer> map) {
		Integer head = map.get(msg.getDescriptorForType().getName());
		if (head != null) {
		
			InnerMessage enMsg = InnerMessage.newBuilder().setCommand(head.intValue()).setBody(msg.toByteString()).build();
			byte data[] = enMsg.toByteArray();
			
			ByteBuf buffer =ByteBufAllocator.DEFAULT.directBuffer(data.length + NetConst.CODE_LENGTH);
			buffer.writeInt(data.length);
			buffer.writeBytes(data);
			return buffer;
		} else {
			log.info("not have head：" + msg.getDescriptorForType().getName());
		}
		
		return null;
	}
	
	public InnerMessage decode(byte data[]) throws Exception {
		return InnerMessage.parseFrom(data);
	}
	
}