B label1
ADDI X0, XZR, #10
ADDI X0, XZR, #20
label1: ADDI X0, XZR, #30
BL label2 
ADDI X0, XZR, #40
ADDI X0, XZR, #50
label2: ADDI X1, XZR, #10
BR LR
