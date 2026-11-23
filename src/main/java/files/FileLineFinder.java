
package files;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileLineFinder {

    public String findLineWithMostWords(Path filePath) throws IOException {
        String longestLine = null;
        int maxWords = -1;

        try (BufferedReader reader = Files.newBufferedReader(
                filePath, StandardCharsets.UTF_8)) {

            String line;

            while ((line = reader.readLine()) != null) {
                int wordCount = countWords(line);

                if (wordCount > maxWords) {
                    maxWords = wordCount;
                    longestLine = line;
                }
            }
        }

        return longestLine;
    }

    private int countWords(String line) {
        if (line.isBlank()) {
            return 0;
        }

        return line.trim().split("\\s+").length;
    }
}
