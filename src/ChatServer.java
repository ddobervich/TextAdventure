import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.JOptionPane;

/**
 * A multithreaded chat room server. When a client connects the server requests
 * a screen name by sending the client the text "SUBMITNAME", and keeps
 * requesting a name until a unique one is received. After a client submits a
 * unique name, the server acknowledges with "NAMEACCEPTED". Then all messages
 * from that client will be broadcast to all other clients that have submitted a
 * unique screen name. The broadcast messages are prefixed with "MESSAGE ".
 *
 * Because this is just a teaching example to illustrate a simple chat server,
 * there are a few features that have been left out. Two are very useful and
 * belong in production code:
 *
 * 1. The protocol should be enhanced so that the client can send clean
 * disconnect messages to the server.
 *
 * 2. The server should do some logging.
 */
public class ChatServer {

	/**
	 * The port that the server listens on.
	 */
	private static final int PORT = 9001;

	// names

	private static HashMap<String, PrintWriter> users = new HashMap<String, PrintWriter>();
	// how to send the people messages

	private static Game game;

	public static final int GAME_TICK_DELAY = 500;

	/**
	 * The appplication main method, which just listens on a port and spawns
	 * handler threads.
	 */
	public static void main(String[] args) throws Exception {
		Skill.loadSkillsFromFile();
		Item.loadItemsFromFile();
		Room.loadRoomsFromFile();
		Enemy.loadEnemiesFromFile();
		game = new Game(100);
		JOptionPane.showMessageDialog(null, "The Server is running");
		System.out.println("The Server is running");
		ServerSocket listener = new ServerSocket(PORT);

		(new Thread() {
			public void run() {
				long lastUpdate = System.currentTimeMillis();
				while (true) {

					if (System.currentTimeMillis() - lastUpdate > GAME_TICK_DELAY) {
						lastUpdate = System.currentTimeMillis();
						game.tick();
						for (Player p : game.getPlayers()) {
							users.get(p.getName()).println("DATA" + game.getStateText(p.getName()));
						}
					}
				}
			}
		}).start();

		try {
			while (true) {
				new Handler(listener.accept()).start();
			}
		} finally {
			listener.close();
		}
	}

	/**
	 * A handler thread class. Handlers are spawned from the listening loop and
	 * are responsible for a dealing with a single client and broadcasting its
	 * messages.
	 */
	private static class Handler extends Thread {
		private String name;
		private Socket socket;
		private BufferedReader in;
		private PrintWriter out;

		/**
		 * Constructs a handler thread, squirreling away the socket. All the
		 * interesting work is done in the run method.
		 */
		public Handler(Socket socket) {
			this.socket = socket;
		}

		/**
		 * Services this thread's client by repeatedly requesting a screen name
		 * until a unique one has been submitted, then acknowledges the name and
		 * registers the output stream for the client in a global set, then
		 * repeatedly gets inputs and broadcasts them.
		 */
		public void run() {
			try {

				// Create character streams for the socket.
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream(), true);

				// Request a name from this client. Keep requesting until
				// a name is submitted that is not already used. Note that
				// checking for the existence of a name and adding the name
				// must be done while locking the set of names.
				while (true) {
					out.println("SUBMITNAME");
					name = in.readLine();
					if (name == null) {
						return;
					}
					synchronized (users) {
						if (!users.containsKey(name)) {
							out.println("NAMEACCEPTED");
							users.put(name, out);
							game.addPlayer(name);
							break;
						}
					}
				}

				// Accept messages from this client and broadcast them.
				// Ignore other clients that cannot be broadcasted to.
				while (true) {

					String input = in.readLine();
					if (input == null) {
						return;
					}
					// update for side bar
					users.get(name).println("DATA" + game.getStateText(name));
					// updates for all everyone
					HashMap<String, String> toSend = game.processAction(name, input);
					for (String name : toSend.keySet()) {
						users.get(name).println("MESSAGE" + toSend.get(name));
					}
				}
			} catch (IOException e) {
				System.out.println(e);
			} finally {
				// This client is going down! Remove its name and its print
				// writer from the sets, and close its socket.
				if (name != null) {
					game.removePlayer(name);
					users.remove(name);
				}
				try {
					socket.close();
				} catch (IOException e) {
				}
			}
		}
	}
}