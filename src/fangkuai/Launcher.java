package fangkuai;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Window;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Launcher implements Runnable {
	public static void main(String[] args) {
		new MyWindow();
	}

	static final int[][] patten = { // 16进制代表每种方块
	{ 0x0f00, 0x4444, 0x0f00, 0x4444 },// 长条
			{ 0x6600, 0x6600, 0x6600, 0x6600 },// 正方块
			{ 0x04e0, 0x0464, 0x00e4, 0x04c4 },// 三角
			{ 0x08e0, 0x0644, 0x00e2, 0x044c },// 弯折一下，1、3，1左
			{ 0x02e0, 0x0446, 0x00e8, 0x0c44 },// 弯折一下，1、3，1右
			{ 0x0462, 0x006c, 0x0462, 0x006c },// 弯折两下，1、2、1，1左上；1右下
			{ 0x0264, 0x00c6, 0x0264, 0x00c6 } // 弯折两下，1、2、1，1右上；1左下
	};

	private int row, col; // 方块所在的行数，列数
	private int blockType = -1; // 方块类型，7种，大小范围0-6
	private int blockState = -1; // 方块状态，4种，大小范围0-3
	private int isfall = 1;// 绘画颜色标志

	TopJPanel topj;

	public Launcher(TopJPanel topJPanel) {
		this.topj = topJPanel;
		row = 0;
		col = 3;
	}

	@Override
	public void run() {
		topj.requestFocusInWindow();
		drawfk(blockType);
		System.out.println("ok");
	}

	public synchronized void drawfk(int blockType) {
		blockType = 5;
		blockState = 2;

		int comIndex = 0x8000;
		for (int i = row; i < row + 4; i++)
			for (int j = col; j < col + 4; j++) {
				if ((patten[blockType][blockState] & comIndex) != 0) {
					if (isfall == 1)
						// lsc.drawUnit(i, j, 1); // 再画，画为RED
						topj.unitState[i][j] = 1; // 将状态记录改变
					else if (isfall == 0) {
						// lsc.drawUnit(i, j, 2); // 无法下落，画为BLUE
						topj.unitState[i][j] = 2;// 将状态记录改变，用于画下张图
						// topj.deleteFullLine(i); // 判断此行是否可以消
					}
				}
				comIndex = comIndex >> 1;
			}
		Image image;
		image = topj.createImage(topj.getWidth(), topj.getHeight());
	}

}

// 窗口
class MyWindow extends JFrame {

	private static final long serialVersionUID = 1L;

	TopJPanel topJPanel;
	BottomJPanel bp;

	Launcher launcher;

	public MyWindow() {
		super("俄罗斯方块-made in china！");
		setBounds(500, 100, 500, 455);
		setLayout(new GridLayout(1, 2, 10, 100));

		bp = new BottomJPanel();
		bp.setSize(80, 600);
		topJPanel = new TopJPanel(bp);
		launcher = new Launcher(topJPanel);

		// 监听
		bp.jb[2].addActionListener(new MyKeyListener(topJPanel, launcher, bp));

		this.add(topJPanel);
		this.add(bp);

		this.setVisible(Boolean.TRUE);
	}

}