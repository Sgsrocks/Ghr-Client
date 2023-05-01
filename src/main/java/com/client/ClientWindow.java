package com.client;

import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.net.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowListener;

import javax.swing.*;

import com.client.features.settings.InformationFile;

public class ClientWindow extends Client implements ActionListener, WindowListener {

	private static final long serialVersionUID = -6978617783576386732L;

	private InformationFile informationFile = new InformationFile();
	
	String userNameFrameTitle;
	
	public void initUI() {
		try {
			informationFile.read();
			String playerName = informationFile.getStoredUsername();
			//System.out.print("Name:" + playerName.substring(0, 1).toUpperCase() + playerName.substring(1)  + ": \n");
			if(!playerName.equalsIgnoreCase("")) {
				userNameFrameTitle = " - [" + playerName.substring(0, 1).toUpperCase() + playerName.substring(1) + "]";
			} else {
				userNameFrameTitle = "";
			}

			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			JPopupMenu.setDefaultLightWeightPopupEnabled(false);
			frame = new JFrame(Configuration.CLIENT_TITLE + userNameFrameTitle);
			frame.setLayout(new BorderLayout());
			frame.setIconImage(new ImageIcon(new URL("https://i.imgur.com/9KMJnuc.png")).getImage());
			setFocusTraversalKeysEnabled(false);
			frame.setResizable(false);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			JPanel gamePanel = new JPanel();
			gamePanel.setLayout(new BorderLayout());
			gamePanel.add(this);
			gamePanel.setPreferredSize(new Dimension(765, 503));
			frame.getContentPane().add(gamePanel, BorderLayout.CENTER);
			frame.pack();
			insets = frame.getInsets();
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getClientUserName() {
		return Client.local_player.username;
	}

	public ClientWindow(String args[]) {
		super();
		try {
			com.client.sign.Signlink.startpriv(InetAddress.getByName(server));
			initUI();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public URL getCodeBase() {
		try {
			return new URL("http://" + server + "/overlays");
		} catch (Exception e) {
			return super.getCodeBase();
		}
	}

	@Override
	public URL getDocumentBase() {
		return getCodeBase();
	}

	public void loadError(String s) {
		System.out.println("loadError: " + s);
	}

	@Override
	public String getParameter(String key) {
		return "";
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		
	}
	public static Point getStretchedMouseCoordinates(MouseEvent mouseEvent) {
		//return mouseEvent.getPoint();
		return getStretchedMouseCoordinates(mouseEvent.getPoint());
	}

	public static Point getStretchedMouseCoordinates(Point point) {
		Point2D.Double scale = RSImageProducer.getStretchScale();
		return new Point((int) (point.getX() / scale.getX()), (int) (point.getY() / scale.getY()));
	}
	private static JFrame frame;
	
	public static JFrame getFrame() {
		return frame;
	}
	
	private static Insets insets;
	
	public static Insets getInset() {
		return insets;
	}

}