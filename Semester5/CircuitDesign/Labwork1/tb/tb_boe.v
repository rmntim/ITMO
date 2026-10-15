`timescale 1ns/1ps
module tb_boe;
    initial begin
        $fatal(1, "TODO: select a variant and implement the BOE tests");
    end
    // TODO: exhaustively test the elementary truth table for both versions.
    // Compare the parameterized block with an independent reference model.
    // Test WIDTH=1 and a wider instance, boundary cases and carry/borrow.
    // Test the integrated registered block: reset, capture, hold, reset priority.
endmodule
