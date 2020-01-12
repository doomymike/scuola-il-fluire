
/* [GameWindow.java]
 * Authors: Royi Luo, Michael Oren, Brian Zhang, Bill Liu
 * game window subclass to open a window for the game
 * Date Completed: Jan 19, 2018
 */

import java.awt.Graphics;

import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.IOException;

import java.awt.Color;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

//class for game window
class GameWindow extends JFrame {

	public static long lastSpawn;
	public static int spawn;
	public static Random rng = new Random();
	public static int waveNumber = 0;

	/**
	 * GameWindow
	 * constructor for GameWindow
	 */
	public GameWindow() {
		spawn = 0;
		lastSpawn = System.nanoTime();
		setResizable(true);
		setTitle("Scuola Il Fluire");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().add(new GamePanel());
		pack();
		setVisible(true);
	}

	/**
	 * GamePanel
	 * @author Royi Luo, Michael Oren, Brian Zhang, Bill Liu
	 * inner class for GamePanel of GameWindow
	 * Date completed: Jan. 19, 2018
	 */
	static class GamePanel extends JPanel implements KeyListener {

		// images for displaying
		private BufferedImage[] healthSprites;
		BufferedImage playerShine;

		// whether the game has started
		private boolean startGame = false;

		// whether an ending screen is being displayed
		private boolean prependGame = false;

		// beginning screen the player is at
		private int advanceCount = 0;

		private int killCount = 0;

		// number of papers left
		private int paperCount = 8;

		// images for displaying to the user
		private Image image, image1, image2, image3, image4, image5, image6;

		/**
		 * GamePanel
		 * constructor for GamePanel
		 */
		public GamePanel() {
			Game.screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			setPreferredSize(Game.screenSize);
			addKeyListener(this);
			setFocusable(true);
			requestFocusInWindow();
			setBackground(Color.BLACK);

			// loads images into the game
			try {
				playerShine = ImageIO.read(new File("img/misc/PlayerShine.png"));
			} catch (IOException e) {
				System.out.println("Check");
			}

			image = Toolkit.getDefaultToolkit().createImage("img/misc/source.gif");
			image1 = Toolkit.getDefaultToolkit().createImage("img/misc/triresource.gif");
			image2 = Toolkit.getDefaultToolkit().createImage("img/misc/sourcing.gif");
			image3 = Toolkit.getDefaultToolkit().createImage("img/misc/tetraresource.gif");
			image4 = Toolkit.getDefaultToolkit().createImage("img/misc/BottomText.png");
			image5 = Toolkit.getDefaultToolkit().createImage("img/misc/Instructions.png");
			image6 = Toolkit.getDefaultToolkit().createImage("img/misc/Instructions2.png");

			// creates player, map
			Game.player = new Player();
			Game.map = new Map((int) Game.screenSize.getWidth(), (int) Game.screenSize.getHeight());
			Game.player.setDimensions();

			// resizes all enemies;
			for (Enemy e : Game.enemies) {
				e.setDimensions();
			}

			// loads the projectile sprites
			try {
				Paper.image = ImageIO.read(new File("img/misc/paper.png"));
				ProjectileEnemy.img = ImageIO.read(new File("img/misc/red.png"));
				ProjectilePlayer.img = ImageIO.read(new File("img/misc/blue.png"));
			} catch (Exception e) {
			}

			// adds papers to the game
			Game.papers.add(new Paper(178, 356));
			Game.papers.add(new Paper(178, 3649));
			Game.papers.add(new Paper(3293, 267));
			Game.papers.add(new Paper(4984, 3649));
			Game.papers.add(new Paper(6141, 356));
			Game.papers.add(new Paper(3649, 267));
			Game.papers.add(new Paper(4450, 3649));
			Game.papers.add(new Paper(3293, 3026));

			// loads the health sprites
			healthSprites = new BufferedImage[6];
			try {
				for (int i = 0; i < 6;) {
					healthSprites[i] = ImageIO.read(new File("img/misc/" + (++i) + "Health.png"));
				}
			} catch (Exception e) {
				System.out.println("Error loading sprites");
			}
		}

