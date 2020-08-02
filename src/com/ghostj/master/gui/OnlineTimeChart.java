package com.ghostj.master.gui;

import javax.swing.*;
import java.awt.*;

public class OnlineTimeChart extends JPanel {
	@Override
	public void paint(Graphics g){
		g.setColor(this.getBackground());
		g.fillRect(0,0,this.getWidth(),this.getHeight());
	}
}
