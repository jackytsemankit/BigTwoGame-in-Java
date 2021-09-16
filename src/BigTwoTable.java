
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;

import javax.swing.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;


/**
 * The BigTwoTable class implements the CardGameTable interface. It is used to build a GUI
 * for the Big Two card game and handle all user actions. Below is a detailed description for the
 * BigTwoTable class.
 * 
 * @author Tse Man Kit
 *
 */
public class BigTwoTable implements CardGameTable {
	
	/**
	 * a card game associates with this table.
	 */
	private CardGame game =null;
	/**
	 * an arraylist that stores the four players
	 */
	private ArrayList<CardGamePlayer> playerList = null;
	
	/**
	 * a boolean array indicating which cards are being selected.
	 */
	private boolean[] selected = null;
	
	/**
	 * an integer specifying the index of the active player.
	 */
	private int activePlayer = -1;
	/**
	 * the main window of the application.
	 */
	private JFrame frame = null;
	/**
	 * a panel for showing the cards of each player and the cards
	 * played on the table.
	 */
	private JPanel bigTwoPanel = null;
	/**
	 * a “Play” button for the active player to play the selected cards.
	 */
	private JButton playButton = null;
	/**
	 * a “Pass” button for the active player to pass his/her turn to the
	 * next player.
	 */
	private JButton passButton = null;
	/**
	 * a text area for showing the current game status as well as end of
	 * game messages.
	 */
	private JTextArea msgArea = null;
	
	/**
	 * a JPanel containing the chatbox and the chat messages
	 */
	private JPanel chatboxPanel = null;
	
	/**
	 * a JTextField for players to enter their messages
	 */
	private JTextField chatbox = null;
	
	/**
	 * a JTextArea for showing the messages from all players
	 */
	private JTextArea chatMsgArea = null;

	/**
	 * a menu that contains the quit and restart button
	 */
	private JMenu menu = null;
	/**
	 * a 2D array storing the images for the faces of the cards.
	 */
	private Image[][] cardImages = null;
	/**
	 * an image for the backs of the cards.
	 */
	private Image cardBackImage = null;
	/**
	 * an array storing the images for the avatars.
	 */
	private Image[] avatars = null;
	/**
	 * an int storing the width of a card
	 */
	private int cardWidth = 0;
	/**
	 * an int storing the height of a card
	 */
	private int cardHeight = 0;
	/**
	 * an int storing the width that each overlapping card shows
	 */
	private int deltaCardWidth = 0;
	/**
	 * an int storing the change of card height when a card is selected
	 */
	private int deltaCardHeight = 0;
	/**
	 * an int storing the height of a player's icon
	 */
	private int iconHeight = 0;
	/**
	 * an int storing the weight of a player's icon
	 */
	private int iconWidth = 0;
	
	/**
	 * a JMenuItem for the "Connect" button
	 */
	private JMenuItem connect = null;
 	
	
	/**
	 * a constructor for creating a BigTwoTable. The
	 * parameter game is a reference to a card game associates with this table.
	 * @param game
	 */
	public BigTwoTable (CardGame game) {
		
		this.game=game;
		this.selected = new boolean[13];
		playerList = game.getPlayerList();
		
		frame = new JFrame();
		bigTwoPanel = new BigTwoPanel();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();

		
		cardImages = new Image[4][13];
		char[] suits = {'d', 'c', 'h', 's'};
		char[] ranks = {'a','2','3','4','5','6','7','8','9','0','j','q','k'};
		
		//initiate images and self declared variables
		for (int i=0;i<4;i++) {
			for (int j=0;j<13;j++) {
				cardImages[i][j]=new ImageIcon("images/"+ranks[j]+suits[i]+".gif").getImage();
				}
			}
	
		cardBackImage = new ImageIcon("images/b.gif").getImage();
		cardWidth = cardImages[0][0].getWidth(null);
		cardHeight = cardImages[0][0].getHeight(null);
		deltaCardWidth = (int) Math.round(cardWidth/5.0);
		deltaCardHeight = (int) Math.round(cardHeight/5.0);
		
		iconHeight = (int) (cardHeight*1.3);
		iconWidth = (int) Math.round(iconHeight*0.9);
		
		//setting up the main frame
		frame = new JFrame("Big Two");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1200,1000);
		frame.setResizable(false);
		
