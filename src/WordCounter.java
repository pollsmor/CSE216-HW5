import java.io.File;
import java.nio.file.*;
import java.util.*;

public class WordCounter {
    // The following are the ONLY variables we will modify for grading.
    // The rest of your code must run with no changes.
    public static final Path FOLDER_OF_TEXT_FILES = Paths.get("C:\\Users\\kevin\\OneDrive\\Documents\\Stony\\CSE 216\\CSE216-HW5\\textfiles"); // path to the folder where input text files are located
    public static final Path WORD_COUNT_TABLE_FILE = Paths.get("C:\\Users\\kevin\\OneDrive\\Documents\\Stony\\CSE 216\\CSE216-HW5\\output.txt"); // path to the output plain-text (.txt) file
    public static final int NUMBER_OF_THREADS = 2;                // max. number of threads to spawn

    public static List<String> filenames; // Column names
    public static HashMap<String, List<Integer>> wordTracker = new HashMap<>();

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        try {
            File[] files = new File(FOLDER_OF_TEXT_FILES.toUri()).listFiles(); // List all files in specified directory
            if (files == null) return;  // Shouldn't ever happen?

            filenames = new ArrayList<>(files.length);
            for (File f : files) {
                String filename = f.getName().toLowerCase();
                filenames.add(filename.substring(0, filename.indexOf('.')));
            }
            Collections.sort(filenames); // I don't think this is needed, but just to be safe

            // Part that will make use of multithreading ============================================================
            int filenameIdx = 0; // Index for List value in wordTracker
            for (File f : files) {
                String contents = stripPunctuation(new String(Files.readAllBytes(Paths.get(f.toURI()))).toLowerCase());
                String[] split = contents.split(" ");
                for (String s : split) addToWordTracker(s, filenameIdx);
                filenameIdx += 1;
            }
            // ======================================================================================================

            Map<String, List<Integer>> map = new TreeMap<>(wordTracker); // TreeMap sorts alphabetically
            StringBuilder output = new StringBuilder(); // Write this to file at the end

            // First row's data ======================================================
            int longestWordLength = 0;
            for (String word : map.keySet())
                if (word.length() > longestWordLength) longestWordLength = word.length();

            for (int i = 0; i <= longestWordLength; i++) // Skip this many spaces in the top left of the file
                output.append(' ');

            for (String filename : filenames) { // Column names
                output.append(filename);
                output.append("    "); // 4 spaces
            }

            output.append("total\n");

            // Other rows ============================================================
            filenameIdx = 0; // Need this again so reset to 0
            for (Map.Entry<String, List<Integer>> entry : map.entrySet()) { // Loop through wordTracker
                String word = entry.getKey();
                output.append(word);
                int paddingSpaces = longestWordLength - word.length(); // Add this many spaces to line everything up
                for (int i = 0; i < paddingSpaces; i++) output.append(' ');
                output.append(' ');

                int total = 0;
                int widthOfCol = filenames.get(filenameIdx).length() + 4;
                for (Integer appearances : entry.getValue()) {
                    output.append(appearances);
                    total += appearances;
                    int paddingSpaces2 = widthOfCol - appearances.toString().length(); // Again, for lining up
                    for (int i = 0; i < paddingSpaces2; i++) output.append(' ');
                }

                output.append(total);
                output.append('\n');
            }

            // =======================================================================
            Files.write(WORD_COUNT_TABLE_FILE, output.toString().getBytes());
            System.out.println(System.nanoTime() - startTime); // Check runtime of program
        } catch (Exception e) {
            System.out.printf("Error when accessing directory: %s, or file: %s", FOLDER_OF_TEXT_FILES, WORD_COUNT_TABLE_FILE);
        }
    }

    // Use a method that strips these characters: . , : ; ! ? from a sentence.
    public static String stripPunctuation(String input) {
        StringBuilder output = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (c != '.' && c != ',' && c != ':' && c != ';' && c != '!' && c != '?')
                output.append(c);
        }

        return output.toString();
    }

    // Method that adds word to wordTracker HashMap
    public static void addToWordTracker(String word, int index) {
        if (wordTracker.containsKey(word)) { // Simply increment count by 1
            int count = wordTracker.get(word).get(index);
            wordTracker.get(word).set(index, count + 1);
        } else { // Add new word entry to wordTracker
            List<Integer> lst = new ArrayList<>(Collections.nCopies(filenames.size(), 0)); // Pre-fill List with 0s
            lst.set(index, 1);
            wordTracker.put(word, lst);
        }
    }
}
