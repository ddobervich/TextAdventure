public class Attack {

	private double attack;
	private double speed;
	private int count;
	private int delayTotal;
	private Skill skill;
	private boolean side;
	// true - offenders attack
	// false - others attack

	public Attack(double attack, double speed, boolean side, int delayTotal, Skill skill) {
		this.attack = attack;
		this.speed = speed;
		this.side = side;
		this.count = 0;
		this.delayTotal = delayTotal;
		this.skill = skill;
	}

	public Attack(Fight f, Player p, Skill s) {
		this.attack = p.getAttack();
		this.speed = p.getSpeed();
		this.side = f.isOffender(p.getName());
		this.count = 0;
		this.delayTotal = Functions.attackDelay(attack, speed);
		this.skill = s;
	}

	public void tick() {
		count++;
	}

	public boolean isOver() {
		return count > delayTotal;
	}

	public double getAttack() {
		return attack;
	}

	public double getSpeed() {
		return speed;
	}

	public int getCount() {
		return count;
	}

	public int getDelayTotal() {
		return delayTotal;
	}

	public Skill getSkill() {
		return skill;
	}

	public boolean isOffenders() {
		return side;
	}

	public String getText() {
		String r = skill.getName() + " " + attack + "   ";

		for (int i = 0; i < delayTotal; i++) {
			if (i <= (delayTotal - count)) {
				r += "*";
			} else {
				r += "-";
			}
		}
		return r;
	}

}