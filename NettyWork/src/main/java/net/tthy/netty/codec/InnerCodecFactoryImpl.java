package net.tthy.netty.codec;

import java.util.List;
import java.util.Map;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

public class InnerCodecFactoryImpl  extends ByteToMessageCodec<Object> {
	private final InnerProtocolEncoder encoder;
	private final InnerProtocolDecoder decoder;
	private ProtocolCodec codec = new ProtocolCodec();


	public InnerCodecFactoryImpl(Map<String,Integer> map) {
	
		this.encoder = new InnerProtocolEncoder(this.codec,map);
		this.decoder = new InnerProtocolDecoder(codec);
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