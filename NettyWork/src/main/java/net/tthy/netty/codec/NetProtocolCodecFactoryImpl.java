package net.tthy.netty.codec;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.Message;
import com.lyh.protocol.BaseMessage.NetMessage;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

public class NetProtocolCodecFactoryImpl extends ByteToMessageCodec<Object> {
	private Map<String, Integer> map;
	private Logger logger = LoggerFactory.getLogger(NetProtocolCodecFactoryImpl.class);
	private final NettyProtocolEncoder encoder;
	private final NettyProtocolDecoder decoder;

	private NetProtocolCodec codec = new NetProtocolCodec();

	public NetProtocolCodecFactoryImpl(Map<String, Integer> map) {

		this.encoder = new NettyProtocolEncoder(this.codec, map);
		this.decoder = new NettyProtocolDecoder(codec);
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		// TODO Auto-generated method stub
		decoder.decode(ctx, in, out);
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, Object obj, ByteBuf out) throws Exception {
		// TODO Auto-generated method stub
		encoder.encode(ctx, obj, out);

	}



}