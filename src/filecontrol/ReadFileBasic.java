package filecontrol;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import org.fusesource.hawtbuf.ByteArrayOutputStream;

public class ReadFileBasic {

	private static String filepath01 = "/Users/wangqiang/WebFileSpace/001.log";
	private static String newFilepath01 = "/Users/wangqiang/WebFileSpace/copyed_001_01.log";
	private static String newFilepath02 = "/Users/wangqiang/WebFileSpace/copyed_001_02.log";
	
	public static void main(String[] args) throws Exception {
		System.out.println(readAllByIO(filepath01).length);
//		System.out.println(readAllToByteArray(filepath01).length);
//		System.out.println(readAllByByteBuffer(filepath01).length);
		
//		System.out.println(readAllByFiles(filepath01).length);
//		System.out.println(readAllLinesByFiles(filepath01).size());
		
//		System.out.println(readAllByMapperdBuffer(filepath01).length);
		// 将字节转变成字符串输出
//		System.out.println(new String(readAllByIO(filepath01), StandardCharsets.UTF_8));
		System.out.println(readAllByBuffered(filepath01).length);
		
		// 文件复制
//		copyByTransferTo(filepath01, newFilepath01);
//		copyByChannel(filepath01, newFilepath02);
	}
	
	// 通过Files的“暴力”方式
	private static byte[] readAllByFiles(String filepath) throws IOException {
		return Files.readAllBytes(new File(filepath).toPath());
	}
	
	// 通过Files按行读取
	private static List<String> readAllLinesByFiles(String filepath) throws IOException {
		return Files.readAllLines(new File(filepath).toPath(), StandardCharsets.UTF_8);
	}

	// 以传统方式读取所有的字节
	private static byte[] readAllByIO(String filepath) throws IOException {
		long st = System.currentTimeMillis();
		File file = new File(filepath);
		long fileSize = file.length();
		if (fileSize > Integer.MAX_VALUE) {
			System.out.println("file size is too big...");
			return null;
		}
		byte[] result = null;
		try (FileInputStream fis = new FileInputStream(file);) {
			result = new byte[(int) fileSize];
			int offset = 0, len = 0;
			while (offset < result.length
					&& (len = fis.read(result, offset, result.length - offset)) >= 0) {
				offset += len;
			}
			// 确保数据已经完整读取
			if (offset != result.length) {
				throw new IOException("could not comletely read file");
			}
		}
		long cost = System.currentTimeMillis() - st;
		System.out.printf("ByIO cost %dms\n", cost);
		return result;
	}
	
	private static byte[] readAllByBuffered(String filepath) throws IOException {
		long st = System.currentTimeMillis();
		File file = new File(filepath);
		byte[] result = null;
		
		try (FileInputStream fis = new FileInputStream(file);
				BufferedInputStream bis = new BufferedInputStream(fis);) {
			long size = fis.getChannel().size();
			// 不能读取文件字节数超过最大整数长度的文件
			result = new byte[(int)size];
			byte[] buffered = new byte[4096];
			int readLength = -1;
			int copyLength = 0;
			while ((readLength = fis.read(buffered)) > 0) {
				System.arraycopy(buffered, 0, result, copyLength, readLength);
				copyLength += readLength;
			}
		}
		long cost = System.currentTimeMillis() - st;
		System.out.printf("AllByBuffered cost %dms\n", cost);
		return result;
	}
	
	// 输出到字节数组流
	private static byte[] readAllToByteArray(String filepath) throws IOException {
		File file = new File(filepath);
		byte[] result = null;
		byte[] buffer = new byte[128];
		int read = 0;
		try (FileInputStream fis = new FileInputStream(file);
				ByteArrayOutputStream output = new ByteArrayOutputStream()) {
			while ((read = fis.read(buffer, 0, 128)) > -1) {
				output.write(buffer, 0, read);
			}
			result = output.toByteArray();
		}
		return result;
	}
	
	// 使用ByteBuffer读取数据
	private static byte[] readAllByByteBuffer(String filepath) throws IOException {
		long st = System.currentTimeMillis();
		File file = new File(filepath);
		byte[] result = null;
		ByteBuffer buffer = ByteBuffer.allocate((int)file.length());
		try (FileInputStream fis = new FileInputStream(file);
				FileChannel fc = fis.getChannel()) {
			while (fc.read(buffer) > 0);
			result = buffer.array();
		}
		long cost = System.currentTimeMillis() - st;
		System.out.printf("ByByteBuffer cost %dms\n", cost);
		return result;
	}
	
	// 使用MappedByteBuffer读取，使用内存映射的方式提升大文件读取的性能
	private static byte[] readAllByMapperdBuffer(String filepath) throws IOException {
		long st = System.currentTimeMillis();
		byte[] result = null;
		try (RandomAccessFile acf = new RandomAccessFile(filepath, "r");
				FileChannel fc = acf.getChannel()) {
			MappedByteBuffer buffer = fc.map(MapMode.READ_ONLY, 0, fc.size()).load();
			result = new byte[(int) fc.size()];
			if (buffer.remaining() > 0) {
//			if (buffer.hasRemaining()) {
				buffer.get(result, 0, buffer.remaining());
			}
		}
		long cost = System.currentTimeMillis() - st;
		System.out.printf("ByMapperdBuffer cost %dms\n", cost);
		return result;
	}
	
	// 通过transferTo零拷贝机制复制文件
	// 当复制大文件时性能更佳
	private static void copyByTransferTo(String srcpath, String destpath) throws IOException {
		long st = System.currentTimeMillis();
		try (
			FileInputStream fis = new FileInputStream(srcpath);
			FileOutputStream fos = new FileOutputStream(destpath);
			FileChannel srcChannel = fis.getChannel();
			FileChannel destChannel = fos.getChannel();
		) {
			srcChannel.transferTo(0, srcChannel.size(), destChannel);
		}
		long cost = System.currentTimeMillis() - st;
		System.out.printf("cost %d\n", cost);
	}
	
	// 通过Channel+ByteBuffer复制文件
	// 复制小文件时（大约100MB以内，具体视机器性能定）时耗时更短
	private static void copyByChannel(String srcpath, String destpath) throws IOException {
		long st = System.currentTimeMillis();
		try (
				FileInputStream fis = new FileInputStream(srcpath);
				FileOutputStream fos = new FileOutputStream(destpath);
				FileChannel srcChannel = fis.getChannel();
				FileChannel destChannel = fos.getChannel();
			) {
			// 此处用ByteBuffer.allocateDirect堆外内存时，耗时更短	
			ByteBuffer bb = ByteBuffer.allocate((int)srcChannel.size());
			while (srcChannel.read(bb) > 0);
			bb.flip();
			while (destChannel.write(bb) > 0);
			
			// 对于较大文件的拷贝，最好采用分块读取的方式
			/*
			ByteBuffer bb = ByteBuffer.allocate(4096);
			while (srcChannel.read(bb) > 0) {
				bb.flip();
				destChannel.write(bb);
				bb.clear();
			}
			*/
		}
		long cost = System.currentTimeMillis() - st;
		System.out.printf("cost: %d\n", cost);
	}
}
