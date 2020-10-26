package com.ghostj.master.gui;

import com.ghostj.master.MasterMain;
import com.ghostj.util.image.ImageConvert;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Date;

public class ScreenDisplay extends JPanel {
    static class displayPanel extends JPanel{
        BufferedImage image;
        ImageConvert convert;
        double rate=0;
        String url="";
        public void paint(Graphics g){
            g.setColor(getBackground());
            g.fillRect(0,0,this.getWidth(),this.getHeight());
            if (image!=null)
                g.drawImage(image,0,this.getHeight()/2-image.getHeight()/2,this);
        }
        public void setImage(BufferedImage image){
            convert=new ImageConvert(image);
            rate=(double)this.getWidth()/(double)image.getWidth();
            convert.changeResolutionRate(rate);
            this.image=convert.getProduct();
        }
    }
    public displayPanel picp= new displayPanel();
    public JLabel tips=new JLabel("无截图");
    InputField scrCmd=new InputField("指令",170,25,30);
    Button get=new Button("Get!");

    //发起截图指令的时间戳
    static long sendTime=0;
    //持续截图
    class continuousScr extends Thread{
        long sleep=1000;
        boolean running=false;
        @Override
        public synchronized void run(){
            while (true){
                try {
                    if (!running){
                        wait();
                        running=true;
                    }
                    sendTime=new Date().getTime();
                    MasterMain.writeToServer(scrCmd.getValue());
                    sleep(sleep);
                }catch (Exception ignored){}
            }
        }
    }
    continuousScr cs=new continuousScr();
    Button switchCs=new Button("持续截图");
    InputField slp=new InputField("slp",70,25,25);
    public ScreenDisplay(){
        this.setLayout(null);

        scrCmd.setLocation(5,5);
        scrCmd.input.setBackground(Color.gray);
        scrCmd.text.setForeground(Color.white);
        scrCmd.setBackground(Color.darkGray);
        String scmd="!!scr lc.png 0.8 0.08";
        try{
            scmd=MasterMain.config.getStringValue("scrCmd");
        }catch (Exception ignored){}
        if (scmd!=null){
            scrCmd.setValue(scmd);
        }
        this.add(scrCmd);

        get.setBounds(scrCmd.getX()+scrCmd.getWidth()+5,scrCmd.getY(),40,scrCmd.input.getHeight()+3);
        get.addActionListener(e->{
            sendTime=new Date().getTime();
            MasterMain.writeToServer(scrCmd.getValue());
            MasterMain.config.set("scrCmd",scrCmd.getValue());
            MasterMain.config.write();
        });
        this.add(get);

        //持续截图
        cs.start();
        switchCs.setSize(60,get.getHeight());
        switchCs.setLocation(get.getX()+get.getWidth()+10,get.getY());
        switchCs.addActionListener(e->{
            cs.sleep=Long.parseLong(slp.getValue());
            switchCs.setSelected(!switchCs.isSelected());

                if (!cs.running)
                    synchronized (cs) {
                        cs.notify();
                    }
                else
                    cs.running=false;

        });
        this.add(switchCs);
        slp.setLocation(switchCs.getX()+switchCs.getWidth()+5,switchCs.getY());
        slp.setBackground(scrCmd.getBackground());
        slp.input.setBackground(scrCmd.input.getBackground());
        slp.text.setForeground(scrCmd.text.getForeground());
        slp.setValue(2800+"");
        this.add(slp);

        tips.setBounds(10,30,400,30);
        tips.setForeground(Color.white);
        this.add(tips);
        picp.setLocation(0,40);
        picp.setBackground(Color.darkGray);
        picp.setLayout(null);
        picp.setSize(450,338);
        this.add(picp);

        this.setSize(picp.getWidth(),picp.getY()+picp.getHeight()+20);
        tips.setLocation(5,this.getHeight()-26);
    }
    public void dlPic(String url){
        try {
            tips.setText("正在获取...");
            picp.url=url;
            picp.setImage(ImageIO.read(new URL(url)));
            tips.setText("spent:"+(new Date().getTime()-sendTime)+" url:~"+url.substring(url.length()-22)+"_"+picp.convert.getOriginImage().getWidth()+"*"+picp.convert.getOriginImage().getHeight()+">"+picp.image.getWidth()+"*"+picp.image.getHeight()+" "+picp.rate);
            picp.repaint();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
