#ifndef LISP_CONVERTER_H
#define LISP_CONVERTER_H

#include "lisp.h"
#include "VirtualMachine.h"

class Converter {
private:
    vector<Class_> classes;
    vector<uint8_t> input;
    uint32_t position;
    uint8_t* read(int32_t length);
    uint32_t* read_int();
    function* read_function(function*);
    immediate* read_immediate(immediate*);
public:
    void IRtoWC(string const& file_name);
    void IRtoMemory(string const& file_name);
    void WCtoMemory(string const& file_name);
};


#endif //LISP_CONVERTER_H
