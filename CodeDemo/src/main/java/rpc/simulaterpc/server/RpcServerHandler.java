package rpc.simulaterpc.server;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;

import rpc.simulaterpc.protocol.Invocation;

public class RpcServerHandler extends Thread {
	private ServerSocket socket;
	private Server server;

	private Logger log = Logger.getLogger(RpcServerHandler.class);
	
	public RpcServerHandler(Server server) {
		this.server = server;
	}

	@Override
	public void run() {

		log.info("Listener:" + server.getPort());
		try {
			socket = new ServerSocket(server.getPort());
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}
		while (server.isRunning()) {
			try {
				Socket client = socket.accept();
				log.info("Receive a new request");
				ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
				Invocation invo = (Invocation) ois.readObject();
				log.info("Obejct of Server is invocation:" + invo);

				server.call(invo);//调用服务端对象，并初始化invo返回内容的属性值
				
				ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
				oos.writeObject(invo);//把初始化好的服务端对象，传递给客户端
				oos.flush();
				oos.close();
				ois.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			if (socket != null && !socket.isClosed())
				socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
