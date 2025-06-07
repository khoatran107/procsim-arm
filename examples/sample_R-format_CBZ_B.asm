start:  CBZ X2, loop
        ADD X2, X2, X1 // tinh X0 * X1
loop:   ADD X3, X3, X0
        ADD X4, X3, X0
        B start
        ADD X10, X10, X3