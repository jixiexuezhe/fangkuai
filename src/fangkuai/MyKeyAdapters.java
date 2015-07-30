package fangkuai;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MyKeyAdapters extends KeyAdapter {
	Launcher launcher;

	public MyKeyAdapters(Launcher launcher) {
		this.launcher = launcher;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode()==KeyEvent.VK_LEFT){
			launcher.leftmove();
		}else  if(e.getKeyCode()==KeyEvent.VK_RIGHT){
			launcher.rightmove();
		}else if (e.getKeyCode()==KeyEvent.VK_DOWN) {
			launcher.fallmove();
		}else if (e.getKeyCode()==KeyEvent.VK_SPACE) {
			launcher.turn();
		}
	}
	
	
}