		/**
		 * paintComponent
		 * draws components onto the screen
		 * @param 
		 */
		public void paintComponent(Graphics g) {
			// makes sure panel is redrawn correctly
			super.paintComponent(g);

			// spawns a new enemy randomly in one of 9 locations spread around the map
			long timeElapsed = System.nanoTime() - lastSpawn;
			if (timeElapsed >= 1L << 32) {
				int type = rng.nextInt(4);
				Point loc;
				switch (spawn) {
				case 0:
					loc = new Point(445, 445);
					break;
				case 1:
					loc = new Point(3115, 534);
					break;
				case 2:
					loc = new Point(445, 3293);
					break;
				case 3:
					loc = new Point(3115, 3293);
					break;
				case 4:
					loc = new Point(4183, 623);
					break;
				case 5:
					loc = new Point(5607, 801);
					break;
				case 6:
					loc = new Point(4183, 3204);
					break;
				default:
					loc = new Point(5706, 3204);
					break;
				}

				// spawns which type of enemy
				Enemy e;
				switch (type) {
				case 0:
					e = new Ghost(loc.x, loc.y, killCount);
					break;
				case 1:
					e = new BatBlue(loc.x, loc.y, killCount);
					break;
				case 2:
					e = new BatPurple(loc.x, loc.y, killCount);
					break;
				default:
					e = new Skull(loc.x, loc.y, killCount);
				}

				// sizes the enemy
				e.setDimensions();

				// makes sure enemy cannot spawn on top of player
				if (!e.getHitbox().intersects(Game.player.getHitbox())) {
					Game.enemies.add(e);
					lastSpawn = System.nanoTime();
				}
				// makes sure the next enemy spawns at the next place instead
				spawn = (spawn + 1) % 9;
			}

			// updates the game
			Game.player.update();

			// all enemies act
			for (Enemy e : Game.enemies) {
				e.chase();
			}

			// draws screen
			Game.map.draw(g);

			for (int i = 0; i < Game.papers.size();) {
				Paper p = Game.papers.get(i);
				if (Game.player.getHitbox().intersects(p.getHitbox())) {
					Game.papers.remove(i);
					p = null;
					--paperCount;
				} else {
					p.draw(g);
					++i;
				}
			}

			// Unique Lighting
			g.setColor(new Color(0, 0, 0, 230));
			g.fillRect(0, 0, 1920, 1080);

			g.drawImage(playerShine, Game.player.loc.x - 324 - (rng.nextInt(10) - 20),
					Game.player.loc.y - 264 - (rng.nextInt(10) - 20), 542, 542, null);

			Game.player.draw(g);
			// draws all enemies
			for (int i = 0; i < Game.enemies.size();) {
				Enemy e = Game.enemies.get(i);
				// spawns an item if the enemy is dead
				if (e.getHealth() <= 0) {
					int itemSpawn = rng.nextInt(4);// drop rate of items
					if (itemSpawn == 0) {
						int itemType = rng.nextInt(4);// types of items
						if (itemType >= 2) {// less chance for powerups
							FloorWeapon weapon = new FloorWeapon("Power Apple", e.pos.x, e.pos.y,
									Game.player.weapon.getDamage() + 1);
							Game.groundItems.add(weapon);// called weapon as damage increased
						} else {// more chance for potions
							FloorPotion potion = new FloorPotion("Healing Apple", e.pos.x, e.pos.y, 3);
							Game.groundItems.add(potion);// called potion as heals player
						}
					}
					// gets rid of all dead enemies
					Game.enemies.remove(i);
					e = null;
					++killCount;
				} else {
					// if enemies are not dead then draw them
					if (Math.abs(Game.player.pos.x - e.getPos().x)
							- e.getDimensions().y <= (int) Game.screenSize.getWidth() / 2
							&& Math.abs(Game.player.pos.y - e.getPos().y)
									- e.getDimensions().x <= (int) Game.screenSize.getHeight() / 2) {
						e.draw(g);
					}
					// goes to next enemy
					++i;
				}
			}

			// checks if the item was picked up
			for (int i = 0; i < Game.groundItems.size(); i++) {
				if (Game.groundItems.get(i).check()) {
					Game.groundItems.remove(i);
				} else {
					Game.groundItems.get(i).draw(g);
				}
			}

			// checks if projectiles exist or hit anywhere
			for (int i = 0; i < Game.projectiles.size();) {
				Projectile cur = Game.projectiles.get(i);
				cur.update();
				if (cur.isDestroyed()) {
					Game.projectiles.remove(i);
					cur = null;
				} else {
					++i;
					cur.draw(g);
				}
			}

			// checks if the player died
			if (Game.player.getHealth() <= 0) {
				g.drawImage(image1, 0, 0, 1920, 1080, this);
				prependGame = true;
			}

			if (paperCount == 0) {
				try {
					g.drawImage(ImageIO.read(new File("img/misc/win.png")), 0, 0, (int) Game.screenSize.getWidth(),
							(int) Game.screenSize.getHeight(), null);
				} catch (Exception e) {
				}
				prependGame = true;
			}

			// displays health left in top left corner of display screen
			if (Game.player.getHealth() > 0) {
				g.drawImage(healthSprites[Game.player.getHealth() - 1], 0, 0, 544, 96, null);
			}

			for (int i = 0; i < paperCount; ++i) {
				g.drawImage(Paper.image, i * 75, 100, 75, 75, null);
			}

			// Splashes and UI
			if (image != null) {
				if (!startGame) {
					g.drawImage(image2, 0, 0, 1920, 1080, this);
					g.drawImage(image3, 310, 0, 1240, 960, this);
					g.drawImage(image4, 310, 0, 1220, 1080, this);
					lastSpawn = System.nanoTime();
				}
				// Image Preparation for Splash Screens and Instruction Screens
				if (startGame) {
					if (advanceCount == 1) {
						g.drawImage(image5, 0, 0, 1920, 1080, this);
						lastSpawn = System.nanoTime();
					} else if (advanceCount == 2) {
						g.drawImage(image6, 0, 0, 1920, 1080, this);
						lastSpawn = System.nanoTime();
					}
				}

			}

			// repaints the window
			repaint();
		}

