# Self-modifying Java programs

The programs in this repository are Java programs that modify
themselves.

For details see the individual files
[Replacement1.java](finl/Replacement1.java) and
[Replacement2.java](finl/Replacement2.java).

Additional information about the programs is available on the blog
post at [Galileo
Onwards](https://medium.com/galileo-onwards/java-self-modify-ecae04189196).

## Building and running

The respective java files also include build and run instructions.

## Modifying the files

The files which we run, the ones in the [`finl`](./finl) folder,
contain a method named `replacement()`.  The byte array returned by
this method is the byte contents of the corresponding fil in the
[`orig`](./orig) folder.

### Example

For example, here's how I update
[Replacement1.java](finl/Replacement1.java):

1. Make changes to the original
   [Replacement1.java](orig/Replacement1.java).
2. Compile the file.
3. Generate the byte code contents of this file using the
   [GenByteArray.java](./GenByteArray.java) script.  See below for an
   example.
4. Paste these contents into the final
   [Replacement1.java's](finl/Replacement1.java) `replacement()` method.

**How to run GenByteArray.java**

```shell
$ java GenByteArray.java orig/Replacement1.class
        byte y=10,L=11,r=12,T=14,J=16,H=18,I=21,K=25,N=30,U=36,v=40,w=41,O=45;
        byte d=47,P=54,M=58,n=59,D=67,Q=68,Z=-70,A=-72,V=72,E=73,G=-74,m=76;
        byte R=77,F=79,p=83,S=84,W=86,a=97,C=98,u=99,s=100,f=101,e=103,z=104;
        byte j=105,g=106,t=107,h=108,B=109,b=110,l=111,x=112,k=114,o=115;
        byte c=116,q=117,i=118,X=120,Y=121;
        return new byte[] {
            -54,-2,Z,-66,0,0,0,55,0,X,y,0,27,0,42,8,0,43,8,0,44,8,0,O,L,0,46,
            0,d,7,0,48,y,0,6,0,42,8,0,49,y,0,6,0,50,9,0,51,0,52,y,0,53,0,P,y,
    ... 59 lines elided ...
            0,Q,0,0,0,T,0,2,0,69,0,1,0,70,0,69,0,1,0,E,
        };
```

## License

For license information, see [LICENSE.md](LICENSE.md).

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as
published by the Free Software Foundation, either version 3 of the
License, or (at your option) any later version.
