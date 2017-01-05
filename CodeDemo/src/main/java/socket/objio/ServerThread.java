package socket.objio;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.Socket;
/**
 * 该类为多线程类，用于服务端
 */
public class ServerThread implements Runnable {

	private Socket client = null;
	public ServerThread(Socket client){
		this.client = client;
	}
	public void run() {
		try{
			//获取Socket的输出流，用来向客户端发送数据
			 ObjectInputStream chatois = new ObjectInputStream(client.getInputStream()); 
			//获取Socket的输入流，用来接收从客户端发送过来的数据
//			BufferedReader buf = new BufferedReader(new InputStreamReader(client.getInputStream()));
			 int i = 0;
			boolean flag =true;
			while(flag){
				i++;
				message ms = (message)chatois.readObject();
				if(i==3){
					client.close();
					return;
				}
				System.out.println("发送人"+ms.getSender()+"收到人"+ms.getGetter()+"消息内容"+ms.getCont());
			}
			client.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
