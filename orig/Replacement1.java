import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.sun.jdi.connect.IllegalConnectorArgumentsException;

/*
 * <h3>Compilation Instructions</h3>
 * <pre>
 * javac Replacement1.java
 * </pre>
 *
 * <h3>Run instructions</h3>
 * <pre>
 * java -agentlib:jdwp=transport=dt_socket,address=2718,server=y,suspend=n     \
 *         Replacement1
 * </pre>
 */
public class Replacement1 {
    public static void main(String[] args)
            throws Exception {
        final var i = 3;
        final var s = ".14159";
        final var l = List.of("2", "6", "53");

        final var r = new Replacement1();

        final var run1 = r.dwim(i, s, l);
        System.out.println(run1); // prints 3.14159[2, 6, 53]

        doTheDeed();

        final var run2 = r.dwim(i, s, l);
        System.out.println(run2); // prints 2.718281828[4, 5]
    }

    private final <T> String dwim(
            final int i,
            final String s,
            final List<? extends T> l) {
        // sure, I could just return 2.718281828[4, 5]
        // but what's the fun in that?
        String s1 = (String) l.get(1);
        int    i0 = Integer.parseInt(l.get(0).toString());
        String s2 = l.get(2).toString();
        char   c2 = Character.valueOf((char) Integer.parseInt(s2));
        return String.valueOf(i / Double.parseDouble(s) * 0.1282938413421733d)
                + List.of(Integer.parseInt(s1) - i0, "" + c2);
    }

    private static final void doTheDeed() throws Exception {}

    private static final byte[] replacement() { return null; }
}
