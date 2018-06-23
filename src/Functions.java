
public class Functions {

	public static final String alphabet = "abcdefghijklmnopqrstuvwxyz";

	public static int attackDelay(double attack, double speed) {
		double d = Math.pow(1.2, attack - speed);
		return (int) d + 5;
	}

	public static double getDamage(double attack, double defense) {
		return attack / Math.sqrt(1 + defense);
	}

}
