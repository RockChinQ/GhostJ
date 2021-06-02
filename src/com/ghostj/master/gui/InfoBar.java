package com.ghostj.master.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;
import java.util.LinkedHashMap;

public class InfoBar extends JPanel {
    private static final Color linkCl=new Color(59, 135, 224, 255);
    public static class OptionList extends LinkedHashMap<String,Runnable>{
        public OptionList add(String key,Runnable runnable){
            super.put(key,runnable);
            return this;
        }
    }
    public class EntryLabel extends JLabel{
        public EntryLabel(String text,Runnable runnable){
            this.setText("|"+text);
            this.setForeground(linkCl);
            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    runnable.run();
                }
                @Override
                public void mouseEntered(MouseEvent e){
                    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                }
                @Override
                public void mouseExited(MouseEvent e){
                    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }
            });
        }
    }
    private JLabel msgLabel=new JLabel("NoMessageToDisplayNow");
    public InfoBar(int width,int height){
        this.setSize(width,height);
        this.setLayout(null);

        OptionList ol=new OptionList()
                .add("Welcome",()->javax.swing.JOptionPane.showMessageDialog(null,"hello!"))
                .add("Exit",()->System.exit(0));
        this.show("NoMessageToDisplayNow",ol);
        this.setVisible(true);
    }
    public void setTextForeground(Color color){
        this.msgLabel.setForeground(color);
    }
    public void show(String msg){
        show(msg,null);
    }
    //接受一个传入的选项列表，每个选项由提示字和Runnable接口组成
    public void show(String msg,LinkedHashMap<String,Runnable> optionMap){
        this.removeAll();
        msgLabel.setBounds(3,0,this.getWidth(),this.getHeight()/2);
        this.add(msgLabel);
        Date d=new Date();
        this.msgLabel.setText(d.getHours()+":"+d.getMinutes()+":"+d.getSeconds()+" "+msg);
        if (optionMap==null){
            this.msgLabel.setLocation(3,this.getHeight()/2-msgLabel.getHeight()/2);
        }else {
            this.msgLabel.setLocation(3,0);
            int i=0;
            int step=(this.getWidth()-6)/optionMap.size();
            for (String key:optionMap.keySet()){
                EntryLabel entryLabel=new EntryLabel(key,optionMap.get(key));
                entryLabel.setBounds(step*i,this.getHeight()/2,step,this.getHeight()/2);
                this.add(entryLabel);
                i++;
            }
        }
        repaint();
    }
}
