package cglib;

import net.sf.cglib.proxy.Enhancer;

public class CglibMain {

	public static void main(String[] args) {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(ServiceHandler.class);
		enhancer.setCallback(new LogInterceptor());
		
		ServiceHandler service = (ServiceHandler)enhancer.create();
		service.service("wangqiang");
		
		try {
			service.cal(10, 0);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
