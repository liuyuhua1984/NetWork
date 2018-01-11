package net.tthy.netty.codec;

public class ProxyXmlBean {
	private Integer code;
	private String className;
	private String methodName;
	private String entryBean;
	private String rights;

	public String getRights() {
		return this.rights;
	}

	public void setRights(String rights) {
		this.rights = rights;
	}

	public String getClassName() {
		return this.className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return this.methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getEntryBean() {
		return this.entryBean;
	}

	public void setEntryBean(String entryBean) {
		this.entryBean = entryBean;
	}

	public Integer getCode() {
		return this.code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}
}