
public class Functions {

	public static int attackDelay(double attack, double speed) {
		double d = Math.pow(1.2, attack - speed);
		return (int) d + 1;
	}

	public static double getDamage(double attack, double defense) {
		return attack / Math.sqrt(1 + defense);
	}

}
