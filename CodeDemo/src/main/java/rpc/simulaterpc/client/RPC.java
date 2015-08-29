package rpc.simulaterpc.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import rpc.simulaterpc.po.parameter;
import rpc.simulaterpc.protocol.Invocation;


public class RPC {
	
	public static <T> T getProxy(final Class<T> clazz,final String key) {
		
		parameter  param = new parameter(key);
		final ClientSocket client = new ClientSocket(param);
		InvocationHandler handler = new InvocationHandler() {
			
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				Invocation invo = new Invocation();
				invo.setInterfaces(clazz);
				invo.setMethod(new rpc.simulaterpc.protocol.Method(method.getName(),method.getParameterTypes()));
				invo.setParams(args);
				invo.setObjectKey(key);
				client.invoke(invo);
				return invo.getResult();
			}
		};
		T t = (T) Proxy.newProxyInstance(RPC.class.getClassLoader(), new Class[] {clazz}, handler);
		return t;
	}
}
	



	
	
	

