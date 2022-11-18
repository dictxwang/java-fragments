package jackson;

public class JacksonBasic {

	public static void main(String[] args) {
		
		T t = new T();
		t.name = "xyz";
		
		String s = JacksonFastWorker.getInstance().toJsonString(t);
		System.out.println(s);
		
		s = "{\"name\":\"xyz\", \"h\":\"\"}";
		t = JacksonFastWorker.getInstance().fromJsonString(s, T.class);
		System.out.println(t);
	}

	private static class T {
		public String name;
		public H h;
		
		public String toString() {
			return String.format("name=%s,h=%s", this.name, this.h != null ? this.h.toString() : "");
		}
	}
	
	private static class H {
		public int id;
		
		public String toString() {
			return String.format("id=%d", this.id);
		}
	}
}
