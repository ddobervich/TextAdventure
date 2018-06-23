
import java.io.File;
import java.util.ArrayList;

public class Item {

	private String name;
	private String description;

	private String type;
	private int rarity; // larger is more rare

	private double attack; // additional damage
	private double defense; // additional defense
	private double speed; // speed of weapon
	private double weight; // weight of item
	private double thickness; // determines how much can be equipped
	public ArrayList<Effect> effects;

	public static final ArrayList<Item> items = new ArrayList<Item>();

	/*
	 * Weapon; Food; Armor/Clothes; Object;
	 */

	public Item(String name, String description, String type, int rarity, double attack, double defense, double speed,
			double weight, double thickness, ArrayList<Effect> effects) {
		super();
		this.name = name;
		this.description = description;
		this.type = type;
		this.rarity = rarity;
		this.attack = attack;
		this.defense = defense;
		this.speed = speed;
		this.weight = weight;
		this.thickness = thickness;
		this.effects = effects;
	}

	public Item getClone() {
		ArrayList<Effect> effectCopy = new ArrayList<Effect>();
		for (Effect e : effects) {
			effectCopy.add(e.getClone());
		}
		return new Item(name, description, type, rarity, attack, defense, speed, weight, thickness, effectCopy);
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getType() {
		return type;
	}

	public int getRarity() {
		return rarity;
	}

	public double getAttack() {
		return attack;
	}

	public double getDefense() {
		return defense;
	}

	public double getSpeed() {
		return speed;
	}

	public double getWeight() {
		return weight;
	}

	public double getThickness() {
		return thickness;
	}

	public ArrayList<Effect> getEffects() {
		return effects;
	}

	public static void loadItemsFromFile() {
		File folder = new File("./assets/items");
		for (File file : folder.listFiles()) {
			FileReader t = new FileReader(file);

			String n = t.tagS("name");
			String d = t.tagS("desc");
			String ty = t.tagS("type");
			int r = t.tagI("rarity");
			double a = t.tagD("attack");
			double de = t.tagD("defense");
			double s = t.tagD("speed");
			double w = t.tagD("weight");
			double th = t.tagD("thickness");
			ArrayList<Effect> ef = new ArrayList<Effect>();
			String[] tempEffects = t.tagList("effects");
			for (String effectString : tempEffects) {
				ef.add(new Effect(effectString));
			}
			items.add(new Item(n, d, ty, r, a, de, s, w, th, ef));
		}

	}

	public static Item getRandomItem() {
		Item r = null;
		while (r == null) {
			int t = (int) (Math.random() * items.size());
			for (int i = 0; i < items.get(t).rarity; i++) {
				if (Math.random() > .5) {
					break;
				}
				if (i == items.get(t).rarity - 1) {
					r = items.get(t).getClone();
				}
			}
		}
		return r;
	}

	public static Item getSpecificItem(String item) {
		for (Item i : items) {
			if (i.getName().equals(item)) {
				return i.getClone();
			}
		}
		return null;
	}

}
