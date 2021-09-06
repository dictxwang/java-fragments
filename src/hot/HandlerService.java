package hot;

public class HandlerService {
	
	private static HandlerServiceImpl impl = new HandlerServiceImpl();

	public static void handle() {
		// 这里也会触发相关类的重新加载
		HandlerServiceImpl.handle();
		System.out.println(String.format("Your name is %s", impl.showName()));
	}
}
