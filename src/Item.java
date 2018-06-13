import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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

	public static ArrayList<Item> items = new ArrayList<Item>();
	/*
	 * Weapon; Food; Armor/Clothes; Object;
	 */

	public Item(String name, String description, String type, int rarity, double attack, double defense, double speed,
			double weight, double thickness) {
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
	}

	public Item getClone() {
		return new Item(name, description, type, rarity, attack, defense, speed, weight, thickness);
	}

	public String toString() {
		return name + ";" + description + ";" + type + ";" + rarity + ";" + attack + ";" + defense + ";" + speed + ";"
				+ weight + ";" + thickness;

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

	public static void loadItemsFromFile() {
		String fileName = "./assets/items";
		String line = null;
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				String[] t = line.split(";");
				if (t.length != 9) {
					System.out.println(line);
					continue;
				}
				try {
					items.add(new Item(t[0], t[1], t[2], Integer.parseInt(t[3]), Double.parseDouble(t[4]),
							Double.parseDouble(t[5]), Double.parseDouble(t[6]), Double.parseDouble(t[7]),
							Double.parseDouble(t[8])));
				} catch (Exception e) {
					continue;
				}
			}

			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fileName + "'");
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
