import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.sun.jdi.connect.AttachingConnector;

/**
 * A Java program that modifies itself using the JDI API.  For details
 * see the blog post at <a
 * href="https://medium.com/galileo-onwards/java-self-modify-ecae04189196">
 * Galileo Onwards</a>
 *
 * <p>The code has been tested on OpenJDK 11 and AWS Corretto 11.
 * Given that the program uses the {@code var} construct, introduced
 * in Java 10, it won't work on earlier versions.
 *
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
 *
 * <h3>Alternative run instructions</h3>
 *
 * <a href="https://openjdk.java.net/jeps/330">JEP 330</a> released in
 * Java 11 allows direct execution of single file Java programs
 * without the compilation step.  This program can also be run as follows:
 *
 * <pre>
 * java -agentlib:jdwp=transport=dt_socket,address=2718,server=y,suspend=n      \
 *         Replacement1.java
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
        return String.format("%s%s%s", i, s, l);
    }

    private static final void doTheDeed() throws Exception {
        var vmm = com.sun.jdi.Bootstrap.virtualMachineManager();

        AttachingConnector socketConnector = null;
        for (var connector : vmm.attachingConnectors()) {
            if (connector.description().contains("socket")) {
                socketConnector = connector;
                break;
            }
        }

        var defaultArguments = socketConnector.defaultArguments();
        defaultArguments.get("port").setValue("2718");

        var vm = socketConnector.attach(defaultArguments);
        var replacementReferenceType = vm.classesByName("Replacement1").stream()
                .findFirst()
                .orElseThrow();

        vm.redefineClasses(Map.of(replacementReferenceType, replacement()));
    }

    private static final byte[] replacement() {
        byte y=10,L=11,r=12,T=14,J=16,H=18,I=21,K=25,N=30,U=36,v=40,w=41,O=45;
        byte d=47,P=54,M=58,n=59,D=67,Q=68,Z=-70,A=-72,V=72,E=73,G=-74,m=76;
        byte R=77,F=79,p=83,S=84,W=86,a=97,C=98,u=99,s=100,f=101,e=103,z=104;
        byte j=105,g=106,t=107,h=108,B=109,b=110,l=111,x=112,k=114,o=115;
        byte c=116,q=117,i=118,X=120,Y=121;
        return new byte[] {
            -54,-2,Z,-66,0,0,0,55,0,X,y,0,27,0,42,8,0,43,8,0,44,8,0,O,L,0,46,
            0,d,7,0,48,y,0,6,0,42,8,0,49,y,0,6,0,50,9,0,51,0,52,y,0,53,0,P,y,
            0,6,0,55,L,0,46,0,56,7,0,57,y,0,27,0,M,y,0,n,0,60,y,0,61,0,62,y,0,
            61,0,63,y,0,64,0,65,6,63,-64,t,-18,-66,t,-30,g,y,0,T,0,66,y,0,n,0,
            D,H,0,0,0,71,L,0,46,0,V,H,0,1,0,74,7,0,75,1,0,6,60,j,b,j,c,62,1,0,
            3,v,w,W,1,0,4,D,l,s,f,1,0,4,B,a,j,b,1,0,22,v,91,m,g,a,i,a,d,h,a,b,
            e,d,p,c,k,j,b,e,n,w,W,1,0,y,69,X,u,f,x,c,j,l,b,o,7,0,m,1,0,4,s,
            119,j,B,1,0,55,v,E,m,g,a,i,a,d,h,a,b,e,d,p,c,k,j,b,e,n,m,g,a,i,a,
            d,q,c,j,h,d,m,j,o,c,n,w,m,g,a,i,a,d,h,a,b,e,d,p,c,k,j,b,e,n,1,0,9,
            p,j,e,b,a,c,q,k,f,1,0,p,60,S,M,m,g,a,i,a,d,h,a,b,e,d,F,C,g,f,u,c,
            n,62,v,E,m,g,a,i,a,d,h,a,b,e,d,p,c,k,j,b,e,n,m,g,a,i,a,d,q,c,j,h,
            d,m,j,o,c,60,43,S,S,n,62,n,w,m,g,a,i,a,d,h,a,b,e,d,p,c,k,j,b,e,n,
            1,0,9,s,l,S,z,f,Q,f,f,s,1,0,L,k,f,x,h,a,u,f,B,f,b,c,1,0,4,v,w,91,
            66,r,0,28,0,29,1,0,1,50,1,0,1,P,1,0,2,53,51,7,0,R,r,0,78,0,F,1,0,
            r,82,f,x,h,a,u,f,B,f,b,c,49,1,0,6,46,49,52,49,53,57,r,0,35,0,U,7,
            0,80,r,0,81,0,82,7,0,p,r,0,S,0,85,r,0,39,0,29,r,0,W,0,87,1,0,J,g,
            a,i,a,d,h,a,b,e,d,p,c,k,j,b,e,r,0,88,0,89,7,0,90,r,0,91,0,92,7,0,
            93,r,0,94,0,95,r,0,96,0,a,7,0,C,r,0,u,0,s,r,0,94,0,f,r,0,94,0,102,
            1,0,J,66,l,l,c,o,c,k,a,x,R,f,c,z,l,s,o,15,6,0,e,8,0,z,r,0,j,0,g,r,
            0,78,0,t,8,0,h,r,0,j,0,B,1,0,J,g,a,i,a,d,h,a,b,e,d,F,C,g,f,u,c,1,
            0,19,g,a,i,a,d,h,a,b,e,d,69,X,u,f,x,c,j,l,b,1,0,T,g,a,i,a,d,q,c,j,
            h,d,m,j,o,c,1,0,2,l,102,1,0,V,v,m,g,a,i,a,d,h,a,b,e,d,F,C,g,f,u,c,
            n,m,g,a,i,a,d,h,a,b,e,d,F,C,g,f,u,c,n,m,g,a,i,a,d,h,a,b,e,d,F,C,g,
            f,u,c,n,w,m,g,a,i,a,d,q,c,j,h,d,m,j,o,c,n,1,0,J,g,a,i,a,d,h,a,b,e,
            d,p,Y,o,c,f,B,1,0,3,l,q,c,1,0,I,m,g,a,i,a,d,j,l,d,80,k,j,b,c,p,c,
            k,f,a,B,n,1,0,19,g,a,i,a,d,j,l,d,80,k,j,b,c,p,c,k,f,a,B,1,0,7,x,k,
            j,b,c,h,b,1,0,I,v,m,g,a,i,a,d,h,a,b,e,d,p,c,k,j,b,e,n,w,W,1,0,3,e,
            f,c,1,0,I,v,E,w,m,g,a,i,a,d,h,a,b,e,d,F,C,g,f,u,c,n,1,0,8,c,l,p,c,
            k,j,b,e,1,0,20,v,w,m,g,a,i,a,d,h,a,b,e,d,p,c,k,j,b,e,n,1,0,17,g,a,
            i,a,d,h,a,b,e,d,E,b,c,f,e,f,k,1,0,8,x,a,k,o,f,E,b,c,1,0,I,v,m,g,a,
            i,a,d,h,a,b,e,d,p,c,k,j,b,e,n,w,E,1,0,19,g,a,i,a,d,h,a,b,e,d,D,z,
            a,k,a,u,c,f,k,1,0,7,i,a,h,q,f,F,102,1,0,24,v,D,w,m,g,a,i,a,d,h,a,
            b,e,d,D,z,a,k,a,u,c,f,k,n,1,0,9,u,z,a,k,W,a,h,q,f,1,0,3,v,w,D,1,0,
            J,g,a,i,a,d,h,a,b,e,d,Q,l,q,C,h,f,1,0,L,x,a,k,o,f,Q,l,q,C,h,f,1,0,
            I,v,m,g,a,i,a,d,h,a,b,e,d,p,c,k,j,b,e,n,w,Q,1,0,I,v,Q,w,m,g,a,i,a,
            d,h,a,b,e,d,p,c,k,j,b,e,n,1,0,22,v,E,w,m,g,a,i,a,d,h,a,b,e,d,E,b,
            c,f,e,f,k,n,y,0,b,0,l,1,0,1,1,1,0,23,B,a,t,f,D,l,b,u,a,c,87,j,c,z,
            D,l,b,o,c,a,b,c,o,1,0,I,v,D,w,m,g,a,i,a,d,h,a,b,e,d,p,c,k,j,b,e,n,
            1,0,P,v,m,g,a,i,a,d,h,a,b,e,d,F,C,g,f,u,c,n,m,g,a,i,a,d,h,a,b,e,d,
            F,C,g,f,u,c,n,w,m,g,a,i,a,d,q,c,j,h,d,m,j,o,c,n,1,0,2,1,1,1,0,P,v,
            m,g,a,i,a,d,h,a,b,e,d,p,c,k,j,b,e,n,m,g,a,i,a,d,q,c,j,h,d,m,j,o,c,
            n,w,m,g,a,i,a,d,h,a,b,e,d,p,c,k,j,b,e,n,7,0,x,r,0,j,0,c,1,0,U,g,a,
            i,a,d,h,a,b,e,d,j,b,i,l,t,f,d,p,c,k,j,b,e,D,l,b,u,a,c,70,a,u,c,l,
            k,Y,7,0,i,1,0,6,m,l,l,t,q,x,1,0,r,E,b,b,f,k,D,h,a,o,o,f,o,1,0,
            -104,v,m,g,a,i,a,d,h,a,b,e,d,j,b,i,l,t,f,d,R,f,c,z,l,s,V,a,b,s,h,
            f,o,U,m,l,l,t,q,x,n,m,g,a,i,a,d,h,a,b,e,d,p,c,k,j,b,e,n,m,g,a,i,a,
            d,h,a,b,e,d,j,b,i,l,t,f,d,R,f,c,z,l,s,S,Y,x,f,n,m,g,a,i,a,d,h,a,b,
            e,d,p,c,k,j,b,e,n,91,m,g,a,i,a,d,h,a,b,e,d,F,C,g,f,u,c,n,w,m,g,a,
            i,a,d,h,a,b,e,d,j,b,i,l,t,f,d,D,a,h,h,p,j,c,f,n,7,0,119,1,0,37,g,
            a,i,a,d,h,a,b,e,d,j,b,i,l,t,f,d,R,f,c,z,l,s,V,a,b,s,h,f,o,U,m,l,l,
            t,q,x,1,0,N,g,a,i,a,d,h,a,b,e,d,j,b,i,l,t,f,d,R,f,c,z,l,s,V,a,b,s,
            h,f,o,0,33,0,6,0,27,0,0,0,0,0,5,0,1,0,28,0,29,0,1,0,N,0,0,0,17,0,
            1,0,1,0,0,0,5,42,-73,0,1,-79,0,0,0,0,0,9,0,31,0,32,0,2,0,N,0,0,0,
            E,0,4,0,7,0,0,0,61,H,2,H,3,H,4,A,0,5,78,-69,0,6,89,-73,0,7,M,4,K,
            4,6,H,8,O,G,0,9,M,5,-78,0,y,K,5,G,0,L,A,0,r,K,4,6,H,8,O,G,0,9,M,6,
            -78,0,y,K,6,G,0,L,-79,0,0,0,0,0,33,0,0,0,4,0,1,0,34,0,H,0,35,0,U,
            0,2,0,N,0,0,0,g,0,4,0,8,0,0,0,94,O,4,-71,0,13,2,0,-64,0,T,M,4,O,3,
            -71,0,13,2,0,G,0,15,A,0,J,P,5,O,5,-71,0,13,2,0,G,0,15,M,6,K,6,A,0,
            J,-110,A,0,17,G,0,H,P,7,27,-121,44,A,0,19,l,20,0,20,t,A,0,22,K,4,
            A,0,J,I,5,s,A,0,23,I,7,Z,0,24,0,0,A,0,K,Z,0,26,0,0,-80,0,0,0,0,0,
            37,0,0,0,2,0,38,0,26,0,39,0,29,0,2,0,N,0,0,0,13,0,0,0,0,0,0,0,1,
            -79,0,0,0,0,0,33,0,0,0,4,0,1,0,34,0,26,0,v,0,w,0,1,0,N,0,0,0,T,0,
            1,0,0,0,0,0,2,1,-80,0,0,0,0,0,2,0,o,0,0,0,y,0,1,0,113,0,q,0,k,0,K,
            0,Q,0,0,0,T,0,2,0,69,0,1,0,70,0,69,0,1,0,E,
        };
    }
}
