import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Enemy {

	private String name;
	private String description;

	private String type;
	private int rarity; // larger is more rare

	private double attack;
	private double defense;
	private double speed;

	private double HP;
	private double HPMax;

	public boolean inFight = false;

	public static final ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	/*
	 * Passive; Neutral; Aggressive;
	 */

	public Enemy(String name, String description, String type, int rarity, double attack, double defense, double speed,
			double HP) {
		super();
		this.name = name;
		this.description = description;
		this.type = type;
		this.rarity = rarity;
		this.attack = attack;
		this.defense = defense;
		this.speed = speed;
		this.HP = HP;
		this.HPMax = HP;
	}

	public Enemy getClone() {
		return new Enemy(name, description, type, rarity, attack, defense, speed, HPMax);
	}

	public String toString() {
		return name + ";" + description + ";" + type + ";" + rarity + ";" + attack + ";" + defense + ";" + speed + ";"
				+ HPMax;

	}

	public void moveToFight() {
		inFight = true;
	}

	public void moveOutOfFight() {
		inFight = false;
	}

	public void tick(Room r) {
		if (!inFight && speed > 0) {
			if (type.equals("aggressive") && r.getPlayers().size() > 0 && Math.random() < 0) {
				Fight f = new Fight(r.getPlayers().get(0), this);
				r.addFight(f);
			} else if (Math.random() < .1) {
				// move
				r.removeEnemy(this);
				r.getConnectedRooms().get((int) (Math.random() * r.getConnectedRooms().size())).addEnemy(this);
			}
		}
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

	public double getHP() {
		return HP;
	}

	public double getHPMax() {
		return HPMax;
	}

	public static void loadEnemiesFromFile() {
		File folder = new File("./assets/enemies");
		for (File file : folder.listFiles()) {
			FileReader t = new FileReader(file);

			String n = t.tagS("name");
			String d = t.tagS("desc");
			String ty = t.tagS("type");
			int r = t.tagI("rarity");
			double a = t.tagD("attack");
			double de = t.tagD("defense");
			double s = t.tagD("speed");
			double hp = t.tagD("HP");
			enemies.add(new Enemy(n, d, ty, r, a, de, s, hp));
		}

	}

	public static Enemy getRandomEnemy() {
		Enemy r = null;
		while (r == null) {
			int t = (int) (Math.random() * enemies.size());
			for (int i = 0; i < enemies.get(t).getRarity(); i++) {
				if (Math.random() > .5) {
					break;
				}
				if (i == enemies.get(t).getRarity() - 1) {
					r = enemies.get(t).getClone();
				}
			}
		}
		return r;
	}

	public static Enemy getSpecificItem(String item) {
		for (Enemy e : enemies) {
			if (e.getName().equals(item)) {
				return e.getClone();
			}
		}
		return null;
	}

}
