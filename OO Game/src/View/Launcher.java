package View;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import Controller.Keyboard;
import Model.Game;

public class Launcher{
	JFrame launcher;
	LauncherPanel panel = new LauncherPanel();
	JPanel startPanel = new JPanel();
	JButton startButton;
	
	JComboBox<String> mapList;
	int mapSize=11;
	
	public Launcher() {
		launcher = new JFrame("Buried Launcher");
		startPanel.setPreferredSize(new Dimension(800,500));
		
		launcher.setLayout(new BorderLayout());
		startButton = new JButton("Start Game");
		JPanel invisiblePanel = new JPanel();
		invisiblePanel.setOpaque(false);
		invisiblePanel.setPreferredSize(new Dimension(800,400));
		JPanel invisiblePanel2 = new JPanel();
		invisiblePanel2.setOpaque(false);
		invisiblePanel2.setPreferredSize(new Dimension(300,10));
		
		startButton.addActionListener(new StartButtonListener());
		
		JLabel mapSize = new JLabel("Map Size : ");
		mapSize.setForeground(Color.WHITE);
		
		startPanel.add(invisiblePanel2);
		startPanel.add(mapSize,BorderLayout.LINE_START);
		
		mapList = new JComboBox<String>();
		mapList.setPreferredSize(new Dimension(100,40));

		mapList.addItem("Small");mapList.addItem("Medium");mapList.addItem("Huge");

		startPanel.add(mapList,BorderLayout.CENTER);
		startPanel.add(startButton,BorderLayout.LINE_END);
		startPanel.setOpaque(false);
		mapList.addActionListener(new MapSizeListener());
		
		
		launcher.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		launcher.setResizable(false);
		launcher.setLocation(0,0);
		launcher.setTitle("Launcher");
		
	
		panel.setPreferredSize(new Dimension(800,500));
		
		panel.add(invisiblePanel,BorderLayout.NORTH);
		panel.add(startPanel, BorderLayout.SOUTH);
		launcher.add(panel);
		
		startButton.setPreferredSize(new Dimension(110,40));
		launcher.setVisible(true);
		launcher.pack();
	}
	

	class MapSizeListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(mapList.getSelectedItem()=="Small")mapSize=11;
			if(mapList.getSelectedItem()=="Medium") mapSize=15;
			if(mapList.getSelectedItem()=="Huge") mapSize=19;
		}

	}
	
	class StartButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			launcher.dispose();
				
				Window window = new Window(mapSize);
				Game game = new Game(window,mapSize);
				Keyboard keyboard = new Keyboard(game);
				window.setKeyListener(keyboard);
			
		}
	}
}