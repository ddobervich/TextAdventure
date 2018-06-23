import java.io.File;
import java.util.ArrayList;

public class Skill {
	private String name;
	private int rarity;
	private double speed;
	private boolean canBeFound;

	public static final ArrayList<Skill> skills = new ArrayList<Skill>();

	public Skill(String name, int rarity, double speed, boolean canBeFound) {
		super();
		this.name = name;
		this.rarity = rarity;
		this.speed = speed;
		this.canBeFound = canBeFound;
	}

	public Skill getClone() {
		return new Skill(name, rarity, speed, canBeFound);
	}

	public static void loadSkillsFromFile() {
		File folder = new File("./assets/skills");
		for (File file : folder.listFiles()) {
			FileReader t = new FileReader(file);

			String n = t.tagS("name");
			int r = t.tagI("rarity");
			double s = t.tagD("speed");
			boolean f = t.tagB("find");
			skills.add(new Skill(n, r, s, f));
		}

	}

	public static Skill getRandomSkill() {
		Skill r = null;
		while (r == null) {
			int t = (int) (Math.random() * skills.size());
			if (!skills.get(t).canBeFound) {
				continue;
			}
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

	public static String convertNameToSecretLanguage(String s) {
		String ret = s.substring(0);
		for (int i = 0; i < 3; i++) {
			int r = (int) (Math.random() * ret.length());
			String c = ret.substring(r, r + 1);

			String rep = "";
			while (rep.length() < 3) {
				r = (int) (Math.random() * Functions.alphabet.length());
				rep += Functions.alphabet.substring(r, r + 1);
			}
			rep = rep.replaceAll(c, "");
			ret = ret.replaceAll(c, rep);
		}
		return ret;
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
