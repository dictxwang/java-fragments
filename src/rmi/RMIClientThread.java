package rmi;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Random;
import java.util.UUID;

public class RMIClientThread extends Thread {

	private String name = "";
	private static final Random r = new Random();
	
	public RMIClientThread(String name) {
		this.name = name;
	}

	@Override
	public void run() {
		int count = 0;
		while (++count <= 10) {
			try {
				// 如果用Naming方式，需要注意lookup的name包括了host和端口信息
//				IHello rhello = (IHello)Naming.lookup("rmi://10.131.30.221:8000/rhello");
				// 使用getRegistry的方式更加合理
				Registry registry = LocateRegistry.getRegistry("10.131.30.221", 8000);
				IHello rhello = (IHello)registry.lookup("rmi://127.0.0.1:8000/rhello");
				/**
				 * 列出所有的 name bound
				 */
//				String[] allServices = registry.list();
//				for (String service : allServices) {
//					System.out.println(service);
//				}
				// lookup的name需要和server端一致
				
				String uuid = UUID.randomUUID().toString();
				System.out.println(name + "-"+ count +":" + rhello.helloWorld());
				System.out.println(name + "-"+ count +":" + rhello.sayHelloToSomeBody(uuid));
				System.out.println(name + "-" + count + ":" + rhello.getFruit("apple", "green").toString());
				long rnd = r.nextInt(1500) + 500;
				Thread.sleep(rnd);
			} catch (Exception exp) {
				System.out.println(name + "-" + count + ":rmi客户端错误>>>>>>");
				exp.printStackTrace();
			}
		}
	}
}
