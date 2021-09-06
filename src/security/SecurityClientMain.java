package security;

import java.io.File;

/**
 * 模拟另一个工程，引用lab-security.jar
 * @author wangqiang
 *
 */
public class SecurityClientMain {

	public static void main(String[] args) {
		/**
		 * 启用权限管理（或者通过配置jvm参数 -Djava.security.manager）
		 * 同时设置jvm参数 -Djava.security.policy=D:\\xx\\filetool_policy.txt
		 * filetool_policy.txt内容：
		 * 
		   grant codebase "file:D:/java_study_project/lab-client/lib/-"  
 		   {   
  		   		permission java.io.FilePermission   
    	   		"D:\\java_study_project\\lab-client\\temp\\*", "write";   
 		   };  
		 * 
		 */
		System.setSecurityManager(new SecurityManager());
		// 成功通过特权创建文件
		SecurityFileTool.doPrivilegedAction("001.txt");
		try {
			File fs = new File("D:\\java_study_project\\lab-client\\temp\\002.txt");
			// 文件创建失败，抛出异常（access denied）
			fs.createNewFile();
		} catch (Exception exp) {
			exp.printStackTrace();
		}
		// 文件创建失败，抛出异常（access denied）
		SecurityFileTool.makeFile("003.txt");
	}
}