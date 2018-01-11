package net.tthy.netty.codec;

import net.tthy.netty.session.IConnect;

public abstract interface IBeanInvoke<T> {
	public abstract void invoke(T msg, IConnect con);
}