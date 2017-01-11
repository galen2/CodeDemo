package ssl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.security.KeyStore;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.TrustManagerFactory;

/**
 * 操作流程：
 * 1、使用Java自带的keytool命令创建证书，然后导出证书文件，提供给客户端；
 * 2、客户端利用keytoole的import参数导入此证书，这样完成了SSL的单项认证；
 * 3、服务端也可以利用相同的方式进行，完成双向认证
 * 注意事项：
 * 1、服务端在启动服务时候发现并未指定某个具体证书条目的密码，只是指定了证书库密码，
 * 因此证书条目密码必须和证书库密码保持一致；（原因未确定），这样代码底层默认用仓库密码去找具体的证书条目;
 * 2、如果证书库中有多个证书条目，且一个密码和证书库一样，另外一个不一样，此时服务器启动会跑出错误异常：
 * java.security.UnrecoverableKeyException: Cannot recover key
 * 3、如果证书库中有多个证书条目，且每一个条目都和证书库密码一样，此时是没问题的，由于某一个证书会发送给客户端，让其导入，
 * 但是客户端的请求过来连接的是哪个证书条目呢？目前猜测是，客户端请求过来之后，服务器从证书库里面遍历，只要遍历到一个合适的
 * 就可以认为请求时没问的。
 */
public class Server implements Runnable {

	/**
	 * 监听端口号
	 */
	private static final int DEFAULT_PORT = 7777;

	/**
	 * keytool创建证书库密码
	 */
	private static final String SERVER_KEY_STORE_PASSWORD = "123456";
	/**
	 * keytool创建受信任的证书库，存放导入的证书。证书库密码和上面值一样，因为某前都是同一个证书库
	 */
	private static final String SERVER_TRUST_KEY_STORE_PASSWORD = "123456";
	
	/**
	 * 证书库文件路径，当然也可以放到其他目录下面
	 */
	private static final String KEY_STORE_LOCATE = "/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home/bin/kserver.keystore";
	private static final String KEY_TRUST_STORE_LOCATE = "/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home/bin/kserver.keystore";

	private SSLServerSocket serverSocket;

	public static void main(String[] args) {
		Server server = new Server();
		server.init();
		Thread thread = new Thread(server);
		thread.start();
	}

	public synchronized void start() {
		if (serverSocket == null) {
			System.out.println("ERROR");
			return;
		}
		try {
			System.out.println("服务启动，准备接受请求");
			Socket s = serverSocket.accept();
			InputStream input = s.getInputStream();
			OutputStream output = s.getOutputStream();
	
			BufferedInputStream bis = new BufferedInputStream(input);
			BufferedOutputStream bos = new BufferedOutputStream(output);
			while (true) {
				byte[] buffer = new byte[20];
				bis.read(buffer);
				System.out.println("------receive:--------"
						+ new String(buffer).toString());
	
				bos.write("yes".getBytes());
				bos.flush();
//				s.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void init() {
		try {
			SSLContext ctx = SSLContext.getInstance("SSL");

			KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
			TrustManagerFactory tmf = TrustManagerFactory
					.getInstance("SunX509");

			KeyStore ks = KeyStore.getInstance("JKS");
			KeyStore tks = KeyStore.getInstance("JKS");

			ks.load(new FileInputStream(KEY_STORE_LOCATE),
					SERVER_KEY_STORE_PASSWORD.toCharArray());
			tks.load(new FileInputStream(KEY_TRUST_STORE_LOCATE),
					SERVER_TRUST_KEY_STORE_PASSWORD.toCharArray());

			/**
			 * 1、被客户端使用的证书密码(不是秘钥库密码)；
			 * 2、此密码必须保持和秘钥库密码一致，否则会抛出错误，因此，这里没有地方可以传入证书条目的密码；
			 */
			kmf.init(ks, SERVER_KEY_STORE_PASSWORD.toCharArray());
			tmf.init(tks);

			ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

			serverSocket = (SSLServerSocket) ctx.getServerSocketFactory()
					.createServerSocket(DEFAULT_PORT);
			serverSocket.setNeedClientAuth(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run() {
		start();
	}
}