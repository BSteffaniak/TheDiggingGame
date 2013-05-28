package net.foxycorndog.thedigginggame.launcher.menu;

import net.foxycorndog.jfoxylib.Display;
import net.foxycorndog.jfoxylib.Frame;
import net.foxycorndog.jfoxylib.components.Button;
import net.foxycorndog.jfoxylib.components.Component;
import net.foxycorndog.jfoxylib.components.Image;
import net.foxycorndog.jfoxylib.components.Panel;
import net.foxycorndog.jfoxylib.events.ButtonEvent;
import net.foxycorndog.jfoxylib.events.ButtonListener;
import net.foxycorndog.jfoxylib.font.Font;

public class VideoOptionsMenu extends Menu
{
	private Image	backgroundImage;
	
	private Button	vSyncButton, backButton;
	
	public VideoOptionsMenu(final OptionsMenu optionsMenu, Font font, Panel parent)
	{
		super(parent);
		
		ButtonListener listener = new ButtonListener()
		{
			public void buttonUnHovered(ButtonEvent event)
			{
				
			}
			
			public void buttonReleased(ButtonEvent event)
			{
				if (event.getSource() == vSyncButton)
				{
					boolean vSync = !Frame.isVSyncEnabled();
					
					Frame.setVSyncEnabled(vSync);
					
					if (vSync)
					{
						vSyncButton.setText("VSync: On");
					}
					else
					{
						vSyncButton.setText("VSync: Off");
					}
				}
				else if (event.getSource() == backButton)
				{
					optionsMenu.closeVideoOptionsMenu();
				}
			}
			
			public void buttonPressed(ButtonEvent event)
			{
				
			}
			
			public void buttonHovered(ButtonEvent event)
			{
				
			}
		};
		
		vSyncButton = new Button(this);
		vSyncButton.setImage(getLargeButtonImage());
		vSyncButton.setHoverImage(getLargeButtonHoverImage());
		vSyncButton.setFont(font);
		vSyncButton.setText(Frame.isVSyncEnabled() ? "VSync: On" : "VSync: Off");
		vSyncButton.setAlignment(Component.CENTER, Component.CENTER);
		vSyncButton.setLocation(0, -42);
		vSyncButton.addButtonListener(listener);
		
		backButton = new Button(this);
		backButton.setImage(getLargeButtonImage());
		backButton.setHoverImage(getLargeButtonHoverImage());
		backButton.setFont(font);
		backButton.setText("Back");
		backButton.setAlignment(Component.CENTER, Component.CENTER);
		backButton.setLocation(0, -77);
		backButton.addButtonListener(listener);
		
		int max = Math.max(Display.getWidth(), Display.getHeight());
		
		backgroundImage = new Image(this);
		backgroundImage.setSize(max, max);
		backgroundImage.setImage(getBackgroundImage(), 75, 75);
	}
}