		bigTwoPanel.setLayout(new BorderLayout());
		
		
		JPanel messagePanel = new JPanel();
		messagePanel.setLayout(new BorderLayout());
		msgArea = new JTextArea("");
		msgArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(msgArea,ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);;
		scrollPane.setViewportView(msgArea);
		scrollPane.setPreferredSize(new Dimension(400,470));
		scrollPane.setVisible(true);
		chatboxPanel = new JPanel();
		chatbox = new JTextField("");
		chatbox.addKeyListener(new KeyAdapter() {
	        @Override
	        public void keyPressed(KeyEvent e) {
	            if(e.getKeyCode() == KeyEvent.VK_ENTER){
	               String message = chatbox.getText();
	               CardGameMessage msg=new CardGameMessage(7,game.getCurrentIdx(),message);
	               BigTwoClient btc= (BigTwoClient) game;
	               btc.sendMessage(msg);
	               chatbox.setText("");
	            }
	        }
		});
		chatMsgArea = new JTextArea("");
		chatMsgArea.setEditable(false);
		JScrollPane messagescroll= new JScrollPane(chatMsgArea,ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		JLabel messageLabel= new JLabel("Message:");
		
		messagescroll.setVisible(true);
		messagescroll.setViewportView(chatMsgArea);
		chatboxPanel.add(messageLabel);
		chatboxPanel.add(chatbox);
		chatbox.setVisible(true);
		chatbox.setPreferredSize(new Dimension(350,29));
		
		messagePanel.add(scrollPane,BorderLayout.NORTH);
		messagePanel.add(messagescroll,BorderLayout.CENTER);
		messagePanel.add(chatboxPanel,BorderLayout.SOUTH);
		frame.add(messagePanel,BorderLayout.EAST);
		
		JMenuBar menuBar = new JMenuBar();
		menu = new JMenu("Game");
		menuBar.add(menu);
		
		connect = new JMenuItem("Connect");
		//JMenuItem menuItem0 = new JMenuItem("Restart");
		//menuItem0.addActionListener(new RestartMenuItemListener());
		connect.addActionListener(new ConnectMenuItemListener());
		JMenuItem menuItem1 = new JMenuItem("Quit");
		menuItem1.addActionListener(new QuitMenuItemListener());
		menu.add(menuItem1);
		menu.add(connect);
		frame.add(menuBar,BorderLayout.NORTH);
		
		//setting up the buttons
		playButton = new JButton("Play");
		playButton.setPreferredSize(new Dimension(80,30));
		playButton.addActionListener(new PlayButtonListener());
		
		passButton = new JButton("Pass");
		passButton.setPreferredSize(new Dimension(80,30));
		passButton.addActionListener(new PassButtonListener());
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(passButton);
		buttonPanel.add(playButton);
		
		
		bigTwoPanel.add(buttonPanel,BorderLayout.SOUTH);
		
		frame.add(bigTwoPanel);
		frame.setVisible(true);
		
	}
	
	/**
	 * an inner class that extends the JPanel class and implements the
	 * MouseListener interface. Overrides the paintComponent() method inherited from the
	 * JPanel class to draw the card game table. Implements the mouseClicked() method from
	 * the MouseListener interface to handle mouse click events.
	 * @author Tse Man Kit
	 *
	 */
	private class BigTwoPanel extends JPanel implements MouseListener {
	
		/**
		 * auto-generated serial version UID
		 */
		private static final long serialVersionUID = 8659972583864985476L;

		public BigTwoPanel() {
			setPreferredSize(new Dimension(1200,1200));
			setBackground(new Color(30,160,100));
			addMouseListener(this);
		}
		
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			avatars = new Image[4];
			
			for (int i=0;i<4;i++) {
				avatars[i] = new ImageIcon("images/icon"+i+".png").getImage();
				
			}
			
			Dimension d = this.getSize();
	        //draw 4 horizontal lines to divide the bitTwoPanel into 5 horizontal sections
	        g.drawLine(0,190,d.width,190);
	        g.drawLine(0,370,d.width,370);	        
	        g.drawLine(0,550,d.width,550);
	        g.drawLine(0,730,d.width,730);
	        