		/**
		 * keyTyped
		 * event handler for key being typed
		 * @param the event occuring
		 */
		public void keyTyped(KeyEvent e) {
			// moves the player in a direction
			if (e.getKeyChar() == 'a') { // left
				if (Game.player.direction.x != -1) {
					Game.player.direction.x = -1;
					Game.player.move();
				}
			} else if (e.getKeyChar() == 's') {// down
				if (Game.player.direction.y != 1) {
					Game.player.direction.y = 1;
					Game.player.move();
				}
			} else if (e.getKeyChar() == 'd') {// right
				if (Game.player.direction.x != 1) {
					Game.player.direction.x = 1;
					Game.player.move();
				}
			} else if (e.getKeyChar() == 'w') {// up
				if (Game.player.direction.y != -1) {
					Game.player.direction.y = -1;
					Game.player.move();
				}
			} else if (e.getKeyChar() == ' ') {// button to attack
				Game.player.attack();
			} else if (e.getKeyChar() == 'i') {// button to open inventory
				Game.backpack.interact();

			} else if (e.getKeyChar() == '1') {// switches to melee weapon
				Game.player.weaponType = false;

			} else if (e.getKeyChar() == '2') {// switches to ranged weapon
				Game.player.weaponType = true;
			} else if (e.getKeyChar() == 'z') {// starts game if on start screen, restarts on end
				startGame = true;
				advanceCount++;
				if (prependGame) {
					// setVisible(false);
					Game.initGame();
				}
			}
		}

		/**
		 * keyPressed
		 * useless function
		 * @param event being handled
		 */
		public void keyPressed(KeyEvent e) {
		}

		/**
		 * keyReleased
		 * stops moving the player in a direction if key is released
		 * @event key event being handled
		 */
		public void keyReleased(KeyEvent e) {
			switch (e.getKeyChar()) {
			case 'a':
				if (Game.player.direction.x == -1) {
					Game.player.direction.x = 0;
				}
				break;
			case 's':
				if (Game.player.direction.y == 1) {
					Game.player.direction.y = 0;
				}
				break;
			case 'd':
				if (Game.player.direction.x == 1) {
					Game.player.direction.x = 0;
				}
				break;
			case 'w':
				if (Game.player.direction.y == -1) {
					Game.player.direction.y = 0;
				}
				break;
			}
		}

	}// end of game panel
}// end of game window