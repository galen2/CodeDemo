package socket.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.net.InetAddress;

/**
 * TCP/IP的NIO非阻塞方式 客户端
 * */
public class NIOClient_2 {

	// 创建缓冲区
	private ByteBuffer buffer = ByteBuffer.allocate(512);

	// 访问服务器

	public void query(String host, int port) throws IOException {
		InetSocketAddress address = new InetSocketAddress(InetAddress.getByName(host), port);
		SocketChannel socket = null;
		byte[] bytes = new byte[512];
		while (true) {
			try {
				System.in.read(bytes);
				socket = SocketChannel.open();
				socket.connect(address);
				buffer.clear();
				buffer.put(bytes);
				buffer.flip();
				socket.write(buffer);
				buffer.clear();
				
				//读取服务器数据
				ByteBuffer buffer = ByteBuffer.allocate(100);
				socket.read(buffer);//没有读到数据时候，依然阻塞
				byte[] data = buffer.array();
				String msg = new String(data).trim();
				System.out.println("客户端收到信息："+msg);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				/*if (socket != null) {
					socket.close();
				}*/
			}
		}
	}

	public static void main(String[] args) throws IOException {
		new NIOClient_2().query("localhost", 8000);
	}
}
