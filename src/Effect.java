
import java.io.File;
import java.util.ArrayList;

public class Effect {

	private int turns;
	private int counter = 0;

	private double attack; // a
	private double defense; // d
	private double speed; // s
	private double weight; // w
	private double health; // he
	private double hunger; // hu

	/*
	 * Effects are in the following format
	 * 
	 * name|value|turns,name|value|turns,name|value|turns, a|4|3,d|1|0,he|2|2
	 * 
	 */

	public Effect(int turns, double attack, double defense, double speed, double weight, double health, double hunger) {
		this.turns = turns;
		counter = 0;
		this.attack = attack;
		this.defense = defense;
		this.speed = speed;
		this.weight = weight;
		this.health = health;
		this.hunger = hunger;
	}

	public Effect(String in) {
		String[] vals = in.split(";");
		int turns = Integer.parseInt(vals[2]);
		double val = Double.parseDouble(vals[1]);
		this.turns = turns;
		counter = 0;
		if (vals[0].equals("a") || vals[0].equals("attack")) {
			this.attack = val;
		}
		if (vals[0].equals("d") || vals[0].equals("defense")) {
			this.defense = val;
		}
		if (vals[0].equals("s") || vals[0].equals("speed")) {
			this.speed = val;
		}
		if (vals[0].equals("w") || vals[0].equals("weight")) {
			this.weight = val;
		}
		if (vals[0].equals("he") || vals[0].equals("health")) {
			this.health = val;
		}
		if (vals[0].equals("hu") || vals[0].equals("hunger")) {
			this.hunger = val;
		}
	}

	public boolean isOver() {
		return counter >= turns;
	}

	public void effect(Player p) {
		counter++;
		p.heal(health);
	}

	public Effect getClone() {
		return new Effect(turns, attack, defense, speed, weight, health, hunger);
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

	public double getHealth() {
		return health;
	}

	public double getHunger() {
		return hunger;
	}

}
