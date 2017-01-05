package socket.io;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.Socket;
/**
 * 该类为多线程类，用于服务端
 */
public class ServerThreadTwo implements Runnable {

	private Socket client = null;
	public ServerThreadTwo(Socket client){
		this.client = client;
	}
	
	public void run() {
		try{
			//获取Socket的输出流，用来向客户端发送数据
			PrintStream out = new PrintStream(client.getOutputStream());
			//获取Socket的输入流，用来接收从客户端发送过来的数据
//			BufferedReader buf = new BufferedReader(new InputStreamReader(client.getInputStream()));
			InputStream input = client.getInputStream();
			ObjectInputStream buf = new ObjectInputStream(client.getInputStream());
			boolean flag =true;
			while(flag){
				//接收从客户端发送过来的数据,阻塞等待
				int str = buf.read();
				System.out.println(str);
				out.println("返回给客户端");
				
//				String str =  buf.readLine();
//				if(str == null || "".equals(str)){
//					flag = false;
//				}else{
//					if("bye".equals(str)){
//						flag = false;
//					}else{
//						//将接收到的字符串前面加上echo，发送到对应的客户端
//						out.println("echo:" + str);
//					}
//				}
			}
			out.close();
			client.close();
			System.out.println();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
