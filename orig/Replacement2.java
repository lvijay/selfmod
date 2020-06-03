import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.newOutputStream;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.WRITE;
import static java.util.stream.Collectors.joining;

import java.io.ByteArrayInputStream;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;
import java.nio.file.Paths;
import java.util.List;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;

import com.sun.tools.attach.VirtualMachine;

/*
 * <h3>Compile Instructions</h3>
 * <pre>
 * javac -d . Replacement2.java
 * </pre>
 *
 * <h3>Run Instructions</h3>
 * <pre>
 * java -cp . -Djdk.attach.allowAttachSelf=true Replacement2
 * </pre>
 */
public class Replacement2 {
    public static void main(String[] args)
            throws Exception {
        final var i = 3;
        final var s = ".14159";
        final var l = List.of("2", "6", "53");

        final var r = new Replacement2();

        final var run1 = r.dwim(i, s, l);
        System.out.println(run1); // prints 3.14159[2, 6, 53]

        doTheDeed();

        final var run2 = r.dwim(i, s, l);
        System.out.println(run2); // prints 2.71828[1, 8, 2, 8]
    }

    private final <T> String dwim(
            final int i,
            final String s,
            final List<? extends T> l) {
        String s1 = (String) l.get(1);
        int    i0 = Integer.parseInt(l.get(0).toString());
        String s2 = l.get(2).toString();
        char   c2 = Character.valueOf((char) Integer.parseInt(s2));
        return String.valueOf(i / Double.parseDouble(s) * 0.1282938413421733d)
                + List.of(Integer.parseInt(s1) - i0, "" + c2);
    }

    private static Instrumentation ageinst;

    public static void agentmain(
            @SuppressWarnings("unused") String args,
            Instrumentation inst) {
        ageinst = inst;
    }

    private static void doTheDeed() throws Exception {}
}
