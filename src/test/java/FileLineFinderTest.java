
import files.FileLineFinder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class FileLineFinderTest {

    @TempDir
    Path tempDir;

    private final FileLineFinder finder = new FileLineFinder();

    @Test
    void findsLineWithMostWords() throws IOException {
        Path file = tempDir.resolve("text.txt");
        Files.writeString(file,
                "Це короткий рядок\n" +
                "Це рядок з найбільшою кількістю слів у файлі\n" +
                "Ще один приклад",
                StandardCharsets.UTF_8);

        assertEquals(
                "Це рядок з найбільшою кількістю слів у файлі",
                finder.findLineWithMostWords(file));
    }

    @Test
    void returnsFirstLineWhenWordCountsAreEqual() throws IOException {
        Path file = tempDir.resolve("equal.txt");
        Files.writeString(file,
                "Один два три\nЧотири п'ять шість",
                StandardCharsets.UTF_8);

        assertEquals("Один два три",
                finder.findLineWithMostWords(file));
    }

    @Test
    void handlesEmptyFile() throws IOException {
        Path file = tempDir.resolve("empty.txt");
        Files.writeString(file, "");

        assertNull(finder.findLineWithMostWords(file));
    }

    @Test
    void handlesExtraSpaces() throws IOException {
        Path file = tempDir.resolve("spaces.txt");
        Files.writeString(file,
                "Одне слово\n" +
                "Два    три     чотири",
                StandardCharsets.UTF_8);

        assertEquals("Два    три     чотири",
                finder.findLineWithMostWords(file));
    }

    @Test
    void handlesBlankLines() throws IOException {
        Path file = tempDir.resolve("blank.txt");
        Files.writeString(file,
                "\n   \nТут два",
                StandardCharsets.UTF_8);

        assertEquals("Тут два",
                finder.findLineWithMostWords(file));
    }

    @Test
    void handlesMissingFile() {
        Path file = tempDir.resolve("missing.txt");

        assertThrows(IOException.class,
                () -> finder.findLineWithMostWords(file));
    }
}
