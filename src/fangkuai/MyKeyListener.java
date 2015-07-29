package fangkuai;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;

public class MyKeyListener implements ActionListener {

	TopJPanel top;

	Launcher launcher;
	BottomJPanel bom;
	
	public  MyKeyListener(TopJPanel top,Launcher launcher,BottomJPanel bom){
		this.top=top;
		this.launcher=launcher;
		this.bom=bom;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(bom.jb[2])){
			Thread th=new Thread(launcher);
			th.start();
		}
		if(e.getSource().equals(bom.jb[1])){
			launcher.fallmove();
		}
		top.requestFocusInWindow();
	}
}
