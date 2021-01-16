package com.ghostj.master.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.net.URL;
import java.util.Date;

/**
 * 用于大窗口显示截图
 *
 */
public class MonitorDisplay extends JFrame {
    ScreenDisplay.displayPanel displayPanel=new ScreenDisplay.displayPanel();
    public MonitorDisplay(){
        this.setBackground(Color.gray);
        this.setLayout(null);
        this.setBounds(50,50,900,800);
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                displayPanel.setBounds(10,70,getWidth()-50,getHeight()-120);
            }
        });
        displayPanel.setBackground(Color.black);
        displayPanel.setBounds(10,70,getWidth()-50,getHeight()-120);
        this.add(displayPanel);
    }
    class ToolBar extends JPanel{

    }
    public void dlPic(String url){
        if (isVisible()){

            try {
//                tips.setText("正在获取...");
                displayPanel.url=url;
                displayPanel.setImage(ImageIO.read(new URL(url)));
//                tips.setText("spent:"+(new Date().getTime()-sendTime)+" url:~"+url.substring(url.length()-22)+"_"+picp.convert.getOriginImage().getWidth()+"*"+picp.convert.getOriginImage().getHeight()+">"+picp.image.getWidth()+"*"+picp.image.getHeight()+" "+picp.rate);
                displayPanel.repaint();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
