package com.ghostj.master.gui;

import com.alibaba.fastjson.JSONObject;
import com.ghostj.master.MasterMain;
import com.ghostj.master.util.FileRW;
import com.ghostj.master.util.Out;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class BatPanel extends JPanel {
    static final Color batBgc=new Color(140,140,140),batSel=new Color(60,60,60),nameCl=new Color(130, 188, 252);
    static final Font batFont=new Font("Consolas",Font.PLAIN,16),batName=new Font("Consolas",Font.BOLD,17);
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
            g.setFont(batFont);
            g.drawString(cmds,4,29);
            g.setColor(nameCl);
            g.setFont(batName);
            g.drawString(name,10,14);
        }
    }

    public JLabel tag=new JLabel("Saved Commands");
    public Button run=new Button("Run");
    public Button add=new Button("Add");
    public Button del=new Button("Del");
    public Button edi=new Button("Edit");

    ArrayList<bat> bats=new ArrayList<>();
    JPanel panelForBats=new JPanel();

    static final Font btnF=new Font("Consolas",Font.BOLD,19);
    public BatPanel(){
        this.setLayout(null);

//        this.setBackground(MasterMain.initGUI.clientTable.getBackground());

        tag.setFont(batFont);
        tag.setSize(130,20);
        tag.setLocation(5,0);
        tag.setForeground(Color.white);
        this.add(tag);

        run.setBounds(0,20,40,30);
        add.setBounds(42,20,40,30);
        del.setBounds(84,20,40,30);
        edi.setBounds(126,20,40,30);

        run.setForeground(Color.green);
        run.setText(">>");
        run.setFont(btnF);

        add.setText("+");
        add.setFont(btnF);

        del.setText("—");
        del.setFont(btnF);
        del.setForeground(Color.red);

        this.add(add);
        this.add(run);
        this.add(del);
        this.add(edi);

        add.addActionListener((e)->{
            showEditDialog("","",true);
        });
        del.addActionListener((e)->{
            bats.remove(whichSelected());
            packBats();
            loadBats();
            this.repaint();
        });
        run.addActionListener((e)->{
//            int option=javax.swing.JOptionPane.showConfirmDialog(MasterMain.initGUI.mainwd,whichSelected().cmds,"确认命令"+whichSelected().name+"的内容",JOptionPane.INFORMATION_MESSAGE);
            int option=0;
            //System.out.println(option);
            if(option==0){
                try {
                    String cmdsp[]=whichSelected().cmds.split("\n\r");
                    for(String cmd:cmdsp){
                        System.out.println(cmd);
                        MasterMain.bufferedWriter.write(cmd);
                        MasterMain.bufferedWriter.newLine();
                        MasterMain.bufferedWriter.flush();
                    }
                }catch (Exception er){
                    er.printStackTrace();
                    Out.say("BatPanel","无法将批处理指令发送至服务端");
                }
            }
        });
        edi.addActionListener((e)->{
            showEditDialog(whichSelected().name,whichSelected().cmds,false);
        });


        panelForBats.setLayout(null);
        panelForBats.setLocation(0,add.getY()+add.getHeight()+10);
        panelForBats.setBackground(this.getBackground());
        this.add(panelForBats);

        loadBats();
    }

    public void showEditDialog(String names,String cmdss,boolean isNew){

        JDialog jDialog=new JDialog(MasterMain.initGUI.mainwd);

        jDialog.setLayout(null);
//            jDialog.setBackground(MasterMain.initGUI.bgp.getBackground());
        jDialog.setTitle("批处理");
        jDialog.setSize(400,500);
        jDialog.setLocation(MasterMain.initGUI.mainwd.getLocation());

        InputField name=new InputField("名称",340,35,50);
        name.setLocation(10,5);
        name.setValue(names);
        jDialog.add(name);

        InputAreaField cmds=new InputAreaField("命令",340,100,50);
        cmds.setLocation(name.getX(),name.getY()+name.getHeight()+10);
        cmds.setValue(cmdss);
        jDialog.add(cmds);

        Button save=new Button("保存");
        save.setSize(50,30);
        save.setLocation(cmds.getX(),cmds.getY()+cmds.getHeight()+10);
        save.addActionListener((e0)->{
            if(isNew) {
                bat bat = new bat();
                bat.name = name.getValue();
                bat.cmds = cmds.getValue();
                bats.add(bat);
            }else {
                for(bat bat:bats){
                    if(bat.name.equals(names)){
                        bat.name=name.getValue();
                        bat.cmds=cmds.getValue();
                        break;
                    }
                }
            }
            packBats();
            loadBats();
            jDialog.dispose();
        });
        jDialog.add(save);

        jDialog.setSize(400,save.getY()+save.getHeight()+50);

        jDialog.setVisible(true);
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
        if(!new File("batList.json").exists()){
            return;
        }
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
            bat.setLocation(0,((bat.getHeight()+5)*yIndex++));
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