	        //drawing the four players and their cards
	        for (int i=0;i<game.getPlayerList().size();i++) {
	        	
	        	
	        	if (i==game.getCurrentIdx()) {
	        		CardList list = new CardList();
		        	list = game.getPlayerList().get(i).getCardsInHand();
	        		g.setColor(Color.BLUE);
	        		g.drawString(playerList.get(i).getName(), 10, i*180+35);
	        		g.drawImage(avatars[i], 10, i*180+40,iconWidth,iconHeight, this);
	        		
	        	
	        		
	        		if (i==activePlayer) {
	        			for (int j=0;j<list.size();j++) {
	        				int s = list.getCard(j).getSuit();
	        				int r = list.getCard(j).getRank();
	        				if (selected[j]==true) {
	        					g.drawImage(cardImages[s][r], 140+j*deltaCardWidth, (i*180)+20, this);
	        					
	        				}
	        				else {
	        					g.drawImage(cardImages[s][r], 140+j*deltaCardWidth, (i*180)+40, this);
	        				}
	        			}
	        		}
	        		else {
	        			for (int j=0;j<list.size();j++) {
	        				g.drawImage(cardBackImage, 140+j*deltaCardWidth, 40+i*180,this);
	        			}
	        		}
	        	}
	        	else
	        	 {
	        		 g.setColor(Color.BLACK);
	        		 g.drawString(game.getPlayerList().get(i).getName(), 10, i*180+35);
	        		 g.drawImage(avatars[i], 10, i*180+40,iconWidth,iconHeight, this);
	        		 CardList list= new CardList();
	        		 list=game.getPlayerList().get(i).getCardsInHand();
	        		 if(i==activePlayer){
	        			 for(int j=0;j<list.size();j++) {
	        			 	int r= list.getCard(j).getRank();
	        			 	int s= list.getCard(j).getSuit();
	        			 	if(!selected[j])
	        				 	g.drawImage(cardImages[s][r], 140+j*deltaCardWidth, (i*180)+40, this);
	        			 	else
	        			 		g.drawImage(cardImages[s][r], 140+j*deltaCardWidth, (i*180)+20, this);
	        		 	}
	        		 }
	        		 else
	        			 for(int j=0;j<list.size();j++) {
	        			 g.drawImage(cardBackImage, 140+j*deltaCardWidth, (i*180)+40, this);
	        			 }
	        	 }
	        }
	        
	        if (game.getHandsOnTable().size()!=0) {
	        	Hand lastHandOnTable=game.getHandsOnTable().get(game.getHandsOnTable().size()-1);
	        
	        	g.drawString("Played by "+lastHandOnTable.getPlayer().getName(), 10, 4*180+35);
	        	for (int i=0;i<lastHandOnTable.size();i++) {
	        		int s = lastHandOnTable.getCard(i).getSuit();
    				int r = lastHandOnTable.getCard(i).getRank();
    				g.drawImage(cardImages[s][r],140+i*deltaCardWidth,40+4*180,this);
	        	}
	        }
		}
		
