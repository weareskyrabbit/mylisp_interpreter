#include "VirtualMachine.h"


void VirtualMachine::push(immediate i) {
	runtime_stack.push(i);
}
immediate VirtualMachine::pop() {
	immediate tmp = runtime_stack.top();
	runtime_stack.pop();
	return tmp;
}
void VirtualMachine::execute(instruction* instructions) {
    pushc(0);
    disp();
    // not false or 100 > 50
    pushb(false);
    pushi(100);
    pushi(50);
    igt();
    bor();
    bnot();
    disp();
	
// direct threading
#if defined __GNUC__ || defined __clnag__ || defined __INTEL_COMPILER
  #define DIRECT_THREADED
#endif

#ifdef DIRECT_THREADED
  #define INIT_DISPATCH JUMP;
  #define CASE(op) L_ ## op:
  #define NEXT i=*++pc; goto *table[i.type]
  #define JUMP i=*pc; goto *table[i.type]
  #define END_DISPATCH
#else
  #define INIT_DISPATCH while(true) { i = *pc; switch (i.type) {
  #define CASE(op) case op:
  #define NEXT pc++; break;
  #define JUMP break
  #define END_DISPATCH }}
#endif
   instruction* pc = instructions;
   instruction i;
#ifdef DIRECT_THREADED
   static void* table[] = {
       /* 00 */ &&L_NOP,  /* 01 */ &&L_NOP,  /* 02 */ &&L_NOP,  /* 03 */ &&L_NOP,
       /* 04 */ &&L_NOP,  /* 05 */ &&L_NOP,  /* 06 */ &&L_NOP,  /* 07 */ &&L_NOP,
       /* 08 */ &&L_NOP,  /* 09 */ &&L_NOP,  /* 0a */ &&L_NOP,  /* 0b */ &&L_NOP,
       /* 0c */ &&L_NOP,  /* 0d */ &&L_NOP,  /* 0e */ &&L_NOP,  /* 0f */ &&L_NOP,

       /* 10 */ &&L_CPUSH, /* 11 */ &&L_POP,  /* 12 */ &&L_LOAD, /* 13 */ &&L_STORE,
       /* 14 */ &&L_AND,  /* 15 */ &&L_OR,   /* 16 */ &&L_INC,  /* 17 */ &&L_DEC,
       /* 18 */ &&L_CMP,  /* 19 */ &&L_CMPL, /* 1a */ &&L_CMPG, /* 1b */
       /* 1c */ /* 1d */ /* 1e */ /* 1f */

       /* 20 */ &&L_IADD,  /* 21 */ &&L_ISUB,  /* 22 */ &&L_IMUL,  /* 23 */ &&L_IDIV,
	   /* 24 */ &&L_ILT,   /* 25 */ &&L_ILE,   /* 26 */ &&L_IGT,   /* 27 */ &&L_IGE,
       /* 28 */ &&L_IOUT,  /* 29 */ &&L_IRET,  /* 2a */ &&L_ZERO,  /* 2b */ &&L_ONE,
       /* 2c */ &&L_IPUSH, /* 2d */ &&L_IEQ,   /* 2e */ &&L_ILOAD, /* 2f */ &&L_ISTORE,

       /* 30 */ &&L_BAND,  /* 31 */ &&L_BOR,   /* 32 */ &&L_BNOT,  /* 33 */ &&L_NOP,
       /* 34 */ &&L_NOP,   /* 35 */ &&L_NOP,   /* 36 */ &&L_NOP,   /* 37 */ &&L_NOP,
       /* 38 */ &&L_BOUT,  /* 39 */ &&L_BRET,  /* 3a */ &&L_TRUE,  /* 3b */ &&L_FALSE,
       /* 3c */ &&L_BPUSH, /* 3d */ &&L_BEQ,   /* 3e */ &&L_BLOAD, /* 3f */ &&L_BSTORE,

       /* 40 */ &&L_GOTO,  /* 41 */ &&L_IFNE,  /* 42 */ &&L_IFLT,  /* 43 */ &&L_IFLE,
       /* 44 */ &&L_IFGT,  /* 45 */ &&L_IFGE,  /* 46 */ &&L_NOP,   /* 47 */ &&L_NOP,
       /* 48 */ &&L_CALL,  /* 49 */ &&L_RET,   /* 4a */ &&L_NOP,   /* 4b */ &&L_NOP,
       /* 4c */ &&L_NOP,   /* 4d */ &&L_NOP,   /* 4e */ &&L_NOP,   /* 4f */ &&L_NOP,

       /* 50 */ &&L_NEW,
	   /* 60 */ &&L_IADDL, /* 61 */ &&L_ISUBL, /* 62 */ &&L_BANDL, /* 63 */ &&L_BORL,

   };
#endif
	INIT_DISPATCH {
        CASE(NOP) {
		} NEXT;
		
		CASE(CPUSH) {
			push(constant_pool[pc->operand0]);
		} NEXT;
		CASE(POP) {
			for (uint32_t c = 0; c < pc->operand0; c++) {
				pop();
			}
		} NEXT;

        CASE(IADD) {
            immediate right = pop();
            immediate left = pop();
            push(new immediate(left.i + right.i));
        } NEXT;
        CASE(ISUB) {
            immediate right = pop();
            immediate left = pop();
            push(new immediate(left.i - right.i));
        } NEXT;
        CASE(IMUL) {
            immediate right = pop();
            immediate left = pop();
            push(new immediate(left.i * right.i));
        } NEXT;
        CASE(IDIV) {
            immediate right = pop();
            immediate left = pop();
            push(new immediate(left.i / right.i));
        } NEXT;
		CASE(ILT) {
            immediate right = pop();
            immediate left = pop();
            push(new immediate(left.i < right.i));
        } NEXT;
        CASE(ILE) {
            immediate right = pop();
            immediate left = pop();
            push(new immediate(left.i <= right.i));
        } NEXT;
        CASE(IGT) {
            immediate right = pop();
            immediate left = pop();
            push(new immediate(left.i > right.i));
        } NEXT;
        CASE(IGE) {
            immediate right = pop();
            immediate left = pop();
            push(new immediate(left.i >= right.i));
        } NEXT;
		CASE(IOUT) {
			immediate tmp = pop();
			cout << tmp.i;
		} NEXT;
		CASE(IRET) {
			// TODO
		} NEXT;
		CASE(ZERO) {
			push(new immediate(0u));
		} NEXT;
		CASE(ONE) {
			push(new immediate(1u));
		} NEXT;
		CASE(IPUSH) {
			push(new immediate((uint32_t) pc->operand0));
		} NEXT;
		CASE(IEQ) {
			immediate right = pop();
            immediate left = pop();
            push(new immediate(left.i == right.i));
		} NEXT;


		CASE(BAND) {
            immediate right = pop();
            immediate left = pop();
            push(new immediate(left.b && right.b));
        } NEXT;
		CASE(BOR) {
	        immediate right = pop();
            immediate left = pop();
            push(new immediate(left.b || right.b));
        } NEXT;
		CASE(BNOT) {
            immediate tmp = pop();
            push(new immediate(!tmp.b));
        } NEXT;
		CASE(BOUT) {
			immediate tmp = pop();
            cout << tmp.b;
		} NEXT;
		CASE(BRET) {
			// TODO
		} NEXT;
		CASE(TRUE) {
			push(new immediate(true));
		} NEXT;
		CASE(FALSE) {
			push(new immediate(false));
		} NEXT;
		CASE(BPUSH) {
			push(new immediate((uint32_t) pc->operand0));
		} NEXT;
		CASE(BEQ) {
			immediate right = pop();
            immediate left = pop();
            push(new immediate(left.b == right.b));
		} NEXT;

		CASE(GOTO) {
			pc += pc->operand0;
		} JUMP;

		CASE(IADDL) {
            push(__sync_fetch_and_add();
        } NEXT;
        CASE(ISUBL) {
            push(__sync_fetch_and_sub();
        } NEXT;
		CASE(BANDL) {
            __sync_fetch_and_and();
        } NEXT;
		CASE(BORL) {
            __sync_fetch_and_or();
        } NEXT;
    } END_DISPATCH;
}


int main(uint32_t argc, void** argv) {
	auto tmp1 = new immediate((uint8_t*) "Hello, World!");
	auto vm = new VirtualMachine(tmp1);
	auto tmp2 = {
		// 1 + 2 * 3
		*new instruction(0x2c010000u), // ipush 1
		*new instruction(0x2c020000u), // ipush 2
		*new instruction(0x2c030000u), // ipush 3
		*new instruction(0x22000000u), // imul
		*new instruction(0x20000000u), // iadd
		*new instruction(0x28000000u), // iout
		// "Hello, World!"
		*new instruction(0x10000000u), // cpush 0
	};
    vm->execute(tmp2);
    // pop null
    // invoke=call
    // new setf getf sets gets ineg
	// runtime_stack.push(new Immediate<Function<Integer, Integer>>(x -> x + 1));
	// cast
}