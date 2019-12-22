#include "VirtualMachine.h"

void VirtualMachine::push(immediate i) {
	runtime_stack.push_back(i);
}
immediate VirtualMachine::pop() {
	immediate tmp = runtime_stack.back();
	runtime_stack.pop_back();
	return tmp;
}
void VirtualMachine::load(uint8_t* c, uint32_t count, uint32_t* i) {
    // TODO
    this->constant_pool = new immediate(c);
    vector<instruction> tmp = vector<instruction>();
    for (uint32_t u = 0u; u < count; u++) {
        tmp.push_back(*new instruction(i[u]));
    }
    this->execute(&tmp.front());
}
vector<instruction> VirtualMachine::execute(instruction* instructions) {
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
   instruction i = *new instruction(0u);
#ifdef DIRECT_THREADED
   static void* table[] = {
       /* 00 */ &&L_NOP,   /* 01 */ &&L_EXIT,  /* 02 */ &&L_DEBUG,  /* 03 */ &&L_NOP,
       /* 04 */ &&L_NOP,   /* 05 */ &&L_NOP,   /* 06 */ &&L_NOP,    /* 07 */ &&L_NOP,
       /* 08 */ &&L_NOP,   /* 09 */ &&L_NOP,   /* 0a */ &&L_NOP,    /* 0b */ &&L_NOP,
       /* 0c */ &&L_NOP,   /* 0d */ &&L_NOP,   /* 0e */ &&L_NOP,    /* 0f */ &&L_NOP,

       /* 10 */ &&L_CPUSH, /* 11 */ &&L_IPUSH, /* 12 */ &&L_NOP,    /* 13 */ &&L_NOP,
       /* 14 */ &&L_POP,   /* 15 */ &&L_CALL,  /* 16 */ &&L_NOP,    /* 17 */ &&L_NOP,
       /* 18 */ &&L_SOUT,  /* 19 */ &&L_NOP,   /* 1a */ &&L_NOP,    /* 1b */ &&L_NOP,
       /* 1c */ &&L_NOP,   /* 1d */ &&L_NOP,   /* 1e */ &&L_STORE,  /* 1f */ &&L_LOAD,

       /* 20 */ &&L_IADD,  /* 21 */ &&L_ISUB,  /* 22 */ &&L_IMUL,   /* 23 */ &&L_IDIV,
	   /* 24 */ &&L_ILT,   /* 25 */ &&L_ILE,   /* 26 */ &&L_IGT,    /* 27 */ &&L_IGE,
       /* 28 */ &&L_IOUT,  /* 29 */ &&L_IRET,  /* 2a */ &&L_IEQ,    /* 2b */ &&L_NOP,
       /* 2c */ &&L_ZERO,  /* 2d */ &&L_ONE,   /* 2e */ &&L_NOP,    /* 2f */ &&L_NOP,

       /* 30 */ &&L_BAND,  /* 31 */ &&L_BOR,   /* 32 */ &&L_BNOT,   /* 33 */ &&L_GOTO,
       /* 34 */ &&L_NOP,   /* 35 */ &&L_NOP,   /* 36 */ &&L_NOP,    /* 37 */ &&L_NOP,
       /* 38 */ &&L_BOUT,  /* 39 */ &&L_BRET,  /* 3a */ &&L_BEQ,    /* 3b */ &&L_NOP,
       /* 3c */ &&L_TRUE,  /* 3d */ &&L_FALSE, /* 3e */ &&L_NOP,    /* 3f */ &&L_NOP,

       /* 40 */ &&L_NOP,   /* 41 */ &&L_NOP,   /* 42 */ &&L_NOP,    /* 43 */ &&L_NOP,
       /* 44 */ &&L_SETF,  /* 45 */ &&L_GETF,  /* 46 */ &&L_NOP,    /* 47 */ &&L_NOP,
       /* 48 */ &&L_NOP,   /* 49 */ &&L_NOP,   /* 4a */ &&L_NOP,    /* 4b */ &&L_NOP,
       /* 4c */ &&L_NOP,   /* 4d */ &&L_NOP,   /* 4e */ &&L_NOP,    /* 4f */ &&L_NOP,
   };
#endif
	INIT_DISPATCH {
        CASE(NOP) {
		} NEXT;
		CASE(EXIT) {
			exit(pc->operand0);
		} NEXT;
		CASE(DEBUG) {
            pc++;
		    printf("DEBUG| type: %x operand0: %x operand1: %x operand2: %x\n",
		            pc->type, pc->operand0, pc->operand1, pc->operand2);
		} JUMP;
		
		CASE(CPUSH) {
			push(constant_pool[pc->operand0]);
		} NEXT;
		CASE(POP) {
			for (uint32_t c = 0; c < pc->operand0; c++) {
				pop();
			}
		} NEXT;
		CASE(SOUT) {
		    immediate tmp = pop();
		    cout << tmp.s;
		} NEXT;
        CASE(CALL) {
            immediate arg = pop();
            immediate ret = self->methods[pc->operand0].f(arg);
            push(ret);
        } NEXT;
        CASE(STORE) {
            immediate tmp = pop();
            locals[pc->operand0] = tmp;
        } NEXT;
        CASE(LOAD) {
            immediate tmp = locals[pc->operand0];
            push(tmp);
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
            cout << hex << tmp.i;
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
            cout << boolalpha << tmp.b;
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

       CASE(GETF) {
            immediate tmp = pop();
        self->fields[pc->operand0] = tmp;
        } NEXT;
        CASE(SETF) {
            immediate tmp = self->fields[pc->operand0];
            push(tmp);
        } NEXT;
    } END_DISPATCH;
}