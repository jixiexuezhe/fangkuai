package testimg;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class WinDrawImageTes {
	 public static void main(String[] args)
	    {
	        JFrame frame = new JFrame();
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
	        frame.add(new MyGraphics());
	        
	        frame.setVisible(true);
	    }
}


 class MyGraphics extends JPanel{
    private static final long serialVersionUID = 1L;
    public void paint(Graphics g)
    {
        Toolkit tool = this.getToolkit();
        Image image = tool.getImage(MyGraphics.class.getClassLoader().getResource("2.gif"));
        g.drawImage(image, 0, 0, image.getWidth(this),image.getHeight(this), null);
    }
}