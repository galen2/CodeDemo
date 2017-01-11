package ssl;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.KeyStore;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManagerFactory;

public class Client {   
	  
    private static final String DEFAULT_HOST                    = "172.16.70.107";   
    private static final int    DEFAULT_PORT                    = 7777;   
  
    /**
     * 证书库密码
     */
    private static final String CLIENT_KEY_STORE_PASSWORD       = "100200";   
    
    /**
     * 证书库密码
     */
    private static final String CLIENT_TRUST_KEY_STORE_PASSWORD = "100200";   
    
    /**
     * 证书库地址
     */
    private static final String KEY_STORE_LOCATE = "D:/programFiles/jdk1.7_32/bin/klient.keystore";   
  
    private SSLSocket           sslSocket;   
  
    /**  
     * 启动客户端程序  
     *   
     * @param args  
     */  
    public static void main(String[] args) {   
       Client client = new Client();   
        client.init();   
        client.process();   
    }   
  
    
    public void process() {   
        if (sslSocket == null) {   
            System.out.println("ERROR");   
            return;   
        }   
        try {   
            InputStream input = sslSocket.getInputStream();   
            OutputStream output = sslSocket.getOutputStream();   
  
            BufferedInputStream bis = new BufferedInputStream(input);   
            BufferedOutputStream bos = new BufferedOutputStream(output);   
            while(true){
            	System.out.print("请输入继续：");
            	InputStream in = System.in;
            	InputStreamReader reader = new InputStreamReader(in);
            	BufferedReader breader = new BufferedReader(reader);
            	String readLine = breader.readLine();
            	bos.write(readLine.getBytes());   
                bos.flush();   
                byte[] buffer = new byte[20];   
                bis.read(buffer);   
                System.out.println("发送数据:"+readLine);   
                System.out.println("接受数据:"+new String(buffer));   
            }
//            sslSocket.close();   
        } catch (IOException e) {   
            System.out.println(e);   
        }   
    }   
  
  
    public void init() {   
        try {   
            SSLContext ctx = SSLContext.getInstance("SSL");   
  
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");   
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");   
  
            KeyStore ks = KeyStore.getInstance("JKS");   
            KeyStore tks = KeyStore.getInstance("JKS");   
  
            ks.load(new FileInputStream(KEY_STORE_LOCATE), CLIENT_KEY_STORE_PASSWORD.toCharArray());   
            tks.load(new FileInputStream(KEY_STORE_LOCATE), CLIENT_TRUST_KEY_STORE_PASSWORD.toCharArray());   
  
            kmf.init(ks, CLIENT_KEY_STORE_PASSWORD.toCharArray());   
            tmf.init(tks);   
  
            ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);   
  
            sslSocket = (SSLSocket) ctx.getSocketFactory().createSocket(DEFAULT_HOST, DEFAULT_PORT);   
        } catch (Exception e) {   
            System.out.println(e);   
        }   
    }   
  
}   