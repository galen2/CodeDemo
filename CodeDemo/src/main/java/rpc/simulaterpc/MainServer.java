package rpc.simulaterpc;

import rpc.simulaterpc.po.PersionImpl;
import rpc.simulaterpc.server.RPCServer;
import rpc.simulaterpc.server.Server;

public class MainServer {
	public static void main(String[] args) {
		Server server = new RPCServer();
		server.register("//localhost:9813/Person", PersionImpl.class);
		server.start();
	}

}
