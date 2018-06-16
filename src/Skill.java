import java.io.File;
import java.util.ArrayList;

public class Skill {
	private String name;
	private int rarity;
	private double speed;

	public static final ArrayList<Skill> skills = new ArrayList<Skill>();

	public Skill(String name, int rarity, double speed) {
		super();
		this.name = name;
		this.rarity = rarity;
		this.speed = speed;
	}

	public Skill getClone() {
		return new Skill(name, rarity, speed);
	}

	public static void loadSkillsFromFile() {
		File folder = new File("./assets/skills");
		for (File file : folder.listFiles()) {
			FileReader t = new FileReader(file);

			String n = t.tagS("name");
			int r = t.tagI("rarity");
			double s = t.tagD("speed");
			skills.add(new Skill(n, r, s));
		}

	}

	public static Skill getRandomSkill() {
		Skill r = null;
		while (r == null) {
			int t = (int) (Math.random() * skills.size());
			for (int i = 0; i < skills.get(t).getRarity(); i++) {
				if (Math.random() > .5) {
					break;
				}
				if (i == skills.get(t).getRarity() - 1) {
					r = skills.get(t).getClone();
				}
			}
		}
		return r;
	}

	public static Skill getSpecificSkill(String skill) {
		for (Skill s : skills) {
			if (s.getName().equals(skill)) {
				return s.getClone();
			}
		}
		return null;
	}

	public String getName() {
		return name;
	}

	public int getRarity() {
		return rarity;
	}

	public double getSpeed() {
		return speed;
	}

}
