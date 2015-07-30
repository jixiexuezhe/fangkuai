package fangkuai;

import java.awt.GridLayout;

import javax.swing.*;

public class BottomJPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	JButton[] jb = new JButton[4];
	JButton[] jb2 = new JButton[3];

	JLabel jl;
	JTextField jtf;

	JPanel jp1, jp2;

	public BottomJPanel() {
		jb[0] = new JButton("←");
		jb[1] = new JButton("↓");
		jb[2] = new JButton("开始");
		jb[3] = new JButton("暂停");

		jb2[0] = new JButton("→");
		jb2[1] = new JButton("翻转");
		jb2[2] = new JButton("结束");

		jl = new JLabel("得分：");
		jtf = new JTextField("0",5);

		jp1 = new JPanel();
		jp2 = new JPanel();
		jp1.setLayout(new GridLayout(4, 2, 10, 10));
		jp2.setLayout(new GridLayout(4, 2, 10, 10));

		for (int i = 0; i < 4; i++) {
			jp1.add(jb[i]);
		}

		for (int i = 0; i < 3; i++) {
			jp2.add(jb2[i]);
		}

		jp1.add(jl);
		jp2.add(jtf);
		this.add(jp1, "South");
		this.add(jp2, "South");
	}
}
