import static java.lang.Math.ceil;
import static java.lang.Math.log;
import static java.lang.Math.max;
import static java.util.Comparator.comparing;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.IntStream;

/**
 * Given a file, generates a byte array which can represent that file.
 *
 * <p>Sample usage:
 * <pre><code>
 * $ java GenByteArray.java <(echo -ne '\xA\xA\xA\xA\xAA AAAAddddd\x1\x1')
 *        byte a=100,b=10,c=65;
 *        return new byte[] {
 *            b,b,b,b,-86,32,c,c,c,c,a,a,a,a,a,1,1,
 *        };
 * </code></pre>
 *
 * <p>More typical usage:
 * <pre>
 * $ java GenByteArray.java GenByteArray.java
 *         byte A=44,B=42,C=61,D=83,E=123,F=125,G=106,H=120,I=62,J=119,K=49,L=48;
 * 222 more output lines elided....
 * </pre>
 */
/*
 * The code isn't optimal.  Some things I know we can do better:
 *
 * <ul>
 * <li> Take positive and negative numbers into consideration
 * together.  If we used {@code byte a=100;} to represent 100 (just
 * {@code a}), then -100 would be {@code -a}.  Depending on the
 * frequency, {@code byte a = -100} might be better too</li>
 * <li> Include the cost of other additional Strings such as {@code
 * byte[]}, {@code byte}, {@code return} and so on.</li>
 * </ul>
 */
public class GenByteArray {
    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            throw new IllegalArgumentException("no data provided");
        }
        String pathname = args[0];
        byte[] array = Files.readAllBytes(Paths.get(pathname));

        System.out.println(new GenByteArray().freq(array));
    }

    public String freq(byte[] bytes) {
        Map<String, Integer> counter = new HashMap<>(256);

        for (byte b : bytes) {
            counter.merge(Byte.toString(b), 1, (i, j) -> i + j);
        }

        var mostFreq = counter.entrySet().stream()
                .sorted(comparingInt((Entry<String, Integer> e)
                        -> e.getKey().length() * e.getValue())
                        .reversed())
                .collect(toList());

        var byteVarname = new HashMap<Byte, String>(256);
        var varCounter = new Counter();
        for (Entry<String, Integer> e : mostFreq) {
            byte v = Byte.parseByte(e.getKey());
            int freq = e.getValue().intValue();

            int bytesOccupied = e.getKey().length() * freq;
            var variable = varCounter.peekCounter();
            int bytesSaved = bytesOccupied - ((variable.length() * freq)
                    + variable.length() + 2);

            if (bytesSaved > 0) {
                byteVarname.put(Byte.valueOf(v), varCounter.nextCounter());
            }
        }

        var buf = new StringBuilder();
        var byteVarnames = byteVarname.entrySet().stream()
                .sorted(comparing(e -> e.getValue()))
                .collect(toList());
        var declarations = append(
                byteVarnames,
                "        byte ",
                e -> String.format("%s=%d", e.getValue(), e.getKey()),
                ',',
                String.format(";%n"),
                80);
        buf.append(declarations);

        var bytevalues = append(
                IntStream.range(0, bytes.length)
                        .mapToObj(i -> Byte.valueOf(bytes[i]))
                        .collect(toList()),
                "            ",
                b -> byteVarname.getOrDefault(
                        b, b.toString()),
                ',',
                String.format(",%n"),
                80);

        // generate the byte array itself
        buf
                .append(String.format("        return new byte[] {%n"))
                .append(bytevalues)
                .append(String.format("        };"));

        return buf.toString();
    }

    private <T> String append(
            List<? extends T> list,
            String prfx,
            Function<? super T, String> toString,
            char joinChar,
            String suffix,
            int limit) {
        var buf = new StringBuilder(4096);
        var lyn = new StringBuilder(limit + 1);
        var joinLen = 1; // joinChar.length = 1
        lyn.append(prfx);

        for (T e : list) {
            String decl = toString.apply(e);
            if (lyn.length() + decl.length() + joinLen + suffix.length() > limit) {
                // flush
                buf
                        .append(lyn.substring(0, lyn.length() - 1))
                        .append(suffix);
                lyn.delete(0, lyn.length());
                lyn.append(prfx);
            }
            lyn.append(decl).append(joinChar);
        }

        if (lyn.length() > 0) {
            buf.append(lyn.substring(0, lyn.length() - 1))
                    .append(suffix);
        }

        return buf.toString();
    }

    /**
     * A variable name generator.  It doesn't work for counting up to
     * thousands but it generates unique variable names for a domain
     * of 256 values.  It'll do.
     *
     * <p>Typical usage:
     *
     * <pre>{@code
     * var counter = new Counter();
     *
     * var variableName = counter.nextCounter();
     * for (int i = 0; i < N; ++i) {
     *     System.out.println(counter.nextCounter());
     * }
     * }</pre>
     */
    private static final class Counter {
        private static final String COUNTERS1 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        private static final String COUNTERS2 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_";

        private int counterId = 0;

        /**
         * Returns the next variable name.
         */
        public String nextCounter() {
            String d = peekCounter();

            ++counterId;

            return d;
        }

        public String peekCounter() {
            int v = counterId % COUNTERS1.length();
            String d = toBaseN(v, COUNTERS1);

            v = counterId / COUNTERS1.length();

            if (v > 0) {
                d = toBaseN(v - 1, COUNTERS2) + d;
            }

            return d;
        }

        private String toBaseN(int v, String chars) {
            if (v == 0) {
                return chars.substring(0, 1);
            }

            int base = chars.length();

            final int len;
            if (v < base) {
                len = 1;
            } else {
                len = (int) ceil(log(max(1, v)) / log(base));
            }

            StringBuilder num = new StringBuilder(len);

            for (; v > 0; v /= base) {
                num.append(chars.charAt(v % base));
            }

            return num.reverse().toString();
        }
    }
}
