package com.ghostj.server.conn;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

/**
 * 描述了一个conn处理对象的基本信息
 * @author Rock Chin
 */
public abstract class AbstractConnHandler implements Runnable{
	protected Socket socket;
	protected DataInputStream inputStream;
	protected DataOutputStream outputStream;
	public Socket getSocket() {
		return socket;
	}
	public DataInputStream getInputStream() {
		return inputStream;
	}
	public DataOutputStream getOutputStream() {
		return outputStream;
	}
	/**
	 * 设置socket对象并自动生成io流对象
	 * @param socket
	 */
	public void initIOStream(Socket socket)throws Exception{
		this.socket=socket;
		this.inputStream=new DataInputStream(socket.getInputStream());
		this.outputStream=new DataOutputStream(socket.getOutputStream());
	}
	public void writeUTF(String str)throws Exception{
		getOutputStream().writeUTF(str);
	}
	public void writeUTFIgnoreException(String str){
		try{
			writeUTF(str);
		}catch (Exception ignored){}
	}
	public void write(byte[] b)throws Exception{
		getOutputStream().write(b);
	}
	/**
	 * 代理线程
	 * 以本实现Runnable的对象实例化的Thread对象，仅能使用一次
	 */
	private final Thread proxyThr=new Thread(this);
	public void start(){
		this.proxyThr.start();
	}
	public void stop(){
		this.proxyThr.stop();
	}

	/**
	 * 释放本连接
	 * @throws Exception
	 */
	protected void dispose()throws Exception{
		this.socket.close();
	}
	protected void disposeIgnoreException(){
		try {
			dispose();
		}catch (Exception ignored){}
	}
}
