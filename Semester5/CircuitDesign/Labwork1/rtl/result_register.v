`timescale 1ns/1ps
`default_nettype none

// Active-high asynchronous reset; capture on the rising clock edge.
module result_register #(
    parameter WIDTH = 8
) (
    input wire clk,
    input wire rst,
    input wire [WIDTH-1:0] data,
    output reg [WIDTH-1:0] result
);
    always @(posedge clk or posedge rst) begin
        if (rst)
            result <= {WIDTH{1'b0}};
        else
            result <= data;
    end
endmodule

`default_nettype wire
