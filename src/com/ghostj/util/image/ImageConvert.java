package com.ghostj.util.image;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.util.Date;

/**
 * Convert image object to bytecode or get from bytecode.
 * Provided methods to convert image to any resolution rate or color area.
 * @author Rock Chin
 */
public class ImageConvert {
	private BufferedImage origin;
	private BufferedImage product;
	public BufferedImage getOriginImage() {
		return origin;
	}

	public void setOriginImage(BufferedImage origin) {
		this.origin = origin;
	}

	public BufferedImage getProduct() {
		return product;
	}
	public void setProduct(BufferedImage product) {
		this.product = product;
	}

	public ImageConvert(BufferedImage origin){
		this.origin=origin;
	}

	/**
	 * change resolution rate
	 * @param rate rate on width OR height
	 * @return this
	 */
	public ImageConvert changeResolutionRate(double rate){
		long s=new Date().getTime();
		if (rate>1){
			throw new IllegalArgumentException("rate should <=1");
		}
		product=new BufferedImage((int)(origin.getWidth()*rate),(int)(origin.getHeight()*rate)
				,BufferedImage.TYPE_INT_ARGB);
//		System.out.println(product.getWidth()+" "+product.getHeight()+" "+origin.getWidth()+" "+origin.getHeight());
		//遍历origin，跳过不需要的像素
		//计算跳过几格跳过一格
		int step=(int)(1.0/(1.0-rate));
		//product的指针
		int x=0,y=0;
		for(int i=0;i<origin.getHeight();i++){
			if (i%step==0)
				continue;
			for(int j=0;j<origin.getWidth();j++){
				if (j%step==0)
					continue;
				if (x<product.getWidth()&&y<product.getHeight()&&j<origin.getWidth()&&i<origin.getHeight())
					product.setRGB(x++,y,origin.getRGB(j,i));
			}
			y++;
			x=0;
		}
//		System.out.println("change resolution spent:"+(new Date().getTime()-s));
		return this;
	}

	/**
	 * change color space
	 * @param rate color space(e.g. 0.72)
	 * @return this
	 */
	public ImageConvert changeColorSpace(double rate){
		long s=new Date().getTime();
		if (rate>1){
			throw new IllegalArgumentException("rate should <=1");
		}
//		System.out.println("step:"+(int)(1.0/rate));
		product=new BufferedImage(origin.getWidth(),origin.getHeight(),BufferedImage.TYPE_INT_ARGB);
		for(int i=0;i<origin.getHeight();i++){
			for(int j=0;j<origin.getWidth();j++){
				product.setRGB(j,i,limitColor(origin.getRGB(j,i),(int)(1.0/rate)));
			}
		}
//		System.out.println("change color space spent:"+(new Date().getTime()-s));
		return this;
	}
	public int limitColor(int rgb,int step){
		Color before=new Color(rgb);
		Color result=new Color(before.getRed()-before.getRed()%step,before.getGreen()-before.getGreen()%step,before.getBlue()-before.getBlue()%step);
		return result.getRGB();
	}

}
