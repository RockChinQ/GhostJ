package com.ghostj.master.gui;

import javax.swing.*;
import java.awt.*;

public class Button extends JButton {
	BasicStroke borderStroke=new BasicStroke(3);
	Color bgColor=new Color(140,140,140),selectedBgColor=new Color(1, 99, 135);
	private Color fontColor=new Color(240,240,240);
	private Font font0=new Font("Serif",Font.PLAIN,16);
	private int x=0,y=0;
	private FontMetrics fontMetrics=null;

	public Button(String text){
		super(text);
		init();
	}
	public Button(ImageIcon imageIcon){
		super(imageIcon);
		init();
	}
	public Button(String text, ImageIcon imageIcon){
		super(text,imageIcon);
		init();
	}

	public void init(){
		this.setFont(font0);
		this.setForeground(fontColor);
	}

	public void paint(Graphics g){
		//super.paint(g);

		this.fontMetrics=g.getFontMetrics(font0);
		x=this.getWidth()/2-fontMetrics.stringWidth(this.getText())/2;
		y=this.getHeight()/2-fontMetrics.getHeight()/2+17;
		//((Graphics2D)g).setStroke(borderStroke);
		g.setColor(this.isSelected()?selectedBgColor:bgColor);
		//g.setColor(this.hasFocus()?selectedBgColor:bgColor);
//		g.fillRoundRect(0,0,this.getWidth(),this.getHeight(),10,10);
		g.fillRect(0,0,this.getWidth(),this.getHeight());
		g.setColor(this.getForeground());
		g.setFont(this.getFont());

		g.drawString(this.getText(),x,y);

		if(this.hasFocus()){
			g.drawRect(x-2,y-17,fontMetrics.stringWidth(this.getText())+4,fontMetrics.getHeight()-2);
		}
	}


	@Override
	public void setFont(Font font){
		super.setFont(font);
	}
	@Override
	public void setText(String text){
		super.setText(text);
	}
}
