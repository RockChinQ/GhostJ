package com.ghostj.client.util;

import com.ghostj.util.image.ImageConvert;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class PrtScreen {
	public static void saveScreen(double rate,String file)throws Exception{
		try {

			// 获取截图的大小
			Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
			// java自动化类
			Robot robot = new Robot();
			// 创建输出
			OutputStream out = new FileOutputStream(new File(file));

			// 截图
			BufferedImage image = robot.createScreenCapture(screenRect);
			BufferedImage result=new ImageConvert(image).changeResolveRate(rate).getProduct();
			// 保存为png
			ImageIO.write(result, "png", out);
			out.close();
		}catch (Exception e){
			throw e;
		}
	}
}
