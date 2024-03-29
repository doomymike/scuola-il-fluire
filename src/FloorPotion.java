
/* [FloorPotion.java]
 * Authors: Royi Luo, Michael Oren, Brian Zhang, Bill Liu
 * healing apple subclass extending potion 
 * Date Completed: Jan 19, 2018
 */

import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Point;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

//health potion subclass extending potion 
class FloorPotion extends Potion {

	// creates a hitbox
	public Rectangle hitbox;

	/**
	 * FloorPotion
	 * constructor for FloorPotion
	 * @param n name
	 * @param x of potion
	 * @param y of potion
	 * @param amount of healing
	 */
	FloorPotion(String n, int x, int y, int amount) {
		super(n, amount);
		int pixelsPerBlock = Game.map.getPixelsPerBlock();
		hitbox = new Rectangle(x, y, pixelsPerBlock, pixelsPerBlock);
	}

	/**
	 * check
	 * checks if it intersects with the player
	 * @return if it intersects
	 */
	public boolean check() {
		boolean test = false;
		// if player overlaps the item
		if (hitbox.intersects(Game.player.getHitbox())) {
			// checks all the spaces of the backpack
			for (int i = 0; i < 5; i++) {
				// if there is an empty space
				if (Game.backpack.getItem(i) == null) {
					// adds the potion to the backpack
					Game.backpack.setItem(i, new Potion(super.getName(), amount));
					test = true;
					return test;
				}
			}
		}
		return test;
	}

	/**
	 * draw
	 * draws the potion
	 * @param graphics to draw to
	 */
	public void draw(Graphics g) {
		// loads image
		BufferedImage[] image = new BufferedImage[1];
		try {
			image[0] = ImageIO.read(new File("img/misc/Shinyapple.png"));
		} catch (Exception f) {
		}

		// draws image onto game window
		int pixelsPerBlock = Game.map.getPixelsPerBlock();
		Point dimensions = new Point(pixelsPerBlock, pixelsPerBlock);
		g.drawImage(image[0], hitbox.x - Game.player.pos.x + (int) Game.screenSize.getWidth() / 2 - pixelsPerBlock,
				hitbox.y - Game.player.pos.y + (int) Game.screenSize.getHeight() / 2 - pixelsPerBlock, dimensions.x,
				dimensions.y, null);
	}
}