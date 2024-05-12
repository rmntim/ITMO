ORG 0x119

ARG1:   WORD 0x0100 ; 256
ARG2:   WORD 0xFF00 ; -256
ARG3:   WORD 0x5B0A ; 23306
ARG4:   WORD 0x11CD ; 4557
ARG5:   WORD 0x0250 ; 592
ARG6:   WORD 0x0180 ; 384
CHECK1: WORD 0x0    ; 200
CHECK2: WORD 0x0    ; -32764
CHECK3: WORD 0x0    ; 0
FINAL:  WORD 0x0
RES1:   WORD 0x0
RES2:   WORD 0x0
RES3:   WORD 0x0

START:
        CLA
        CLC
        CALL TEST1
        CLA
        CLC
        CALL TEST2
        CLA
        CLC
        CALL TEST3
        LD #0x1
        AND CHECK1
        AND CHECK2
        AND CHECK3
        ST FINAL
        HLT

TEST1:  LD ARG1
        PUSH
        ST RES1
        LD ARG2
        PUSH
        CLC
        CMC
        ADC RES1
        ST RES1
        CLC
        CMC
        WORD 0x0F10 ; ADCSP
        POP
        CMP RES1
        BEQ DONE1
        ST RES1
        POP
        POP
        CLA
        ST CHECK1
        RET

DONE1:  ST RES1
        POP
        POP
        LD #0x1
        ST CHECK1
        CLA
        RET

TEST2:  LD ARG3
        PUSH
        ST RES2
        LD ARG4
        PUSH
        CLC
        ADC RES2
        ST RES2
        CLC
        WORD 0x0F10 ; ADCSP
        POP
        CMP RES2
        BEQ DONE2
        ST RES2
        POP
        POP
        CLA
        ST CHECK2
        RET

DONE2:  ST RES2
        POP
        POP
        LD #0x1
        ST CHECK2
        CLA
        RET

TEST3:  LD ARG5
        PUSH
        ST RES3
        LD ARG6
        PUSH
        CLC
        CMC
        ADC RES3
        ST RES3
        CLC
        CMC
        WORD 0x0F10 ; ADCSP
        POP
        CMP RES3
        BEQ DONE3
        ST RES3
        POP
        POP
        CLA
        ST CHECK3
        RET

DONE3:  ST RES3
        POP
        POP
        LD #0x1
        ST CHECK3
        CLA
        RET