		/**
		 * a method that handles mouse click events
		 */
		@Override
		public void mouseClicked(MouseEvent e) {
			
			int x=e.getX();
			int y=e.getY(); 
			int i;
			
			if(!game.endOfGame()) {
			for(i=0;i<game.getPlayerList().get(activePlayer).getCardsInHand().size()-1;i++)
			{
				if(!selected[i]&&x>=140+i*deltaCardWidth&&x<140+(i+1)*deltaCardWidth&&y>=(activePlayer*180)+40&&y<=(activePlayer*180)+40+cardHeight) {
					selected[i]=true;
				}
				else if(selected[i]&&x>=140+i*deltaCardWidth&&x<140+(i+1)*deltaCardWidth&&y>=(activePlayer*180)+20&&y<=(activePlayer*180)+20+cardHeight) {
					selected[i]=false;
				}
				//below is for clicking under selected card cases	
				
				
				if (!selected[i]&&selected[i+1]&&x>=140+(i+1)*deltaCardWidth&& x<=140+(i+2)*deltaCardWidth&&y>=activePlayer*180+40+cardHeight-deltaCardHeight&&y<=(activePlayer*180)+40+cardHeight){
					selected[i]=true;

				}
				if (i<game.getPlayerList().get(activePlayer).getCardsInHand().size()-2) {
					if (!selected[i]&&selected[i+1]&&selected[i+2]&&x>=140+(i+2)*deltaCardWidth&& x<=140+(i+3)*deltaCardWidth&&y>=activePlayer*180+40+cardHeight-deltaCardHeight&&y<=(activePlayer*180)+40+cardHeight){
						selected[i]=true;

					}
				}
				
				if (i<game.getPlayerList().get(activePlayer).getCardsInHand().size()-3) {
					if (!selected[i]&&selected[i+1]&&selected[i+2]&&selected[i+3]&&x>=140+(i+3)*deltaCardWidth&& x<=140+(i+4)*deltaCardWidth&&y>=activePlayer*180+40+cardHeight-deltaCardHeight&&y<=(activePlayer*180)+40+cardHeight){
						selected[i]=true;

					}
				}
				
				if (i<game.getPlayerList().get(activePlayer).getCardsInHand().size()-4) {
					if (!selected[i]&&selected[i+1]&&selected[i+2]&&selected[i+3]&&selected[i+4]&&x>=140+(i)*deltaCardWidth&& x<=140+(i)*deltaCardWidth+cardWidth&&y>=activePlayer*180+40+cardHeight-deltaCardHeight&&y<=(activePlayer*180)+40+cardHeight){
						selected[i]=true;

					}
				}
				
				if (i==game.getPlayerList().get(activePlayer).getCardsInHand().size()-2) {
					if (!selected[i]&&selected[i+1]&&x>=140+(i)*deltaCardWidth&& x<=140+(i)*deltaCardWidth+cardWidth&&y>=activePlayer*180+40+cardHeight-deltaCardHeight&&y<=(activePlayer*180)+40+cardHeight){
						selected[i]=true;

					}
				}
				if (i==game.getPlayerList().get(activePlayer).getCardsInHand().size()-3) {
					if (!selected[i]&&selected[i+1]&&selected[i+2]&&x>=140+(i)*deltaCardWidth&& x<=140+(i)*deltaCardWidth+cardWidth&&y>=activePlayer*180+40+cardHeight-deltaCardHeight&&y<=(activePlayer*180)+40+cardHeight){
						selected[i]=true;

					}
				}
				
				if (i==game.getPlayerList().get(activePlayer).getCardsInHand().size()-4) {
					if (!selected[i]&&selected[i+1]&&selected[i+2]&&selected[i+3]&&x>=140+(i)*deltaCardWidth&& x<=140+(i)*deltaCardWidth+cardWidth&&y>=activePlayer*180+40+cardHeight-deltaCardHeight&&y<=(activePlayer*180)+40+cardHeight){
						selected[i]=true;

					}
				}
				
			}
			
			
			int size=game.getPlayerList().get(activePlayer).getCardsInHand().size()-1;
			
			if(!selected[size]&&x>=140+(size)*deltaCardWidth&&x<210+size*deltaCardWidth&&y>=(activePlayer*180)+40&&y<=(activePlayer*180)+40+95) {
				selected[size]=true;
			}
			else if(selected[size]&&x>=140+size*deltaCardWidth&&x<210+size*deltaCardWidth&&y>=(activePlayer*180)+20&&y<=(activePlayer*180)+20+95) {
				selected[size]=false;
			}
			
			
			repaint();	
			}	
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
	}
	
	/**
	 * an inner class that implements the ActionListener
	 * interface. Implements the actionPerformed() method from the ActionListener interface
	 * to handle button-click events for the “Play” button. When the “Play” button is clicked,
	 * you should call the makeMove() method of your CardGame object to make a move.
	 * @author Tse Man Kit
	 *
	 */
	private class PlayButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			if (game.getCurrentIdx()==activePlayer) {
				if (getSelected()!=null) {
					game.makeMove(activePlayer, getSelected());
					resetSelected();
					repaint();
					playSound("playcard.wav");
				}
				else {
					printMsg("No Cards Selected\n");
					playSound("error.wav");
				}
			}
		}
		
	}
	
	/**
	 * an inner class that implements the ActionListener
	 * interface. Implements the actionPerformed() method from the ActionListener interface
	 * to handle button-click events for the “Pass” button. When the “Pass” button is clicked,
	 * you should call the makeMove() method of your CardGame object to make a move.
	 * @author Tse Man Kit
	 *
	 */
	private class PassButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if (game.getCurrentIdx()==activePlayer) {
				game.makeMove(activePlayer, null);
				resetSelected();
				repaint();
				playSound("pass.wav");
			}
		}
		
	}
	
	/**
	 * an inner class that implements the ActionListener
	 * interface. Implements the actionPerformed() method from the ActionListener interface
	 * to handle menu-item-click events for the “Connect” menu item.
	 * @author Tse Man Kit
	 *
	 */
	private class ConnectMenuItemListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			//if (enable()) {
			//	playSound("cardshuffle.wav");
			//}
