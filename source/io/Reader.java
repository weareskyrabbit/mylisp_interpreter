package io;

import java.io.FileReader;
import java.io.IOException;

public class Reader {
    /* member variables */
    private final FileReader reader;
    /* constructors */
    private Reader(final String file_name) throws IOException {
        reader = new FileReader(file_name);
    }
    /* member functions */
    private void close() throws IOException {
        reader.close();
    }
    public String read() throws IOException {
        StringBuilder builder = new StringBuilder();
        for (int result = (char) reader.read(); result != -1; result = reader.read()) {
            builder.append((char) result);
        }
        return builder.toString();
    }
    /* static functions */
    public static String use(final String file_name,
                           final UseReader<Reader, String, IOException> block) throws IOException {
        final Reader reader = new Reader(file_name);
        try {
            return block.apply(reader);
        } finally {
            reader.close();
        }
    }
}
