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

/**
 * A self modifying program which uses the Java Agents API.  For
 * details see the blog post at <a
 * href="https://medium.com/galileo-onwards/java-self-modify-ecae04189196">
 * Galileo Onwards</a>
 *
 * <h3>Compile Instructions</h3>
 * <pre>
 * javac Replacement2.java
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
        return String.format("%s%s%s", i, s, l);
    }

    private static Instrumentation ageinst;

    public static void agentmain(
            @SuppressWarnings("unused") String args,
            Instrumentation inst) {
        ageinst = inst;
    }

    private static void doTheDeed() throws Exception {
        var manifestEntries = List.of(
                "Implementation-Title: selfmod",
                "Implementation-Version: 0.0.1",
                "Agent-Class: Replacement2",
                "Can-Redefine-Classes: true",
                "Can-Retransform-Classes: true",
                "Can-Set-Native-Method-Prefix: false");

        var manifest = new Manifest();
        manifest.read(new ByteArrayInputStream(manifestEntries.stream()
                .collect(joining("\n"))
                .getBytes(UTF_8)));

        var jarFile = Paths.get("selfmod.jar");
        try (var out = newOutputStream(jarFile, WRITE, CREATE)) {
            var jout = new JarOutputStream(out);

            jout.putNextEntry(new ZipEntry("META-INF/MANIFEST.MF"));
            jout.write(manifestEntries.stream()
                    .collect(joining("\n"))
                    .getBytes(UTF_8));
            jout.closeEntry();

            jout.putNextEntry(new ZipEntry("Replacement.class"));
            jout.write(replacement());
            jout.closeEntry();
            jout.finish();
        }
        jarFile.toFile().deleteOnExit();

        var vm = VirtualMachine.attach("" + ProcessHandle.current().pid());

        vm.loadAgent("selfmod.jar");

        ageinst.redefineClasses(new ClassDefinition(
                Replacement2.class,
                replacement()));
    }

    private static final byte[] replacement() {
        byte x=10,N=11,s=12,J=16,H=18,I=21,K=25,P=33,X=36,v=40,w=41,L=45,d=47;
        byte Y=49,Q=54,M=58,n=59,Z=62,E=67,R=68,B=-72,T=72,D=73,G=-74,m=76;
        byte U=77,S=-79,F=79,q=83,V=84,W=86,a=97,C=98,y=99,t=100,f=101,e=103;
        byte A=104,i=105,g=106,u=107,h=108,r=109,b=110,l=111,z=112,k=114;
        byte o=115,c=116,p=117,j=118,O=121;
        return new byte[] {
            -54,-2,-70,-66,0,0,0,55,0,124,x,0,28,0,L,8,0,46,8,0,d,8,0,48,N,0,
            Y,0,50,7,0,51,x,0,6,0,L,8,0,52,x,0,6,0,53,9,0,Q,0,55,x,0,56,0,57,
            x,0,6,0,M,N,0,Y,0,n,7,0,60,x,0,28,0,61,x,0,Z,0,63,x,0,64,0,65,x,0,
            64,0,66,x,0,E,0,R,6,63,-64,u,-18,-66,u,-30,g,x,0,14,0,69,x,0,Z,0,
            70,H,0,0,0,74,N,0,Y,0,75,H,0,1,0,U,9,0,6,0,78,7,0,F,1,0,7,a,e,f,i,
            b,o,c,1,0,38,m,g,a,j,a,d,h,a,b,e,d,i,b,o,c,k,p,r,f,b,c,d,D,b,o,c,
            k,p,r,f,b,c,a,c,i,l,b,n,1,0,6,60,i,b,i,c,Z,1,0,3,v,w,W,1,0,4,E,l,
            t,f,1,0,4,r,a,i,b,1,0,22,v,91,m,g,a,j,a,d,h,a,b,e,d,q,c,k,i,b,e,n,
            w,W,1,0,x,69,120,y,f,z,c,i,l,b,o,7,0,80,1,0,4,t,119,i,r,1,0,55,v,
            D,m,g,a,j,a,d,h,a,b,e,d,q,c,k,i,b,e,n,m,g,a,j,a,d,p,c,i,h,d,m,i,o,
            c,n,w,m,g,a,j,a,d,h,a,b,e,d,q,c,k,i,b,e,n,1,0,9,q,i,e,b,a,c,p,k,f,
            1,0,q,60,V,M,m,g,a,j,a,d,h,a,b,e,d,F,C,g,f,y,c,n,Z,v,D,m,g,a,j,a,
            d,h,a,b,e,d,q,c,k,i,b,e,n,m,g,a,j,a,d,p,c,i,h,d,m,i,o,c,60,43,V,V,
            n,Z,n,w,m,g,a,j,a,d,h,a,b,e,d,q,c,k,i,b,e,n,1,0,9,a,e,f,b,c,r,a,i,
            b,1,0,n,v,m,g,a,j,a,d,h,a,b,e,d,q,c,k,i,b,e,n,m,g,a,j,a,d,h,a,b,e,
            d,i,b,o,c,k,p,r,f,b,c,d,D,b,o,c,k,p,r,f,b,c,a,c,i,l,b,n,w,W,1,0,9,
            t,l,V,A,f,R,f,f,t,s,0,31,0,32,1,0,1,50,1,0,1,Q,1,0,2,53,51,7,0,81,
            s,0,82,0,q,1,0,s,82,f,z,h,a,y,f,r,f,b,c,50,1,0,6,46,Y,52,Y,53,57,
            s,0,38,0,39,7,0,V,s,0,85,0,W,7,0,87,s,0,88,0,89,s,0,44,0,32,s,0,
            90,0,91,1,0,J,g,a,j,a,d,h,a,b,e,d,q,c,k,i,b,e,s,0,92,0,93,7,0,94,
            s,0,95,0,96,7,0,a,s,0,C,0,y,s,0,t,0,f,7,0,102,s,0,e,0,A,s,0,C,0,i,
            s,0,C,0,g,1,0,J,66,l,l,c,o,c,k,a,z,U,f,c,A,l,t,o,15,6,0,u,8,0,h,s,
            0,r,0,b,s,0,82,0,l,8,0,z,s,0,r,0,113,s,0,29,0,30,1,0,J,g,a,j,a,d,
            h,a,b,e,d,F,C,g,f,y,c,1,0,19,g,a,j,a,d,h,a,b,e,d,69,120,y,f,z,c,i,
            l,b,1,0,14,g,a,j,a,d,p,c,i,h,d,m,i,o,c,1,0,2,l,102,1,0,T,v,m,g,a,
            j,a,d,h,a,b,e,d,F,C,g,f,y,c,n,m,g,a,j,a,d,h,a,b,e,d,F,C,g,f,y,c,n,
            m,g,a,j,a,d,h,a,b,e,d,F,C,g,f,y,c,n,w,m,g,a,j,a,d,p,c,i,h,d,m,i,o,
            c,n,1,0,J,g,a,j,a,d,h,a,b,e,d,q,O,o,c,f,r,1,0,3,l,p,c,1,0,I,m,g,a,
            j,a,d,i,l,d,80,k,i,b,c,q,c,k,f,a,r,n,1,0,19,g,a,j,a,d,i,l,d,80,k,
            i,b,c,q,c,k,f,a,r,1,0,7,z,k,i,b,c,h,b,1,0,I,v,m,g,a,j,a,d,h,a,b,e,
            d,q,c,k,i,b,e,n,w,W,1,0,3,e,f,c,1,0,I,v,D,w,m,g,a,j,a,d,h,a,b,e,d,
            F,C,g,f,y,c,n,1,0,8,c,l,q,c,k,i,b,e,1,0,20,v,w,m,g,a,j,a,d,h,a,b,
            e,d,q,c,k,i,b,e,n,1,0,17,g,a,j,a,d,h,a,b,e,d,D,b,c,f,e,f,k,1,0,8,
            z,a,k,o,f,D,b,c,1,0,I,v,m,g,a,j,a,d,h,a,b,e,d,q,c,k,i,b,e,n,w,D,1,
            0,19,g,a,j,a,d,h,a,b,e,d,E,A,a,k,a,y,c,f,k,1,0,7,j,a,h,p,f,F,102,
            1,0,24,v,E,w,m,g,a,j,a,d,h,a,b,e,d,E,A,a,k,a,y,c,f,k,n,1,0,9,y,A,
            a,k,W,a,h,p,f,1,0,3,v,w,E,1,0,J,g,a,j,a,d,h,a,b,e,d,R,l,p,C,h,f,1,
            0,N,z,a,k,o,f,R,l,p,C,h,f,1,0,I,v,m,g,a,j,a,d,h,a,b,e,d,q,c,k,i,b,
            e,n,w,R,1,0,I,v,R,w,m,g,a,j,a,d,h,a,b,e,d,q,c,k,i,b,e,n,1,0,22,v,
            D,w,m,g,a,j,a,d,h,a,b,e,d,D,b,c,f,e,f,k,n,x,0,k,0,o,1,0,1,1,1,0,
            23,r,a,u,f,E,l,b,y,a,c,87,i,c,A,E,l,b,o,c,a,b,c,o,1,0,I,v,E,w,m,g,
            a,j,a,d,h,a,b,e,d,q,c,k,i,b,e,n,1,0,Q,v,m,g,a,j,a,d,h,a,b,e,d,F,C,
            g,f,y,c,n,m,g,a,j,a,d,h,a,b,e,d,F,C,g,f,y,c,n,w,m,g,a,j,a,d,p,c,i,
            h,d,m,i,o,c,n,1,0,2,1,1,1,0,Q,v,m,g,a,j,a,d,h,a,b,e,d,q,c,k,i,b,e,
            n,m,g,a,j,a,d,p,c,i,h,d,m,i,o,c,n,w,m,g,a,j,a,d,h,a,b,e,d,q,c,k,i,
            b,e,n,7,0,c,s,0,r,0,120,1,0,X,g,a,j,a,d,h,a,b,e,d,i,b,j,l,u,f,d,q,
            c,k,i,b,e,E,l,b,y,a,c,70,a,y,c,l,k,O,7,0,122,1,0,6,m,l,l,u,p,z,1,
            0,s,D,b,b,f,k,E,h,a,o,o,f,o,1,0,-104,v,m,g,a,j,a,d,h,a,b,e,d,i,b,
            j,l,u,f,d,U,f,c,A,l,t,T,a,b,t,h,f,o,X,m,l,l,u,p,z,n,m,g,a,j,a,d,h,
            a,b,e,d,q,c,k,i,b,e,n,m,g,a,j,a,d,h,a,b,e,d,i,b,j,l,u,f,d,U,f,c,A,
            l,t,V,O,z,f,n,m,g,a,j,a,d,h,a,b,e,d,q,c,k,i,b,e,n,91,m,g,a,j,a,d,
            h,a,b,e,d,F,C,g,f,y,c,n,w,m,g,a,j,a,d,h,a,b,e,d,i,b,j,l,u,f,d,E,a,
            h,h,q,i,c,f,n,7,0,123,1,0,37,g,a,j,a,d,h,a,b,e,d,i,b,j,l,u,f,d,U,
            f,c,A,l,t,T,a,b,t,h,f,o,X,m,l,l,u,p,z,1,0,30,g,a,j,a,d,h,a,b,e,d,
            i,b,j,l,u,f,d,U,f,c,A,l,t,T,a,b,t,h,f,o,0,P,0,6,0,28,0,0,0,1,0,x,
            0,29,0,30,0,0,0,5,0,1,0,31,0,32,0,1,0,P,0,0,0,17,0,1,0,1,0,0,0,5,
            42,-73,0,1,S,0,0,0,0,0,9,0,34,0,35,0,2,0,P,0,0,0,D,0,4,0,7,0,0,0,
            61,H,2,H,3,H,4,B,0,5,78,-69,0,6,89,-73,0,7,M,4,K,4,6,H,8,L,G,0,9,
            M,5,-78,0,x,K,5,G,0,N,B,0,s,K,4,6,H,8,L,G,0,9,M,6,-78,0,x,K,6,G,0,
            N,S,0,0,0,0,0,X,0,0,0,4,0,1,0,37,0,H,0,38,0,39,0,2,0,P,0,0,0,g,0,
            4,0,8,0,0,0,94,L,4,-71,0,13,2,0,-64,0,14,M,4,L,3,-71,0,13,2,0,G,0,
            15,B,0,J,Q,5,L,5,-71,0,13,2,0,G,0,15,M,6,K,6,B,0,J,-110,B,0,17,G,
            0,H,Q,7,27,-121,44,B,0,19,l,20,0,20,u,B,0,22,K,4,B,0,J,I,5,t,B,0,
            23,I,7,-70,0,24,0,0,B,0,K,-70,0,26,0,0,-80,0,0,0,0,0,v,0,0,0,2,0,
            w,0,9,0,42,0,43,0,1,0,P,0,0,0,17,0,1,0,2,0,0,0,5,43,-77,0,27,S,0,
            0,0,0,0,x,0,44,0,32,0,2,0,P,0,0,0,13,0,0,0,0,0,0,0,1,S,0,0,0,0,0,
            X,0,0,0,4,0,1,0,37,0,2,0,119,0,0,0,x,0,1,0,p,0,O,0,j,0,K,0,71,0,0,
            0,14,0,2,0,T,0,1,0,D,0,T,0,1,0,m,
        };
    }
}
