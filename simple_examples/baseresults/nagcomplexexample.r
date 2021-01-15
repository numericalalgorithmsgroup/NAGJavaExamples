Please read the source of NAGComplexExample.java for a better understanding of how to use NAGComplex

*****BASICS*****

Constructing z1 and z2;
z1 = (0.0, 0.0)
z2 = (2.0, 3.0)

Changing z1 values using NAGComplex.setRe() and NAGComplex.setIm();
z1 = (3.0, 4.0)

Copying z2 to z3 using NAGComplex.clone();
z3 = (2.0, 3.0)
Copying z1 to z4 using static method NAGComplex.clone(NAGComplex z)
z4 = (3.0, 4.0)

Array created using NAGComplex.createArray(5);
(0.0, 0.0) (0.0, 0.0) (0.0, 0.0) (0.0, 0.0) (0.0, 0.0)

The same array updated using NAGComplex[i].setRe() and NAGComplex[i].setIm();
(1.0, 9.0) (2.0, 8.0) (3.0, 7.0) (4.0, 6.0) (5.0, 5.0)

*****ARITHMETIC*****

z1 = (3.0, 4.0)
z2 = (2.0, 3.0)
z3 = (6.0, -2.0)
z4 = (4.0, -5.0)

z1 + z2 = (5.0, 7.0)
z3 + z4 = (10.0, -7.0)

z1 - z2 = (1.0, 1.0)
z3 - z4 = (2.0, 3.0)

z1 * z2 = (-6.0, 17.0)
z3 * z4 = (14.0, -38.0)

z1 / z2 = (1.3846153846153846, -0.07692307692307696)
z3 / z4 = (0.829268292682927, 0.5365853658536587)

-(z1) = (-3.0, -4.0)
-(z2) = (-2.0, -3.0)

conjugate(z1) = (3.0, -4.0)
conjugate(z2) = (2.0, -3.0)

z3 == z4? - false
z4 = (6.0, -2.0)
z3 == z4? - true
|z1| = 5.0
|z2| = 3.6055512754639896

arg(z1) = 0.9272952180016122
arg(z2) = 0.982793723247329

z5 = (-5.0, 12.0)
z6 = (-9.0, 40.0)
Sqrt(z5) = (2.0, 3.0)
Sqrt(z6) = (4.0, 5.0)