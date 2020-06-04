import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

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
        var bytecode = new GZIPInputStream(new ByteArrayInputStream(replacement()))
                .readAllBytes();

        vm.redefineClasses(Map.of(replacementReferenceType, bytecode));
    }

    private static final byte[] replacement() {
        byte A=-19,B=-21,C=-30,D=-41,E=-59,F=-65,G=-67,H=-76,I=102,J=20,K=31;
        byte L=41,M=-100,N=-109,O=-107,P=-114,Q=-113,R=-119,S=-118,T=-125;
        byte U=-124,V=-121,W=110,X=117,Y=118,Z=126,a=-115,aa=-23,ab=-29;
        byte ac=-31,ad=-35,ae=-46,af=-48,ag=-50,ah=-78,ai=-84,aj=-88,ak=104;
        byte al=106,am=-101,an=-102,ao=-105,ap=-127,aq=-128,b=-110,c=101;
        byte d=-103,e=-117,f=119,g=-126,h=-45,i=-68,j=-72,k=29,l=-90,m=114;
        byte n=-15,o=-25,p=-49,q=-52,r=-79,s=-87,t=109,u=-108,v=-106,w=-111;
        byte x=-116,y=-96,z=115;
        return new byte[] {
            K,e,8,8,5,113,-40,94,2,3,82,c,112,108,97,99,c,t,c,W,116,49,46,99,
            108,97,z,z,0,-99,85,ad,Y,19,85,J,-2,78,Z,I,ae,aa,80,-38,af,22,66,
            5,69,e,78,aq,16,11,52,O,-92,J,t,2,54,90,40,b,88,-60,42,58,77,P,
            -55,36,N,d,-104,57,L,-12,9,i,-9,-54,-57,80,47,74,ai,107,r,j,-10,
            49,124,0,K,ap,E,62,d,u,52,t,-22,-22,98,-42,an,z,I,127,-13,A,G,F,G,
            o,M,51,-1,i,-38,125,1,96,1,p,52,q,-32,82,4,ao,35,j,18,65,98,J,87,
            w,84,n,r,6,69,-94,z,m,-66,54,g,B,j,-95,97,30,L,105,46,72,ag,39,42,
            W,74,p,H,-122,12,22,53,-36,-62,b,28,W,107,-8,J,-97,L,-73,95,-42,
            -1,123,81,-1,-73,l,97,12,-53,b,w,a,82,ah,p,-91,ab,74,J,12,95,aj,
            -8,b,65,89,H,28,75,44,49,4,a,-8,58,67,40,B,v,57,77,13,h,m,24,l,a,
            a,-43,an,-71,c,38,t,h,s,36,11,-94,c,57,O,x,-92,105,f,-98,O,120,83,
            88,-82,ab,s,88,37,Z,-7,s,-43,96,88,48,-14,V,-7,62,ae,22,v,-99,92,
            -75,60,w,R,K,l,48,x,J,ai,S,99,S,Y,e,b,23,22,e,aa,125,M,-75,-51,26,
            47,R,q,ae,49,98,47,94,46,22,51,75,71,100,40,-69,E,42,p,113,94,I,
            24,t,n,l,t,v,120,T,59,g,-60,27,n,a,c,k,-17,-32,28,3,-69,70,f,S,33,
            48,127,93,E,61,k,-9,r,-58,y,63,-20,h,o,aj,c,87,o,W,q,-51,-33,-44,
            n,1,I,85,60,af,n,21,30,-86,40,-24,40,C,107,k,K,ac,M,P,X,60,98,24,
            63,aj,66,-57,55,120,ai,C,91,k,27,-8,78,E,-9,58,-98,-32,7,k,63,-62,
            84,r,s,-93,U,ah,68,j,28,Z,34,-9,c,D,21,-98,ak,d,-51,123,92,84,ad,
            ah,f,82,65,37,g,-86,14,11,53,s,ai,30,ap,45,a,-58,64,42,F,93,12,
            -89,-6,af,am,Q,E,48,54,-40,48,42,-44,-91,84,43,-58,ac,Z,K,7,R,K,
            -4,-74,T,69,111,123,g,N,j,y,-37,l,68,83,62,D,m,N,15,aj,23,g,58,
            -62,-51,70,I,79,o,32,q,y,54,-91,c,N,C,L,99,-8,18,12,86,j,x,al,-28,
            ab,V,X,49,68,U,B,z,25,38,a,-95,B,97,-94,Q,-27,k,-63,43,i,69,94,77,
            -77,-27,n,i,35,P,72,am,K,ak,107,-74,al,-74,q,b,-112,P,-22,v,105,
            -73,-7,26,-75,-14,x,w,ad,-97,-18,13,71,-82,-64,18,25,B,b,-40,ad,t,
            -39,ap,I,-27,-36,-10,l,77,47,70,-69,18,-10,ai,-95,42,m,18,p,13,45,
            al,122,-80,k,G,-62,50,26,28,j,H,-80,25,-61,aa,-122,89,o,89,D,L,d,
            C,w,37,-86,-12,-28,9,h,17,-98,x,d,k,26,51,c,i,-27,82,8,-56,124,L,
            ab,-83,78,4,21,77,-71,H,aa,67,q,-10,-33,89,ag,v,91,o,61,S,95,-61,
            93,al,-82,-37,-38,86,r,69,-5,m,-43,X,B,A,38,A,D,i,ab,-16,86,-42,
            54,61,Q,83,97,F,A,ao,af,e,ac,111,s,21,h,L,-37,-36,am,-11,k,51,71,
            L,61,-20,88,-36,W,-14,33,-12,a,35,-5,ah,63,68,-42,H,A,g,37,56,21,
            -7,u,ac,C,r,H,49,M,-1,127,30,46,af,95,97,6,-14,10,h,ac,78,V,25,a,
            o,-55,an,y,d,73,-12,ae,z,-80,63,-70,U,17,i,e,-9,16,-24,-66,ag,35,
            4,O,-26,91,-47,64,52,24,13,X,16,-66,-1,55,u,-57,p,-95,l,67,r,-112,
            18,a,36,Y,48,b,14,-1,9,45,22,-34,-63,ak,7,122,K,85,36,-86,16,-22,
            V,G,64,f,-120,m,G,Q,aj,60,26,123,9,al,U,69,ak,Z,b,8,-3,-123,19,1,
            i,-60,88,58,u,8,Y,-97,f,112,ah,T,n,84,56,17,-34,51,h,74,76,33,-24,
            D,14,38,Y,16,77,s,51,F,92,aa,-32,u,59,R,-55,122,7,h,49,ae,55,62,
            21,46,f,112,122,74,ad,E,25,y,T,-40,46,ag,2,F,f,21,92,-92,59,aq,15,
            9,y,83,j,ao,-1,4,-4,e,k,J,121,22,6,C,G,38,a,X,91,68,ag,q,15,J,aq,
            71,-93,70,-24,p,ak,-93,-123,24,m,93,86,0,f,8,-69,-37,k,-13,-81,1,
            D,A,d,7,F,7,0,0,
        };
    }
}
