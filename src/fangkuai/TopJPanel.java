package fangkuai;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;

public class TopJPanel extends Canvas {
	// 单元格大小
	private static int unitSize = 20;
	// 最大行
	private static int maxrow = 10;
	// 最大列
	private static int maxcol = 20;

	int[][] unitState;// 0为黑色，1为红色，2位蓝色
	

	BottomJPanel bJPanel;

	public TopJPanel(BottomJPanel bJPanel) {
		this.bJPanel = bJPanel;

		unitState = new int[maxrow][maxcol];
	}

	@Override
	public void paint(Graphics g) {
		Graphics gr = getGraphics();
		for (int i = 0; i < maxrow; i++) {
			for (int j = 0; j < maxcol; j++) {
				drawUnit(i, j, unitState[i][j]);
			}
			if (i == 3) {
				gr.setColor(Color.GREEN);
				gr.drawLine(0, i * (unitSize + 1) - 1, maxcol * (unitSize + 1) - 1, i * (unitSize + 1) - 1);
			}
		}
	}

	void drawUnit(int i, int j, int us) {

		unitState[i][j] = us;

		Graphics gr = getGraphics();

		switch (us) {
		case 0:
			gr.setColor(Color.black);
			break;
		case 1:
			gr.setColor(Color.red);
			break;
		case 2:
			gr.setColor(Color.blue);
			break;
		default:
			gr.setColor(Color.white);
			break;
		}

		gr.fillRect(i * (unitSize + 1), j * (unitSize + 1), unitSize, unitSize);
	}
}
