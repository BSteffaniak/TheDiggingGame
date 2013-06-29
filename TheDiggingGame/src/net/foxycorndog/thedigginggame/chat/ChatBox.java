package net.foxycorndog.thedigginggame.chat;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import net.foxycorndog.jfoxylib.Color;
import net.foxycorndog.jfoxylib.Frame;
import net.foxycorndog.jfoxylib.components.Image;
import net.foxycorndog.jfoxylib.components.TextField;
import net.foxycorndog.jfoxylib.events.KeyEvent;
import net.foxycorndog.jfoxylib.events.KeyListener;
import net.foxycorndog.jfoxylib.events.MouseEvent;
import net.foxycorndog.jfoxylib.events.MouseListener;
import net.foxycorndog.jfoxylib.font.Font;
import net.foxycorndog.jfoxylib.input.Keyboard;
import net.foxycorndog.jfoxylib.input.Mouse;
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
	
	private float				scale;
	
	private	Image				historyBackground, messageBackground;
	private	Image				textFieldImage;
	
	private	TextField			textField;
	
	private	ArrayList<Message>	history;
	
	public ChatBox(final Player player, float scale)
	{
		this.scale = scale;
		
		history    = new ArrayList<Message>();
		
		BufferedImage image = new BufferedImage(1, 1, BufferedImage.BITMASK);
		
		Graphics2D    g     = image.createGraphics();
		
		g.setColor(new Color(255, 255, 255, 255));
		g.fillRect(0, 0, image.getWidth(), image.getHeight());
		
		g.dispose();
		
		Texture white   = new Texture(image);
		
		Font    font    = TheDiggingGame.getFont();
		
//		float   scale[] = GL.getAmountScaled();
//		
//		int     height  = Math.round(font.getGlyphHeight() * scale[1] * (1 / 0.8f));
		
		textFieldImage = new Image(null);
		textFieldImage.setImage(white);
		textFieldImage.setSize(Frame.getWidth() - 20, 0);
		
		textField = new TextField(null);
		textField.setFont(font);
		textField.setFontColor(new net.foxycorndog.jfoxylib.Color(255, 255, 255, 255));
		textField.setLocation(10, 10);
		textField.setCaretChar('_');
		
		historyBackground = new Image(null);
		historyBackground.setImage(white);
		historyBackground.setLocation(10, textField.getY() + textField.getHeight() + 50);
		
		messageBackground = new Image(null);
		messageBackground.setImage(white);
		
		updateSizes();
		
		close();
		
		Keyboard.addKeyListener(new KeyListener()
		{
			private	int		historyId = -1;
			
			private	String	currentText;

			public void keyDown(KeyEvent event)
			{
			}

			public void keyPressed(KeyEvent event)
			{
				int code = event.getKeyCode();
				
				if (code == Keyboard.KEY_ENTER)
				{
					String text = getText();
					
					if (text.startsWith("/"))
					{
						String response = Command.run(text.substring(1), player);
						
						if (response != null)
						{
							postMessage(text, player);
							postMessage(response, player);
						}
					}
					else
					{
						if (text.length() > 0)
						{
							postMessage(text, player);
						}
					}
					
					close();
					
					historyId = -1;
				}
				else if (code == Keyboard.KEY_ESCAPE)
				{
					close();
				}
				else if (code == Keyboard.KEY_UP)
				{
					saveCurrentText();
					
					historyId++;
					
					if (historyId > history.size() - 1)
					{
						historyId = history.size() - 1;
					}
					
					setTextToHistory(historyId);
				}
				else if (code == Keyboard.KEY_DOWN)
				{
					saveCurrentText();
					
					historyId--;
					
					if (historyId < 0)
					{
						historyId = -1;
						
						setText(currentText);
					}
					else
					{
						setTextToHistory(historyId);
					}
				}
			}

			public void keyReleased(KeyEvent event)
			{
				
			}

			public void keyTyped(KeyEvent event)
			{
				
			}
			
			private void setTextToHistory(int id)
			{
				if (historyId < 0 || historyId >= history.size())
				{
					return;
				}
				
				setText(history.get(id).message);
			}
			
			private void saveCurrentText()
			{
				if (historyId == -1)
				{
					currentText = textField.getText();
				}
			}
		});
		
		Mouse.addMouseListener(new MouseListener()
		{
			/**
			 * Make sure the TextField keeps focus even if the user clicks
			 * away while it is open.
			 * 
			 * @see net.foxycorndog.jfoxylib.events.MouseListener#mouseUp(net.foxycorndog.jfoxylib.events.MouseEvent)
			 */
			public void mouseUp(MouseEvent event)
			{
				if (open)
				{
					textField.setFocused(true);
				}
			}
			
			public void mouseReleased(MouseEvent event)
			{
				
			}
			
			public void mousePressed(MouseEvent event)
			{
				
			}
			
			public void mouseMoved(MouseEvent event)
			{
				
			}
			
			public void mouseExited(MouseEvent event)
			{
				
			}
			
			public void mouseEntered(MouseEvent event)
			{
				
			}
			
			public void mouseDown(MouseEvent event)
			{
				
			}
		});
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
	
	public void updateSizes()
	{
		textFieldImage.setSize(Frame.getWidth() - 20, Math.round((TheDiggingGame.getFont().getGlyphHeight() + 2) * scale));
		
		textField.setBackgroundImage(textFieldImage);
		textField.setTextScale(scale);
		
		historyBackground.setSize(Frame.getWidth() - 20, Frame.getHeight() - 200);
		
		messageBackground.setSize(Frame.getWidth() - 20, textFieldImage.getHeight());
	}
	
	/**
	 * Render the ChatBox at its current state. If the ChatBox is closed,
	 * it doesnt render anything but recent messages that have been said.
	 * However, if the user is typing something, it will show the
	 * TextField with the current text.
	 * 
	 * @param scale The scale in which to render the ChatBox in.
	 */
	public void render()
	{
		Font  font    = TheDiggingGame.getFont();
		
		float height  = font.getGlyphHeight() + 2;
		
		height *= scale;
		
		GL.pushMatrix();
		{
			Color color = GL.getColor();
			
			GL.translate(0, 0, 25);
			
			if (!open)
			{
				long  currentTime = System.currentTimeMillis();
				
				float alpha       = 1;
				
				GL.translate(historyBackground.getX(), historyBackground.getY(), 0);
				
				GL.beginClipping(0, 0, historyBackground.getWidth(), historyBackground.getHeight());
				{
					for (Message message : history)
					{
						long postTime = message.postTime;
						
						if (currentTime <= postTime + 11000)
						{
							if (currentTime >= postTime + 10000)
							{
								int diff = (int)(currentTime - postTime - 10000);
								
								alpha = 1 - diff / 1000f;
							}
							else
							{
								alpha = 1;
							}
							
							GL.setColor(0, 0, 0, alpha - 0.2f);
							
							messageBackground.render();
							
							
							GL.setColor(1, 1, 1, alpha);
							
							font.render(message.toString(), 2, 2, 1, scale, null);
							
							GL.translate(0, height, 0);
						}
					}
				}
				GL.endClipping();
			}
			else
			{
				GL.setColor(0, 0, 0, 0.85f);
				
				textField.render();
				
				historyBackground.render();
				
				GL.setColor(1, 1, 1, 1);
				
				GL.translate(historyBackground.getX(), historyBackground.getY(), 0);
				
				GL.beginClipping(0, 0, historyBackground.getWidth(), historyBackground.getHeight());
				{
					for (Message message : history)
					{
						font.render(message.toString(), 2, 2, 0, scale, null);
						
						GL.translate(0, height, 0);
					}
				}
				GL.endClipping();
			}
				
			GL.setColor(color);
		}
		GL.popMatrix();
	}
	
	/**
	 * Post a message with the specified sender.
	 * 
	 * @param message The message to send.
	 * @param sender The sender that wrote the message.
	 */
	public void postMessage(String message, Player sender)
	{
		Message m = new Message(message, sender);
		
		m.post();
		
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