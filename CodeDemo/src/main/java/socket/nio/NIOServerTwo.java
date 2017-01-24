package socket.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * 1、本例子只是模拟一个连接连接过来；测试channel和buffer用法
 * 2、channel必须注册到selector选择器上，然后返回SelectionKey对象
 * 。当然SelectionKey可以获取到相应的Channel、Selector、interest事件等等；
 * 3、具体哪些事件准备好了，如读、写等等，SelectionKey可以判断； 4、数据读写都是通过channel对象来完成；
 */
public class NIOServerTwo {

	public NIOServerTwo() throws IOException {
	}

	/**
	 * 获取系统的通道管理器。由于此方法会抛出必须捕获的IO异常。 但是此方法再类变量，所以必须声明构造函数类捕获此异常
	 */
	final Selector selector = Selector.open();

	SelectionKey serverSocketChannelSelectionKey = null;
	SelectionKey socketChannelSelectionKey = null;

	SocketChannel socketChanel = null;

	/**
	 * 获得一个ServerSocket通道，并对该通道做一些初始化的工作
	 * 
	 * @param port
	 *            绑定的端口号
	 * @throws IOException
	 */
	public void initServer(int port) throws IOException {
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.configureBlocking(false);
		InetSocketAddress socketAddress = new InetSocketAddress(port);
		serverSocketChannel.bind(socketAddress);

		ChannelPerson person = new ChannelPerson("zhangsan", "port");
		serverSocketChannelSelectionKey = serverSocketChannel.register(
				selector, SelectionKey.OP_ACCEPT, person);
		//serverSocketChannelSelectionKey.attach(person);
		listen(serverSocketChannel);
	}

	@SuppressWarnings("unchecked")
	public void listen(ServerSocketChannel serverSocketChannel)
			throws IOException {
		while (true) {
			// select函数必须被调用，表示select模型开始执行
			selector.select();
			System.out.println("开始执行");
			/**
			 * 表示这个channel是否可以接受请求，一旦服务启动，它一直在接受新的连接请求，自然这个判断就一直成立的
			 * 因此acceptAble要判断是否为空，来确定是否有新的对象
			 */
			if (serverSocketChannelSelectionKey.isAcceptable()) {
				acceptAble(serverSocketChannel);
			}
			if (socketChannelSelectionKey.isReadable()) {
				read(socketChannelSelectionKey);
			}
		}
	}

	public void acceptAble(ServerSocketChannel serverSocketChannel)
			throws IOException {
//		 serverSocketChannelSelectionKey.attach(person)设置对象可以通过下面获取
		Object attachMent = serverSocketChannelSelectionKey.attachment();
		System.out.println(attachMent);
		// 创建一个新的SocketChannel类型的channel，注意他和serverSocketChannel是不一样的。
		SocketChannel newSocketChanel = serverSocketChannel.accept();
		if (newSocketChanel == null)
			return;
		socketChanel = newSocketChanel;
		// 必须设置为非阻塞
		socketChanel.configureBlocking(false);
		socketChanel.write(ByteBuffer.wrap(new String("接收到一个连接").getBytes()));
		socketChannelSelectionKey = socketChanel.register(selector,
				SelectionKey.OP_READ);
	}

	/**
	 * 处理读取客户端发来的信息 的事件
	 * 
	 * @param key
	 * @throws IOException
	 */
	public void read(SelectionKey key) throws IOException {
		// 创建读取的缓冲区
		ByteBuffer buffer = ByteBuffer.allocate(100);
		socketChanel.read(buffer);
		byte[] data = buffer.array();
		String msg = new String(data).trim();
		System.out.println("服务端收到信息：" + msg);
		ByteBuffer outBuffer = ByteBuffer.wrap(msg.getBytes());
		socketChanel.write(outBuffer);// 将消息回送给客户端
	}

	/**
	 * 启动服务端测试
	 * 
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		NIOServerTwo server = new NIOServerTwo();
		server.initServer(8000);
	}
}
