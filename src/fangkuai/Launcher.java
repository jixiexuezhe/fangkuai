package fangkuai;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Window;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Launcher extends Canvas {
	private static final long serialVersionUID = -7173674968562118385L;
	
	private	static MyDraw draw;
	public static void main(String[] args) {
		Image image;
		image=draw.createImage(draw.getWidth(), draw.getHeight());
		Graphics graphics=image.getGraphics();
		draw.paint(graphics);
		graphics.drawImage(image, 0, 0, draw);
		new MyWindow();
	}

}

// 画方格
class MyDraw extends Canvas {
	// 单元格大小
	private static int unitSize = 20;
	// 最大行
	private static int maxrow = 10;
	// 最大列
	private static int maxcol = 20;

	JPanel jPanel;
	public MyDraw(JPanel jPanel){
		this.jPanel=jPanel;
	}
	
	@Override
	public void paint(Graphics g) {
		Graphics gr = getGraphics();
		for (int i = 0; i < maxrow; i++) {
			for (int j = 0; j < maxcol; j++) {
				gr.setColor(Color.yellow);
				gr.fillRect(i * (unitSize + 1), j * (unitSize + 1), unitSize, unitSize);
			}
		}
	}
}

// 窗口
class MyWindow extends JFrame {

	private static final long serialVersionUID = 1L;

	MyDraw draw;
	
	public MyWindow() {
		super("俄罗斯方块-made in china！");
		 JPanel rp=new JPanel();
		this.setBounds(130, 130, 500, 450);
		 this.setBackground(Color.black);
		// this.setLayout(new GridLayout(1, 2, 50, 30));
		// this.setSize(80, 160);

		Graphics graphics = this.getGraphics();
		// graphics.setColor(Color.yellow);
		// graphics.fillRect(Launcher.unitSize+1, lc.unitSize+2, lc.unitSize,
		// lc.unitSize);;
		// this.add(rp);
		
		draw=new MyDraw(rp);
		draw.paint(graphics);
		this.setVisible(Boolean.TRUE);
	}

}