#include <iostream>
#include <stack>
#include <cstdint>

using namespace std;

union immediate {
	bool b;
	uint32_t i;
	uint8_t* s;
	immediate(uint32_t i) { this->i = i; }
	immediate(bool b)     { this->b = b; }
	immediate(uint8_t* s) { this->s = s; }
};
struct instruction {
	uint8_t type;
	uint8_t operand0;
	uint8_t operand1;
	uint8_t operand2;
	instruction(uint32_t i) {
		this->operand2 = i & 0x11;
		this->operand1 = i >> 8 & 0x11;
		this->operand0 = i >> 16 & 0x11;
		this->type = i >> 24;
	}
};

class VirtualMachine {
public:
	VirtualMachine(immediate* constant_pool) {
		this->constant_pool = constant_pool;
		stack<immediate>().swap(runtime_stack);
	}
	void execute(instruction*);
private:
	const immediate* constant_pool;
    const stack<immediate> runtime_stack;
	void push(immediate);
	immediate pop();

    void call() { /* pointer of function */ }
    void disp() {
        cout << pop().value;
    }
};