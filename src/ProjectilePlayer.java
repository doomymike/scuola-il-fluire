
/* [ProjectilePlayer.java]
 * Authors: Royi Luo, Michael Oren, Brian Zhang, Bill Liu
 * subclass of projectiles fired by the player
 * Date Completed: Jan 19, 2018
 */

import java.awt.Graphics;
import java.awt.image.BufferedImage;

//subclass of projectiles fired from the player
public class ProjectilePlayer extends Projectile {
	// image to draw
	public static BufferedImage img;

	// projectile damage
	public static int projectileDamage;

	/**
	 * ProjectilePlayer
	 * constructor for projectile
	 * @param x of projectile
	 * @param y of projectile
	 * @param xDiff x difference between enemy, player
	 * @param yDiff y difference
	 * @param s speed of the projectile
	 */
	ProjectilePlayer(int x, int y, int xDiff, int yDiff, int s, int damage) {
		super(x, y, xDiff, yDiff, s);
		projectileDamage = damage;
	}

	/**
	 * update
	 * updates the location of the player, checks for collisions
	 */
	public void update() {
		if (destroyed) {
			return;
		}

		// movement of the projectile
		int timeElapsed = (int) ((System.nanoTime() - startTime) / (40000 * speed));
		pos.x = startPos.x + (int) (diff.x * timeElapsed);
		pos.y = startPos.y + (int) (diff.y * timeElapsed);
		int pixelsPerBlock = Game.map.getPixelsPerBlock();
		// checks if the projectile has hit an obstacle
		// obstacles such as water; projectiles can pass over; however, solid walls
		// block projectiles
		if (Game.map.cannotShoot(pos.y / pixelsPerBlock, pos.x / pixelsPerBlock)) {
			destroyed = true;
			return;
		}

		// checks if the projectile has hit the hitbox of enemies
		hitbox.x = pos.x;
		hitbox.y = pos.y;
		for (Enemy e : Game.enemies) {
			if (hitbox.intersects(e.getHitbox())) {
				destroyed = true;
				e.damage(projectileDamage);
				return;
			}
		}
	}

	/**
	 * draw
	 * draws projectile
	 * @param g graphics to draw to
	 */
	public void draw(Graphics g) {
		int pixelsPerBlock = Game.map.getPixelsPerBlock();
		g.drawImage(img, pos.x - Game.player.pos.x + (int) Game.screenSize.getWidth() / 2 - pixelsPerBlock,
				pos.y - Game.player.pos.y + (int) Game.screenSize.getHeight() / 2 - pixelsPerBlock, 20, 20, null);
	}
}
