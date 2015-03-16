package rpc.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class HelloImpl extends UnicastRemoteObject implements IHello {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String bodyName = "";
	
    public HelloImpl() throws RemoteException {
    }

    public String helloWorld() throws RemoteException {
    	System.out.println("helloWorld  success ");
        return bodyName;
    }

    public String sayHelloToSomeBody(String someBodyName) throws RemoteException {
    	System.out.println("helloWorld  success ");
    	this.bodyName = someBodyName;
        return "someBodyName";
    }
}
