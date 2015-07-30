package fangkuai;

import java.awt.GridLayout;

import javax.swing.JFrame;

public class MyWindow extends JFrame{
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
		topJPanel.addKeyListener(new MyKeyAdapters(launcher));
		

		this.add(topJPanel);
		this.add(bp);

		this.setVisible(Boolean.TRUE);
	}
}
