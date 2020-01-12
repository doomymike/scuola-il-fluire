/* [Weapon.java]
 * Authors: Royi Luo, Michael Oren, Brian Zhang, Bill Liu
 * class of items that have the ability to change your damage output
 * Date Completed: Jan 19, 2018
 */

//subclass of damage items (weapons)
class Weapon extends Item {
	private int damage;

	/**
	 * Weapon
	 * Weapon constructor
	 * @param name
	 * @param itemDamage
	 */
	Weapon(String name, int itemDamage) {
		super(name);
		damage = itemDamage;
	}

	/**
	 * getDamage
	 * @return damage of weapon
	 */
	public int getDamage() {
		return damage;
	}
}
