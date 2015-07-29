package fangkuai;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

 class Block implements Runnable {
	static final int type = 7, state = 4;

	static final int[][] patten = { // 16进制代表每种方块
	{ 0x0f00, 0x4444, 0x0f00, 0x4444 },// 长条
			{ 0x6600, 0x6600, 0x6600, 0x6600 },// 正方块
			{ 0x04e0, 0x0464, 0x00e4, 0x04c4 },// 三角
			{ 0x08e0, 0x0644, 0x00e2, 0x044c },// 弯折一下，1、3，1左
			{ 0x02e0, 0x0446, 0x00e8, 0x0c44 },// 弯折一下，1、3，1右
			{ 0x0462, 0x006c, 0x0462, 0x006c },// 弯折两下，1、2、1，1左上；1右下
			{ 0x0264, 0x00c6, 0x0264, 0x00c6 } // 弯折两下，1、2、1，1右上；1左下
	};

	private int blockType = -1; // 方块类型，7种，大小范围0-6

	private int blockState;// 方块状态，4种，大小范围0-3

	private int row, col; // 方块所在的行数，列数

	private int oldRow, oldCol; // 记录方块变化前所在的行数，列数

	private int oldType = -1, oldState; // 记录方块变化前的类型和状态

	private int isfall = 1; // 标记若画，画成什么颜色的，

	// 1表示可以下落，画为红色；0表示不可下落，画为蓝色

	private boolean end = false;// 结束标记，为true时表示结束

	LeftShowCanvas lsc;

	public Block(LeftShowCanvas lsc) {
		this.lsc = lsc;
		row = 0;
		col = 3;
		oldRow = row;
		oldCol = col;
	}

	public void reInit() // 一个方块无法下落后，重新初始化
	{
		blockType = -1;
		isfall = 1;
	}

	public void reInitRowCol() // 初始化方块起始点
	{
		row = 0;
		col = 3;
	}

	public void run() // 下落线程
	{
		lsc.requestFocusInWindow(); // 获得焦点
		while (!end) {
			int blocktype = (int) (Math.random() * 100) % 7;
			drawBlock(blocktype);
			do {
				try {
					Thread.sleep(50); // 控制下落速度
				} catch (InterruptedException e) {

				}
			} while (fallMove()); // 下落
			for (int j = 0; j < lsc.maxcols; j++)
				// 判断是否结束
				if (lsc.unitState[3][j] == 2)
					end = true;
		}
	}

	public synchronized void drawBlock(int blockType) // 画方块
	{
		if (this.blockType != blockType)
			blockState = (int) (Math.random() * 100) % 4; // 状态
		this.blockType = blockType; // 样式
		if (!isMove(3)) // 判断是否能画
		{
			this.blockType = oldType;
			this.blockState = oldState;
			return;
		}
		int comIndex = 0x8000;
		if (this.oldType != -1) {
			for (int i = oldRow; i < oldRow + 4; i++)
				for (int j = oldCol; j < oldCol + 4; j++) {
					if ((patten[oldType][oldState] & comIndex) != 0 && lsc.unitState[i][j] == 1)
						// lsc.drawUnit(i, j, 0); // 先还原
						lsc.unitState[i][j] = 0;// 将状态记录改变，用于画下张图
					comIndex = comIndex >> 1;
				}
		}
		comIndex = 0x8000;
		for (int i = row; i < row + 4; i++)
			for (int j = col; j < col + 4; j++) {
				if ((patten[blockType][blockState] & comIndex) != 0) {
					if (isfall == 1)
						// lsc.drawUnit(i, j, 1); // 再画，画为RED
						lsc.unitState[i][j] = 1; // 将状态记录改变
					else if (isfall == 0) {
						// lsc.drawUnit(i, j, 2); // 无法下落，画为BLUE
						lsc.unitState[i][j] = 2;// 将状态记录改变，用于画下张图
//						lsc.deleteFullLine(i); // 判断此行是否可以消
					}
				}
				comIndex = comIndex >> 1;
			}

		Image image; // 创建缓冲图片,利用双缓冲消除闪烁，画的下个状态图
		image = lsc.createImage(lsc.getWidth(), lsc.getHeight());
		Graphics g = image.getGraphics();
		lsc.paint(g);
		g.drawImage(image, 0, 0, lsc);

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

	public void leftTurn() // 旋转，左转
	{
		if (this.blockType != -1) {
			blockState = (blockState + 1) % 4;
			if (isMove(3))
				drawBlock(blockType);
			else
				blockState = (blockState + 3) % 4;
		}
	}

	public void leftMove() // 左移
	{
		if (this.blockType != -1 && isMove(0)) {
			col -= 1;
			drawBlock(blockType);
		}
	}

	public void rightMove() // 右移
	{
		if (this.blockType != -1 && isMove(1)) {
			col += 1;
			drawBlock(blockType);
		}
	}

	public boolean fallMove() // 下移
	{
		if (this.blockType != -1) {
			if (isMove(2)) {
				row += 1;
				drawBlock(blockType);
				return true;
			} else {
				isfall = 0;
				drawBlock(blockType);
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
					if (tag == 0 && (j == 0 || lsc.unitState[i][j - 1] == 2))// 是否能左移
						return false;
					else if (tag == 1 && // 是否能右移
							(j == lsc.maxcols - 1 || lsc.unitState[i][j + 1] == 2))
						return false;
					else if (tag == 2 && // 是否能下移
							(i == lsc.maxrows - 1 || lsc.unitState[i + 1][j] == 2))
						return false;
					else if (tag == 3 && // 是否能旋转
							(i > lsc.maxrows - 1 || j < 0 || j > lsc.maxcols - 1 || lsc.unitState[i][j] == 2))
						return false;
				}
				comIndex = comIndex >> 1;
			}
		return true;
	}
}

class LeftShowCanvas extends Canvas {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	int maxrows, maxcols; // 画布最大行数，列数

	int unitSize; // 单元格的大小，小正方格

	int[][] unitState; // 每个小方格的状态 0、1、2表示

	RightPanel rp;

	int score;

	public LeftShowCanvas(RightPanel rp) {
		this.rp = rp;
//		score = Integer.valueOf(rp.jtf.getText());
		maxrows = 20;
		maxcols = 10;
		unitSize = 20;
		unitState = new int[maxrows][maxcols];
//		initCanvas();
	}

	public void initCanvas() // 初始化，画布方格
	{
		for (int i = 0; i < maxrows; i++)
			for (int j = 0; j < maxcols; j++)
				unitState[i][j] = 0;
	}

	public void paint(Graphics g) {
		for (int i = 0; i < maxrows; i++) {
			for (int j = 0; j < maxcols; j++)
				drawUnit(i, j, unitState[i][j]); // 画方格
			if (i == 3) {
				g.setColor(Color.RED);
				g.drawLine(0, (i + 1) * (unitSize + 1) - 1, maxcols * (unitSize + 1) - 1, (i + 1) * (unitSize + 1) - 1);
			}
		}
	}

	public void drawUnit(int row, int col, int tag) // 画方格
	{
		unitState[row][col] = tag; // 记录状态
		Graphics g = getGraphics();
		switch (tag) {
		case 0: // 初始黑色
			g.setColor(Color.BLACK);
			break;
		case 1: // 方格黑色
			g.setColor(Color.RED);
			break;
		case 2:
			g.setColor(Color.BLUE);
			break;
		}
		g.fillRect(col * (unitSize + 1), row * (unitSize + 1), unitSize, unitSize);
	}

	public void deleteFullLine(int row) // 判断此行是否可以消，同时可消就消行
	{
		for (int j = 0; j < maxcols; j++)
			if (unitState[row][j] != 2)
				return;

		for (int i = row; i > 3; i--)
			// 到此即为可消，将上面的移下消此行
			for (int j = 0; j < maxcols; j++)
				// drawUnit(i, j, unitState[i - 1][j]);
				unitState[i][j] = unitState[i - 1][j];// 将状态记录改变，用于画下张图
		score++;
		rp.jtf.setText(String.valueOf(score));
	}
}

class RightPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JButton[] jbt = new JButton[7];

	JButton[] jbt2 = new JButton[4];

	JButton jbt3;

	JTextField jtf;

	JLabel jlb;

//	MyJPanel jp1, jp2;
	JPanel jp1, jp2;

	public RightPanel() {
		jbt[0] = new JButton("长条");
		jbt[1] = new JButton("方块");
		jbt[2] = new JButton("三角");
		jbt[3] = new JButton("左三");
		jbt[4] = new JButton("右三");
		jbt[5] = new JButton("左二");
		jbt[6] = new JButton("右二");
		jbt2[0] = new JButton("左移");
		jbt2[1] = new JButton("右移");
		jbt2[2] = new JButton("下移");
		jbt2[3] = new JButton("翻转");

		jbt3 = new JButton("开始");
		jtf = new JTextField("0", 5);
		jlb = new JLabel("得分", JLabel.CENTER);

		jp1 = new JPanel(); // 左边的上面板
		jp2 = new JPanel(); // 左边的下面板
		jp1.setLayout(new GridLayout(4, 2, 20, 10)); // 网格布局
		jp2.setLayout(new GridLayout(4, 2, 20, 10)); // 网格布局
		this.setLayout(new BorderLayout()); // 边界布局
		for (int i = 0; i < 7; i++)
			jp1.add(jbt[i]);

		jp1.add(jbt3);

		for (int i = 0; i < 4; i++)
			jp2.add(jbt2[i]);

		jp2.add(jlb);
		jp2.add(jtf);

		this.add(jp1, "North");
		this.add(jp2, "Center");
	}
}

