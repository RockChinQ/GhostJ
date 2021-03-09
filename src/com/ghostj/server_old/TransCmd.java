package com.ghostj.server_old;

import com.ghostj.util.FileRW;
import com.ghostj.util.Out;
import com.ghostj.util.TimeUtil;
import com.rft.core.server.BufferedFileReceiver;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TransCmd extends  Thread{
    Map<String,String> renPlan=new HashMap<>();
    public void run(){
        BufferedReader typeReader = new BufferedReader(new InputStreamReader(System.in));//键盘的reader
        while (true) {
            try {
                String typeCmd = typeReader.readLine();
                //检查是否是工作信息
                String cmd[] = typeCmd.split(" ");
                handleCommand(typeCmd);
            } catch (Exception e) {
                e.printStackTrace();
                Out.say("TransCmd", "处理指令失败");
                ServerMain.cmdProcessFinish();
            }
        }
    }
    public void handleCommand(String typeCmd) throws IOException {
        try {
            Out.say(typeCmd);
            String[] cmd =typeCmd.split(" ");
            switch (cmd[0]) {
                case "!help": {
                    if(new File(("serverHelp.txt")).exists())
                        Out.say("TransCmd-help", FileRW.read("serverHelp.txt").replaceAll("ln", "\n\r").replaceAll("tab","\t"));
                    else {
                        Out.say("TransCme-help", "command      description\n!list   列表所有连接的主机\n!focus <connName(wordStartwith)>   聚焦\n!dfocus    退出聚焦\n!chname <connName(wordStartWith)> <newName>     修改客户端名称\n!stop    关闭服务端\n!close    关闭服务端" +
                                "\n!pw <newPassword> 修改master连接的密码" +
                                "\n!test [timeout] 测试每个客户端连接，period为超时时间");
                    }
                    ServerMain.cmdProcessFinish();
                    return;
                }
                case "!list": {
                    Out.say("TransCmd-info", "已建立的连接(" + ServerMain.socketArrayList.size() + ")：\nindex\tname\tconnTime\tstate\tversion\tinstallTime");
                    for (HandleConn conn : ServerMain.socketArrayList) {
                        Date d = new Date(conn.connTime);
                        Out.say(conn.rtIndex + "\t" +String.format("%-14s", conn.hostName) + "\t" + (d.getDate() + "," + d.getHours() + ":" + d.getMinutes() + ":" + d.getSeconds()) + "\t"
                                + (conn.equals(ServerMain.focusedConn) ? "聚焦" : "后台")+"\t"+conn.version+"\t"+conn.installTime);
                    }
                    Out.say("TransCmd-info", "列表完成.");
                    ServerMain.cmdProcessFinish();
                    return;
                }
                case "!echo":{
                    if (cmd.length>1){
                        Out.sayThisLine("[TransCmd-echo]");
                        for(int i=1;i<cmd.length;i++){
                            Out.sayThisLine(cmd[i]+" ");
                        }
                    }
                    Out.sayThisLine("\n");
                    return;
                }
                case "!pw": {
                    ServerMain.masterPw = cmd[1];
                    ServerMain.config.set("master.pw", cmd[1]);
                    ServerMain.config.write();
                    Out.say("TransCmd-pw", "master新密码:" + ServerMain.masterPw);
                    ServerMain.cmdProcessFinish();
                    return;
                }
                case "!dfocus": {
                    ServerMain.focusedConn = null;
                    Out.say("TransCmd-dfocus", "取消聚焦");
                    ServerMain.sendListToMaster();
                    ServerMain.cmdProcessFinish();
                    return;
                }
                case "!focus": {
                    if (cmd.length < 2) {
                        Out.say("TransCmd-focus", "正确语法\n!focus <connName(wordStartWith)>\n!focus %<index>");
                        ServerMain.cmdProcessFinish();
                        return;
                    }
                    if (cmd[1].startsWith("&")) {
                        try {
                            long i = Integer.parseInt(cmd[1].substring(1));
                            if(ServerMain.focusedConn!=null&&ServerMain.focusedConn.rtIndex==i)//已经聚焦到目标
                                return;
                            //Out.say("index:" + i);
                            for (HandleConn conn : ServerMain.socketArrayList) {
                                if (conn.rtIndex == i) {
                                    ServerMain.focusedConn = conn;
                                    Out.say("TransCmd-focus", "聚焦" + ServerMain.focusedConn.hostName);
                                    ServerMain.focusedConn.showHistory();
                                    Out.say("TransCmd-focus","===========client:"+conn.hostName+"的历史消息==========");
                                    ServerMain.sendListToMaster();
                                    ServerMain.cmdProcessFinish();
                                    return;
                                }
                            }
                        } catch (Exception e) {
                            Out.say("TransCmd-focus", "正确语法\n!focus &<index>");
                        }
                    } else {
                        if(ServerMain.focusedConn!=null&&ServerMain.focusedConn.hostName.startsWith(cmd[1])){//已经聚焦到目标
                            return;
                        }
                        for (HandleConn conn : ServerMain.socketArrayList) {
                            if (conn.hostName.startsWith(cmd[1])) {
                                ServerMain.focusedConn = conn;
                                Out.say("TransCmd-focus", "聚焦" + ServerMain.focusedConn.hostName);
                                ServerMain.focusedConn.showHistory();
                                Out.say("TransCmd-focus","===========client:"+conn.hostName+"的历史消息==========");
                                ServerMain.sendListToMaster();
                                ServerMain.cmdProcessFinish();
                                return;
                            }
                        }
                    }
                    Out.say("TransCmd-focus", "无法找到连接");
                    ServerMain.cmdProcessFinish();
                    return;
                }
                case "!test": {
                    int time = 500;
                    try {
                        time = Integer.parseInt(cmd[1]);
                    } catch (Exception e) {
                        Out.say("可设置间隔时间");
                    }
                    new CheckAliveMaster(time).start();
                    return;
                }
                case "!chname": {
                    if (cmd.length >= 3) {
                        for (HandleConn conn : ServerMain.socketArrayList) {
                            if (conn.hostName.startsWith(cmd[1])) {
                                conn.hostName = cmd[2];
                                conn.bufferedWriter.write("!!name " + cmd[2]);
                                conn.bufferedWriter.newLine();
                                conn.bufferedWriter.flush();
                                conn.bufferedWriter.write("!!cfg write");
                                conn.bufferedWriter.newLine();
                                conn.bufferedWriter.flush();
                                ServerMain.tagLog.addTag(cmd[2],"login");
                                Out.say("TransCmd-chname", "已修改名称");
                                ServerMain.sendListToMaster();
                                ServerMain.cmdProcessFinish();
                                return;
                            }
                        }
                        Out.say("TransCmd-chname", "无此名称连接,设置重命名计划:"+cmd[1]+"->"+cmd[2]);
                        renPlan.put(cmd[1],cmd[2]);
                    } else {
                        Out.say("TransCmd-chname", "正确语法\n!chname <connName(wordStartWith)> <newName>");
                    }
                    ServerMain.cmdProcessFinish();
                    return;
                }
                case "!rmtag":{
                    if(cmd.length<2){
                        Out.say("TransCmd-rmtag","正确语法\n!rmtag <tagOwnerName(wordStartWith)>");
                        ServerMain.cmdProcessFinish();
                        return;
                    }
                    ArrayList<String> delete=new ArrayList<>();
                    for(String owner:ServerMain.tagLog.allOwner.keySet()){
                        if(owner.startsWith(cmd[1])){
                            delete.add(owner);
                        }
                    }
                    if(delete.size()!=0){
                        for(String dk:delete){
                            Out.say("TransCmd-rmtag","清除tagLog:"+dk);
                            ServerMain.tagLog.allOwner.remove(dk);
                        }
                        ServerMain.tagLog.pack();
                    }else {
                        Out.say("TransCmd-rmtag","未找到相应的tagOwner");
                    }
                    ServerMain.cmdProcessFinish();
                    return;
                }
                case "!lstag":{
                    Out.say("TransCmd-lstag","列表所有tagLog");
                    int i=0;
                    for(String owner:ServerMain.tagLog.allOwner.keySet()){
                        Out.say(++i+" "+owner);
                    }
                    Out.say("TransCmd-lstag","列表完成");
                    ServerMain.cmdProcessFinish();
                    return;
                }
                case "!lsmst":{
                    Out.say("TransCmd-lsmst","列出所有在线master的信息");
                    int i=0;
                    for(HandleMaster master:AcceptMaster.masters){
                        Out.say(i+++"  ip"+master.socket.getInetAddress()+"  t"+ TimeUtil.millsToMMDDHHmmSS(master.connTime));
                    }
                    Out.say("TransCmd-lsmst","列表完成");
                    ServerMain.cmdProcessFinish();
                    return;
                }
                case "!svfile":{
                    switch (cmd[1]){
                        case "dir":{
                            String reply="!svDir "+ServerMain.fileManager.getFileListString()+"!";
                            ServerMain.sendToSpecificMaster(reply,"serverFileExplorer");
                            break;
                        }
                        case "cd":{
                            if (cmd.length<3){
                                ServerMain.sendToSpecificMaster("ParamsNotEnough","serverFileExplorer");
                                break;
                            }
                            String reply=ServerMain.fileManager.changeDir(cmd[2]);
                            break;
                        }
                    }
                    return;
                }
                case "!rft":{
                    if(cmd.length<2){
                        Out.say("TransCmd-rft","命令语法不正确");
                        ServerMain.cmdProcessFinish();
                        return;
                    }
                    switch (cmd[1]){
                        case "chdir":{//修改文件保存根目录
                            if(cmd.length<3){
                                Out.say("TransCmd-rft chdir","命令语法不正确");
                                ServerMain.cmdProcessFinish();
                                return;
                            }
                            ServerMain.fileReceiver.setRootPath(cmd[2]);
                            Out.say("TransCmd-rft chdir","已修改文件服务器的保存根目录为"+cmd[2]);
                            ServerMain.cmdProcessFinish();
                            return;
                        }
                        case "dir":{
                            Out.say("TransCmd-rft dir","文件服务器的根目录是"+ServerMain.fileReceiver.getRootPath());
                            ServerMain.cmdProcessFinish();
                            return;
                        }
                        case "task":{
                            try {
                                Map<String, BufferedFileReceiver.ReceiveTask> taskMap = ServerMain.fileReceiver.getTaskMap();
                                Out.say("TransCmd-rft task","遍历所有正在进行的文件接收任务\ntoken    fileName    length    received   savePath");
                                for (String token:taskMap.keySet()){
                                    Out.say(token+"   "+taskMap.get(token).getInfo().getName()+"   "+taskMap.get(token).getInfo().getSize()+"    "+taskMap.get(token).getReceivedSize()+"   "+ServerMain.fileReceiver.getRootPath()+taskMap.get(token).getInfo().getSavePath());
                                }
                                Out.say("TransCmd-rft task","列表完成");
                            }catch (Exception e){
                                Out.say("TransCmd-rft task","无法遍历所有task");
                            }
                            ServerMain.cmdProcessFinish();
                            return;
                        }
                        case "stop":{
                            if(cmd.length<3){
                                Out.say("TransCmd-rft stop","命令语法不正确");
                                ServerMain.cmdProcessFinish();
                                return;
                            }

                            Map<String, BufferedFileReceiver.ReceiveTask> taskMap = ServerMain.fileReceiver.getTaskMap();
//                            Out.say("TransCmd-rft task","遍历所有正在进行的文件接收任务\ntoken    fileName    length    received   savePath");
                            for (String token:taskMap.keySet()){
//                                Out.say(token+"   "+taskMap.get(token).getInfo().getName()+"   "+taskMap.get(token).getInfo().getSize()+"    "+taskMap.get(token).getReceivedSize()+"   "+ServerMain.fileReceiver.getRootPath()+taskMap.get(token).getInfo().getSavePath());
                                if(token.startsWith(cmd[2])){
                                    ServerMain.fileReceiver.interruptFile(token);
                                    Out.say("TransCmd-rft stop","已停止文件接收任务:"+token);
                                }
                            }
                            ServerMain.cmdProcessFinish();
                            return;
                        }
                        default:{
                            Out.say("TransCmd-rft default","无此二级命令");
                        }
                    }
                    ServerMain.cmdProcessFinish();
                    return;
                }
                /**
                 * 免安装jre管理指令
                 * view
                 *     扫描jre目录并更新实例中的记录
                 *     列出jre目录中所有的文件
                 *       编号  文件名   jreVer.txt中登记的版本
                 * reg <versionNum> <文件编号(,分隔每个文件)>
                 *     登记列出的文件
                 */
                case "!jre":{
                    if(cmd.length<2){
                        Out.say("TransCmd-jre","命令语法不正确");
                        ServerMain.cmdProcessFinish();
                        return;
                    }
                    switch (cmd[1]){
                        /**
                         * 先将实例中的记录与文件目录同步
                         * 然后读取jreVer.txt分析后输出
                         */
                        case "view":{
                            ServerMain.jreRegister.sync();
                            Out.say("TransCmd-jre view","列表所有jre实体文件\nindex\tversion\tfilePath\tfileName\t    tag");
                            int index=0;
                            for(JRERegister.jreFile jreFile:ServerMain.jreRegister.files){
                                Out.say(index+++"\t"+jreFile.version+"\t"+jreFile.fileName+"\t"+jreFile.filePath+"\t"+jreFile.tag);
                            }
                            ServerMain.cmdProcessFinish();
                            return;
                        }
                        case "reg":{
                            if(cmd.length<4){
                                Out.say("TransCmd-jre reg","命令语法不正确");
                                ServerMain.cmdProcessFinish();
                                return;
                            }
                            String fileIndexs[]=cmd[3].split(",");
                            for(String index:fileIndexs){
                                if(index.equals("all")){
                                    for(JRERegister.jreFile jreFile:ServerMain.jreRegister.getFiles()){
                                        jreFile.version=Long.parseLong(cmd[2]);
                                        Out.say(cmd[2]+"\t"+jreFile.filePath+"\t"+jreFile.fileName+"\t"+jreFile.tag);
                                    }
                                    break;
                                }
                                if(Integer.parseInt(index)>=ServerMain.jreRegister.getFiles().size()||Integer.parseInt(index)<0){
                                    Out.say("无index为"+index+"的文件");
                                    continue;
                                }
                                ServerMain.jreRegister.getFiles().get(Integer.parseInt(index)).version=Long.parseLong(cmd[2]);
                                Out.say(cmd[2]+"\t"+ServerMain.jreRegister.getFiles().get(Integer.parseInt(index)).filePath+"\t"+ServerMain.jreRegister.getFiles().get(Integer.parseInt(index)).fileName);
                            }
                            ServerMain.jreRegister.writeToFile();
                            Out.say("TransCmd-jre reg","已写入文件");
                            ServerMain.cmdProcessFinish();
                            return;
                        }
                        case "tag":{//为字段打标记
                            if(cmd.length<4){
                                Out.say("TransCmd-jre tag","命令语法不正确");
                                ServerMain.cmdProcessFinish();
                                return;
                            }
                            String fileIndexs[]=cmd[3].split(",");
                            for(String index:fileIndexs){
                                if(index.equals("all")){
                                    for(JRERegister.jreFile jreFile:ServerMain.jreRegister.getFiles()){
                                        jreFile.tag=cmd[2];
                                        Out.say(cmd[2]+"\t"+jreFile.filePath+"\t"+jreFile.fileName+"\t"+jreFile.tag);
                                    }
                                    break;
                                }
                                if(Integer.parseInt(index)>=ServerMain.jreRegister.getFiles().size()||Integer.parseInt(index)<0){
                                    Out.say("无index为"+index+"的文件");
                                    continue;
                                }
                                ServerMain.jreRegister.getFiles().get(Integer.parseInt(index)).tag=cmd[2];
                                Out.say(cmd[2]+"\t"+ServerMain.jreRegister.getFiles().get(Integer.parseInt(index)).filePath+"\t"+ServerMain.jreRegister.getFiles().get(Integer.parseInt(index)).fileName);
                            }
                            ServerMain.jreRegister.writeToFile();
                            Out.say("TransCmd-jre tag","已写入文件");
                            ServerMain.cmdProcessFinish();
                            return;
                        }
                        case "rmtag":{
                            if(cmd.length<4){
                                Out.say("TransCmd-jre rmtag","命令语法不正确");
                                ServerMain.cmdProcessFinish();
                                return;
                            }
                            String[] fileIndexs =cmd[3].split(",");
                            for(String index:fileIndexs){
                                if(index.equals("all")){
                                    for(JRERegister.jreFile jreFile:ServerMain.jreRegister.getFiles()){
                                        jreFile.tag="";
                                        Out.say("清除tag\t"+jreFile.filePath+"\t"+jreFile.fileName+"\t"+jreFile.tag);
                                    }
                                    break;
                                }
                                if(Integer.parseInt(index)>=ServerMain.jreRegister.getFiles().size()||Integer.parseInt(index)<0){
                                    Out.say("无index为"+index+"的文件");
                                    continue;
                                }
                                ServerMain.jreRegister.getFiles().get(Integer.parseInt(index)).tag="";
                                Out.say("清除tag\t"+ServerMain.jreRegister.getFiles().get(Integer.parseInt(index)).filePath+"\t"+ServerMain.jreRegister.getFiles().get(Integer.parseInt(index)).fileName);
                            }
                            ServerMain.jreRegister.writeToFile();
                            Out.say("TransCmd-jre rmtag","已写入文件");
                            ServerMain.cmdProcessFinish();
                            return;
                        }
                        default:{
                            ServerMain.cmdProcessFinish();
                            return;
                        }
                    }
                }
                case "!note":{
                    if(cmd.length<2){
                        Out.say("transCmd-note","命令语法不正确");
                        ServerMain.cmdProcessFinish();
                        return;
                    }
                    String note=typeCmd.substring(cmd[0].length()+1,typeCmd.length());
                    Out.say("note:"+note);
                    ServerMain.note=new StringBuffer(note);
                    ServerMain.cmdProcessFinish();
                    return;
                }
                case "!exit":{
                    if(cmd.length<2){
                        Out.say("TransCmd-exit","命令语法不正确");
                        ServerMain.cmdProcessFinish();
                        return;
                    }
                    ArrayList<HandleConn> kill=new ArrayList<>();
                    if(cmd[1].equalsIgnoreCase("-m")){
                        ArrayList<String> alreadyScanHostNames=new ArrayList<>();
                        Out.say("TransCmd-exit-m","扫描并清除重复的连接:");
                        for(HandleConn conn:ServerMain.socketArrayList){
                            Out.sayThisLine(conn.hostName+":");
                            if (alreadyScanHostNames.contains(conn.hostName)){
                                kill.add(conn);
                                Out.say("kill");
                            }else {
                                alreadyScanHostNames.add(conn.hostName);
                                Out.say("keep");
                            }
                        }
                        Out.say("TransCmd-exit-m","扫描完成，清除"+kill.size()+"个重复连接");
                    }else {
                        for (HandleConn conn : ServerMain.socketArrayList) {
                            if (conn.hostName.equals(cmd[1])) {
                                kill.add(conn);
                            }
                        }
                    }
                    if (kill.size()==0){
                        Out.say("无符合条件的连接");
                    }
                    for(HandleConn conn: kill){
                        try{
                            conn.bufferedWriter.write("!!exit "+conn.hostName);
                            conn.bufferedWriter.newLine();
                            conn.bufferedWriter.flush();
                        }catch (Exception ignored){}
                        ServerMain.killConn(conn);
                    }
                    return;
                }
                case "!hst":{
                    try{
                        Out.sayThisLine(Out.history.toString());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    return;
                }
                case "!log":{
                    if(cmd.length>1) {
                        if(cmd[1].equalsIgnoreCase("flush")) {
                            Out.flushLoggedHistoryBuffer();
                            Out.say("TransCmd-flush", "已强制刷新日志历史记录缓冲区");
                        }else if(cmd[1].equalsIgnoreCase("len")){
                            Out.say("TransCmd-log","日志缓冲区(已缓存/设定大小/占比):"+Out.loggedHistory.length()
                                    +"/"+Out.LOGGED_HISTORY_BUFFER_LEN
                                    +"/"+((float)Out.loggedHistory.length()/(float) Out.LOGGED_HISTORY_BUFFER_LEN)*100+"%");
                        }
                    }else{
                        Out.say("TransCmd-log","命令语法不正确");
                    }
                    return;
                }
                case "!all":{
                    for(HandleConn conn:ServerMain.socketArrayList){
                        try {
                            conn.bufferedWriter.write(typeCmd.substring(5));
                            conn.bufferedWriter.newLine();
                            conn.bufferedWriter.flush();
                        }catch (Exception ignored){}
                    }
                    return;
                }
                case "!ban":{
                    if(cmd.length<2){
                        Out.say("TransCmd-ban","命令语法不正确");
                        ServerMain.cmdProcessFinish();
                        return;
                    }
                    switch (cmd[1]){
                        case "add":{
                            if (cmd.length<3){
                                Out.say("TransCmd-banAdd","命令语法不正确");
                                ServerMain.cmdProcessFinish();
                                return;
                            }
                            FileRW.write("banIps.txt",AcceptConn.getBannedIpsStr()+cmd[2]+";");
                            AcceptConn.loadBanList();
                            Out.say("TransCmd-ban-add","已添加新的banIp:"+cmd[2]);
                            return;
                        }
                        case "ls": {
                            Out.say("TranCmd-ban-ls", "总共bannedIP:" + AcceptConn.banList.size());
                            for (String ip : AcceptConn.banList) {
                                Out.say(ip);
                            }
                            Out.say("TransCmd-ban-ls", "列表完成");
                            return;
                        }
                        default:{
                            Out.say("TransCmd-banAdd","命令语法不正确");
                            ServerMain.cmdProcessFinish();
                            return;
                        }
                    }
                }
	            case "!desc":{
					if(cmd.length<2){
						Out.say("TransCmd-desc","命令语法不正确");
						ServerMain.cmdProcessFinish();
						return;
					}
					switch (cmd[1]){
						case "add":{
							if(cmd.length<4){
								Out.say("TransCmd-desc","命令语法不正确");
								ServerMain.cmdProcessFinish();
								return;
							}
							//遍历查找完整主机名
							for(HandleConn conn:ServerMain.socketArrayList){
								if(conn.hostName.startsWith(cmd[2])){
									AcceptConn.description.put(conn.hostName,cmd[3]);
									AcceptConn.saveDescription();
									Out.say("TransCmd-desc","设置在线"+conn.hostName+"的描述为"+cmd[3]);
									return;
								}
							}
							if(cmd.length>=5&&cmd[4].equalsIgnoreCase("-offline")){
								AcceptConn.description.put(cmd[2],cmd[3]);
								AcceptConn.saveDescription();
								Out.say("TransCmd-desc-offline","设置离线主机"+cmd[2]+"的描述为"+cmd[3]);
							}else {
								Out.say("TransCmd-desc","无此在线主机且未指定-offline选项");
							}
							return;
						}
						case "ls":{
							Out.say("TransCmd-desc-ls","列出所有已设置的描述("+AcceptConn.description.size()+")\nname\tdescription");
							int index=0;
							for(String key:AcceptConn.description.keySet()){
								Out.say(++index+" "+key+"\t"+AcceptConn.description.get(key));
							}
							Out.say("TransCmd-desc-ls","列表完成");
							return;
						}
						case "rm":{
							if(cmd.length<3){
								Out.say("TransCmd-desc","命令语法不正确");
								ServerMain.cmdProcessFinish();
								return;
							}
							for(String key:AcceptConn.description.keySet()){
								if(key.startsWith(cmd[2])){
									AcceptConn.description.remove(key);
									AcceptConn.saveDescription();
									Out.say("TransCmd-desc-rm","删除描述"+key);
									return;
								}
							}
							Out.say("TransCmd-desc-rm","没有符合条件的描述键");
							return;
						}
					}
	            	return;
	            }
                case "!close":
                case "!stop": {
                    Out.say("TransCmd-stop", "关闭服务端");
                    ServerMain.cmdProcessFinish();
                    ServerMain.stopServer(0);
                }
            }
            //传输指令
            if (ServerMain.focusedConn == null) {
                Out.say("TransCmd", "没有聚焦任何连接");
                ServerMain.cmdProcessFinish();
                return;
            }
            try {
                ServerMain.focusedConn.bufferedWriter.write(typeCmd);
                ServerMain.focusedConn.bufferedWriter.newLine();
                ServerMain.focusedConn.bufferedWriter.flush();
            } catch (Exception e) {
                e.printStackTrace();
                Out.say("TransCmd", "指令传输失败");
                ServerMain.killConn(ServerMain.focusedConn);
                ServerMain.cmdProcessFinish();
            }
        }catch (Exception e){
            throw e;
        }
    }
}
