package filecontrol;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class WriteFile {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//String message = "hello";
		//String filename = "D:\\wangqiang.log";
		
		String filename = "D:\\country.txt";
		
		//writeflie(filename, message);
		readfile(filename, "utf-8");
	}
	
	public static void writeflie(String filename, String filecontent) {
		BufferedWriter writer = null;
		
		try {
			File file = new File(filename);
			if (!file.exists()) {
				file.createNewFile();
			}
			writer = new BufferedWriter(new FileWriter(file, true));
			writer.write(filecontent);
			writer.newLine();
		} catch (IOException exp) {
		} finally {
			try {
				writer.close();
			} catch (Exception exp) {
			}
		}
	}
	
	public static void readfile(String filename) {
		BufferedReader reader = null;
		
		try {
			File file = new File(filename);
			if (!file.exists()) {
				return;
			}
			reader = new BufferedReader(new FileReader(file));
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}
		} catch (IOException exp) {
		} finally {
			try {
				reader.close();
			} catch (Exception exp) {
				
			}
		}
	}
	
	public static void readfile(String filename, String encoding) {
		InputStreamReader reader = null;
		BufferedReader breader = null;
		
		try {
			File file = new File(filename);
			if (!file.exists()) {
				return;
			}
			reader = new InputStreamReader(new FileInputStream(file), encoding);
			breader = new BufferedReader(reader);
			String line = null;
			while ((line = breader.readLine()) != null) {
				System.out.println(line);
			}
		} catch (IOException exp) {
			
		} finally {
			try {
				reader.close();
				breader.close();
			} catch (Exception exp) {
				
			}
		}
	}
}
