package rpc.simulaterpc.server;

import rpc.simulaterpc.protocol.Invocation;

public interface Server {
	public void stop();
	
	public void start();
	
	/**
	 * 服务器启动时候，存放对象以及对象key表示在内存里面
	 * @param keyObject 对象key
	 * @param impl 对象类
	 */
	public void register(String key,Class impl);
	
	public void call(Invocation invo);
	
	public boolean isRunning();
	
	public int getPort();
}
