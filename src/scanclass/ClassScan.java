package scanclass;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassScan {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List<String> classes = doScan("basic");
		for (String clazz : classes) {
			try {
				System.out.println(clazz);
//				Class classObj = Class.forName(clazz);
//				Annotation annotation = classObj.getAnnotation(Addressing.class);
//				if (annotation != null) {
//					System.out.println("==find this class==");
//					System.out.println(clazz);
//				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public static List<String> doScan(String packagePath) {
		List<String> result = new ArrayList<String>();
		Class classScan = ClassScan.class;
		String scanPath = packagePath.replace(".", "/");
		List<File> dirs = new ArrayList<File>();
		List<URL> jars = new ArrayList<URL>();
		try {
			Enumeration<URL> urls = classScan.getClassLoader().getResources(scanPath);
			while (urls.hasMoreElements()) {
				URL url = urls.nextElement();
				if ("file".equals(url.getProtocol())) {
					dirs.add(new File(url.getFile()));
				} else if ("jar".equals(url.getProtocol())) {
					jars.add(url);
				}
			}
			
			// 扫描文件夹或者文件
			for (File file : dirs) {
				result.addAll(findClasses(file, packagePath));
			}
			
			// 扫描jar包
			for (URL url : jars) {
				result.addAll(findClasses(url, packagePath));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 找出jar包下的class
	 * */
	public static List<String> findClasses(URL url, String packagePath) {
		List<String> result = new ArrayList<String>();
		JarFile jar;
		try {
			jar = ((JarURLConnection)url.openConnection()).getJarFile();
			Enumeration<JarEntry> jarEntries = jar.entries();
			while (jarEntries.hasMoreElements()) {
				JarEntry jarEntry = jarEntries.nextElement();
				String pname = jarEntry.getName();
				if (pname.startsWith("/")) {
					pname = pname.substring(1);
				}
				pname = pname.replace("/", ".");
				if (pname.startsWith(packagePath) && pname.endsWith(".class")) {
					result.add(pname.substring(0, pname.length() - 6));
				}
			}
		} catch (Exception exp) {
			exp.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 文件夹或者文件类型
	 * */
	public static List<String> findClasses(File directory, String packagePath) {
		List<String> result = new ArrayList<String>();
		if (!directory.exists()) {
			return result;
		}
		File[] files = directory.listFiles();
		String packagePrefix = packagePath.length() == 0 ? "" : packagePath + ".";
		for (File file : files) {
			if (file.isFile()) {
				result.add(packagePrefix + 
			file.getName().substring(0, file.getName().length() - 6));
			} else {
				result.addAll(findClasses(file, packagePrefix + file.getName()));
			}
		}
		
		return result;
	}
}
