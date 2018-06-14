import java.io.File;
import java.util.ArrayList;

public class Room {

	private ArrayList<Room> connectedRooms;
	private ArrayList<Player> players;
	private ArrayList<Enemy> enemies;
	private ArrayList<Fight> fights;

	private ArrayList<Item> items;

	private String nameMod = "";
	private String name;
	private String description;
	private int rarity;

	public static final ArrayList<Room> allRooms = new ArrayList<Room>();

	private Room(String name, String description, int rarity) {
		this.name = name;
		this.description = description;
		this.rarity = rarity;
		players = new ArrayList<Player>();
		connectedRooms = new ArrayList<Room>();
		items = new ArrayList<Item>();
		enemies = new ArrayList<Enemy>();
		fights = new ArrayList<Fight>();
	}

	public void connectTo(Room r) {
		if (connectedRooms.contains(r)) {
			return;
		}
		connectedRooms.add(r);
		r.getConnectedRooms().add(this);
	}

	public void fillItems(int amount) {
		for (int i = 0; i < amount; i++) {
			items.add(Item.getRandomItem());
		}
	}

	public void fillEnemies(int amount) {
		for (int i = 0; i < amount; i++) {
			enemies.add(Enemy.getRandomEnemy());
		}
	}

	public void tick() {
		// move entities
		// do combat
		for (int i = 0; i < enemies.size(); i++) {
			enemies.get(i).tick(this);
		}
	}

	public ArrayList<Item> getItems() {
		return items;
	}

	public ArrayList<Room> getConnectedRooms() {
		return connectedRooms;
	}

	public void setRooms(ArrayList<Room> rooms) {
		this.connectedRooms = rooms;
	}

	public void addPlayer(Player p) {
		players.add(p);
	}

	public void addEnemy(Enemy e) {
		enemies.add(e);
	}

	public void removeEnemy(Enemy e) {
		enemies.remove(e);
	}

	public void removePlayer(Player p) {
		players.remove(p);
	}

	public void removeFight(Fight f) {
		fights.remove(f);
	}

	public void addFight(Fight f) {
		fights.add(f);
	}

	public Player getPlayer(String name) {
		for (Player p : players) {
			if (p.getName().equals(name)) {
				return p;
			}
		}
		return null;
	}

	public Fight getFight(String name) {
		for (Fight f : fights) {
			if (f.contains(name)) {
				return f;
			}
		}
		return null;

	}

	public Room getAdjacentRoom(String name) {
		for (Room r : connectedRooms) {
			if (r.getName().equals(name)) {
				return r;
			}
		}
		return null;
	}

	public int numberOfConnections() {
		return connectedRooms.size();
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public ArrayList<Enemy> getEnemies() {
		return enemies;
	}

	public ArrayList<Fight> getFights() {
		return fights;
	}

	public void setModifier(String m) {
		nameMod = m;
	}

	public String getName() {
		return name + nameMod;
	}

	public String getNameUnmodified() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public int getRarity() {
		return rarity;
	}

	public boolean containsPlayer(String name) {
		for (Player p : players) {
			if (p.getName().equals(name)) {
				return true;
			}
		}
		for (Fight f : fights) {
			if (f.getOffender() != null && f.getOffender().getName().equals(name)) {
				return true;
			}
			if (f.getDefender() != null && f.getOffender().getName().equals(name)) {
				return true;
			}
		}
		return false;
	}

	public boolean containsItem(String name) {
		for (Item i : items) {
			if (i.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}

	public Item getItem(String name) {
		for (Item i : items) {
			if (i.getName().equals(name)) {
				return i;
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

	public void removeItem(Item i) {
		items.remove(i);
	}

	public Enemy getEnemy(String name) {
		for (Enemy e : enemies) {
			if (e.getName().equals(name)) {
				return e;
			}
		}
		return null;
	}

	public Enemy getEnemyAndRemove(String name) {
		for (int i = 0; i < enemies.size(); i++) {
			if (enemies.get(i).getName().equals(name)) {
				return enemies.remove(i);
			}
		}
		return null;
	}

	public void addItem(Item i) {
		items.add(i);
	}

	public Room getClone() {
		return new Room(name, description, rarity);
	}

	public String toString() {
		return name + ";" + description + ";" + rarity;

	}

	public static void loadRoomsFromFile() {
		File folder = new File("./assets/room");
		for (File file : folder.listFiles()) {
			FileReader t = new FileReader(file);

			String n = t.tagS("name");
			String d = t.tagS("desc");
			int r = t.tagI("rarity");
			allRooms.add(new Room(n, d, r));
		}
	}

	public static Room getRandomRoom() {
		Room r = null;
		while (r == null) {
			int t = (int) (Math.random() * allRooms.size());
			for (int i = 0; i < allRooms.get(t).rarity; i++) {

				if (Math.random() > .5) {
					break;
				}
				if (i == allRooms.get(t).rarity - 1) {
					r = allRooms.get(t).getClone();
				}
			}
		}
		return r;
	}

}