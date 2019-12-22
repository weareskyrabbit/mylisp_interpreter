#include "lisp.h"
#include "VirtualMachine.h"

#include <vector>

#ifdef __GNUC__
immediate sub(immediate a) {
    immediate __lambda_expression__ (immediate b) {
        return new immediate(a - b.i);
    }
    return new immediate(__lambda_expression__);
}
#endif

int main() {
	auto vm = new VirtualMachine();
	auto tmp2 = vector<uint32_t>();
	// 1 + 2 * 3
	tmp2.push_back(0x11010000u); // ipush 1
	tmp2.push_back(0x11020000u); // ipush 2
	tmp2.push_back(0x11030000u); // ipush 3
	tmp2.push_back(0x22000000u); // imul
	tmp2.push_back(0x20000000u); // iadd
    tmp2.push_back(0x28000000u); // iout
	// "Hello, World!"
	tmp2.push_back(0x10000000u); // cpush 0
	tmp2.push_back(0x18000000u); // sout
	// not false or 1 > 0
	tmp2.push_back(0x3d000000u); // false
	tmp2.push_back(0x11010000u); // ipush 1
	tmp2.push_back(0x11000000u); // ipush 0
	tmp2.push_back(0x26000000u); // igt
	tmp2.push_back(0x31000000u); // bor
	tmp2.push_back(0x32000000u); // bnot
	tmp2.push_back(0x38000000u); // bout

    vm->load((uint8_t*) "Hello, World!", 15, &tmp2.front());
    // pop null
    // invoke=call
    // new setf getf sets gets ineg
	// runtime_stack.push(new Immediate<Function<Integer, Integer>>(x -> x + 1));
	// cast
	// output ??????
	// bpush  ??????

	// pointer of function
	// hash table for object
	// type system
}