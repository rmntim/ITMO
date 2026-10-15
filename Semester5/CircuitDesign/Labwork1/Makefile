IVERILOG ?= iverilog
VVP ?= vvp
TYPST ?= typst

.PHONY: test test-register test-boe report
test: test-register test-boe

build:
	mkdir -p build

test-register: | build
	$(IVERILOG) -g2012 -Wall -s tb_result_register -o build/register rtl/result_register.v tb/tb_result_register.v
	$(VVP) build/register

test-boe: | build
	$(IVERILOG) -g2012 -Wall -s tb_boe -o build/boe rtl/boe_assign.v rtl/boe_always.v rtl/boe_param.v rtl/boe_registered.v rtl/result_register.v tb/tb_boe.v
	$(VVP) build/boe

report:
	$(TYPST) compile --root . report/main.typ report/report.pdf
