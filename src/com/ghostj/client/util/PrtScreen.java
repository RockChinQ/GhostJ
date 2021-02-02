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
	public static Robot robot = null;

	static {
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	public static long saveScreen(double szRate,double clRate,String file,Dimension size,Point startPoint,int hint)throws Exception{
		long s=new Date().getTime();
		try {
			// 获取截图的大小
			Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
			// java自动化类
			// 创建输出
			OutputStream out = new FileOutputStream(new File(file));
//			System.out.println(screenRect.getWidth()+" "+screenRect.getHeight());
			// 截图
			BufferedImage image = robot.createScreenCapture(screenRect);
			//调整颜色
			BufferedImage result2=image;
			if(clRate<=0.999||clRate>=1.0001)
				result2=new ImageConvert(image).changeColorSpace(clRate).getProduct();

			//裁剪
			//调整大小参数
			if (size.width==-1){
				size.setSize(result2.getWidth(),result2.getHeight());
			}
			//调整起点参数
			int x=0,y=0,rh= size.height,sx= startPoint.x,sy= startPoint.y, rw= size.width, ih=result2.getHeight(),iw=result2.getWidth();
			if (sx+rw>iw) {
				sx=iw-rw;
			}
			if (sy+rh>ih) {
				sy=ih-rh;
			}
			if (sx<0)
				sx=0;
			if (sy<0)
				sy=0;
			BufferedImage result3=new BufferedImage(size.width,size.height,hint);
			result3.createGraphics().drawImage(
					result2.getSubimage(sx,sy,size.width,size.height),0,0,null);

			//调整缩放
			BufferedImage result=result3;
			if(szRate<=0.999||szRate>=1.0001)
				result=new ImageConvert(result3).changeResolutionRate(szRate).getProduct();

			// 保存为png
			ImageIO.write(result, "png", out);
			out.close();
		}catch (Exception e){
			throw e;
		}
		return new Date().getTime()-s;
	}
	public static long saveScreen(double szRate,double clRate,String file)throws Exception{
		return saveScreen(szRate,clRate,file,new Dimension(-1,-1),new Point(0,0),BufferedImage.TYPE_INT_ARGB);
	}
}
