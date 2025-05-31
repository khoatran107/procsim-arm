        ADDI    X3, XZR, #100
        ADDI    X4, XZR, #500
loop:   
		STUR    X3, [X0, #0]
        ADDI    X0, X0, #4
        ADDI    X3, X3, #20
        CMP     X3, X4
        B.EQ    exit
        B       loop
exit:   