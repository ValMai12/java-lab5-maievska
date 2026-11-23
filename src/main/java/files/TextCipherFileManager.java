
package files;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class TextCipherFileManager {

    public void encryptFile(Path inputFile, Path outputFile, char key)
            throws IOException {

        checkPaths(inputFile, outputFile);

        StringWriter encryptedText = new StringWriter();

        try (Reader reader = Files.newBufferedReader(
                inputFile, StandardCharsets.UTF_8);
             CipherWriter writer = new CipherWriter(encryptedText, key)) {

            char[] buffer = new char[1024];
            int count;

            while ((count = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, count);
            }
        }

        createParentDirectory(outputFile);

        try (ObjectOutputStream output = new ObjectOutputStream(
                Files.newOutputStream(outputFile))) {

            output.writeObject(encryptedText.toString());
        }
    }

    public void decryptFile(Path inputFile, Path outputFile, char key)
            throws IOException, ClassNotFoundException {

        checkPaths(inputFile, outputFile);

        String encryptedText;

        try (ObjectInputStream input = new ObjectInputStream(
                Files.newInputStream(inputFile))) {

            Object data = input.readObject();

            if (!(data instanceof String)) {
                throw new IOException("Невірний формат файлу.");
            }

            encryptedText = (String) data;
        }

        createParentDirectory(outputFile);

        try (CipherReader reader = new CipherReader(
                new StringReader(encryptedText), key);
             Writer writer = Files.newBufferedWriter(
                     outputFile, StandardCharsets.UTF_8)) {

            char[] buffer = new char[1024];
            int count;

            while ((count = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, count);
            }
        }
    }

    private void createParentDirectory(Path filePath)
            throws IOException {

        Path parent = filePath.toAbsolutePath().getParent();

        if (parent != null) {
            Files.createDirectories(parent);
        }
    }

    private void checkPaths(Path inputFile, Path outputFile)
            throws IOException {

        if (inputFile.toAbsolutePath().normalize().equals(
                outputFile.toAbsolutePath().normalize())) {

            throw new IOException(
                    "Вхідний та вихідний файли повинні бути різними.");
        }
    }
}
