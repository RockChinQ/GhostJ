package com.ghostj.console.model.routine;

/**
 * Defines the model of a routine that can be exec.
 * @author Rock Chin
 */
public abstract class AbstractRoutine implements Runnable{
	private static long RID_INDEX=0;
	private final long R_ID=RID_INDEX++;
	private String R_NAME="Routine-"+R_ID;

	public String getNAME() {
		return R_NAME;
	}

	public void setNAME(String NAME) {
		R_NAME = NAME;
	}

	public long getID() {
		return R_ID;
	}
	//proxy thread
	private Thread proxyThr=new Thread(this);
	public abstract void run();

}
