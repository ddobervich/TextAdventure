import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Game {
	public static final int ROOM_CONNECTION_CONSTANT = 3;
	ArrayList<Room> rooms;

	public Game(int size) {
		rooms = new ArrayList<Room>();
		for (int i = 0; i < size; i++) {
			Room temp = Room.getRandomRoom();
			temp.fillItems((int) (Math.random() * 4));
			temp.fillEnemies((int) (5 + Math.random() * 1));
			int count = 1;
			for (int j = 0; j < rooms.size(); j++) {
				if (rooms.get(j).getNameUnmodified().equals(temp.getNameUnmodified())) {
					count++;
				}
			}
			if (count != 1) {
				temp.setModifier(count + "");
			}
			rooms.add(temp);
		}
		boolean finish = false;
		int i = 0;
		while (!finish) {
			if (i >= rooms.size()) {
				i = 0;
				finish = true;
			}
			if (rooms.get(i).numberOfConnections() < ROOM_CONNECTION_CONSTANT) {
				finish = false;
				int t = -1;
				do {
					t = (int) (Math.random() * rooms.size());
				} while (t == i);
				rooms.get(i).connectTo(rooms.get(t));
			}
			i++;
		}

	}

	public void tick() {
		for (Room r : rooms) {
			r.tick();
		}
	}

	public void addPlayer(String name) {
		Player p = new Player(name);
		// TODO load if player already exists
		rooms.get(rooms.size() - 1).addPlayer(p);
	}

	/**
	 * Gets all Players Currently in game to be used for sending data. Players
	 * should not be moved or modified through this method.
	 * 
	 * @return
	 */
	public ArrayList<Player> getPlayers() {
		ArrayList<Player> r = new ArrayList<Player>();
		for (Room room : rooms) {
			for (Player player : room.getPlayers()) {
				r.add(player);
			}
		}
		return r;
	}

	public Player getPlayer(String name) {
		for (Player p : getPlayers()) {
			if (p.getName().equals(name)) {
				return p;
			}
		}
		System.out.println(name + "KK");
		return null;
	}

	public Room getRoomofPlayer(String name) {
		for (Room r : rooms) {
			if (r.containsPlayer(name)) {
				return r;
			}
		}
		return null;
	}

	public void removePlayer(String name) {
		for (Room room : rooms) {
			for (int i = 0; i < room.getPlayers().size(); i++) {
				Player p = room.getPlayers().get(i);
				if (p.getName().equals(name)) {
					room.removePlayer(p);
					return;
				}
			}
		}
	}

	public String getStateText(String name) {
		Room room = getRoomofPlayer(name);
		Player player = getPlayer(name);

		if (!player.inFight) {
			return look(room, name);
		} else {
			Fight f = room.getFight(name);
			String out = "You are in a fight in " + room.getName();
			out += "\n";
			out += f.getOffender().getName() + "\n";
			out += "vs\n";
			out += f.getOpponentName() + "\n";
			for (Attack a : f.getAttacks()) {
				if (true || a.isOffenders() != f.isOffender(name)) {
					out += a.getText() + "\n";
				}
			}
			return out;
		}
	}

	public HashMap<String, String> processAction(String name, String input) {
		HashMap<String, String> r = new HashMap<String, String>();
		Room currentRoom = getRoomofPlayer(name);
		Player p = currentRoom.getPlayer(name);
		// LOGIC

		if (input.startsWith("say ")) {
			for (int i = 0; i < getPlayers().size(); i++) {
				r.put(getPlayers().get(i).getName(), name + ": " + input.substring(4));
			}
			return r;
		}
		if (input.startsWith("look")) {
			Room room = getRoomofPlayer(name);
			r.put(name, look(room, name));
			return r;
		}
		if (input.startsWith("observe ")) {
			String object = input.substring(8);
			return observe(currentRoom, name, object);

		}
		if (input.startsWith("move ")) {
			String location = input.substring(5);
			return move(p, currentRoom, name, location);
		}

		if (input.startsWith("help")) {
			return help(name);
		}

		if (input.startsWith("take ") || input.startsWith("pick up ")) {
			String object = "";
			if (input.startsWith("take "))
				object = input.substring(5);
			if (input.startsWith("pick up "))
				object = input.substring(8);
			return take(p, currentRoom, name, object);
		}

		if (input.startsWith("inventory")) {
			return inventory(p, name);
		}

		if (input.startsWith("drop ")) {
			String object = input.substring(5);
			return drop(p, currentRoom, name, object);
		}

		if (input.startsWith("equip ")) {
			String object = input.substring(6);
			return equip(p, name, object);
		}

		if (input.startsWith("unequip ")) {
			String object = input.substring(8);
			return unEquip(p, name, object);
		}

		if (input.startsWith("unequip all")) {
			return unEquipAll(p, name);
		}

		if (input.startsWith("stats")) {
			return stats(p);
		}

		if (input.startsWith("fight ")) {
			String object = input.substring(6);
			return fight(p, currentRoom, object, name);
		}

		if (input.startsWith("leave")) {
			return leave(currentRoom, name);
		}

		if (p.inFight) {
			Fight f = currentRoom.getFight(name);
			for (Skill s : p.getSkills()) {
				if (input.startsWith(s.getName())) {
					f.addAttack(new Attack(f, p, s));
				}
			}
		}

		return r;

	}

	/**
	 * 
	 * @param room
	 *            Player's room
	 * @param name
	 *            Player's name
	 * @return String of what player sees
	 */
	private String look(Room room, String name) {
		String out = "You are in " + room.getName();
		out += "\n";
		for (Item i : room.getItems()) {
			out += "\nYou see a " + i.getName();
		}
		out += "\n";
		for (Room roomPath : room.getConnectedRooms()) {
			String roomName = roomPath.getName();
			out += "\nYou can enter the " + roomName;
		}
		out += "\n";
		for (Enemy e : room.getEnemies()) {
			out += "\nYou see " + e.getName();
		}
		out += "\n";
		for (Player p : room.getPlayers()) {
			if (p.getName().equals(name)) {
				continue;
			}
			out += "\nYou see " + p.getName();
		}
		return out;
	}

	/**
	 * Gives the Player information about a given object
	 * 
	 * @param currentRoom
	 *            Player's room
	 * @param name
	 *            Player's name
	 * @param object
	 *            Object to be observed
	 * @return String of how object is observed
	 */
	private HashMap<String, String> observe(Room currentRoom, String name, String object) {
		HashMap<String, String> r = new HashMap<String, String>();
		if (currentRoom.getName().equals(object)) {
			r.put(name, currentRoom.getDescription());
			return r;
		}
		Item item = currentRoom.getItem(object);
		if (item != null) {
			// TODO also print out item stats
			r.put(name, item.getName() + "\n" + item.getDescription());
			return r;
		}
		Enemy e = currentRoom.getEnemy(object);
		if (e != null) {
			r.put(name, e.getName() + "\n" + e.getDescription());
		}
		return r;
	}

	/**
	 * Moves Player to given room
	 * 
	 * @param p
	 *            Player
	 * @param currentRoom
	 *            Player's room
	 * @param name
	 *            Player's name
	 * @param location
	 *            Destination
	 * @return HashMap of text to be sent to other users.
	 */
	private HashMap<String, String> move(Player p, Room currentRoom, String name, String location) {
		HashMap<String, String> r = new HashMap<String, String>();
		for (Room newRoom : currentRoom.getConnectedRooms()) {
			if (!newRoom.getName().equals(location)) {
				continue;
			}
			currentRoom.removePlayer(p);
			newRoom.addPlayer(p);
			r.put(name, "You walk into " + newRoom.getName());
			return r;

		}
		return r;
	}

	/**
	 * Sends Player help message
	 * 
	 * @param name
	 *            Player to send message to
	 * @return String help message
	 */
	private HashMap<String, String> help(String name) {
		// TODO Keep Updated
		HashMap<String, String> r = new HashMap<String, String>();
		String out = "";
		out += "You are a dungeon explorer!\nType your way through the dungeon.\n";
		out += "To talk type: \"say <your message>\"\n";
		out += "To look type: \"look\"\n";
		out += "To observe something type: \"observe <name of thing>\"\n";
		out += "To move type: \"move <name of location>\"\n";
		out += "Have Fun!!!\n\n";
		r.put(name, out);
		return r;
	}

	/**
	 * Picks up an item from the given room for the player
	 * 
	 * @param p
	 *            Player
	 * @param currentRoom
	 *            Player's room
	 * @param name
	 *            Player's name
	 * @param object
	 *            Name of Object as String
	 * @return HashMap of text to be sent to other users.
	 */
	private HashMap<String, String> take(Player p, Room currentRoom, String name, String object) {
		HashMap<String, String> r = new HashMap<String, String>();
		if (currentRoom.getName().equals(object)) {
			r.put(name, "You fool! You can't take a room... not yet at least");
			return r;
		}
		Item item = currentRoom.getItem(object);
		if (item != null) {
			if (!p.canTakeItem()) {
				r.put(name, "Your inventory is full");
				return r;
			}
			p.addItem(item);
			currentRoom.removeItem(item);
			r.put(name, "You have picked up a " + object);
		}
		return r;
	}

	/**
	 * Returns the String representation of the inventory of the given player
	 * 
	 * @param p
	 *            Player
	 * @param name
	 *            Player's name
	 * @return String of inventory
	 */
	private HashMap<String, String> inventory(Player p, String name) {
		HashMap<String, String> r = new HashMap<String, String>();
		r.put(name, "Inventory\n" + p.getInventoryAsString());
		return r;
	}

	/**
	 * Drop a given item from the players inventory
	 * 
	 * @param p
	 *            Player
	 * @param currentRoom
	 *            Room of Player
	 * @param name
	 *            Player's name
	 * @param object
	 *            Object name as String
	 * @return HashMap of text to be sent to other users.
	 */
	private HashMap<String, String> drop(Player p, Room currentRoom, String name, String object) {
		HashMap<String, String> r = new HashMap<String, String>();
		Item item = p.getItemAndRemove(object);
		if (item != null) {
			currentRoom.addItem(item);
			r.put(name, "You have dropped a " + object);
		} else {
			r.put(name, "You can't drop what you don't have");
		}
		return r;
	}

	/**
	 * Equip given item from given player
	 * 
	 * @param p
	 *            Player
	 * @param name
	 *            Player's name
	 * @param object
	 *            Name of object as String
	 * @return HashMap of text to be sent to other users.
	 */
	private HashMap<String, String> equip(Player p, String name, String object) {
		HashMap<String, String> r = new HashMap<String, String>();
		Item item = p.getItemExcludingEquipped(object);
		if (item != null) {
			if (item.getType().equals("weapon")) {
				p.hold(object);
				r.put(name, "You have equipped a " + object);
			} else if (item.getType().equals("armor")) {
				if (!p.canEquip()) {
					r.put(name, "You are too... thick. You can't equip any more.");
					return r;
				}
				p.equip(object);
				r.put(name, "You have equipped a " + object);
			}
		} else {
			r.put(name, "You can't equip what you don't have");
		}
		return r;
	}

	/**
	 * Unequip given item from given player
	 * 
	 * @param p
	 *            Player
	 * @param name
	 *            Player's name
	 * @param object
	 *            Name of object as String
	 * @return HashMap of text to be sent to other users.
	 */
	private HashMap<String, String> unEquip(Player p, String name, String object) {
		HashMap<String, String> r = new HashMap<String, String>();
		Item item = p.getItem(object);
		if (item != null) {
			if (item.getType().equals("weapon")) {
				p.unHold(object);
				r.put(name, "You have unequipped a " + object);
			} else if (item.getType().equals("armor")) {
				p.unEquip(object);
				r.put(name, "You have unequipped a " + object);
			}
		} else {
			r.put(name, "You can't unequip what you don't have");
		}
		return r;
	}

	/**
	 * Unequip all of the given players items
	 * 
	 * @param p
	 *            Player
	 * @param name
	 *            Player's name
	 * @return HashMap of text to be sent to other users.
	 */
	private HashMap<String, String> unEquipAll(Player p, String name) {
		HashMap<String, String> r = new HashMap<String, String>();
		if (p.getInventory().size() == 0) {
			r.put(name, "Like I guess... you don't really have anything though");
			return r;
		}
		p.unEquipAll();
		r.put(name, "You have unequiped everything");
		return r;
	}

	/**
	 * Returns the stats of the given player
	 * 
	 * @param p
	 *            Player
	 * @return String of stats
	 */
	private HashMap<String, String> stats(Player p) {
		HashMap<String, String> r = new HashMap<String, String>();
		String out = "";
		out += "Player: " + p.getName() + "\n";
		out += "HP: " + p.getHP() + "/" + p.getHPMax() + "\n";
		out += "Attack: " + p.getAttack() + "\n";
		out += "Defense: " + p.getDefense() + "\n";
		out += "Weight: " + p.getWeight() + "/" + p.getWeightMax() + "\n";
		out += "Thickness: " + p.getThickness() + "/" + p.getThicknessMax() + "\n";
		r.put(p.getName(), out);
		return r;
	}

	/**
	 * Puts player into a fight with the enemy they selected.
	 * 
	 * @param p
	 *            Player that is attacking
	 * @param currentRoom
	 *            Room of player
	 * @param object
	 *            Object as text
	 * @param name
	 *            Name of player
	 * @return HashMap of text to be sent to other users.
	 */
	private HashMap<String, String> fight(Player p, Room currentRoom, String object, String name) {
		HashMap<String, String> r = new HashMap<String, String>();
		Enemy e = currentRoom.getEnemy(object);
		if (e == null) {
			r.put(name, "That enemy isn't there!");
			return r;
		}
		Fight f = new Fight(p, e);
		currentRoom.addFight(f);
		r.put(name, "You are now in a fight with " + object);
		return r;
	}

	/**
	 * Removes the player from their fight and ends the fight.
	 * 
	 * @param name
	 *            Name of the player
	 * @param currentRoom
	 *            The room of the player.
	 * @return HashMap of text to be sent to other users.
	 */
	private HashMap<String, String> leave(Room currentRoom, String name) {
		HashMap<String, String> r = new HashMap<String, String>();
		currentRoom.getFight(name).endFight(currentRoom);
		r.put(name, "You have left the fight :(");
		return r;
	}

	// Copy paste when making additional actions for the player
	private HashMap<String, String> placeHolder() {
		HashMap<String, String> r = new HashMap<String, String>();

		return r;
	}
}
