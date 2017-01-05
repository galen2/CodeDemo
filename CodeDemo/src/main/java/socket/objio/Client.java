package socket.objio;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class Client {
	public static void main(String[] args) throws IOException {
		//客户端请求与本机在20006端口建立TCP连接 
		Socket client = new Socket("127.0.0.1", 8000);
		client.setSoTimeout(1000);
		//获取键盘输入 
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
	    ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
	    ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
		boolean flag = true;
		while(flag){
			System.out.print("输入信息：");
			String str = input.readLine();
			if(str.equalsIgnoreCase("byte"))
				flag=false;
			if(str.equalsIgnoreCase("close")){
				client.close();	
			}
			message msg = createObject(str);
			oos.writeObject(msg);
			try {
				ois.readObject();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//发送数据到服务端  
		}
		input.close();
	}
	
	private static message createObject(String content){
		message obj = new message();
		obj.setSender("张三");
		obj.setGetter("李四");
		obj.setCont(content);
		return obj;
	}
}