// 重写MyPanel类，使Panel的四周留空间
class MyJPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Insets getInsets() {
		return new Insets(10, 30, 30, 30);
	}
}

class MyActionListener implements ActionListener {
	RightPanel rp;

	Block bl;

	LeftShowCanvas lsc;

	public MyActionListener(RightPanel rp, Block bl, LeftShowCanvas lsc) {
		this.rp = rp;
		this.bl = bl;
		this.lsc = lsc;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(rp.jbt3)) {
			// 这样子则按几次开始按钮就创建几个相同的线程，控制着相同的数据
			Thread th = new Thread(bl);
			th.start();
		}
//		for (int i = 0; i < Block.type; i++)
//			if (e.getSource().equals(rp.jbt[i])) // 看是画哪个
//			{
//				bl.reInitRowCol();
//				bl.drawBlock(i);
//				lsc.requestFocusInWindow(); // 获得焦点
//				return;
//			}
//		if (e.getSource().equals(rp.jbt2[0]))
//			bl.leftMove();
//		else if (e.getSource().equals(rp.jbt2[1]))
//			bl.rightMove();
//		else if (e.getSource().equals(rp.jbt2[2]))
//			bl.fallMove();
//		else if (e.getSource().equals(rp.jbt2[3]))
//			bl.leftTurn();
//		if(e.getSource().equals(rp.jbt2[2])){
//			bl.fallMove();
//		}
//		lsc.requestFocusInWindow(); // 获得焦点
	}
}

