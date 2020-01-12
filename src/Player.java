import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

import javax.imageio.ImageIO;

class Player {

	public Point pos, loc;

	public int screenshake;

	public boolean weaponType;

	private Rectangle hitbox;

	private final int PROJECTILE_SPEED = 40;

	// 0 is none, 1 is right, 2 is down, 3 is left, 4 is down
	public Point direction;

	// times containing last move, attack of player
	private long lastMove, lastAttack;

	// store position of player at last change
	private Point lastTurn;

	// variable relating to sprites
	private BufferedImage[] sprites;
	private int currentSprite;

	// health of player
	private int health;

	// shows if player is performing a melee attack
	private boolean isAttacking;

	// stores dimensions of player
	private Point dimensions;

	// player weapon
	public Weapon weapon;

	// random number generator
	Random rand = new Random();

	/**
	 * Player
	 * constructor for player class
	 */
	public Player() {
		loadSprites();

		currentSprite = 0;
		direction = new Point(0, 0);

		pos = new Point();
		loc = new Point();
		lastTurn = new Point();

		weapon = new Weapon("briefcase", 1);
		health = 6;

		screenshake = 0;

		isAttacking = false;

		lastAttack = System.nanoTime() - 1000000000;
	}

	/**
	 * setDimensions
	 * sets dimensions of the player
	 */
	public void setDimensions() {
		int pixelsPerBlock = Game.map.getPixelsPerBlock() * 8 / 10;
		dimensions = new Point(pixelsPerBlock, pixelsPerBlock * 2);
		hitbox = new Rectangle(pos.x, pos.y, dimensions.x, dimensions.y);
	}

	/**
	 * getHitbox
	 * gets player hitbox
	 * @return hitbox
	 */
	public Rectangle getHitbox() {
		return hitbox;
	}

	/**
	 * getDimensions
	 * gets player Dimensions
	 * @return dimensions
	 */
	public Point getDimensions() {
		return dimensions;
	}

	/**
	 * getHealth
	 * gets health of the player
	 * @return health
	 */
	public int getHealth() {
		return health;
	}

	/**
	 * loadSprites
	 * loads the player sprites
	 */
	public void loadSprites() {
		try {
			sprites = new BufferedImage[36];

			sprites[0] = ImageIO.read(new File("img/player/move/down1.png"));
			sprites[1] = ImageIO.read(new File("img/player/move/down2.png"));
			sprites[2] = ImageIO.read(new File("img/player/move/down3.png"));
			sprites[3] = ImageIO.read(new File("img/player/move/left1.png"));
			sprites[4] = ImageIO.read(new File("img/player/move/left2.png"));
			sprites[5] = ImageIO.read(new File("img/player/move/left3.png"));
			sprites[6] = ImageIO.read(new File("img/player/move/right1.png"));
			sprites[7] = ImageIO.read(new File("img/player/move/right2.png"));
			sprites[8] = ImageIO.read(new File("img/player/move/right3.png"));
			sprites[9] = ImageIO.read(new File("img/player/move/up1.png"));
			sprites[10] = ImageIO.read(new File("img/player/move/up2.png"));
			sprites[11] = ImageIO.read(new File("img/player/move/up3.png"));

			for (int i = 1; i <= 4; ++i) {
				for (int j = 1; j <= 6; ++j) {
					sprites[11 + 6 * (i - 1) + j] = ImageIO
							.read(new File("img/player/attack/Business" + i + "AttackFrame" + j + ".png"));
				}
			}
		} catch (Exception e) {
			System.out.println("error loading sheet");
		}
	}

	/**
	 * draw
	 * draws the player
	 * @param g graphics to draw to
	 */
	public void draw(Graphics g) {
		if (currentSprite <= 11) {
			int pixelsPerBlock = Game.map.getPixelsPerBlock() * 8 / 10;
			g.drawImage(sprites[currentSprite], loc.x - (int) (pixelsPerBlock), loc.y - (int) (pixelsPerBlock),
					pixelsPerBlock, pixelsPerBlock * 2, null);
		} else {
			int pixelsPerBlock = Game.map.getPixelsPerBlock() * 8 / 10;
			g.drawImage(sprites[currentSprite], loc.x - (int) (pixelsPerBlock) * 2,
					loc.y - (int) (pixelsPerBlock * 1.6), pixelsPerBlock * 3, pixelsPerBlock * 3, null);
		}
	}

	/**
	 * shakeReset
	 * resets screen shake
	 */
	public void shakeReset() {
		for (int i = 0; i < 90; i++) {
			if (i == 89) {
				screenshake = 0;
			}
		}
	}

