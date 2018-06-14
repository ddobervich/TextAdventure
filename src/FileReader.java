import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class FileReader {

	private HashMap<String, String> tags;

	public FileReader(File file) {

		try {
			String com = "";
			Scanner s = new Scanner(file);
			while (s.hasNextLine()) {
				com += s.nextLine();
			}
			s.close();

			String[] sets = com.split("<");
			for (String set : sets) {
				int split = set.indexOf('>');
				if (split == set.length() - 1) {
					continue;
				}
				tags.put(set.substring(0, split), set.substring(split + 1));
			}
		} catch (FileNotFoundException e) {

		} catch (Exception e) {

		}

	}

	/**
	 * Returns a certain tag as an integer
	 * 
	 * @param tag
	 * @return
	 */
	public int tagI(String tag) {
		return 0;
	}

	/**
	 * Returns a certain tag as a double
	 * 
	 * @param tag
	 * @return
	 */
	public double tagD(String tag) {
		return 0;
	}

	/**
	 * Returns a certain tag
	 * 
	 * @param tag
	 * @return
	 */
	public String tagS(String tag) {
		return "";
	}

}
