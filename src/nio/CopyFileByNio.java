package nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class CopyFileByNio {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String srcfile = "F:/org.springframework.jms-3.0.3.RELEASE.jar";
		String destfile = "F:/org.springframework.jms-3.0.3.RELEASE.jar.bak";
		copyFile(srcfile, destfile);
	}

	static void copyFile(String srcfile, String destfile) {
		FileInputStream fis = null;
		FileOutputStream fos = null;
		
		try {
			fis = new FileInputStream(srcfile);
			fos = new FileOutputStream(destfile);
			FileChannel cin = fis.getChannel();
			FileChannel cout = fos.getChannel();
			ByteBuffer buffer = ByteBuffer.allocate(4096);
			
			while (true) {
				buffer.clear();
				int len = cin.read(buffer);
				if (len <= 0) {
					break;
				}
				buffer.flip();
				cout.write(buffer);
			}
			System.out.println("done");
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
