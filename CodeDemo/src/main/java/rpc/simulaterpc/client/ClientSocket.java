package rpc.simulaterpc.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import rpc.simulaterpc.po.parameter;
import rpc.simulaterpc.protocol.Invocation;

public class ClientSocket {
	
	public parameter parameter = null;
	
	private Socket socket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;

	public ClientSocket(parameter parameter) {
		this.parameter = parameter;
	}

	public void init() throws UnknownHostException, IOException {
		socket = new Socket(parameter.getHost(), parameter.getPort());
		oos = new ObjectOutputStream(socket.getOutputStream());
	}

	/**
	 * 客户端以Invocation对象作为传输数据的载体，发送到服务端，服务端接收到这个对象之后，
	 * 解析对象，然后调用服务端相应的方法，填充这个对象的result属性字段，然后在传递给客户端，
	 * 客户端现在拿到这个返回对象就是服务端填充数据之后的对象，然后返回给客户端。从而完成RPC
	 * 的调用过程。
	 * 
	 * 扩展：
	 * 1、基本利用socket来完成数据的传递;
	 * 2、也可以利用XML等相关的数据组织方式，作为数据的载体进行数据传递，然后客户端再利用socket来进行数据的接收
	 * 
	 */
	public void invoke(Invocation invo) throws UnknownHostException, IOException, ClassNotFoundException {
		init();
		System.out.println("invoke is runing");
		oos.writeObject(invo);
		oos.flush();
		ois = new ObjectInputStream(socket.getInputStream());
		
		Invocation result = (Invocation) ois.readObject();
		
		invo.setResult(result.getResult());
	}

}
