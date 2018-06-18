import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class FileReader {

	private HashMap<String, String> tags;

	public FileReader(File file) {
		tags = new HashMap<String, String>();
		try {
			String com = "";
			Scanner s = new Scanner(file);
			while (s.hasNextLine()) {
				com += s.nextLine();
			}
			s.close();

			String[] sets = com.split("<");

			for (String set : sets) {
				if (!set.contains(">")) {
					continue;
				}
				String[] split = set.split(">");
				tags.put(split[0], split[1]);
			}
		} catch (FileNotFoundException e) {

		}

	}

	/**
	 * Returns a certain tag as an integer
	 * 
	 * @param tag
	 * @return
	 */
	public int tagI(String tag) {
		try {
			String s = tags.get(tag);
			s.replaceAll(" ", "");
			return Integer.parseInt(s);
		} catch (Exception e) {
			// System.out.println(tag);
			return 1;
		}
	}

	/**
	 * Returns a certain tag as a double
	 * 
	 * @param tag
	 * @return
	 */
	public double tagD(String tag) {
		try {
			String s = tags.get(tag);
			s.replaceAll(" ", "");
			return Double.parseDouble(s);
		} catch (Exception e) {
			// System.out.println(tag);
			return 0;
		}
	}

	/**
	 * Returns a certain tag
	 * 
	 * @param tag
	 * @return
	 */
	public String tagS(String tag) {
		try {
			return tags.get(tag);
		} catch (Exception e) {
			// System.out.println(tag);
			return "";
		}
	}

	public String[] tagList(String tag) {
		try {
			String r = tags.get(tag);
			while (r.startsWith(" ")) {
				r = r.substring(1);
			}
			return r.split(",");
		} catch (Exception e) {
			return null;
		}
	}

}
