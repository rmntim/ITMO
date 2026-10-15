`timescale 1ns/1ps
`default_nettype none

// TODO: define ports after the variant is known. Some variants need carry,
// borrow, comparison chaining, or an encoded address, rather than independent
// bit slices. Output width also depends on the selected operation.
module boe_param #(
    parameter WIDTH = 8
) ();
    // Instantiate the elementary block in a generate loop here.
endmodule

`default_nettype wire
