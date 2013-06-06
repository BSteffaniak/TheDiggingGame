package net.foxycorndog.thedigginggame.chat;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import net.foxycorndog.jfoxylib.Frame;
import net.foxycorndog.jfoxylib.components.Image;
import net.foxycorndog.jfoxylib.components.TextField;
import net.foxycorndog.jfoxylib.font.Font;
import net.foxycorndog.jfoxylib.opengl.GL;
import net.foxycorndog.jfoxylib.opengl.bundle.Bundle;
import net.foxycorndog.jfoxylib.opengl.texture.Texture;
import net.foxycorndog.thedigginggame.TheDiggingGame;

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
	private	boolean		open;
	
	private	Image		historyBackground;
	private	Image		textFieldImage;
	
	private	TextField	textField;
	
	private	String		history[];
	
	public ChatBox()
	{
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
		textField.setFontColor(new net.foxycorndog.jfoxylib.Color(0, 0, 0, 255));
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
		GL.pushMatrix();
		{
			GL.translate(0, 0, 15);
			
			textField.render();
//			System.out.println(textField.getText());
		}
		GL.popMatrix();
	}
}