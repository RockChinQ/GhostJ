package com.ghostj.master.gui;

import com.ghostj.master.MasterMain;
import com.ghostj.master.conn.HandleConn;
import com.ghostj.util.image.ImageConvert;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;

public class ScreenDisplay extends JPanel {
    public static class displayPanel extends JPanel{
        BufferedImage image,orgImg;
        ImageConvert convert;
        double rate=0;
        double orgRate=0;
        String url="";

        private boolean supportZoom=true;   //秦骏言是我仔
        int dx=0,dy=0;
        Point lsPoint=new Point();
        private static final float zoomLs[]=new float[]{1,1.2f,1.5f,2f,2.5f,3f,6f};
        int zoomIndex=zoomLs.length*100;
        private boolean processing=false;


        private boolean supportControl=false;

        public boolean isSupportControl() {
            return supportControl;
        }

        public void setSupportControl(boolean supportControl) {
            this.supportControl = supportControl;
            supportZoom= !supportControl;
        }

        public boolean isSupportZoom() {
            return supportZoom;
        }

        public void setSupportZoom(boolean supportZoom) {
            this.supportZoom = supportZoom;
        }

        public displayPanel(){
            this.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {

                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });
            this.addMouseMotionListener(new MouseMotionListener() {
                @Override
                public void mouseDragged(MouseEvent e) {

                }

                @Override
                public void mouseMoved(MouseEvent e) {

                }
            });
            this.addMouseWheelListener(new MouseWheelListener() {
                @Override
                public void mouseWheelMoved(MouseWheelEvent e) {
                    if (!processing) {
//                        System.out.print(e.getWheelRotation() + " ");
//                        float oldRate= (float) rate;
//                        rate = Math.max(0f, rate - rate / 10 * (float) e.getWheelRotation());
////                        dx-=e.getX()*rate-e.getX()*oldRate;
////                        dy-=e.getY()*rate-e.getY()*oldRate;
//                        int x= (int) (e.getX()*rate),y= (int) (e.getY()*rate);
//                        System.out.println(rate);
//                        zoomImage();
//                        repaint();
                        resizeZoom(e.getX(),e.getY(), Math.max(0f, rate - rate/4 * e.getWheelRotation()));
//                        resizeZoom(e.getX(),e.getY(),zoomLs[(zoomIndex-=e.getWheelRotation())%zoomLs.length]);
                    }
                }
            });
        }
        public void paint(Graphics g){
            long st=new Date().getTime();
            g.setColor(getBackground());
            g.fillRect(0,0,this.getWidth(),this.getHeight());
            if (image!=null) {
//                g.drawImage(image,Math.max(this.getWidth()/2-image.getWidth()/2+dx,0),Math.max(this.getHeight()/2-image.getHeight()/2+dy,0),this);
//                g.drawImage(image,this.getWidth()/2-image.getWidth()/2+dx,this.getHeight()/2-image.getHeight()/2+dy,this);
                g.drawImage(image, -dx, -dy, this);
                g.drawString("rate(wOrgRate):"+String.format("%.2f", rate)+"("+String.format("%.2f",rate/orgRate)+")",10,20);
            }
            processing=false;
//            System.out.println("paint spent:"+((new Date().getTime())-st));
//            System.out.println("paint");
        }
        public void resizeZoom(int mx,int my,double rate){
            processing=true;
            if (rate<orgRate)
                rate=  orgRate;
            double ox= ((mx+dx)/this.rate),oy= ((my+dy)/this.rate);
            double nx= (ox*rate),ny= (oy*rate);
            dx= (int) Math.max(0,nx-mx);
            dy= (int) Math.max(0,ny-my);
            if (rate==orgRate){
                dx=0;dy=0;
            }
//            System.out.println("orgR:"+this.rate+" tgR:"+rate+" mx,my:"+mx+","+my+" ");
            this.rate=rate;
            zoomImage();
            repaint();
        }
        public void zoomImage(){
            long st=new Date().getTime();
            convert=new ImageConvert(orgImg);
            convert.changeResolutionRate(rate);
            this.image=convert.getProduct();
//            System.out.println("zoom spent:"+((new Date().getTime())-st));
        }
        public void setImage(BufferedImage image){
            this.image=image;
            this.orgImg=image;
            rate=Math.min((double)this.getWidth()/(double)image.getWidth(),(double)this.getHeight()/(double)image.getHeight());
            this.orgRate=rate;
            dx=dy=0;
            zoomImage();
            repaint();
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
    InputField slp=new InputField("slp",55,25,25);
    //save
    Button save=new Button("save");
    Button auto=new Button("auto√");
    Button max=new Button("□");
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
            auto.setSelected(true);
            auto.setText("auto"+(auto.isSelected()?"√":"x"));
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

        //save
        save.setBounds(slp.getX()+slp.getWidth()+4,slp.getY(),30,switchCs.getHeight());
        save.addActionListener(e->{
//            save.setText("save");
            File tar=new File("scr.png");
            try {
                ImageIO.write(picp.convert.getOriginImage(),"png",tar);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            tips.setText("已保存到"+tar.getAbsolutePath());
        });
        this.add(save);

        auto.setBounds(save.getX()+save.getWidth()+3,save.getY(),50,save.getHeight());
        auto.addActionListener(e->{
            auto.setSelected(!auto.isSelected());
            auto.setText("auto"+(auto.isSelected()?"√":"x"));
        });
        auto.setSelected(true);
        this.add(auto);

        max.setBounds(auto.getX()+auto.getWidth()+3,auto.getY(),30,auto.getHeight());
        max.addActionListener(e->{
            MasterMain.initGUI.md.setVisible(true);
            auto.setSelected(false);
            auto.setText("auto"+(auto.isSelected()?"√":"x"));
        });
        this.add(max);

        tips.setBounds(10,30,400,30);
        tips.setForeground(Color.white);
        this.add(tips);
        picp.setLocation(0,40);
        picp.setBackground(Color.darkGray);
        picp.setLayout(null);
        picp.setSize(480,338);
        this.add(picp);

        this.setSize(picp.getWidth(),picp.getY()+picp.getHeight()+20);
        tips.setLocation(5,this.getHeight()-26);
    }
    public void dlPic(String url){
        if (!auto.isSelected()){
            tips.setText("自动获取已被禁用");
            return;
        }
        try {
            tips.setText("正在获取...");
            picp.url=url;
            picp.setImage(ImageIO.read(new URL(url)));
            tips.setText("spent:"+(new Date().getTime()-sendTime)+" url:~"+url.substring(url.length()-22)+"_"+picp.convert.getOriginImage().getWidth()+"*"+picp.convert.getOriginImage().getHeight()+">"+picp.image.getWidth()+"*"+picp.image.getHeight()+" "+picp.rate);
//            picp.repaint();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
