org 0x570
iter: word $data
fst:  word 0
snd:  word 0
mask: word 0xff
_start:
    cla        ; prepare for iteration over string
cycle:
    ld (iter)+ ; start iterating by loading symbols in `fst` (first) and `snd` (second)
    st snd
    swab
    st fst
S1: in 3       ; ready to display?
    and #0x40
    bzs S1     ; no
    ld fst
    and mask
    cmp $EOF   ; is EOF?
    beq _fini  ; yes
    out 2      ; display first symbol
S2: in 3       ; ready to display?
    and #0x40
    bzs S2     ; no
    ld snd
    and mask
    cmp $EOF   ; is EOF?
    beq _fini  ; yes
    out 2      ; display second symbol
    jump cycle ; iterate
_fini:
    hlt        ; terminate


org 0x5E5
data:
    word 0xbfd8, 0xe1ef, 0xe220, 0xd4d2, 0xd0 ; msg
EOF:
    word 0x0a                                 ; stop-symbol
