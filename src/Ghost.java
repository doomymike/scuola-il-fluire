
/* [Ghost.java]
 * Authors: Royi Luo, Michael Oren, Brian Zhang, Bill Liu
 * ghost enemy subclass
 * Date Completed: Jan 19, 2018
 */

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

//ghost enemy subclass
public class Ghost extends EnemyRanged {

	/**
	 * Ghost
	 * constructor for ghost
	 * @param x of ghost
	 * @param y of ghost
	 * @param hp number of enemies killed
	 */
	Ghost(int x, int y, int hp) {
		super(3 * (hp / 10 + 1), 10, x, y, 20, 60);
		loadSprites();
	}

	/**
	 * loadSprites
	 * loads the ghost's sprites
	 */
	protected void loadSprites() {
		try {
			sprites = new BufferedImage[1];
			sprites[0] = ImageIO.read(new File("img/enemy/move/GostFrame1.png"));
		} catch (Exception e) {
			System.out.println("error loading sheet");
		}
	}

	/**
	 * changeSprite
	 * sets the ghost's sprite
	 */
	protected void changeSprite() {
		currentSprite = 0;
	}

	/**
	 * updateSprite
	 * sets the ghost's sprite
	 */
	protected void updateSprite() {
		currentSprite = 0;
	}

	/**
	 * setDimensions
	 * sets the ghost's dimensions
	 */
	public void setDimensions() {
		int pixelsPerBlock = Game.map.getPixelsPerBlock();
		dimensions = new Point(pixelsPerBlock, pixelsPerBlock);
		hitbox = new Rectangle(pos.x, pos.y, dimensions.x, dimensions.y);
	}

	/**
	 * draw
	 * draws the ghost
	 * @param graphics to draw to
	 */
	public void draw(Graphics g) {
		int pixelsPerBlock = Game.map.getPixelsPerBlock();
		g.drawImage(sprites[currentSprite],
				pos.x - Game.player.pos.x + (int) Game.screenSize.getWidth() / 2 - pixelsPerBlock,
				pos.y - Game.player.pos.y + (int) Game.screenSize.getHeight() / 2 - pixelsPerBlock, dimensions.x,
				dimensions.y, null);
	}
}
