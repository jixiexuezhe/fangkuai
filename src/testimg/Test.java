package testimg;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * 给JPanel添加KeyListener监听实例
 * 
 * @author 米强
 *
 */
public class Test extends JFrame {
	private JLabel label = null;

	public Test() {
		super("给JPanel添加KeyListener监听");
		label = new JLabel("Key：", JLabel.CENTER);
		JPanel pane = new JPanel();
		// 给JPanel对象添加了KeyListener事件监听
		pane.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {
			}

			public void keyReleased(KeyEvent e) {
			}

			public void keyPressed(KeyEvent e) {
				// 将按键字符显示在JLabel标签中
				label.setText("Key：" + String.valueOf(e.getKeyChar()));
			}
		});
		getContentPane().add(pane);
		getContentPane().add(label, BorderLayout.NORTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(300, 200);
		setLocationRelativeTo(null);
		setVisible(true);
		// KeyListener监听必需在窗体显示之后调用requestFocus方法使其获得焦点，否则KeyListener事件无法触发，鼠标点击事件、拖动事件等则不需要。
		pane.requestFocus();
	}

	public static void main(String[] args) {
		new Test();
	}

}