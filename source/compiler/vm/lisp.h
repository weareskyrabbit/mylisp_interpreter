#ifndef LISP_LISP
#define LISP_LISP

#include <iomanip>
#include <cstdint>

using namespace std;

union immediate {
    uint32_t i;
    bool b;
    uint8_t* s;
    immediate (*f)(immediate);
    /*
     * void(*p)(immediate);
     * immediate (*c)();
     */
    immediate(uint32_t i)                { this->i = i; }
    immediate(bool b)                    { this->b = b; }
    immediate(uint8_t* s)                { this->s = s; }
    immediate(immediate (*f)(immediate)) { this->f = f; }
    /*
     * immediate(void (*p)(immediate))      { this->p = p; }
     * immediate(immediate (*c)())          { this->c = c; }
     */
};
struct instruction {
    uint8_t type;
    uint8_t operand0;
    uint8_t operand1;
    uint8_t operand2;
    instruction(uint32_t i) {
        this->operand2 = i & 0xffu;
        this->operand1 = i >> 8u & 0xffu;
        this->operand0 = i >> 16u & 0xffu;
        this->type = i >> 24u;
    }
};
class Object_ {
public:
    immediate* fields;
    immediate* methods;
};

#endif