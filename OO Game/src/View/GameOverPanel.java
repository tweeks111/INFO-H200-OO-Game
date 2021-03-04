package View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class GameOverPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BufferedImage gameOver;
	private BufferedImage gameOver_newScore;
	private int score;
	private int bestScore;
	private int secondBestScore;
	private boolean newBestScore;
	
	
	
	public GameOverPanel(int score, int bestScore, int secondBestScore,boolean newBestScore){
		
		this.score=score;
		this.bestScore=bestScore;
		this.secondBestScore=secondBestScore;
		this.newBestScore=newBestScore;
		
		Content.loadGameOver();
		
		gameOver= Content.gameOver[0][0];
		gameOver_newScore = Content.gameOver[0][1];
		
		this.setFocusable(true);
		this.requestFocusInWindow();
		setPreferredSize(new Dimension(350,200));
		setOpaque(false);
	
		
	}
	public void paintComponent(Graphics g){
	
		if(newBestScore) g.drawImage(gameOver_newScore,0,0,null);
		else g.drawImage(gameOver,0,0,null);
		g.setColor(Color.WHITE);
		Font font =new Font("Arial",Font.BOLD,20);
		g.setFont(font);
		g.drawString(String.valueOf(score), 200, 120);
		font =new Font("Arial",Font.BOLD,14);
		g.setFont(font);
		g.drawString(String.valueOf(bestScore), 200, 154);
		g.drawString(String.valueOf(secondBestScore), 200, 167);

	}
}
