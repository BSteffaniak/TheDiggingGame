package net.foxycorndog.thedigginggame.chat;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import net.foxycorndog.jfoxylib.Frame;
import net.foxycorndog.jfoxylib.components.Image;
import net.foxycorndog.jfoxylib.components.TextField;
import net.foxycorndog.jfoxylib.font.Font;
import net.foxycorndog.jfoxylib.opengl.GL;
import net.foxycorndog.jfoxylib.opengl.bundle.Bundle;
import net.foxycorndog.jfoxylib.opengl.texture.Texture;
import net.foxycorndog.thedigginggame.TheDiggingGame;
import net.foxycorndog.thedigginggame.actor.Player;

/**
 * 
 * 
 * @author	Braden Steffaniak
 * @since	Jun 5, 2013 at 10:13:53 PM
 * @since	v0.3
 * @version	Jun 5, 2013 at 10:13:53 PM
 * @version	v0.3
 */
public class ChatBox
{
	private	boolean				open;
	
	private	Image				historyBackground;
	private	Image				textFieldImage;
	
	private	TextField			textField;
	
	private	ArrayList<Message>	history;
	
	public ChatBox()
	{
		history = new ArrayList<Message>();
		
		BufferedImage image = new BufferedImage(1, 1, BufferedImage.BITMASK);
		
		Graphics2D    g     = image.createGraphics();
		
		g.setColor(new Color(255, 255, 255, 255));
		g.fillRect(0, 0, image.getWidth(), image.getHeight());
		
		g.dispose();
		
		Texture white = new Texture(image);
		
		Font  font    = TheDiggingGame.getFont();
		
		float scale[] = GL.getAmountScaled();
		
		int height  = Math.round(font.getGlyphHeight() * scale[1] * (1 / 0.8f));
		
		historyBackground = new Image(null);
		historyBackground.setImage(white);
		historyBackground.setSize(Frame.getWidth() - 100, Frame.getHeight() - 200);
		historyBackground.setLocation(10, 100);
		
		textFieldImage = new Image(null);
		textFieldImage.setImage(white);
		textFieldImage.setSize(Frame.getWidth() - 100, height);
		
		textField = new TextField(null);
		textField.setFont(font);
		textField.setBackgroundImage(textFieldImage);
		textField.setFontColor(new net.foxycorndog.jfoxylib.Color(255, 255, 255, 255));
		textField.setLocation(10, 100 - height - 2);
		
		close();
	}
	
	/**
	 * Get the text that is currently in the TextField.
	 * 
	 * @return The text that is currently in the TextField.
	 */
	public String getText()
	{
		return textField.getText();
	}
	
	/**
	 * Set the text of the TextField in the ChatBox.
	 * 
	 * @param text The new String of text to set as the text in the
	 * 		TextField.
	 */
	public void setText(String text)
	{
		textField.setText(text);
	}
	
	/**
	 * Get whether or not the ChatBox is currently open.
	 * 
	 * @return Whether or not the ChatBox is currently open.
	 */
	public boolean isOpen()
	{
		return open;
	}
	
	/**
	 * Open the ChatBox including the extended history.
	 */
	public void open()
	{
		textField.setFocused(true);
		textField.setEnabled(true);
		textField.setVisible(true);
		
		open = true;
	}
	
	/**
	 * Close the ChatBox including the extended history.
	 */
	public void close()
	{
		textField.setText("");
		textField.setFocused(false);
		textField.setEnabled(false);
		textField.setVisible(false);
		
		open = false;
	}
	
	/**
	 * Render the ChatBox at its current state. If the ChatBox is closed,
	 * it doesnt render anything but recent messages that have been said.
	 * However, if the user is typing something, it will show the
	 * TextField with the current text.
	 */
	public void render()
	{
		if (!open)
		{
			return;
		}
		
		GL.pushMatrix();
		{
			float color[] = GL.getColor();
			
			GL.translate(0, 0, 15);
			
			GL.setColor(0, 0, 0, 0.25f);
			
			textField.render();
//			System.out.println(textField.getText());
			
			historyBackground.render();
			
			GL.setColor(1, 1, 1, 1);
			
			Font font     = TheDiggingGame.getFont();
			
			float height  = font.getGlyphHeight() + 1;
			float yOffset = 0;
			
			for (Message message : history)
			{
				font.render(message.toString(), historyBackground.getX(), historyBackground.getY() + yOffset, 0, null);
				
				yOffset += height;
			}
			
			GL.setColor(color);
		}
		GL.popMatrix();
	}
	
	public void postMessage(String message, Player sender)
	{
		Message m = new Message(message, sender);
		
		history.add(0, m);
	}
	
	/**
	 * Class used to keep track of a Message. Each message has a Player
	 * instance for a sender, a String instance for a message, and a
	 * postTime to keep track of how long the Message has been posted.
	 * 
	 * @author	Braden Steffaniak
	 * @since	Jun 6, 2013 at 10:34:12 AM
	 * @since	v0.3
	 * @version	Jun 6, 2013 at 10:34:12 AM
	 * @version	v0.3
	 */
	private class Message
	{
		private	long	postTime;
		
		private	String	message;
		
		private	Player	sender;
		
		/**
		 * Create a message with the specified sender.
		 * 
		 * @param message The message to send.
		 * @param sender The sender that wrote the message.
		 */
		public Message(String message, Player sender)
		{
			this.message = message;
			
			this.sender  = sender;
		}
		
		/**
		 * Post the message to the ChatBox.
		 */
		public void post()
		{
			postTime = System.currentTimeMillis();
		}
		
		/**
		 * Create a String that represents the Message instance.
		 * 
		 * @see java.lang.Object#toString()
		 * 
		 * @return A String that represents the Message instance.
		 */
		public String toString()
		{
			return message;
		}
	}
}