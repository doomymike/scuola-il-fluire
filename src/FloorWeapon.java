
/* [FloorWeapon.java]
 * Authors: Royi Luo, Michael Oren, Brian Zhang, Bill Liu
 * power apple subclass extending weapon 
 * Date Completed: Jan 19, 2018
 */

import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Point;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

//class of weapon on floor
class FloorWeapon extends Weapon {
	public Rectangle hitbox;

	/**
	 * FloorWeapon
	 * constructor for FloorWeapon
	 * @param n name
	 * @param x of the weapon
	 * @param y of the weapon
	 * @param strength
	 */
	FloorWeapon(String n, int x, int y, int strength) {
		super(n, strength);
		int pixelsPerBlock = Game.map.getPixelsPerBlock();
		hitbox = new Rectangle(x, y, pixelsPerBlock, pixelsPerBlock);
	}

	/**
	 * check
	 * checks if player has taken the weapon
	 * @return if the player has taken the weapon
	 */
	public boolean check() {
		boolean test = false;
		if (hitbox.intersects(Game.player.getHitbox())) {
			for (int i = 0; i < 5; i++) {
				if (Game.backpack.getItem(i) == null) {
					// adds to backpack
					Game.backpack.setItem(i, new Weapon(super.getName(), super.getDamage()));
					test = true;
					return test;
				}
			}
		}
		return test;
	}

	/**
	 * draw
	 * draws the weapon
	 * @param graphics to draw to
	 */
	public void draw(Graphics g) {
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