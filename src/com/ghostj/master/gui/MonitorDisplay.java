package com.ghostj.master.gui;

import com.ghostj.master.MasterMain;
import com.ghostj.util.Out;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Objects;

/**
 * 用于大窗口显示截图
 *
 */
public class MonitorDisplay extends JFrame {
    ScreenDisplay.displayPanel displayPanel=new ScreenDisplay.displayPanel();
    static class ToolBar extends JPanel{
        Button getNew=new Button("Get!");
        JComboBox<String> clientList=new JComboBox<>();
        InputField scrCmd=new InputField("指令",170,30,30);
        public JLabel tips=new JLabel("无截图");
        boolean loadingClient=false;

        Button dragMode=new Button("MIX");
        Button ctrlSwitch=new Button("键鼠控制");
        public ToolBar(){
            this.setLayout(null);

            clientList.setBounds(10,10,150,30);
            clientList.addActionListener(e->{
//                System.out.println("select");
                if (!loadingClient) {
                    int id = Integer.parseInt(((String) Objects.requireNonNull(clientList
                            .getSelectedItem())).split("-")[0]);

                    try {
                        Out.say("ClientTable", "focus");
                        MasterMain.bufferedWriter.write("!focus &" + id);
                        MasterMain.bufferedWriter.newLine();
                        MasterMain.bufferedWriter.flush();
                    } catch (Exception err) {
                        err.printStackTrace();
                    }
                }
            });
            this.add(clientList);

            getNew.setBounds(clientList.getX()+clientList.getWidth()+5,clientList.getY(),60,30);
            getNew.addActionListener(e->{
                sendTime=new Date().getTime();
                MasterMain.writeToServer(scrCmd.getValue());
                MasterMain.config.set("scrCmd",scrCmd.getValue());
                MasterMain.config.write();
            });
            this.add(getNew);

            scrCmd.setLocation(getNew.getX()+getNew.getWidth()+10,getNew.getY());
            scrCmd.input.setBackground(Color.gray);
            scrCmd.text.setForeground(Color.darkGray);
//            scrCmd.setBackground(Color.darkGray);
            String scmd="!!scr lc.png 0.8 0.08";
            try{
                scmd=MasterMain.config.getStringValue("scrCmd");
            }catch (Exception ignored){}
            if (scmd!=null){
                scrCmd.setValue(scmd);
            }
            this.add(scrCmd);

            tips.setBounds(scrCmd.getX()+scrCmd.getWidth()+10,getNew.getY(),420,30);
            this.add(tips);


            //tools
            dragMode.setBounds(10,clientList.getY()+clientList.getHeight()+5,50,30);
            dragMode.addActionListener(e->{
                switch (dragMode.getText()){
                    case "ZOOM":{
                        dragMode.setText("MOVE");
                        MasterMain.initGUI.md.displayPanel.dragMode=ScreenDisplay.displayPanel.DRAG_MOVE;
                        break;
                    }
                    case "MOVE":{
                        dragMode.setText("MIX");
                        MasterMain.initGUI.md.displayPanel.dragMode=ScreenDisplay.displayPanel.DRAG_MIX;
                        break;
                    }
                    case "MIX":{
                        dragMode.setText("ZOOM");
                        MasterMain.initGUI.md.displayPanel.dragMode=ScreenDisplay.displayPanel.DRAG_ZOOM;
                        break;
                    }
                }
            });
            this.add(dragMode);

            ctrlSwitch.setBounds(dragMode.getX()+dragMode.getWidth()+5,dragMode.getY(),90,30);
            ctrlSwitch.addActionListener(e->{
                ctrlSwitch.setSelected(!ctrlSwitch.isSelected());
                MasterMain.initGUI.md.displayPanel.setSupportControl(ctrlSwitch.isSelected());
            });
            this.add(ctrlSwitch);
        }
        public void setAllToolUnselected(){
            dragMode.setSelected(false);
        }
        //从ClientTable对象拉取客户端列表
        public void updateClientList(){
            loadingClient=true;
            clientList.removeAllItems();
            for(ClientTable.clientInfo clientInfo:MasterMain.initGUI.clientTable.clients){
                clientList.addItem(clientInfo.id+"-"+clientInfo.name);
                if (clientInfo.status){
                    clientList.setSelectedItem(clientInfo.id+"-"+clientInfo.name);
                }
            }
            loadingClient=false;
        }
    }
    public ToolBar toolBar=new ToolBar();
    public void updateClient(){
        toolBar.updateClientList();
    }
    //发起截图指令的时间戳
    static long sendTime=0;
    public MonitorDisplay(){
        this.setBackground(Color.gray);
        this.setLayout(null);
        this.setBounds(50,50,1100,800);
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                displayPanel.setBounds(10,90,getWidth()-70,getHeight()-140);
            }
        });
        displayPanel.setBackground(Color.black);
        displayPanel.setBounds(10,90,getWidth()-70,getHeight()-140);
        this.add(displayPanel);

        toolBar.setBounds(0,0,1000,70);
        this.add(toolBar);
    }
    public void dlPic(String url){
        if (isVisible()){

            try {
                toolBar.tips.setText("正在获取...");
                displayPanel.url=url;
                displayPanel.setImage(ImageIO.read(new URL(url)));
                toolBar.tips.setText("spent:"+(new Date().getTime()-sendTime)
                        +" url:~"+url.substring(url.length()-22)
                        +"_"+displayPanel.convert.getOriginImage().getWidth()
                        +"*"+displayPanel.convert.getOriginImage().getHeight()
                        +">"+displayPanel.image.getWidth()+"*"
                        +displayPanel.image.getHeight()+" "
                        +String.format("%.2f", displayPanel.rate));
                displayPanel.repaint();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
