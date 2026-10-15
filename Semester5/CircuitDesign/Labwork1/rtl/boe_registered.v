`timescale 1ns/1ps
`default_nettype none

// TODO: add operand ports and wire boe_param to result_register.
// Choose the register width from the actual result width of the variant.
module boe_registered #(
    parameter WIDTH = 8
) (
    input wire clk,
    input wire rst
);
endmodule

`default_nettype wire
