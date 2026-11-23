
import files.CipherReader;
import files.CipherWriter;
import files.TextCipherFileManager;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class TextCipherTest {

    @TempDir
    Path tempDir;

    private final TextCipherFileManager manager =
            new TextCipherFileManager();

    @Test
    void encryptsAndDecryptsText() throws Exception {
        Path input = tempDir.resolve("input.txt");
        Path encrypted = tempDir.resolve("encrypted.dat");
        Path decrypted = tempDir.resolve("decrypted.txt");

        String text = "Hello Java!\nThis is a test.";
        Files.writeString(input, text, StandardCharsets.UTF_8);

        manager.encryptFile(input, encrypted, 'A');
        manager.decryptFile(encrypted, decrypted, 'A');

        assertEquals(text, Files.readString(decrypted));
    }

    @Test
    void handlesUkrainianText() throws Exception {
        Path input = tempDir.resolve("ukrainian.txt");
        Path encrypted = tempDir.resolve("encrypted.dat");
        Path decrypted = tempDir.resolve("decrypted.txt");

        String text = "Привіт! Це перевірка шифрування.";
        Files.writeString(input, text, StandardCharsets.UTF_8);

        manager.encryptFile(input, encrypted, 'К');
        manager.decryptFile(encrypted, decrypted, 'К');

        assertEquals(text, Files.readString(decrypted));
    }

    @Test
    void handlesUnicodeCharacters() throws Exception {
        Path input = tempDir.resolve("unicode.txt");
        Path encrypted = tempDir.resolve("encrypted.dat");
        Path decrypted = tempDir.resolve("decrypted.txt");

        String text = "Java 😀 Україна 🍀";
        Files.writeString(input, text, StandardCharsets.UTF_8);

        manager.encryptFile(input, encrypted, 'A');
        manager.decryptFile(encrypted, decrypted, 'A');

        assertEquals(text, Files.readString(decrypted));
    }

    @Test
    void handlesEmptyFile() throws Exception {
        Path input = tempDir.resolve("empty.txt");
        Path encrypted = tempDir.resolve("encrypted.dat");
        Path decrypted = tempDir.resolve("decrypted.txt");

        Files.writeString(input, "");

        manager.encryptFile(input, encrypted, 'A');
        manager.decryptFile(encrypted, decrypted, 'A');

        assertEquals("", Files.readString(decrypted));
    }

    @Test
    void handlesMissingInputFile() {
        Path input = tempDir.resolve("missing.txt");
        Path encrypted = tempDir.resolve("encrypted.dat");

        assertThrows(IOException.class,
                () -> manager.encryptFile(input, encrypted, 'A'));
    }

    @Test
    void preventsOverwritingInputFile() throws IOException {
        Path file = tempDir.resolve("input.txt");
        Files.writeString(file, "Original text");

        assertThrows(IOException.class,
                () -> manager.encryptFile(file, file, 'A'));

        assertEquals("Original text", Files.readString(file));
    }

    @Test
    void handlesInvalidEncryptedFile() throws IOException {
        Path invalid = tempDir.resolve("invalid.dat");
        Path output = tempDir.resolve("output.txt");

        Files.writeString(invalid, "Invalid encrypted data");

        assertThrows(IOException.class,
                () -> manager.decryptFile(invalid, output, 'A'));
    }

    @Test
    void rejectsWrongObjectType() throws IOException {
        Path input = tempDir.resolve("wrong.dat");
        Path output = tempDir.resolve("output.txt");

        try (ObjectOutputStream stream = new ObjectOutputStream(
                Files.newOutputStream(input))) {
            stream.writeObject(new int[]{1, 2, 3});
        }

        assertThrows(IOException.class,
                () -> manager.decryptFile(input, output, 'A'));
    }

    @Test
    void testsFilterWriterAndFilterReader() throws IOException {
        StringWriter encrypted = new StringWriter();

        try (CipherWriter writer = new CipherWriter(encrypted, 'A')) {
            writer.write('J');
            writer.write(new char[]{'a', 'v', 'a'}, 0, 3);
            writer.write(" 21!", 0, 4);
        }

        try (CipherReader reader = new CipherReader(
                new StringReader(encrypted.toString()), 'A')) {

            assertEquals((int) 'J', reader.read());

            char[] buffer = new char[7];
            assertEquals(7, reader.read(buffer, 0, buffer.length));
            assertEquals("ava 21!", new String(buffer));
            assertEquals(-1, reader.read());
        }
    }
}
