
/**
* [Paper.java]
* @author Royi Luo, Michael Oren, Brian Zhang, Bill Liu
* class for the papers
* Date completed: Jan. 19, 2018
*/

import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

class Paper {
	//paper image
	public static BufferedImage image;
	
	//paper hitbox
	private Rectangle hitbox;

	/**
	 * Paper
	 * constructor for the paper
	 * @param x of the paper
	 * @param y of the paper
	 */
	Paper(int x, int y) {
		int pixelsPerBlock = Game.map.getPixelsPerBlock();
		hitbox = new Rectangle(x, y, pixelsPerBlock, pixelsPerBlock);
	}

	/**
	 * getHitbox
	 * getter for paper hitbox
	 * @return hitbox
	 */
	public Rectangle getHitbox() {
		return hitbox;
	}

	/**
	 * draw
	 * draws the paper
	 * @param graphics to draw to
	 */
	public void draw(Graphics g) {
		int pixelsPerBlock = Game.map.getPixelsPerBlock();
		g.drawImage(image, hitbox.x - Game.player.pos.x + (int) Game.screenSize.getWidth() / 2 - pixelsPerBlock,
				hitbox.y - Game.player.pos.y + (int) Game.screenSize.getHeight() / 2 - pixelsPerBlock, pixelsPerBlock,
				pixelsPerBlock, null);
	}
}