import java.util.ArrayList;

public class Player {
	private String name;
	private ArrayList<Item> items;

	private int hand = -1; // index of hand
	private ArrayList<Integer> body; // indexes of equipped items

	private double weightMax = 5; // how much can be lifted (inventory max)
	private double attack = 1; // base damage
	private double thicknessMax = 5; // how much can be worn

	private double HP = 10;
	private double HPMax = 10;

	public boolean inFight = false;

	private ArrayList<Skill> skills;
	private ArrayList<Effect> effects;

	public Player(String name) {
		this.name = name;
		items = new ArrayList<Item>();
		body = new ArrayList<Integer>();
		skills = new ArrayList<Skill>();
		effects = new ArrayList<Effect>();
		skills.add(Skill.getSpecificSkill("punch"));
	}

	public void tick() {
		for (int i = 0; i < effects.size(); i++) {
			if (effects.get(i).isOver()) {
				effects.remove(i);
				i--;
			}
		}
		for (Effect e : effects) {
			e.effect(this);
		}
	}

	public void applyEffects(Item i) {
		for (Effect c : i.getEffects()) {
			effects.add(c);
		}
	}

	public void takeHit(Attack a) {
		HP -= Functions.getDamage(a.getAttack(), getDefense());
	}

	public void heal(double h) {
		HP += h;
		if (HP > HPMax) {
			HP = HPMax;
		}
	}

	public boolean isDead() {
		return HP < 0;
	}

	public String getName() {
		return name;
	}

	public void moveToFight() {
		inFight = true;
	}

	public void moveOutOfFight() {
		inFight = false;
	}

	public boolean canTakeItem() {
		double sum = 0;
		for (Effect e : effects) {
			sum += e.getWeight();
		}
		return getWeight() < weightMax + sum;
	}

	public double getWeight() {
		double sum = 0;
		for (int i = 0; i < items.size(); i++) {
			sum += items.get(i).getWeight();
		}
		return sum;
	}

	public boolean canEquip() {
		return getThickness() < thicknessMax;
	}

	public double getThickness() {
		double sum = 0;
		for (int i = 0; i < items.size(); i++) {
			sum += items.get(i).getThickness();
		}
		return sum;
	}

	public void addItem(Item i) {
		items.add(i);
	}

	public void removeItem(String name) {
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i).getName().equals(name)) {
				items.remove(i);
				return;
			}
		}
	}

	public void removeItem(Item item) {
		items.remove(item);
	}

	public Item getItem(String name) {
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i).getName().equals(name)) {
				return items.get(i);
			}
		}
		return null;
	}

	public Item getItemExcludingEquipped(String name) {
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i).getName().equals(name) && !body.contains(i) && hand != i) {
				return items.get(i);
			}
		}
		return null;
	}

	public Item getItemAndRemove(String name) {
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i).getName().equals(name)) {
				return items.remove(i);
			}
		}
		return null;
	}

	public ArrayList<Item> getInventory() {
		return items;
	}

	public void addSkill(Skill s) {
		skills.add(s);
	}

	public ArrayList<Skill> getSkills() {
		return skills;
	}

	public String getInventoryAsString() {
		String r = "";
		r += "Hand:\n";
		if (hand != -1) {
			r += items.get(hand).getName();
			r += "\n";
		}
		r += "\n";
		r += "Body:\n";
		for (int i : body) {
			r += items.get(i).getName() + "\n";
		}
		r += "\n";
		for (Item i : items) {
			r += i.getName() + "\n";
		}
		return r.substring(0, r.length() - 1);
	}

	public double getHP() {
		return HP;
	}

	public double getHPMax() {
		return HPMax;
	}

	public double getWeightMax() {
		return weightMax;
	}

	public double getAttack() {
		double sum = 0;
		for (Effect e : effects) {
			sum += e.getAttack();
		}
		if (hand == -1) {
			return attack + sum;
		} else {
			return attack + items.get(hand).getAttack() + sum;
		}
	}

	public double getSpeed() {
		double sum = 0;
		for (Effect e : effects) {
			sum += e.getSpeed();
		}
		if (hand == -1) {
			return sum;
		} else {
			return items.get(hand).getSpeed() + sum;
		}
	}

	public double getDefense() {
		double sum = 0;
		for (int i = 0; i < body.size(); i++) {
			sum += items.get(body.get(i)).getDefense();
		}
		for (Effect e : effects) {
			sum += e.getDefense();
		}
		return sum + sum;
	}

	public double getThicknessMax() {
		return thicknessMax;
	}

	public void hold(String name) {
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i).getName().equals(name)) {
				if (items.get(i).getType().equals("weapon")) {
					hand = i;
					return;
				}
			}
		}
	}

	public void equip(String name) {
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i).getName().equals(name)) {
				if (items.get(i).getType().equals("armor")) {
					if (thicknessMax > getThickness()) {
						body.add(i);
					}
					return;
				}
			}
		}
	}

	public void unHold(String name) {
		if (items.get(hand).getName().equals(name)) {
			hand = -1;
		}
	}

	public void unEquip(String name) {
		for (int i = 0; i < body.size(); i++) {
			if (items.get(body.get(i)).getName().equals(name)) {
				body.remove(i);
				return;
			}
		}
	}

	public void unEquipAll() {
		hand = -1;
		body.clear();
	}

}