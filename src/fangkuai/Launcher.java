package fangkuai;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.util.Arrays;

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

	private int oldRow, oldCol; // 记录方块变化前所在的行数，列数
	private int oldType = -1, oldState; // 记录方块变化前的类型和状态

	private int isfall = 1;// 绘画颜色标志

	private boolean end = false;// 结束标记，为true时表示结束

	private int score = 0;

	TopJPanel topj;
	BottomJPanel bp =new BottomJPanel();

	public Launcher(TopJPanel topJPanel) {
		this.topj = topJPanel;
		row = 0;
		col = 3;
		oldRow = row;
		oldCol = col;
	}

	public void reInit() {
		blockType = -1;
		isfall = 1;
	}

	public void reInitRowCol() // 初始化方块起始点
	{
		row = 0;
		col = 3;
	}

	@Override
	public void run() {
		topj.requestFocusInWindow();
		while (!end) {
			int blocktype = (int) (Math.random() * 100) % 7;
			drawfk(blocktype);
			do {
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} while (fallmove());
			for (int j = 0; j < topj.maxcol; j++) {
				// 判断是否结束
				if (topj.unitState[3][j] == 2) {
					end = true;
				}
			}
		}
	}

	public synchronized void drawfk(int blockType) {

		if (this.blockType != blockType)
			blockState = (int) (Math.random() * 100) % 4; // 状态
		this.blockType = blockType;
		
		
		if(blockType <0){
			System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			System.out.println("the blockType is:"+blockType);
			System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		}

		if (!isMove(3)) // 判断是否能画
		{
			this.blockType = oldType;
			this.blockState = oldState;
			return;
		}

		// 还原
		int comIndex = 0x8000;
		if (this.oldType != -1) {
			for (int i = oldRow; i < oldRow + 4; i++)
				for (int j = oldCol; j < oldCol + 4; j++) {
					if ((patten[oldType][oldState] & comIndex) != 0 && topj.unitState[i][j] == 1)
						// lsc.drawUnit(i, j, 0); // 先还原
						topj.unitState[i][j] = 0;// 将状态记录改变，用于画下张图
					comIndex = comIndex >> 1;
				}
		}

		comIndex = 0x8000;
		for (int i = row; i < row + 4; i++)
			for (int j = col; j < col + 4; j++) {
				if ((patten[blockType][blockState] & comIndex) != 0) {
					if (isfall == 1) {
						// lsc.drawUnit(i, j, 1); // 再画，画为RED
						topj.unitState[i][j] = 1; // 将状态记录改变
					} else if (isfall == 0) {
						// lsc.drawUnit(i, j, 2); // 无法下落，画为BLUE
						topj.unitState[i][j] = 2;// 将状态记录改变，用于画下张图
						this.deleteFullLine(i); // 判断此行是否可以消
					}
				}
				comIndex = comIndex >> 1;
			}
		Image image;
		image = topj.createImage(topj.getWidth(), topj.getHeight());
		Graphics gr = image.getGraphics();
		topj.paint(gr);
		gr.drawImage(image, 0, 0, topj);

		if (isfall == 0) // 无法下落，先判断是否能消行，再重新初始化
		{
			// lsc.deleteFullLine(row,col);
			reInit();
			reInitRowCol();
		}

		oldRow = row;
		oldCol = col;
		oldType = blockType;
		oldState = blockState;
	}

	public void deleteFullLine(int row) // 判断此行是否可以消，同时可消就消行
	{
		for (int j = 0; j < topj.maxcol; j++)
			if (topj.unitState[row][j] != 2)
				return;

		for (int i = row; i > 3; i--)
			// 到此即为可消，将上面的移下消此行
			for (int j = 0; j < topj.maxcol; j++)
				// drawUnit(i, j, unitState[i - 1][j]);
				topj.unitState[i][j] = topj.unitState[i - 1][j];// 将状态记录改变，用于画下张图
		score++;
		bp.jtf.setText(String.valueOf(score));
	}

	void leftmove() {
		if (this.blockType != -1 && isMove(0)) {
			col -= 1;
			drawfk(blockType);
		}
	}

	void rightmove() {
		if (this.blockType != -1 && isMove(1)) {
			col += 1;
			drawfk(blockType);
		}
	}

	void turn() {
		if (this.blockType != -1) {
			blockState = (blockState + 1) % 4;
			if (isMove(3))
				drawfk(blockType);
			else
				blockState = (blockState + 3) % 4;
		}
	}

	boolean fallmove() {
		if (this.blockType != -1) {
			if (isMove(2)) {
				row += 1;
				drawfk(blockType);
				return true;
			} else {
				isfall = 0;
				drawfk(blockType);
				return false;
			}
		}
		return false;
	}

	public synchronized boolean isMove(int tag) // 左 0 ，右 1 ，下 2 ,旋转 3
	{
		int comIndex = 0x8000;
		for (int i = row; i < row + 4; i++)
			for (int j = col; j < col + 4; j++) {
				if ((patten[blockType][blockState] & comIndex) != 0) {
					if (tag == 0 && (j == 0 || topj.unitState[i][j - 1] == 2))// 是否能左移
						return false;
					else if (tag == 1 && (j == topj.maxcol - 1 || topj.unitState[i][j + 1] == 2))// 是否能右移
						return false;
					else if (tag == 2 && (i == topj.maxrow - 1 || topj.unitState[i + 1][j] == 2))// 是否能下移
						return false;
					else if (tag == 3 && (i > topj.maxrow - 1 || j < 0 || j > topj.maxcol - 1 || topj.unitState[i][j] == 2))// 是否能旋转
						return false;
				}
				comIndex = comIndex >> 1;
			}
		return true;
	}
}