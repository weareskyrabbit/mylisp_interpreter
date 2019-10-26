package io;

import java.io.FileWriter;
import java.io.IOException;

public class Writer {
    /* member variables */
    private final FileWriter writer;
    /* constructors */
    private Writer(final String file_name) throws IOException {
        writer = new FileWriter(file_name);
    }
    /* member functions */
    private void close() throws IOException {
        writer.close();
    }
    public void write(final String message) throws IOException {
        writer.write(message);
    }
    /* static functions */
    public static void use(final String file_name,
                           final UseWriter<Writer, IOException> block) throws IOException {
        final Writer writer = new Writer(file_name);
        try {
            block.accept(writer);
        } finally {
            writer.close();
        }
    }
}
