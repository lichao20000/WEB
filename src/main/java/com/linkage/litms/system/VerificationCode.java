package com.linkage.litms.system;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.Global;

import sun.misc.BASE64Encoder;

public class VerificationCode {
	
	private static final int QIAN=1000;
	private static final int SHI=10;
	
	
	/**
	 * 生成验证码
	 * @return
	 * @throws IOException
	 */
	public ArrayList<String> getVerificationCode() throws IOException {
		ArrayList<String> list = new ArrayList<String>();
		if (LipossGlobals.inArea(Global.HNLT) || LipossGlobals.inArea(Global.CQDX)) {
			list = createImageWithVerifyCode(85, 21, 4);
		}
		else {
			//生成附加码
			long checkCode = Math.round(Math.random() * 10000);
			if (checkCode < QIAN) {
				checkCode = checkCode + 1000;
			}
			list.add(String.valueOf(checkCode));
		}
		return list;
	}

	/**
	 * 绘制图片
	 * @param width
	 * @param height
	 * @param length
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("restriction")
	public ArrayList<String> createImageWithVerifyCode(int width, int height, int length) throws IOException {
		ArrayList<String> list = new ArrayList<String>();
		ImageIO.setCacheDirectory(new File(this.getClass().getResource("").getPath()));
	
		String pngBase64 = "";
		// 绘制内存中的图片
		BufferedImage bufferedImage = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		// 得到画图对象
		Graphics graphics = bufferedImage.getGraphics();
		// 绘制图片前指定一个颜色
		graphics.setColor(new Color(255, 255, 255));
		graphics.fillRect(0, 0, width, height);
		// 绘制边框
		graphics.setColor(Color.white);
		graphics.drawRect(0, 0, width - 1, height - 1);
		// 步骤四 四个随机数字
		Graphics2D graphics2d = (Graphics2D) graphics;
		graphics2d.setFont(new Font("宋体", Font.BOLD, 20));
		// 获取验证码
		String word = produceNumAndChar(length);
		// 定义x坐标
		int x = 5;
		for (int i = 0; i < word.length(); i++) {
			// 随机颜色
			graphics2d.setColor(new Color(20 + Global.rand.nextInt(110), 20 + Global.rand
					.nextInt(110), 20 + Global.rand.nextInt(110)));
			// 旋转 -30 --- 30度
			int jiaodu = Global.rand.nextInt(60) - 30;
			// 换算弧度
			double theta = jiaodu * Math.PI / 180;
			// 获得字母数字
			char c = word.charAt(i);
			// 将c 输出到图片
			graphics2d.rotate(theta, x, 18);
			graphics2d.drawString(String.valueOf(c), x, 18);
			graphics2d.rotate(-theta, x, 18);
			x += 20;
		}
		// 绘制干扰线
		graphics.setColor(new Color(192, 192, 192));
		int x1;
		int x2;
		int y1;
		int y2;
		for (int i = 0; i < SHI; i++) {
			x1 = Global.rand.nextInt(width);
			x2 = Global.rand.nextInt(12);
			y1 = Global.rand.nextInt(height);
			y2 = Global.rand.nextInt(12);
			graphics.drawLine(x1, y1, x1 + x2, x2 + y2);
		}
		// 释放资源
		graphics.dispose();
		// io流
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// 写入流中
		ImageIO.write(bufferedImage, "png", baos);
		// 转换成字节
		byte[] bytes = baos.toByteArray();
		BASE64Encoder encoder = new BASE64Encoder();
		pngBase64 = encoder.encodeBuffer(bytes).trim();
		// 删除 \r\n
		pngBase64 = pngBase64.replaceAll("\n", "").replaceAll("\r", "");
		
		list.add(word);
		list.add("data:image/png;base64," + pngBase64);
		return list;
	}

	/**
	 * 获取验证码
	 * @param num
	 * @return
	 */
	public String produceNumAndChar(int num) {
		String code = "";
		String ch = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghjklmnpqrstuvwxyz";
		String n = "123456789";
		for (int i = 0; i < num; i++) {
			int flag = Global.rand.nextInt(2);
			if (flag == 0) {
				// 数字
				code += n.charAt(Global.rand.nextInt(n.length()));
			} else {
				// 字母
				code += ch.charAt(Global.rand.nextInt(ch.length()));
			}
		}
		return code;
	}

}