	/**
	 * update
	 * updates player sprite, location
	 */
	public void update() {
		// if the attacking time is up, stops animation
		if (isAttacking && System.nanoTime() - lastAttack >= 149900000) {
			if (currentSprite >= 12 && currentSprite <= 17) {
				currentSprite = 0;
			} else if (currentSprite >= 30) {
				currentSprite = 6;
			} else if (currentSprite >= 24 && currentSprite <= 29) {
				currentSprite = 3;
			} else {
				currentSprite = 9;
			}
			isAttacking = false;
		}

		// variable used to help with movement
		long timeElapsed = (System.nanoTime() - lastMove) / 3000000;

		// point the player is trying to move to
		Point toMove = new Point(pos.x, pos.y);

		// displays player sprites depending on whether or not it is attacking, and
		// direction the player is facing
		if (isAttacking) {
			int currentStep = (int) ((System.nanoTime() - lastAttack) / 25000000);
			if (currentSprite <= 2 || (currentSprite >= 12 && currentSprite <= 17)) {
				currentSprite = 12 + currentStep;
			} else if ((currentSprite >= 6 && currentSprite <= 8) || currentSprite >= 30) {
				currentSprite = 30 + currentStep;
			} else if ((currentSprite >= 3 && currentSprite <= 5) || (currentSprite >= 24 && currentSprite <= 29)) {
				currentSprite = 24 + currentStep;
			} else {
				currentSprite = 18 + currentStep;
			}
		} else {
			int currentStep = (int) (System.nanoTime() / 25000000) % 24;
			if (direction.x == -1) {
				currentSprite = 3 + currentStep / 8;
			} else if (direction.x == 1) {
				currentSprite = 6 + currentStep / 8;
			} else if (direction.y == -1) {
				currentSprite = 9 + currentStep / 8;
			} else if (direction.y == 1) {
				currentSprite = currentStep / 8;
			}
		}

		// updates player location to move
		if (direction.y == -1) {
			toMove.y = lastTurn.y - (int) timeElapsed;
		} else if (direction.y == 1) {
			toMove.y = lastTurn.y + (int) timeElapsed;
		}

		if (direction.x == -1) {
			toMove.x = lastTurn.x - (int) timeElapsed;
		} else if (direction.x == 1) {
			toMove.x = lastTurn.x + (int) timeElapsed;
		}

		// if the player can move to the location it wants to, it does
		if (canMove(toMove)) {
			pos.x = toMove.x;
			pos.y = toMove.y;
		} else if (canMove(new Point(toMove.x, pos.y))) {// otherwise, tries not moving diagonally
			pos.x = toMove.x;
			hitbox.y = pos.y;
			lastTurn.x = pos.x;
			lastTurn.y = pos.y;
			lastMove = System.nanoTime();
		} else if (canMove(new Point(pos.x, toMove.y))) {
			pos.y = toMove.y;
			hitbox.x = pos.x;
			lastTurn.x = pos.x;
			lastTurn.y = pos.y;
			lastMove = System.nanoTime();
		} else {// otherwise, the player does not move
			lastTurn.x = pos.x;
			lastTurn.y = pos.y;
			hitbox.x = pos.x;
			hitbox.y = pos.y;
			lastMove = System.nanoTime();
		}
	}

