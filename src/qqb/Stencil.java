package qqb;

import java.awt.Canvas;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
//模板类，启动类
public class Stencil extends JFrame {
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		new FinalStencil();
	}

	Fragment fm;

	Stencil sc;

	public Stencil() {
		super("七巧板");
		setBounds(400, 130, 500, 450);
		setLayout(new FlowLayout());
		Fragment frag = new Fragment();
		sc = new Stencil(frag);

		frag.setSize(400, 400);
		frag.addKeyListener(new MyKdyAdapter(sc));

		this.add(frag);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(Boolean.TRUE);
		frag.requestFocus();
	}

	public Stencil(Fragment frag) {
		this.fm = frag;
	}

	// 按键后重画
	public synchronized void drawFragment(String direction) {
		if (direction.equalsIgnoreCase("right")) {
			for (int i = 0; i < fm.maxrow; i++) {
				for (int j = 0; j < fm.maxrow; j++) {
					if (fm.status[i][j] == 1) {
						if (j + 1 >= fm.maxrow) {
							break;
						}
						String strtemp = fm.urls[i][j + 1];
						fm.urls[i][j + 1] = fm.urls[i][j];
						fm.urls[i][j] = strtemp;

						fm.status[i][j] = 0;
						fm.status[i][j + 1] = 1;
						break;
					}
				}
			}
		}
		if (direction.equalsIgnoreCase("left")) {
			for (int i = 0; i < fm.maxrow; i++) {
				for (int j = 0; j < fm.maxrow; j++) {
					if (fm.status[i][j] == 1) {
						if (j - 1 < 0) {
							break;
						}
						String strtemp = fm.urls[i][j - 1];
						fm.urls[i][j - 1] = fm.urls[i][j];
						fm.urls[i][j] = strtemp;

						fm.status[i][j] = 0;
						fm.status[i][j - 1] = 1;
						break;
					}
				}
			}
		}
		if (direction.equalsIgnoreCase("up")) {
			for (int i = 0; i < fm.maxrow; i++) {
				for (int j = 0; j < fm.maxrow; j++) {
					if (fm.status[i][j] == 1) {
						if (i - 1 < 0) {
							break;
						}
						String strtemp = fm.urls[i-1][j];
						fm.urls[i-1][j] = fm.urls[i][j];
						fm.urls[i][j] = strtemp;

						fm.status[i][j] = 0;
						fm.status[i-1][j] = 1;
						break;
					}
				}
			}
		}
		if (direction.equalsIgnoreCase("down")) {
			for (int i = 0; i < fm.maxrow; i++) {
				for (int j = 0; j < fm.maxrow; j++) {
					if (fm.status[i][j] == 1) {
						if (i + 1 >= fm.maxrow) {
							break;
						}
						String strtemp = fm.urls[i+1][j];
						fm.urls[i+1][j] = fm.urls[i][j];
						fm.urls[i][j] = strtemp;

						fm.status[i][j] = 0;
						fm.status[i+1][j] = 1;
						break;
					}
				}
			}
		}

		Image image;
		image = fm.createImage(fm.getWidth(), fm.getHeight());
		Graphics g = image.getGraphics();
		// fm.paint(g);
		fm.repaint();
		g.drawImage(image, 0, 0, 100, 100, fm);
	}

	public void left() {
		drawFragment("left");
	}

	public void right() {
		drawFragment("right");
	}

	public void up() {
		drawFragment("up");
	}

	public void down() {
		drawFragment("down");
	}

}

//画布及拼图碎片类
class Fragment extends Canvas {
	private static final long serialVersionUID = 697768922540312190L;

	int maxrow, maxcol, fragSize;
	int[][] status = { { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 1, 0, 0, 0 } };

	String[][] urls = new String[4][4];

	JPanel jp;

	public Fragment() {
		maxrow = 4;
		maxcol = 4;
		fragSize = 100;
		jp = new JPanel();
		initUrls();
	}

	@Override
	public void paint(Graphics g) {
		Toolkit t = this.getToolkit();
		ClassLoader clLoader = Fragment.class.getClassLoader();
		Image img = null;

		for (int i = 0; i < maxrow; i++) {
			for (int j = 0; j < maxcol; j++) {
				img = t.getImage(clLoader.getResource(urls[i][j]));
				g.drawImage(img, j * (fragSize + 1), i * (fragSize + 1), 100, 100, this);
			}
		}
	}

	// 图片路径
	void initUrls() {
		int count = 0;
		for (int i = 0; i < maxrow; i++) {
			for (int j = 0; j < maxcol; j++) {
				count++;
				urls[i][j] = "qqb/img/" + count + ".gif";
			}
		}
	}
}
//方向键监听类
class MyKdyAdapter extends KeyAdapter{

	Stencil sc;
	public MyKdyAdapter(Stencil sc){
		this.sc=sc;
	}
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode()==KeyEvent.VK_LEFT){
			sc.left();
		}else if(e.getKeyCode()==KeyEvent.VK_RIGHT){
			sc.right();
		}else if(e.getKeyCode()==KeyEvent.VK_UP){
			sc.up();
		}else if(e.getKeyCode()==KeyEvent.VK_DOWN){
			sc.down();
		}
	}
	
}