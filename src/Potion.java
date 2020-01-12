/* [Potion.java]
 * Authors: Royi Luo, Michael Oren, Brian Zhang, Bill Liu
 * class of items that have the ability to heal you
 * Date Completed: Jan 19, 2018
 */

//subclass of healing items (potions)
class Potion extends Item {
	// amount to heal by
	protected int amount;

	/**
	 * Potion
	 * constructor for potion
	 * @param name of the potion
	 * @param quantity to heal by
	 */
	Potion(String name, int quantity) {
		super(name);
		amount = quantity;
	}

	/**
	 * use
	 * the player consumes the potion
	 */
	public void use() {
		Game.player.heal(amount);
	}
}
