package rpc.simulaterpc.protocol;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 客户端与服务端进行数据信息传输的对象，
 * 里面属性是客户端需要给服务端传输的相关信息，以对象序列化的方式，传输到服务端，
 * 因此，这个对象需要序列化
 * @author Administrator
 */
public class Invocation implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String objectKey;
	
	private Class interfaces;
	private Method method;
	private Object[] params;
	private Object result;
	
	
	/**
	 * @return the result
	 */
	public Object getResult() {
		return result;
	}
	/**
	 * @param result the result to set
	 */
	public void setResult(Object result) {
		this.result = result;
	}
	/**
	 * @return the interfaces
	 */
	public Class getInterfaces() {
		return interfaces;
	}
	/**
	 * @param interfaces the interfaces to set
	 */
	public void setInterfaces(Class interfaces) {
		this.interfaces = interfaces;
	}
	/**
	 * @return the method
	 */
	public Method getMethod() {
		return method;
	}
	/**
	 * @param method the method to set
	 */
	public void setMethod(Method method) {
		this.method = method;
	}
	/**
	 * @return the params
	 */
	public Object[] getParams() {
		return params;
	}
	/**
	 * @param params the params to set
	 */
	public void setParams(Object[] params) {
		this.params = params;
	}
	
	
	public String getObjectKey() {
		return objectKey;
	}
	public void setObjectKey(String objectKey) {
		this.objectKey = objectKey;
	}
	@Override
	public String toString() {
		return interfaces.getName()+"."+method.getMethodName()+"("+Arrays.toString(params)+")";
	}
	
}
