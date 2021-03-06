package socket.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

public class TimeServer {

	public void bind(int port) throws Exception {
		// 配置服务端的NIO线程绿
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					.option(ChannelOption.SO_BACKLOG, 1024)
					.childHandler(new ChildChannelHandler());
			// 绑定端口，同步等待成势
			ChannelFuture f = b.bind(port);
//			ChannelFuture f2 = b.bind(8081);
			// 等待服务端监听端口关闿
			f.channel().closeFuture().sync();
		} finally {
			// 优雅逿Ǻ，释放线程池资源
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

	private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {
		@Override
		protected void initChannel(SocketChannel arg0) throws Exception {
			// server端发送的是httpResponse，所以要使用HttpResponseEncoder进行编码
			arg0.pipeline().addLast(new HttpResponseEncoder());
            // server端接收到的是httpRequest，所以要使用HttpRequestDecoder进行解码
			arg0.pipeline().addLast(new HttpRequestDecoder());
            
			//处理字节流的
//			arg0.pipeline().addLast(new TimeServerHandler());
			arg0.pipeline().addLast(new HttpServerHandler());
		}

	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		int port = 8080;
		if (args != null && args.length > 0) {
			try {
				port = Integer.valueOf(args[0]);
			} catch (NumberFormatException e) {
				// 采用默认倿
			}
		}
		new TimeServer().bind(port);
	}
}