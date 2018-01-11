package net.tthy.netty.codec;

import java.util.Map;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.Message;
import com.lyh.protocol.BaseMessage.NetMessage;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import net.tthy.netty.utils.NetConst;


public class NetProtocolCodec implements ICodec<String, Integer> {
	private Logger log = LoggerFactory.getLogger(NetProtocolCodec.class);
	
	public ByteBuf encode(Message msg, Map<String, Integer> map) {
		Integer head = map.get(msg.getDescriptorForType().getName());
		if (head != null) {
			NetMessage enMsg = NetMessage.newBuilder().setCommand(head.intValue()).setBody(msg.toByteString()).build();
			byte data[] = enMsg.toByteArray();
			ByteBuf buffer = ByteBufAllocator.DEFAULT.directBuffer(data.length + NetConst.CODE_LENGTH);
		//	ByteBuf buffer = IoBuffer.allocate(data.length + NetConst.CODE_LENGTH);
			buffer.writeInt(data.length);
			buffer.writeBytes(data);
			
		//	log.info("Net发送消息::"+Integer.toHexString(head.intValue()));
			
			return buffer;
		} else {
			log.info("not have head：" + msg.getDescriptorForType().getName());
		}
		
		return null;
	}
	
	public NetMessage decode(byte data[]) throws Exception {
		return NetMessage.parseFrom(data);
	}
	
}