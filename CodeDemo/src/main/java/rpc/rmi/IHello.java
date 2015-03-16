package rpc.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IHello extends Remote {

    /**
     * 绠�崟鐨勮繑鍥炩�Hello World锛�瀛楁牱
     * @return 杩斿洖鈥淗ello World锛�瀛楁牱
     * @throws java.rmi.RemoteException
     */
    public String helloWorld() throws RemoteException;

    /**
     * 涓�釜绠�崟鐨勪笟鍔℃柟娉曪紝鏍规嵁浼犲叆鐨勪汉鍚嶈繑鍥炵浉搴旂殑闂�璇�
     * @param someBodyName  浜哄悕
     * @return 杩斿洖鐩稿簲鐨勯棶鍊欒
     * @throws java.rmi.RemoteException
     */
    public String sayHelloToSomeBody(String someBodyName) throws RemoteException;
}