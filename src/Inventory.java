
/* [Inventory.java]
 * Authors: Royi Luo, Michael Oren, Brian Zhang, Bill Liu
 * Inventory class for holding items
 * Date Completed: Jan 19, 2018
 */

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.imageio.*;
import java.io.File;
import java.io.IOException;

//inventory class
class Inventory {

	// creates the storage
	private static Item[] storage = new Item[5];

	/**
	 * getItem
	 * gets an item from storage
	 * @param i index of item
	 * @return the item
	 */
	public Item getItem(int i) {
		return storage[i];
	}

	/**
	 * setItem
	 * adds an item to storage
	 * @param i index to add to
	 * @param toAdd item to add
	 */
	public void setItem(int i, Item toAdd) {
		storage[i] = toAdd;
	}

	/**
	 * interact
	 * allows the player to interact with the inventory
	 */
	public void interact() {

		// makes a frame(vertical)
		JFrame frame = new JFrame();
		Box box = Box.createVerticalBox();
		frame.setSize(500, 500);

		// creates the buttons for the inventory
		for (int i = 0; i < 5; i++) {
			try {// makes sure that it doesn't crash because of missing file
				JButton b1 = new JButton(new ImageIcon(ImageIO.read(new File("img/misc/Shinyapple.png"))));
				b1.setMaximumSize(new Dimension(500, 100));
				b1.setVisible(true);
				// displays either name of item or nothing
				if (storage[i] != null) {
					b1.setText(storage[i].getName());
				} else {
					b1.setText("empty");
				}
				// listener to make button active
				b1.addActionListener(new clickButtonListener(i));
				box.add(b1);

				frame.add(box, BorderLayout.CENTER);
			} catch (IOException e) {
			}
		}
		frame.setVisible(true);
	}

	/**
	 * clickButtonListener
	 * @author Michael Oren, Royi Luo, Bill Liu, Brian Zhang
	 * event handler for player using items
	 * Date finished: January 19, 2018
	 */
	public static class clickButtonListener implements ActionListener {
		int id;

		/**
		 * clickButtonListener
		 * constructor for the class
		 * @param id of the listener
		 */
		public clickButtonListener(int id) {
			this.id = id;
		}

		/**
		 * actionPerformed
		 * uses the item when it is used by the player
		 * @param the event to be handled
		 */
		public void actionPerformed(ActionEvent event) {

			// **** This is where the code to respond to the button event goes
			if (storage[id] instanceof Weapon) {
				Game.player.weapon = (Weapon) storage[id];
				storage[id] = null;
			} else if (storage[id] != null) { // using null is impossible so nothing is done with it
				Potion tempPotion = (Potion) storage[id];
				tempPotion.use();
				storage[id] = null;
			}
		}
	}
}