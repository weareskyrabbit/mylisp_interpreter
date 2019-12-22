#ifndef LISP_VIRTUALMACHINE
#define LISP_VIRTUALMACHINE

#include "lisp.h"

#include <iostream>
#include <vector>

class VirtualMachine {
public:
	VirtualMachine() {
		runtime_stack.clear();
		self = nullptr;
	}
	void load(uint8_t*, uint32_t, uint32_t*);
	vector<instruction> execute(instruction*);
private:
	const immediate* constant_pool;
    vector<immediate> runtime_stack;
    immediate* locals; // TODO
    Object_* self;
	void push(immediate);
	immediate pop();
};

#endif