package View;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.*;



public class LauncherPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BufferedImage background;

	
	public LauncherPanel(){
		Content.loadBackground();
		background = Content.background[0][0];
		
		setPreferredSize(new Dimension(800,500));
		setOpaque(false);
	
		
	}
	public void paintComponent(Graphics g){
		g.drawImage(background,0,0,null);
	}
	

	
}
