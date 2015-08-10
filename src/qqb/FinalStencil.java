package qqb;

import java.awt.FlowLayout;

import javax.swing.JFrame;

public class FinalStencil extends JFrame{
	private static final long serialVersionUID = 1L;

	Stencil sc;
	
	public FinalStencil() {
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
}
