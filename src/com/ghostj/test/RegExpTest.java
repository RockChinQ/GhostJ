package com.ghostj.test;

import java.util.regex.Pattern;

public class RegExpTest {
	public static void main(String[] args) {
		System.out.println(Pattern.matches("/192.*","/192.168.1.2"));
	}
}
