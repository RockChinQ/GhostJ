package test;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.Attribute;
import javax.print.attribute.AttributeSet;

public class PrintOperation {
	public Integer getExistQueuePrinter() {
		int queue = 0;
		PrintService myService = null;
		//创建printService对象
		PrintService printService = PrintServiceLookup.lookupDefaultPrintService();
		//有打印机
		if (printService != null) {
			//--> set printService.
			myService = printService;

			//--> get attributes from printService.
			AttributeSet attributes = printService.getAttributes();

			//--> loop attributes.
			for (Attribute a : attributes.toArray()) {
				String name = a.getName();
				String value = attributes.get(a.getClass()).toString();
				//System.out.println(name + " : " + value);
				if (name.equals("queued-job-count")) {
					//System.out.println(name + " : " + value);
					queue = Integer.parseInt(value);
				}
			}
			Object[] obj = attributes.toArray();
			//System.out.println("queue = " + obj[3]);
			return queue;
        /* debug.
         for (Object value : obj) {
         System.out.println("Color = " + value);
         }
         */

		}
		return null;
	}
}
