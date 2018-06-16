import java.util.ArrayList;

public class Fight {

	private Player offender;
	private Player defender;
	private Enemy enemy;

	private boolean PP;

	private ArrayList<Player> spectators;

	private ArrayList<Attack> attacks;

	/*
	 * Player v Player one has to be the offender(person who requests fight)
	 * Player v Enemy player defaults to being the offender although enemy can
	 * just attack
	 */

	public Fight(Player off, Player def) {
		offender = off;
		defender = def;
		off.moveToFight();
		def.moveToFight();
		PP = true;
		spectators = new ArrayList<Player>();
		attacks = new ArrayList<Attack>();
	}

	public Fight(Player off, Enemy e) {
		offender = off;
		enemy = e;
		off.moveToFight();
		e.moveToFight();
		PP = false;
		spectators = new ArrayList<Player>();
		attacks = new ArrayList<Attack>();
	}

	public void tick() {
		for (int i = 0; i < attacks.size(); i++) {
			attacks.get(i).tick();
			if (attacks.get(i).isOver()) {
				if (attacks.get(i).isOffenders()) {
					
				} else {

				}
			}
		}
	}

	public boolean isPlayerOnPlayerFight() {
		return PP;
	}

	public boolean isPlayerOnEnemyFight() {
		return !PP;
	}

	public boolean isOffender(String name) {
		return offender.getName().equals(name);
	}

	public void addAttack(Attack a) {
		attacks.add(a);
	}

	public void endFight(Room r) {
		r.removeFight(this);
		if (offender != null) {
			offender.moveOutOfFight();
			r.addPlayer(offender);
		}
		if (defender != null) {
			defender.moveOutOfFight();
			r.addPlayer(defender);
		}
		if (enemy != null) {
			enemy.moveOutOfFight();
			r.addEnemy(enemy);
		}
		offender = null;
		defender = null;
		enemy = null;
		for (Player p : spectators) {
			r.addPlayer(p);
		}
		spectators.clear();

	}

	public void addSpectator(Player p) {
		spectators.add(p);
	}

	public boolean contains(String name) {
		if (offender.getName().equals(name) || defender.getName().equals(name) || enemy.getName().equals(name)) {
			return true;
		}
		for (Player p : spectators) {
			if (p.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}

	public boolean containsFighter(String name) {
		if (offender.getName().equals(name) || defender.getName().equals(name) || enemy.getName().equals(name)) {
			return true;
		}

		return false;
	}

	public String getOpponentName() {
		if (enemy == null) {
			return defender.getName();
		}
		return enemy.getName();
	}

	public Player getOffender() {
		return offender;
	}

	public Player getDefender() {
		return defender;
	}

	public Enemy getEnemy() {
		return enemy;
	}

	public ArrayList<Player> getSpectators() {
		return spectators;
	}

	public ArrayList<Attack> getAttacks() {
		return attacks;
	}

}
