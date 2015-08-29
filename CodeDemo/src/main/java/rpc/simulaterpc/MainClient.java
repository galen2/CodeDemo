package rpc.simulaterpc;

import rpc.simulaterpc.client.RPC;
import rpc.simulaterpc.po.Person;

public class MainClient {
	public static void main(String[] args) {
		Person echo = RPC.getProxy(Person.class, "//localhost:9813/Person");
		
		System.out.println(echo.sayName("hello,hello"));
		System.out.println(echo.sayName("hellow,rod"));
		System.out.println(echo.sayName("hellow,rod"));
		System.out.println(echo.sayName("hellow,rod"));
		System.out.println(echo.sayName("hellow,rod"));
		System.out.println(echo.sayName("hellow,rod"));
	}
}
