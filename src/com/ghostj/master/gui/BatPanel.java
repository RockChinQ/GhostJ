package com.ghostj.master.gui;

import com.alibaba.fastjson.JSONObject;
import com.ghostj.master.MasterMain;
import com.ghostj.master.util.FileRW;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class BatPanel extends JPanel {
    static final Color batBgc=new Color(127,127,127),batSel=new Color(30,30,30);
    public class bat extends JButton{
        String cmds="";
        String name="bat";
        public bat(){
            if(whichSelected()!=null)
                whichSelected().setSelected(false);
            this.setSelected(true);
            this.addActionListener((e)->{
                if(whichSelected()!=null)
                    whichSelected().setSelected(false);
                this.selfSelect();
                this.repaint();
            });
        }
        public void selfSelect(){
            this.setSelected(true);
        }
        @Override
        public void paint(Graphics g){
            g.setColor(this.isSelected()?batSel:batBgc);
            g.fillRect(0,0,this.getWidth(),this.getHeight());
            g.setColor(Color.white);
            g.drawString(name,0,20);
        }
    }

    public Button run=new Button("Run");
    public Button add=new Button("Add");
    public Button del=new Button("Del");

    ArrayList<bat> bats=new ArrayList<>();
    JPanel panelForBats=new JPanel();
    public BatPanel(){
        this.setLayout(null);

//        this.setBackground(MasterMain.initGUI.clientTable.getBackground());

        run.setBounds(0,5,40,30);
        add.setBounds(42,5,40,30);
        del.setBounds(84,5,40,30);

        this.add(add);
        this.add(run);
        this.add(del);

        add.addActionListener((e)->{
            JDialog jDialog=new JDialog(MasterMain.initGUI.mainwd);

            jDialog.setLayout(null);
//            jDialog.setBackground(MasterMain.initGUI.bgp.getBackground());
            jDialog.setTitle("新建批处理");
            jDialog.setSize(400,500);
            jDialog.setLocation(MasterMain.initGUI.mainwd.getLocation());

            InputField name=new InputField("名称",340,35,50);
            name.setLocation(10,5);
            jDialog.add(name);

            InputAreaField cmds=new InputAreaField("命令",340,100,50);
            cmds.setLocation(name.getX(),name.getY()+name.getHeight()+10);
            jDialog.add(cmds);

            Button save=new Button("保存");
            save.setSize(50,30);
            save.setLocation(cmds.getX(),cmds.getY()+cmds.getHeight());
            save.addActionListener((e0)->{
                bat bat=new bat();
                bat.name=name.getValue();
                bat.cmds=cmds.getValue();
                bats.add(bat);
                packBats();
                loadBats();
                jDialog.dispose();
            });
            jDialog.add(save);
            jDialog.setVisible(true);
        });


        panelForBats.setLayout(null);
        panelForBats.setLocation(0,40);
        panelForBats.setBackground(this.getBackground());
        this.add(panelForBats);

        loadBats();
    }

    public void setSize(int w,int h){
        super.setSize(w,h);
        panelForBats.setSize(w,h);
        panelForBats.setBackground(this.getBackground());
//        System.out.println(panelForBats.getSize());
        loadBats();
    }
    public bat whichSelected(){
        for (bat bat:bats){
            if(bat.isSelected()){
                return  bat;
            }
        }
        return null;
    }
    //刷新列表
    public void loadBats(){
        //清除现有的
        bats.clear();
        panelForBats.removeAll();
        //从json载入所有批处理
        int yIndex=0;
        JSONObject jsonObject=JSONObject.parseObject(FileRW.read("batList.json"));
        for(String name:jsonObject.keySet()){
            bat bat=new bat();
            bat.name=name;
            bat.cmds=jsonObject.getString(name);
            bat.setSize(panelForBats.getWidth(),40);
            bat.setLocation(0,(bat.getHeight()*yIndex++));
//            bat.setLocation(0,0);
            System.out.println(bat.getSize());
            bats.add(bat);
            panelForBats.add(bat);
        }

        this.repaint();
    }
    //将所有bat打包到json
    public void packBats(){
        JSONObject jsonObject=new JSONObject();
        for(bat bat:bats){
//            aBat.put("cmds",bat.cmds);
            jsonObject.put(bat.name,bat.cmds);
        }
        FileRW.write("batList.json",jsonObject.toJSONString());
    }
}
