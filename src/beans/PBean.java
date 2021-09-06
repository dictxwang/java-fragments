package beans;

public class PBean {

	private static String name = "";
	private String content = "";

	public PBean() {
	}
	public PBean(String name, String content) {
		PBean.name = name;
		this.content = content;
	}
	
	public String getName() {
		return name;
	}
	
	// 静态方法的注入
	public void setName(String name) {
		PBean.name = name;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PBean [name=");
		builder.append(name);
		builder.append(", content=");
		builder.append(content);
		builder.append("]");
		return builder.toString();
	}
}
