#ifndef LISP_LISP_H
#define LISP_LISP_H

#include <iomanip>
#include <cstdint>
#include <vector>

using namespace std;

union immediate;
struct instruction;
struct function;
struct Class_;
struct Object_;

union immediate {
    uint32_t i;
    bool b;
    uint8_t* s;
    immediate (*f)(immediate);
    Object_* r;
    /*
     * void(*p)(immediate);
     * immediate (*c)();
     */
    immediate(uint32_t i)                { this->i = i; }
    immediate(bool b)                    { this->b = b; }
    immediate(uint8_t* s)                { this->s = s; }
    immediate(immediate (*f)(immediate)) { this->f = f; }
    immediate(Object_* r)                { this->r = r; }
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
};
struct function {
    instruction* body;
    instruction* ret;
    function(instruction* body) { this->body = body; }
};
struct Class_ {
    immediate* constant_pool;
    uint32_t field_count;
    uint32_t method_count;
    function* methods;
    function* constructors;
};

struct Object_ {
    immediate* fields;
    function* methods;
};

#endif // LISP_LISP_H