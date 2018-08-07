package game.input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Mouse implements MouseListener, MouseMotionListener
{

	/*
	 * Class variables
	 */
	private static boolean buttons[] = new boolean[5];
	private static int x, y;

	/*
	 * Class methods
	 */
	// Private constructor to prevent instantiation
	private Mouse()
	{
	}

	public static int getX()
	{
		return x;
	}

	public static int getY()
	{
		return y;
	}

	public static boolean isButtonDown(int button)
	{
		if (button >= 0 && button < buttons.length)
		{
			return buttons[button];
		}
		return false;
	}

	/*
	 * Instance
	 */
	private static Mouse instance = null;

	public static Mouse instance()
	{
		if (instance == null)
			instance = new Mouse();
		return instance;
	}

	/*
	 * Listeners
	 */
	@Override
	public void mouseDragged(MouseEvent e)
	{
		buttons[e.getButton()] = true;
		x = e.getX();
		y = e.getY();
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		x = e.getX();
		y = e.getY();
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		int button = e.getButton();
		if (button >= 0 && button < buttons.length)
		{
			buttons[button] = true;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		int button = e.getButton();
		if (button >= 0 && button < buttons.length)
		{
			buttons[button] = false;
		}
	}

}
