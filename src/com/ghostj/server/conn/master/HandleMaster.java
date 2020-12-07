package com.ghostj.server.conn.master;

import com.ghostj.server.conn.AbstractConnHandler;
import com.ghostj.server.core.ServerMain;
import com.ghostj.server.log.Log;

import java.net.Socket;

/**
 * 处理Master的连接
 * @author Rock Chin
 */
public class HandleMaster extends AbstractConnHandler {
	public boolean authed=false;
	public HandleMaster(Socket socket){
		try {
			initIOStream(socket);
		} catch (Exception initIO) {
			ServerMain.log.puts(Log.ERROR,"%CLASS%","无法初始化Master连接:"+ServerMain.getErrorInfo(initIO));
		}
	}
	@Override
	public void run() {
		try{
			byte[] msgByte=new byte[8192];
			int len=0;
			while ((len=getInputStream().read(msgByte))!=-1){

			}
		}catch (Exception readingMsg){
			ServerMain.log.puts(Log.ERROR,"%CLASS%","接收来自Master的信息时出现异常:"+ServerMain.getErrorInfo(readingMsg));
		}
		dispose();
	}

	/**
	 * 删除此连接
	 * 需要确保所有连接对象已关闭，之后停止此线程，并在HandleStorage中删除引用
	 * 调用此方法 一定 会使连接被关闭，此方法内忽略所有异常
	 */
	@Override
	public void dispose(){
		try {
			super.disposeIgnoreException();
			ServerMain.handlerStorage.masters.remove(this);
			this.stop();
		}catch (Exception ignored){}
	}
}
