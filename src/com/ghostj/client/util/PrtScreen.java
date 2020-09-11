package com.ghostj.client.util;

import com.ghostj.util.image.ImageConvert;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;

public class PrtScreen {
	public static long saveScreen(double rate,double clRate,String file)throws Exception{
		long s=new Date().getTime();
		try {
			// 获取截图的大小
			Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
			// java自动化类
			Robot robot = new Robot();
			// 创建输出
			OutputStream out = new FileOutputStream(new File(file));
//			System.out.println(screenRect.getWidth()+" "+screenRect.getHeight());
			// 截图
			BufferedImage image = robot.createScreenCapture(screenRect);
			BufferedImage result=image;
			if(rate<=0.999||rate>=1.0001)
				result=new ImageConvert(image).changeResolutionRate(rate).getProduct();
			BufferedImage result2=result;
			if(clRate<=0.999||clRate>=1.0001)
				result2=new ImageConvert(result).changeColorSpace(clRate).getProduct();
			// 保存为png
			ImageIO.write(result2, "png", out);
			out.close();
		}catch (Exception e){
			throw e;
		}
		return new Date().getTime()-s;
	}
}
