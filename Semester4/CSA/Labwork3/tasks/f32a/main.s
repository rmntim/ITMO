    .data
len:             .byte  0
buf:             .byte  '_______________________________'
padding:         .byte  '___'
input_addr:      .word  0x80
output_addr:     .word  0x84
mask:            .byte  0,'___'
bullshit:        .word  0xCCCCCCCC

    .text

.org 0x90
_start:
    @p input_addr b!         \ b = input_addr
    lit buf a!               \ a = buf
    upcase

    @p output_addr b!        \ b = output_addr
    lit len a!               \ a = len
    @+ lit 255 and           \ a = buf, stack = len&0xff

    print_rofl
    halt

upcase:
    @b lit 255 and dup       \ stack = char&0xff, char&0xff
    lit -10 + if end         \ if char&0xff-10 == '\n' goto end
    dup lit -97 +            \ stack = char&0xff, char&0xff-97
    -if do_upcase            \ if char&0xff-97 >= 0 goto do_upcase
    out ;                    \ else goto out
do_upcase:
    lit -32 +                \ stack = char-32
out:
    @p mask +                \ stack = masked
    !+                       \ stack empty \ *buf++ = char-32
    @p len                   \ stack = len
    lit 1 +                  \ stack = len+1
    dup                      \ stack = len+1, len+1
    !p len                   \ stack = len+1
    lit 255 and              \ stack = len+1&0xff
    lit -32 + if err         \ if len+1-32 == 0 goto end
    upcase ;                 \ goto while
err:
    @p bullshit
    @p output_addr b!
    !b
    halt
end:
    drop
    ;

print_rofl:
    dup if end_print
    lit -1 +
    @+ lit 255 and
    !b
    print_rofl ;
end_print:
    drop
    ;