import java.io.File;
import java.nio.file.*;

public class WordCounter {
    // The following are the ONLY variables we will modify for grading.
    // The rest of your code must run with no changes.
    public static final Path FOLDER_OF_TEXT_FILES = Paths.get("C:\\Users\\kevin\\OneDrive\\Documents\\Stony\\CSE 216\\CSE216-HW5\\textfiles"); // path to the folder where input text files are located
    public static final Path WORD_COUNT_TABLE_FILE = Paths.get("C:\\Users\\kevin\\OneDrive\\Documents\\Stony\\CSE 216\\CSE216-HW5\\output.txt"); // path to the output plain-text (.txt) file
    public static final int NUMBER_OF_THREADS = 2;                // max. number of threads to spawn

    public static void main(String[] args) {
        try {
            File[] files = new File(FOLDER_OF_TEXT_FILES.toUri()).listFiles();
            for (File f : files)
                System.out.println(f.toString());
        } catch (Exception e) {
            System.out.printf("Unable to read from directory: %s", FOLDER_OF_TEXT_FILES);
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
}