	/**
	 * canMove
	 * checks if a player can move to a location
	 * @param toMove the location the player wants to move to
	 * @return whether the player can move
	 */
	public boolean canMove(Point toMove) {
		// numbers may need to change depending on map, player dimensions
		int pixelsPerBlock = Game.map.getPixelsPerBlock();

		// checks if the player's hitbox intersects with any obstacles
		Point start = new Point(toMove.x / pixelsPerBlock, toMove.y / pixelsPerBlock);
		Point end = new Point((toMove.x + dimensions.x) / pixelsPerBlock, (toMove.y + dimensions.y) / pixelsPerBlock);

		for (int i = start.y; i <= end.y; ++i) {
			for (int j = start.x; j <= end.x; ++j) {
				if (Game.map.obstacleExists(i, j)) {
					return false;
				}
			}
		}

		hitbox.x = toMove.x;
		hitbox.y = toMove.y;

		// checks if the player's hitbox intersects with any enemies
		for (Enemy e : Game.enemies) {
			if (e.getHitbox().intersects(hitbox)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * move
	 * changes direction of the player movement
	 */
	public void move() {
		lastMove = System.nanoTime();
		lastTurn.x = pos.x;
		lastTurn.y = pos.y;

		// changes the player sprite depending on direction
		if (direction.x == 0 || direction.y == 0) {
			if (direction.x == -1) { // left
				currentSprite = 3;
			} else if (direction.y == -1) { // up
				currentSprite = 9;
			} else if (direction.x == 1) { // right
				currentSprite = 6;
			} else if (direction.y == 1) { // down
				currentSprite = 0;
			}
		} else {
			if (direction.y == -1) {// up left or up right
				currentSprite = 9;
			} else if (direction.y == 1) {// down left or down right
				currentSprite = 0;
			}
		}
	}

	/**
	 * damage
	 * damages the player
	 */
	public void damage() {
		// subtracts from health
		--health;

		// resets screen shake value
		screenshake = rand.nextInt(60) - 30;
	}

	/**
	 * heal
	 * heals the player
	 * @param strength of the potion
	 */
	public void heal(int strength) {
		health = Math.min(6, health + strength);
	}

	/**
	 * attack
	 * called when the player chooses to attack
	 */
	public void attack() {
		// if the player is making a ranged attack
		if (weaponType) {
			// if the player's attack cooldown passed, creates a projectile in the player's
			// direction
			if (System.nanoTime() - lastAttack >= 500000000) {
				if (direction.x == 0 && direction.y == 1) {
					Game.projectiles.add(new ProjectilePlayer(pos.x + dimensions.x / 2, pos.y + dimensions.y / 4, 0, 1,
							PROJECTILE_SPEED, Game.player.weapon.getDamage()));
				} else if (direction.x == 0 && direction.y == -1) {
					Game.projectiles.add(new ProjectilePlayer(pos.x + dimensions.x / 2, pos.y + dimensions.y / 4, 0, -1,
							PROJECTILE_SPEED, Game.player.weapon.getDamage()));
				} else if (direction.x == -1 && direction.y == 0) {
					Game.projectiles.add(new ProjectilePlayer(pos.x + dimensions.x / 2, pos.y + dimensions.y / 4, -1, 0,
							PROJECTILE_SPEED, Game.player.weapon.getDamage()));
				} else if (direction.x == 1 && direction.y == 0) {
					Game.projectiles.add(new ProjectilePlayer(pos.x + dimensions.x / 2, pos.y + dimensions.y / 4, 1, 0,
							PROJECTILE_SPEED, Game.player.weapon.getDamage()));
				} else if (direction.x == 1 && direction.y == 1) {
					Game.projectiles.add(new ProjectilePlayer(pos.x + dimensions.x / 2, pos.y + dimensions.y / 4, 1, 1,
							PROJECTILE_SPEED, Game.player.weapon.getDamage()));
				} else if (direction.x == 1 && direction.y == -1) {
					Game.projectiles.add(new ProjectilePlayer(pos.x + dimensions.x / 2, pos.y + dimensions.y / 4, 1, -1,
							PROJECTILE_SPEED, Game.player.weapon.getDamage()));
				} else if (direction.x == -1 && direction.y == -1) {
					Game.projectiles.add(new ProjectilePlayer(pos.x + dimensions.x / 2, pos.y + dimensions.y / 4, -1,
							-1, PROJECTILE_SPEED, Game.player.weapon.getDamage()));
				} else if (direction.x == -1 && direction.y == 1) {
					Game.projectiles.add(new ProjectilePlayer(pos.x + dimensions.x / 2, pos.y + dimensions.y / 4, -1, 1,
							PROJECTILE_SPEED, Game.player.weapon.getDamage()));
				} else {
					if (currentSprite <= 2) {
						Game.projectiles.add(new ProjectilePlayer(pos.x + dimensions.x / 2, pos.y + dimensions.y / 4, 0,
								1, PROJECTILE_SPEED, Game.player.weapon.getDamage()));
					} else if (currentSprite >= 9) {
						Game.projectiles.add(new ProjectilePlayer(pos.x + dimensions.x / 2, pos.y + dimensions.y / 4, 0,
								-1, PROJECTILE_SPEED, Game.player.weapon.getDamage()));
					} else if (currentSprite <= 5) {
						Game.projectiles.add(new ProjectilePlayer(pos.x + dimensions.x / 2, pos.y + dimensions.y / 4,
								-1, 0, PROJECTILE_SPEED, Game.player.weapon.getDamage()));
					} else {
						Game.projectiles.add(new ProjectilePlayer(pos.x + dimensions.x / 2, pos.y + dimensions.y / 4, 1,
								0, PROJECTILE_SPEED, Game.player.weapon.getDamage()));
					}
				}
				// resets last attack time
				lastAttack = System.nanoTime();
			}
		} else {// if the player is making a melee attack
			// if the player's cooldown passed
			if (System.nanoTime() - lastAttack >= 300000000) {
				//sets variable to begin attack animation
				isAttacking = true;
				
				//start position of attack box
				int y = pos.y, x = pos.x;
				int pixelsPerBlock = Game.map.getPixelsPerBlock();
				Rectangle range;

				//sets location of attack box
				if (currentSprite <= 2 || (currentSprite >= 12 && currentSprite <= 17)) {
					y += pixelsPerBlock * 2;
				} else if ((currentSprite >= 6 && currentSprite <= 8) || currentSprite >= 30) {
					x += pixelsPerBlock;
					y += pixelsPerBlock / 2;
				} else if ((currentSprite >= 3 && currentSprite <= 5) || (currentSprite >= 24 && currentSprite <= 29)) {
					x -= pixelsPerBlock / 2;
					y += pixelsPerBlock / 2;
				} else {
					y -= pixelsPerBlock / 2;
				}

				//creates attack box
				if (currentSprite <= 2 || currentSprite >= 9) {
					range = new Rectangle(x, y, pixelsPerBlock, pixelsPerBlock / 2);
				} else {
					range = new Rectangle(x, y, pixelsPerBlock / 2, pixelsPerBlock);
				}

				//if the attack box intersects with an enemy, damages it
				for (int i = 0; i < Game.enemies.size(); ++i) {
					if (Game.enemies.get(i).getHitbox().intersects(range)) {
						Game.enemies.get(i).damage(Game.player.weapon.getDamage());
					}
				}

				//resets last attack
				lastAttack = System.nanoTime();
			}
		}
	}

}