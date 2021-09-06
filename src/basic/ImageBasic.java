package basic;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import org.fusesource.hawtbuf.ByteArrayOutputStream;

// 常用的图片操作。 
// java图片操作相关的api集中在java.awt和javax.imageio两个包
public class ImageBasic {

	private static String imageJpg = "/Users/wangqiang/EclipseSpace/images/001.jpg";
	
	public static void main(String[] args) {
//		readImage();
//		byte[] verifyImageBytes = generateImageVerifyCode(100, 40);
//		System.out.printf("verifyImageBytes length: %d\n", verifyImageBytes.length);
		String imageBase64 = makeWatermark("wangqiang");
		System.out.printf("Watermark image base64:\n%s\n", imageBase64);
	}
	
	// 读取图片的方式
	public static void readImage() {
		try {
			// 不常使用这种方式，Image类的api较少
			Image image = Toolkit.getDefaultToolkit().getImage(imageJpg);
			System.out.printf("Toolkit getImage: height=%d,width=%d\n",
					image.getHeight(null), image.getWidth(null));
			// 通常使用BufferedImage，api更丰富
			BufferedImage bimage = ImageIO.read(new File(imageJpg));
			System.out.printf("ImageIo read: height=%d,width=%d\n",
					bimage.getHeight(), bimage.getWidth());
			
			// 通过图片字节流构造图片对象，这种方式多应用于从网络上加载的图片
			// 这里为了方便模拟，采用从本地加载图片的字节数组
			File imageFile = new File(imageJpg);
			FileImageInputStream imageStream = new FileImageInputStream(imageFile);
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			int bufferSize = 1024;
			int len = 0;
			byte[] buffer = new byte[bufferSize];
			while ((len = imageStream.read(buffer, 0, 1024)) > 0) {
				outputStream.write(buffer, 0, len);
			}
			byte[] imageBytes = outputStream.toByteArray();
			ByteArrayInputStream intputStream = new ByteArrayInputStream(imageBytes);
			// 通过字节数组输出流生成图像
			Image image2 = ImageIO.read(intputStream);
			System.out.printf("imageFromByteArray: height=%d, width=%d\n", image2.getHeight(null), image2.getWidth(null));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// 生成验证码图片
	public static byte[] generateImageVerifyCode(int imageWidth, int imageHeight) {
		// 创建图片缓冲，类型为3字节的RGB
		BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_3BYTE_BGR);
		// 获取画布
		Graphics graphics = image.getGraphics();
		// 填充画布背景色
		graphics.setColor(new Color(255, 255, 255));
		// 填充矩形,位置坐标(0, 0)
		graphics.fillRect(0, 0, imageWidth, imageHeight);
		Random random = new Random();
		// 绘制验证码字符的数量
		int codeCount = 4;
		// 备选的字符集合
		char[] chars = {'a', 'b', 'c', '1', '2', '3', 'X', 'Y', 'Z'};
		
		// 绘制验证码，逐个字符绘制
		for (int i = 0; i < codeCount; i++) {
			// 设置字符颜色（随机颜色）
			graphics.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
			// 计算字符的位置坐标
			int x = i * imageWidth / codeCount * 90 / 100;
			int y = (int)((imageHeight * 60 / 100) * random.nextDouble() + (imageHeight * 30 / 100));
			// 设置字体信息
			graphics.setFont(new Font(null, Font.BOLD | Font.ITALIC, y));
			String code = String.valueOf(chars[random.nextInt(chars.length)]);
			graphics.drawString(code, x, y);
		}
		
		File outFile = new File("/Users/wangqiang/EclipseSpace/images/verifyCode.jpg");
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		try {
			// 保存到本地文件
			ImageIO.write(image, "JPEG", outFile);
			// 输出到字节流，便于网络传输
			ImageIO.write(image, "JPEG", outStream);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			graphics.dispose();
		}
		return outStream.toByteArray();
	}
	
	/**
	 * 图片加水印。 支持格式：jpg、png、bmp、gif，如果是webp需要先进行格式转换
	 * @param markText
	 * @return
	 */
	public static String makeWatermark(String markText) {
		String resultBase64 = null;
		try {
			// 获取原图的对象
			BufferedImage image = ImageIO.read(new File(imageJpg));
			int width = image.getWidth();
			int height = image.getHeight();
			int fontSize = 20;
			// 构造新图像
			BufferedImage wmImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			// 新建画布
			Graphics2D graphics = wmImage.createGraphics();
			// 先绘制原图作为背景
			graphics.drawImage(image, 0, 0, width, height, null);
			// 设置字体样式
			graphics.setFont(new Font("Arial", Font.PLAIN, fontSize));
			// 水印透明设置
			AlphaComposite tsComposite = AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.2f);
			AlphaComposite normalComposite = AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.1f);
			/** step1：添加时间戳水印 */
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
			String timestamp = sdf.format(new Date());
			// 修正坐标数据，防止越界异常
			int top = fontSize >= height ? height - 1 : fontSize;
			int left = width - fontSize * timestamp.length() / 2;
			if (left < 0) {
				left = 0;
			} else if (left > width) {
				left = width - 1;
			}
			// 获取水印位置的颜色值
			int pixel = image.getRGB(left, top);
			int red = (pixel & 0xff0000) >> 16;
			int green = (pixel & 0xff00) >> 8;
			int blue = (pixel & 0xff);
			// 根据水印位置原图的颜色深浅确定水印字体的颜色
			if ((red & green & blue) > 100) {
				graphics.setColor(new Color(0, 0, 0));
			} else {
				graphics.setColor(new Color(255, 255, 255));
			}
			// 设置透明度
			graphics.setComposite(tsComposite);
			// 绘制时间戳水印
			graphics.drawString(timestamp, left, top);
			
			/** step2: 添加指定水印，如用户ID */
			// 重新设置透明度和颜色
			graphics.setColor(new Color(0, 0, 0));
			graphics.setComposite(normalComposite);
			// 设置旋转，以图像中心为原点旋转
			// 旋转15度
			graphics.rotate(Math.toRadians(15), (double)width / 2, (double)height / 2);
			int wmWidth = markText.length() * fontSize / 2 + 20;
			int x = 5;
			// 因为增加了旋转，因此需要增加水印范围宽度，避免旋转后水印缺失
			for (; x < width * 2; x += wmWidth) {
				int y = 0 - height;
				for (; y < height * 2; y += fontSize + 10) {
					graphics.drawString(markText, x, y);
				}
			}
			// 释放画布
			graphics.dispose();
			
			File outFile = new File("/Users/wangqiang/EclipseSpace/images/wartered.jpg");
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			// 输出到本地文件
			ImageIO.write(wmImage, "JPEG", outFile);
			// 输出到字节数组，并转换成base64编码输出
			ImageIO.write(wmImage, "JPEG", outputStream);
			StringBuilder imageBase64 = new StringBuilder("data:image/jpg");
			imageBase64.append(";base64,");
			// Linux环境下直接用base64命令即可获取图片的base64编码
			imageBase64.append(new String(Base64.getEncoder().encode(outputStream.toByteArray()), "UTF-8"));
			resultBase64 = imageBase64.toString();	
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resultBase64;
	}
}
