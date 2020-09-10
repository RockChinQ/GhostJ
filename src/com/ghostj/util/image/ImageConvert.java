package com.ghostj.util.image;

import java.awt.image.BufferedImage;

/**
 * Convert image object to bytecode or get from bytecode.
 * Provided some methods to convert image to any resolve rate or color area.
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
	 * change solve rate
	 * @param rate rate on width OR height
	 * @return this
	 */
	public ImageConvert changeResolveRate(double rate){
		if (rate>1){
			throw new IllegalArgumentException("rate should <=1");
		}
		product=new BufferedImage((int)(origin.getWidth()*rate),(int)(origin.getHeight()*rate)
				,BufferedImage.TYPE_INT_ARGB);
		int step=(int)(1.0/rate);
//		System.out.println("step:"+step+" rate"+rate);
		for(int i=0;i<product.getHeight();i++){
			for(int j=0;j<product.getWidth();j++){
				if (j*step<origin.getWidth()&&i*step<origin.getHeight())
					product.setRGB(j,i,origin.getRGB(j*step,i*step));
			}
		}
		return this;
	}

}
