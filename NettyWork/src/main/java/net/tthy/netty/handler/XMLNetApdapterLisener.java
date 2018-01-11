package net.tthy.netty.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.tthy.netty.codec.IBeanInvoke;
import net.tthy.netty.codec.ProxyXmlBean;
import net.tthy.netty.session.IConnect;



public abstract class XMLNetApdapterLisener<T> implements INetApdapterLisener<T> {
	private Logger log = LoggerFactory.getLogger(this.getClass());

	public void doProxy(IConnect session, T msg, ProxyXmlBean bean) {
		//String poxyCode = "";

		//poxyCode = String.format("%1$0#6x", new Object[] { Integer.valueOf(po.code) }).toUpperCase();
		if ("all-pass" != bean.getRights()) {
			IBeanInvoke<T> beanInvoke = getInvokeBean(bean.getEntryBean());
			if (beanInvoke != null) {
				beanInvoke.invoke(msg, session);
			} else
				log.error("have not IBeanInvoke:" + bean.getClassName());
		} else {
			
			return;
		}
	}

	public final void doProxy(IConnect session, T po) {
	}


	public void haveNoProxyCodeError(IConnect net) {
	}

	public void haveNoProxyError(IConnect net) {
	}

	public void decodeProxyError(IConnect net) {
	}

	protected abstract IBeanInvoke<T> getInvokeBean(String paramString);
}