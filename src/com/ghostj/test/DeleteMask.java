package com.ghostj.test;

import java.io.File;

public class DeleteMask {
    public static void main(String[] args) {
        String path=javax.swing.JOptionPane.showInputDialog("path");
        File[] root=new File(path).listFiles();
        for (File a:root){
            if (a.isFile()){
                System.out.println(a.getAbsolutePath()+":"+a.renameTo(new File(a.getAbsolutePath().replaceAll("MASK",""))));
            }
        }
    }
}
