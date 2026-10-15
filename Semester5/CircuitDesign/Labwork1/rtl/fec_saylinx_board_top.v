`timescale 1ns/1ps
`default_nettype none

// Safe board scaffold. The variant-specific datapath is not connected yet.
module fec_saylinx_board_top (
    input wire CLK,
    input wire RST_N,
    input wire KEY2_N,
    input wire KEY3_N,
    input wire KEY4_N,
    output wire [3:0] LED,
    output wire [7:0] SEG_DATA,
    output wire [7:0] SEG_SEL,
    output wire [16:0] GPIO_0_out_zero_value,
    input wire [16:0] GPIO_0_input_pullup,
    output wire [16:0] GPIO_1_out_zero_value,
    input wire [16:0] GPIO_1_input_pullup
);
    assign LED = 4'b1111;
    assign SEG_DATA = 8'hff;
    assign SEG_SEL = 8'hff;
    assign GPIO_0_out_zero_value = 17'b0;
    assign GPIO_1_out_zero_value = 17'b0;
    // TODO: connect operands and display the registered result.
    // Board reset is active-low: rst = ~RST_N.
endmodule

`default_nettype wire
