package rpc.rmi;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class RMIClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
            //在RMI服务注册表中查找名称为RHello的对象，并调用其上的方法
			IHello rhello =(IHello) Naming.lookup("//localhost:9813/RHello");
            rhello.sayHelloToSomeBody("lisi");
/*
            URL[] urls = new URL[] {new URL("file:/" + "d:/src/")}; 
            URLClassLoader ul = new URLClassLoader(urls); 
            Class c = ul.loadClass("com.bjsxt.proxy.$Proxy1"); 
            System.out.println(c); 
            Constructor ctr = c.getConstructor(InvocationHandler.class); 
            Object m = ctr.newInstance(h); */
            
            
            
            System.out.println(rhello.helloWorld());
//            System.out.println(rhello.sayHelloToSomeBody("熔岩"));
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();  
        } 

	}

}
