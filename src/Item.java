
/* [Item.java]
 * Authors: Royi Luo, Michael Oren, Brian Zhang, Bill Liu
 * Objects to be placed in inventory
 * Date Completed: Jan 19, 2018
 */

import java.awt.Toolkit;
import java.awt.Image;
import java.awt.Graphics;

//item class
class Item {
	// veriables for the item
	private String name;
	private Image img;

	/**
	 * Item
	 * constructor for Item
	 * @param n name of the item
	 */
	Item(String n) {
		img = Toolkit.getDefaultToolkit().getImage(name);
		name = n;
	}

	/**
	 * use
	 * to be overwritten
	 */
	public void use() {
	}

	/**
	 * check
	 * to be overwritten
	 */
	public boolean check() {
		return true;
	}

	/**
	 * draw
	 * to be overwritten
	 */
	public void draw(Graphics g) {
	}

	/**
	 * getName
	 * gets name
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * getImg
	 * get image
	 * @return img
	 */
	public Image getImg() {
		return img;
	}

}