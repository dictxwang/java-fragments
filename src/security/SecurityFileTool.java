package security;

import java.io.File;
import java.io.IOException;
import java.security.AccessControlException;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * 带安全策略的文件工具
 * @author wangqiang
 * 将此类导出成jar包（lab-security.jar），交由另外的工程引用，在另外的工程中启用安全检查
 */
public class SecurityFileTool {

	private final static String folder = "D:\\java_study_project\\lab-client\\temp";
	
	public static void makeFile(String fileName) {
		try {
			File fs = new File(folder + "\\" + fileName);
			fs.createNewFile();
		} catch (AccessControlException exp) {
			exp.printStackTrace();
		} catch (IOException exp) {
			exp.printStackTrace();
		}
	}
	
	// 通过特权执行
	public static void doPrivilegedAction(final String fileName) {
		AccessController.doPrivileged(new PrivilegedAction<String>() {
			@Override
			public String run() {
				makeFile(fileName);
				return null;
			}
		});
	}
}

