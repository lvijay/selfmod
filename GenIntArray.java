import static java.lang.Math.max;
import static java.util.Comparator.comparing;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntUnaryOperator;
import java.util.stream.IntStream;

import javax.lang.model.SourceVersion;

/**
 * Given a file, generates an int array which can represent that file.
 *
 * <p>Sample usage:
 * <pre><code>
 * $ java GenIntArray.java <(echo -ne '\xA\xA\xA\xA\xAA AAAAddddd\x1\x1')
 *         int a=-1440726719,b=1684300801,c=1094804580,d=168430090,e=16777216;
 *         return new int[] {
 *             1,d,a,c,b,e,
 *         };
 * </code></pre>
 */
public class GenIntArray {
    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            throw new IllegalArgumentException("no data provided");
        }
        String pathname = args[0];
        byte[] array = Files.readAllBytes(Paths.get(pathname));

        System.out.println(new GenIntArray().freq(toIntArray(array)));
    }

    public static int[] toIntArray(byte[] array) {
        if (array.length % 4 != 0) {
            throw new IllegalArgumentException("Cannot convert to a retrievable byte array");
        }

        int len = 1 + max((int) Math.ceil(array.length / 4.0d), 1);
        int[] result = new int[len];

        for (int i = 0; i < array.length; i += 4) {
            result[i / 4] = 0
                    | array[i + 0] << (3 * 8)
                    | array[i + 1] << (2 * 8)
                    | array[i + 2] << (1 * 8)
                    | array[i + 3] << (0 * 8);
        }

        return result;
    }

    public String freq(int[] array) {
        Map<String, Integer> counter = new HashMap<>(array.length / 2);

        for (int v : array) {
            counter.merge(Integer.toString(v), 1, (i, j) -> i + j);
        }

        var mostFreq = counter.entrySet().stream()
                .sorted(comparingInt((Entry<String, Integer> e)
                                -> e.getKey().length() * e.getValue())
                        .reversed())
                .collect(toList());

        var intVarnames = new HashMap<Integer, String>(array.length);
        var varGen = new VarGenerator();
        String variable = null;
        for (Entry<String, Integer> e : mostFreq) {
            int v = Integer.parseInt(e.getKey());
            int freq = e.getValue().intValue();

            int bytesOccupied = e.getKey().length() * freq;
            variable = variable == null ? varGen.nextVar() : variable;
            int bytesSaved = bytesOccupied - ((variable.length() * freq)
                    + variable.length() + 2);

            if (bytesSaved > 0) {
                intVarnames.put(Integer.valueOf(v), variable);
                variable = null;
            }
        }

        var buf = new StringBuilder();
        var varnames = intVarnames.entrySet().stream()
                .sorted((e1, e2) ->
                        (e1.getValue().length() == e2.getValue().length()
                         ? e1.getValue().compareTo(e2.getValue())
                         : e1.getValue().length() - e2.getValue().length()))
                .collect(toList());
        var declarations = append(
                varnames,
                "        int ",
                e -> String.format("%s=%d", e.getValue(), e.getKey()),
                ',',
                String.format(";%n"),
                80);
        buf.append(declarations);

        var intvalues = append(
                Arrays.stream(array)
                        .boxed()
                        .collect(toList()),
                "            ",
                v -> intVarnames.getOrDefault(
                        v, v.toString()),
                ',',
                String.format(",%n"),
                80);

        // generate the byte array itself
        buf
                .append(String.format("        return new int[] {%n"))
                .append(intvalues)
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
     * A variable name generator.  Guarantees that the variable names
     * generated are also valid Java identifiers.
     *
     * <p>Typical usage:
     *
     * <pre>{@code
     * var counter = new Counter();
     *
     * var variableName = counter.nextVar();
     * for (int i = 0; i < N; ++i) {
     *     System.out.println(counter.nextVar());
     * }
     * }</pre>
     */
    private static final class VarGenerator {
        private static final String COUNTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789$_";

        private long counterId = 0;

        /**
         * Returns the next variable name.
         */
        public String nextVar() {
            String num;
            for (num = toBase(counterId, COUNTERS, new StringBuilder());
                    !SourceVersion.isName(num);
                    num = toBase(counterId, COUNTERS, new StringBuilder())) {
                ++counterId;
            }

            ++counterId;
            return num;
        }

        /**
         * Returns value in base represented by the length of chars.
         * The values are collected in the provided builder.
         *
         * @param value the value to be converted.  Must not be negative.
         * @param digits digit representation of the eventual number,
         * the first digit is the smallest number.  For example, the
         * decimal numbers would be passed in as "0123456789".  No
         * checks are performed to ensure an absence of duplicate
         * digits.
         */
        private static final String toBase(
                final long value,
                final String digits,
                final StringBuilder builder) {
            if (value < 0) {
                return builder.reverse().toString();
            }

            int base = digits.length();
            int mod = (int) (value % base);
            long div = value / base - 1;
            char c = digits.charAt(mod);

            builder
                    .append(c);

            return toBase(div, digits, builder);
        }
    }
}