//			resetSelected();
//			clearMsgArea();
//			BigTwoDeck bigTwoDeck = new BigTwoDeck();
//			bigTwoDeck.shuffle();
//			game.start(bigTwoDeck);
//			repaint();
			BigTwoClient client = (BigTwoClient)game;
			client.makeConnection();
			repaint();
		}
		
	}
	
	/**
	 * an inner class that implements the ActionListener
	 * interface. Implements the actionPerformed() method from the ActionListener interface
	 * to handle menu-item-click events for the “Quit” menu item. When the “Quit” menu
	 * item is selected, you should terminate your application.
	 * @author Tse Man Kit
	 *
	 */
	private class QuitMenuItemListener implements ActionListener{

		BigTwoClient client = (BigTwoClient)game;
		public void actionPerformed(ActionEvent e) {
			client.quit();
			System.exit(0);
		}
	}
		
	
	/**
	 * a method for setting the index of the active player (i.e., the current player).
	 */
	@Override
	public void setActivePlayer(int activePlayer) {
		// TODO Auto-generated method stub
		if (activePlayer <0 || activePlayer >= playerList.size()) {
			this.activePlayer=-1;
		
		}
		else{
			this.activePlayer=activePlayer;
	
		}
		
	}
	
	/**
	 * a method for getting an array of indices of the cards selected.
	 */
	@Override
	public int[] getSelected() {
		// TODO Auto-generated method stub
		
		ArrayList<Integer> x = new ArrayList<Integer>();
		for (int i=0;i<this.selected.length;i++) {
			if (this.selected[i]==true) {
				x.add(i);
			}
		}
		if (x.size()==0) {
			return null;
		}
		int[] selectedCardIndexes = new int[x.size()];
		for (int i=0;i<x.size();i++) {
			selectedCardIndexes[i]=x.get(i);
		}
		return selectedCardIndexes;
	}
	
	/**
	 * a method for resetting the list of selected cards.
	 */
	@Override
	public void resetSelected() {
		// TODO Auto-generated method stub
		this.selected= new boolean[13];
	}
	/**
	 * a method for repainting the GUI.
	 */
	@Override
	public void repaint() {
		// TODO Auto-generated method stub
		frame.repaint();		
	}
	/**
	 * a method for printing the specified string to the message
	 * area of the GUI.
	 */
	@Override
	public void printMsg(String msg) {
		// TODO Auto-generated method stub
		msgArea.append(msg);
		msgArea.setCaretPosition(msgArea.getDocument().getLength());
	}
	/**
	 * a method for clearing the message area of the GUI.
	 */
	@Override
	public void clearMsgArea() {
		// TODO Auto-generated method stub
		msgArea.setText("");
		
	}
	/**
	 * a method for resetting the GUI. You should (i) reset the list of selected
	 * cards using resetSelected() method from the CardGameTable interface; (ii) clear the
	 * message area using the clearMsgArea() method from the CardGameTable interface;
	 * and (iii) enable user interactions using the enable() method from the CardGameTable
	 * interface.
	 */
	@Override
	public void reset() {
		// TODO Auto-generated method stub
		this.resetSelected();
		this.clearMsgArea();
		this.enable();		
	}
	/**
	 * a method for enabling user interactions with the GUI. You should (i)
	 * enable the “Play” button and “Pass” button (i.e., making them clickable); and (ii)
	 * enable the BigTwoPanel for selection of cards through mouse clicks.
	 */
	@Override
	public void enable() {
		// TODO Auto-generated method stub
		this.playButton.setEnabled(true);
		this.passButton.setEnabled(true);

	}
	/**
	 * a method for disabling user interactions with the GUI. You should (i)
	 * disable the “Play” button and “Pass” button (i.e., making them not clickable); and (ii)
	 * disable the BigTwoPanel for selection of cards through mouse clicks.
	 */
	@Override
	public void disable() {
		// TODO Auto-generated method stub
		this.playButton.setEnabled(false);
		this.passButton.setEnabled(false);

	}
	/**
	 * a method that facilitate the playing of sound effects
	 * @param soundName
	 */
	public void playSound(String soundName)
	 {
	   try 
	   {
	    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("se/"+soundName).getAbsoluteFile( ));
	    Clip clip = AudioSystem.getClip( );
	    clip.open(audioInputStream);
	    clip.start( );
	   }
	   catch(Exception ex)
	   {
	     System.out.println("Error with playing sound.");
	     ex.printStackTrace( );
	   }
	 }
	
	/**
	 * a method for enabling connection
	 */
	void enableConnect() {
		connect.setEnabled(true);
		
	}
	
	/**
	 * a method for disabling connection
	 */
	void disableConnect() {
		connect.setEnabled(false);
	}
	
	
	/**
	 * a method for adding messages to the chat message area
	 * @param msg: messages from players
	 */
	public void addTochatMsgArea(String msg) {
		chatMsgArea.append(msg);
	}

	

}
