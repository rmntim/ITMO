`timescale 1ns/1ps
module tb_result_register;
    reg clk = 0;
    reg rst = 0;
    reg [7:0] data = 0;
    wire [7:0] result;
    result_register #(.WIDTH(8)) dut (
        .clk(clk), .rst(rst), .data(data), .result(result)
    );
    always #5 clk = ~clk;

    task check;
        input [7:0] expected;
        begin
            if (result !== expected)
                $fatal(1, "Expected %h, got %h", expected, result);
        end
    endtask

    initial begin
        $dumpfile("build/result_register.vcd");
        $dumpvars(0, tb_result_register);
        #1 rst = 1;
        #1 check(0); // Reset acts before the first clock edge.
        data = 8'ha5;
        #4 check(0); // Reset overrides capture.
        rst = 0;
        @(posedge clk); #1 check(8'ha5);
        data = 8'h3c;
        #1 check(8'ha5); // Input changes must not change the register.
        @(negedge clk); #1 check(8'ha5);
        @(posedge clk); #1 check(8'h3c);
        #1 rst = 1;
        #1 check(0); // Reset clears a nonzero result between clock edges.
        rst = 0;
        @(posedge clk); #1 check(8'h3c);
        $display("PASS: result_register");
        $finish;
    end
endmodule
