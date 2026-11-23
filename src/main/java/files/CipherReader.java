
package files;

import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;

public class CipherReader extends FilterReader {

    private final char key;

    public CipherReader(Reader in, char key) {
        super(in);
        this.key = key;
    }

    @Override
    public int read() throws IOException {
        int c = in.read();

        if (c == -1) {
            return -1;
        }

        return (char) (c - key);
    }

    @Override
    public int read(char[] buffer, int offset, int length)
            throws IOException {

        int count = in.read(buffer, offset, length);

        if (count == -1) {
            return -1;
        }

        for (int i = offset; i < offset + count; i++) {
            buffer[i] = (char) (buffer[i] - key);
        }

        return count;
    }
}
