package rpc.simulaterpc.server;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import rpc.simulaterpc.po.parameter;
import rpc.simulaterpc.protocol.Invocation;

public  class RPCServer implements Server{
	
	private Logger log = Logger.getLogger(RPCServer.class);
	
	public  parameter  parameter = null;
	
	private Map<String ,Object> serviceEngine = new HashMap<String, Object>();
	
	@Override
	public void call(Invocation invo) {
		log.info(invo.getClass().getName());
		Object obj = serviceEngine.get(invo.getObjectKey());
		if(obj!=null) {
			try {
				//调用真实的对象方法，然后把返回的数据内容放入Invocation的属性里面，最终返回给客户端
				Method m = obj.getClass().getMethod(invo.getMethod().getMethodName(), invo.getMethod().getParams());
				Object result = m.invoke(obj, invo.getParams());
				
				invo.setResult(result);
			} catch (Throwable th) {
				th.printStackTrace();
			}
		} else {
			throw new IllegalArgumentException("has no these class");
		}
	}

	@Override
	public void register(String key, Class impl) {
		try {
			parameter = new parameter(key);
			this.serviceEngine.put(key, impl.newInstance());
			log.info(serviceEngine);
		} catch (Throwable e) {
			e.printStackTrace();
		} 
	}

	@Override
	public void start() {
		log.info("服务器开始启动");
		RpcServerHandler handler = new RpcServerHandler(this);
		this.getParameter().setRuning(true);
		handler.start();
	}

	@Override
	public void stop() {
		this.getParameter().setRuning(false);
	}

	@Override
	public boolean isRunning() {
		return getParameter().isRuning();
	}

	@Override
	public int getPort() {
		return getParameter().getPort();
	}

	public parameter getParameter() {
		return parameter;
	}

}