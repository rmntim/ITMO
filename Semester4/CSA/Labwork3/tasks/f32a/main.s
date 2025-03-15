    .data

input_addr:      .word  0x80
output_addr:     .word  0x84

    .text

_start:
    @p output_addr b!        \ b = output_addr
    @p input_addr a!         \ a = input_addr

    @                        \ stack = len
while:
    dup                      \ stack = len-1 , len-1
    if end                   \ if len-1 == 0 goto end

    @ lit 255 and dup        \ stack = len-1 , char, char
    lit -97 +                \ stack = len-1 , char, char-97
    -if upcase               \ if char-97 >= 0 goto upcase
    out ;                    \ else goto out
upcase:
    lit -32 +                \ stack = len-1 , char-32
out:
    !b                       \ stack = len-1 \ *output_addr = b
    lit -1 +                 \ stack = len-2
    while ;                  \ goto while
end:
    halt