class MyKeyAdapter extends KeyAdapter {
	Block bl;

	public MyKeyAdapter(Block bl) {
		this.bl = bl;
	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT)
			bl.leftMove();
		else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
			bl.rightMove();
		else if (e.getKeyCode() == KeyEvent.VK_DOWN)
			bl.fallMove();
		else if (e.getKeyCode() == KeyEvent.VK_SPACE)
			bl.leftTurn();
	}
}

 class FinalElsBlock extends JFrame {
	private static final long serialVersionUID = 1L;

	Block bl;

	LeftShowCanvas lsc;

	RightPanel rp;

	public FinalElsBlock() {
		super("ELSBlock Study");
		setBounds(130, 130, 500, 450);
		setLayout(new GridLayout(1, 2, 50, 30));
		rp = new RightPanel();
		lsc = new LeftShowCanvas(rp);
		bl = new Block(lsc);
		rp.setSize(80, 400);
		for (int i = 0; i < 7; i++)
			// 为每个按钮添加消息监听
			rp.jbt[i].addActionListener(new MyActionListener(rp, bl, lsc));
		rp.jbt3.addActionListener(new MyActionListener(rp, bl, lsc));
		for (int i = 0; i < 4; i++)
			rp.jbt2[i].addActionListener(new MyActionListener(rp, bl, lsc));
		lsc.addKeyListener(new MyKeyAdapter(bl));
		this.add(lsc);
		this.add(rp);
//		this.addWindowListener(new WindowAdapter() {
//			public void windowClosing(WindowEvent e) {
//				dispose();
//				System.exit(0);
//			}
//		});
		setVisible(true);
	}
}
 
 
