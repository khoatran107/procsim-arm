        ADDI    X0, XZR, #0       // X0 = address
        ADDI    X1, XZR, #4       // X1 = address increment
        ADDI    X2, XZR, #22      // X2 = number increment
        ADDI    X3, XZR, #100     // X3 = current number
        ADDI    X4, XZR, #1000    // X4 = max number
main:   
		STUR    X3, [X0, #0]
        ADD     X0, X0, X1
        ADD     X3, X3, X2
        CMP     X3, X4
        B.EQ    exit
        B       main
exit: