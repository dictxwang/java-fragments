package proxy;

public class EasyModelMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		SimpleInvokeHandler invokeHandler = new SimpleInvokeHandler();
		EasyModel model = (EasyModel) invokeHandler.newProxy(new EasyModelImpl());
		model.printX();
		model.printY();
	}

}
