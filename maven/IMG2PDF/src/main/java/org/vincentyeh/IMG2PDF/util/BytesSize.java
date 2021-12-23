package org.vincentyeh.IMG2PDF.util;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BytesSize {
    private final long bytes;

    private BytesSize(int number, Suffix suffix) {
        bytes = number * suffix.getBytes();
    }

    public enum Suffix {
        B(1L), KB(1024L), MB(1048576L), GB(1073741824);

        private final long bytes;

        Suffix(long bytes) {
            this.bytes = bytes;
        }

        public long getBytes() {
            return bytes;
        }

    }

    public static BytesSize valueOf(String raw) {
        Pattern pattern = Pattern.compile("([0-9]+)(B|KB|MB|GB)");
        Matcher matcher = pattern.matcher(raw);
        if (matcher.find()) {
            return new BytesSize(Integer.parseInt(matcher.group(1)),Suffix.valueOf(matcher.group(2)));
        }
        throw new IllegalArgumentException("Invalid format:"+raw);
    }

    public long getBytes() {
        return bytes;
    }

    @Override
    public String toString() {
        return  bytes+" bytes";
    }
}
