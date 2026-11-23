
package files;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;

public class CipherWriter extends FilterWriter {

    private final char key;

    public CipherWriter(Writer out, char key) {
        super(out);
        this.key = key;
    }

    @Override
    public void write(int c) throws IOException {
        out.write((char) (c + key));
    }

    @Override
    public void write(char[] buffer, int offset, int length)
            throws IOException {

        for (int i = offset; i < offset + length; i++) {
            write(buffer[i]);
        }
    }

    @Override
    public void write(String text, int offset, int length)
            throws IOException {

        write(text.toCharArray(), offset, length);
    }
}
