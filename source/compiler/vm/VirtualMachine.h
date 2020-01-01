#ifndef LISP_VIRTUALMACHINE_H
#define LISP_VIRTUALMACHINE_H

#include "lisp.h"
#include "Converter.h"

#include <iostream>

class VirtualMachine {
public:
	VirtualMachine() {
	    auto tmp = vector<Class_>();
	    converter = new Converter(tmp);
	    classes = &tmp.front();
		runtime_stack.clear();
		self = nullptr;
	}
	vector<instruction> execute();
    Converter* converter; // TODO
private:
    Class_* classes;
	const immediate* constant_pool;
    vector<immediate> runtime_stack;
    immediate* locals; // TODO
    Object_* self;
    vector<function> stack;
	void push(immediate);
	immediate pop();
};

#endif