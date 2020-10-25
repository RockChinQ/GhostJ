package com.ghostj.master.gui;

import com.ghostj.util.image.ImageConvert;
import com.sun.corba.se.impl.orbutil.graph.Graph;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

public class ScreenDisplay extends JPanel {
    class displayPanel extends JPanel{
        BufferedImage image;
        double rate=0;
        String url="";
        public void paint(Graphics g){
            g.setColor(getBackground());
            g.fillRect(0,0,this.getWidth(),this.getHeight());
            g.drawImage(image,0,0,this);
        }
        public void setImage(BufferedImage image){
            ImageConvert convert=new ImageConvert(image);
            rate=(double)this.getWidth()/(double)image.getWidth();
            convert.changeResolutionRate(rate);
            this.image=convert.getProduct();
        }
    }
    public displayPanel picp=new displayPanel();
    public JLabel tips=new JLabel("无截图");
    public ScreenDisplay(){
        this.setLayout(null);
        tips.setBounds(10,30,100,30);
        tips.setForeground(Color.white);
        this.add(tips);
        picp.setLocation(0,60);
        picp.setBackground(Color.gray);
        picp.setLayout(null);
        picp.setSize(450,350);
        this.add(picp);


        this.setSize(picp.getWidth(),picp.getY()+picp.getHeight());
    }
    public void dlPic(String url){
        try {
            /*downLoadFromUrl(url,"scr.tmp.png","masterTemp","screen"+(new Date().getTime()));
            Image image=Toolkit.getDefaultToolkit().createImage("masterTemp\\scr.tmp.png");
            BufferedImage bi=new BufferedImage(image.getWidth(null),image.getHeight(null),BufferedImage.TYPE_INT_ARGB);*/
            tips.setText("正在获取...");
            picp.url=url;
            picp.setImage(ImageIO.read(new URL(url)));
            tips.setText(picp.image.getWidth()+"*"+picp.image.getHeight()+" "+picp.rate);
            picp.repaint();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
