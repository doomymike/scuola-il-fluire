/* [BatPurple.java]
 * Authors: Royi Luo, Michael Oren, Brian Zhang, Bill Liu
 * Purple Bat enemy subclass
 * Date Completed: Jan 19, 2018
 */

//imports graphics features
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

//extends Purple Bat from Ranged Enemy
public class BatPurple extends EnemyRanged {

	/**
	 * BatPurple
	 * constructor for BatPurple
	 * @param x of the bat
	 * @param y of the bat
	 * @param number of enemies killed
	 */
	BatPurple(int x, int y, int hp) {
		super(2 * (hp / 10 + 1), 14, x, y, 15, 35);
		loadSprites();
	}

	/**
	 * loadSprites
	 * loads the bat sprites
	 */
	public void loadSprites() {
		try {
			sprites = new BufferedImage[6];
			// loads the images for the enemy
			sprites[0] = ImageIO.read(new File("img/enemy/move/LuminFrame1.png"));
			sprites[1] = ImageIO.read(new File("img/enemy/move/LuminFrame2.png"));
			sprites[2] = ImageIO.read(new File("img/enemy/move/LuminFrame3.png"));
			// gets the enemy's display
			int width = sprites[0].getWidth(), height = sprites[0].getHeight();
			// reverses the images
			for (int i = 0; i < 3; ++i) {
				sprites[i + 3] = new BufferedImage(width, height, 2);
				for (int j = 0; j < width; ++j) {
					for (int k = 0; k < height; ++k) {
						sprites[i + 3].setRGB(j, k, sprites[i].getRGB(width - j - 1, k));
					}
				}
			}
			// in case image does not load
		} catch (Exception e) {
			System.out.println("error loading sheet");
		}
	}

	/**
	 * changeSprite
	 * changesSprites of the bat to fit its current direction
	 */
	protected void changeSprite() {
		if (direction.x == -1) {
			currentSprite = 3;
		} else if (direction.x == 1) {
			currentSprite = 0;
		}
	}

	/**
	 * updateSprite
	 * updates sprite to match current movement
	 */
	protected void updateSprite() {
		currentStep = (int) ((System.nanoTime() - lastChange) / 25000000) % 24;
		if (direction.x == -1) {
			currentSprite = 3 + currentStep / 8;
		} else if (direction.x == 1) {
			currentSprite = 0 + currentStep / 8;
		}
	}

	/**
	 * setDimensions
	 * sets dimensions
	 */
	public void setDimensions() {
		int pixelsPerBlock = Game.map.getPixelsPerBlock();
		dimensions = new Point(pixelsPerBlock, pixelsPerBlock);
		hitbox = new Rectangle(pos.x, pos.y, dimensions.x, dimensions.y);
	}

	/**
	 * draw
	 * draws the player
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
