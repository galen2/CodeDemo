package jmx;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import javax.management.MBeanServer;
import javax.management.ObjectName;

/**
 * @Description在Java安装目录bin下面运行jconsole
 * 
 * 操作流程：
 * 1、默认SSL证书导入导出逻辑，在客户端和服务端导入导出响应的证书文件；
 * 2、由于客户端导入了服务端的证书，因此启动时候：
 * jconsole -J-Djavax.net.ssl.trustStore=truststore  -J-Djavax.net.ssl.trustStorePassword=100200
 * 3、参考官网说明http://docs.oracle.com/javase/8/docs/technotes/guides/management/agent.html
 */
public class HelloAgent {
	
	Hello helloBean = new Hello();
	MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
	public void init() {
		try {
			/**
			 * 也可以用启动时候加入格式，java -Dproperty=value。 java  -Dcom.sun.management.jmxremote.port=9999
			 */
			System.setProperty("com.sun.management.jmxremote.port", "9999");
			System.setProperty("com.sun.management.jmxremote.authenticate", "true");
			System.setProperty("com.sun.management.jmxremote.ssl", "true");
			System.setProperty("javax.net.ssl.keyStore", "/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home/bin/kserver.keystore");
			System.setProperty("javax.net.ssl.keyStoreType", "JKS");
			System.setProperty("javax.net.ssl.keyStorePassword", "123456");
			System.setProperty("javax.net.ssl.trustStore", "/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home/bin/kserver.keystore");
			System.setProperty("javax.net.ssl.trustStoreType", "JKS");
			System.setProperty("javax.net.ssl.trustStorePassword", "123456");
			
			ObjectName helloName = new ObjectName("FOO:name=HelloBean");
			mbs.registerMBean(helloBean, helloName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void waitForEnterPressed() {
		try {
			InputStream in = System.in;
			InputStreamReader isr = new InputStreamReader(in);
			BufferedReader bufr = new BufferedReader(isr);
			while (true) {
				System.out.println("Press  to continue:");
				String str = bufr.readLine();
				System.out.println("input value is:" + str);
				helloBean.setMessage(str);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String argv[]) {
		HelloAgent agent = new HelloAgent();
		agent.init();
		System.out.println("SimpleAgent is running...");
		agent.waitForEnterPressed();
	}

}
