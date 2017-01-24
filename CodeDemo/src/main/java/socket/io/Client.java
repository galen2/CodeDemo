package socket.io;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class Client {
	public static void main(String[] args) throws IOException {
		//客户端请求与本机在20006端口建立TCP连接 
		Socket client = new Socket("127.0.0.1", 8000);
		client.setSoTimeout(1000);
		//获取键盘输入 
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		//获取Socket的输出流，用来发送数据到服务端  
		PrintStream out = new PrintStream(client.getOutputStream());
		//获取Socket的输入流，用来接收从服务端发送过来的数据  
		BufferedReader buf =  new BufferedReader(new InputStreamReader(client.getInputStream()));
		boolean flag = true;
		while(flag){
			System.out.print("输入信息：");
			String str = input.readLine();
			if("bye".equals(str)){
				flag = false;
			}
			//发送数据到服务端  
			out.println(str);
			System.out.println("发送到服务端信息："+str);
			try{
				String echo = buf.readLine();
				System.out.println("接收到服务端信息:"+echo);
			}catch(SocketTimeoutException e){
				
			}
			
		}
		input.close();
		if(client != null){
	        //如果构造函数建立起了连接，则关闭套接字，如果没有建立起连接，自然不用关闭
			client.close();	//只关闭socket，其关联的输入输出流也会被关闭
		}
	}
}