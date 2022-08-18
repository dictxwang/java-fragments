package rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServerThread extends Thread {

	@Override
	public void run() {
		try {
			IHello rhello = new HelloImpl();
			// 避免出现客户端链接失败的情况
			System.setProperty("java.rmi.server.hostname ", "127.0.0.1");
			Registry registry = LocateRegistry.createRegistry(8000);
			
			// rebind的name可以是任意字符串，例如wangqiang/rhello也可以
			registry.rebind("rmi://127.0.0.1:8000/rhello", rhello);
//			registry.rebind("wangqiang/rhello", rhello);
			System.out.println("远程对象绑定成功>>>>>");
		} catch (Exception exp) {
			System.out.println("服务端发生异常");
			exp.printStackTrace();
		}
	}
}
