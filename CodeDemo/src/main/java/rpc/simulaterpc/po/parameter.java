package rpc.simulaterpc.po;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.log4j.Logger;

public class parameter {
	
	private Logger log = Logger.getLogger(parameter.class);
	
	 String host;
	 
     int port;
     
     String name;
     
     boolean isRuning = true;
     
     public parameter(String str) {
     	this(str, true);
     }
     
     public  parameter(String str,boolean isRuning) {
		try {
			URI uri = new URI(str);
			String name = uri.getPath().substring(1);
			String host = uri.getHost();
			int port = uri.getPort();
			
			this.host = host;
			this.port = port;
			this.name = name;
			this.isRuning = isRuning;
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
     }
     
 	public Logger getLog() {
		return log;
	}

	public void setLog(Logger log) {
		this.log = log;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isRuning() {
		return isRuning;
	}

	public void setRuning(boolean isRuning) {
		this.isRuning = isRuning;
	}

}
