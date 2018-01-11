package net.tthy.netty.codec;

import java.util.Map;

import com.google.protobuf.Message;

import io.netty.buffer.ByteBuf;

public abstract interface ICodec<K,V> {
	public abstract ByteBuf encode(Message paramProxy,Map<K,V> map);

	public abstract Message decode(byte data[]) throws Exception;
}