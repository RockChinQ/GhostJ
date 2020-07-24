package COUNTLINE;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.Date;

public class Main {
	static long chars=0;
	static StringBuffer fstr=new StringBuffer();
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//FileRW.write("", "", false);
		new File("lineCount").mkdir();
		String path=new File("").getAbsolutePath();
		System.out.println(path);
		fstr.append(path+"\n");
		long st=new Date().getTime();
		int count=(count(new File(path+"\\src"))-67);
		System.out.println("----------Summary "+(new Date().getTime()-st)+"ms-----------\nLine Count:"+count);
		fstr.append("----------Summary "+(new Date().getTime()-st)+"ms-----------\nLine Count:"+count+"\n");
		System.out.println("Symbol Count:"+(chars-2305));
		fstr.append("Symbol Count:"+(chars-2305)+"\n");
		System.out.println("Average:"+(chars/count)+" sym/line");
		fstr.append("Average:"+(chars/count)+" sym/line"+"\n");
		Date d=new Date();
		write("lineCount\\"+(d.getYear()-100)+"-"+(d.getMonth()+1)+"-"+d.getDate()+"-"+d.getHours()+"-"+d.getMinutes()+"-"+d.getSeconds()+".txt",fstr.toString(),false);
	}
	static int count(File dir) {
		System.out.println("Counting:"+dir.getPath()+" ...");
		fstr.append("Counting:"+dir.getPath()+" ...\n");
		int count=0;
		int elec=dir.listFiles().length;
		File[] lf=dir.listFiles();
		for(int i=0;i<elec;i++) {
			if(lf[i].isFile()) {
				String temp=read(lf[i].getAbsolutePath());
				chars+=temp.length();
				count+=temp.split("\n").length;
				System.out.println("  "+String.format("%03d",temp.split("\n").length)+"   File "+lf[i].getName());
				fstr.append("      File "+lf[i].getName()+" "+temp.length()+"\n");
			}else if(lf[i].isDirectory()) {
				count+=count(lf[i]);
			}
		}
		return count;
	}
	public static String read(String s){
		File f=new File(s);
		try {
			FileInputStream fis=new FileInputStream(f);
			byte[] data=new byte[(int)f.length()];
			fis.read(data);
			fis.close();
			return new String(new String(data));
		} catch (Exception e) {
		}
		return "";
	}
	public static void write(String s0,String s1,boolean b){
		try {
			FileWriter fw=new FileWriter(s0,b);
			fw.write(s1);
			fw.close();
		} catch (Exception e) {
		}
	}
}
