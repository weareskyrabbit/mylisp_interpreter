#include <iostream>
#include <fstream>

#include "Converter.h"

#ifdef ON_DEBUG

#define DEBUG_OUT(message, target)     cout << "debug | " << message << target << endl
#define DEBUG_OUT_HEX(message, target) cout << "debug | " << message << hex << target << endl

#else

#define DEBUG_OUT(message, target)
#define DEBUG_OUT_HEX(message, target)

#endif

void Converter::IRtoWC(string const& file_name) {
    // read `.ir` file
    string ir = string(file_name);
    ir += ".ir";
    ifstream fin;
    fin.open(ir.c_str(), ios::in | ios::binary);
    if (!fin) {
        cout << "error : file '" << ir.c_str() << "' didn't open" << endl;
        exit(1);
    }
    // TODO read
    fin.close();
    // TODO convert
    // write `.wc` file
    string wc = string(file_name);
    wc += ".wc";
    ofstream fout;
    fout.open(wc.c_str(), ios::out | ios::binary | ios::trunc);
    if (!fout) {
        cout << "error | file '" << wc.c_str() << "' didn't open" << endl;
        exit(1);
    }
    // TODO write
    fout.close();
}
void Converter::IRtoMemory(string const& file_name) {
    // read `.ir` file
    string ir = string(file_name);
    ir += ".ir";
    ifstream fin;
    fin.open(ir.c_str(), ios::in | ios::binary);
    if (!fin) {
        cout << "error | file '" << ir.c_str() << "' didn't open" << endl;
        exit(1);
    }
    // TODO read
    fin.close();
    // TODO convert
}
void Converter::WCtoMemory(string const& file_name) {
    // read `wc` file
    input.clear();
    position = 0;
    string wc = string(file_name);
    wc += ".wc";
    ifstream fin;
    fin.open(wc.c_str(), ios::in | ios::binary);
    if (!fin) {
        cout << "error | file `"<< wc.c_str() <<"` didn't open" << endl;
        exit(1);
    }
    while (!fin.eof()) {
        uint8_t buffer = 0;
        fin.read((char*) &buffer, 1);
        input.push_back(buffer);
    }
    fin.close();

    // check if magic is valid or not
    uint32_t magic = *read_int();
    DEBUG_OUT_HEX("magic : ", magic);
    if (magic != 0xdeadbeef) {
        cout << "error | file is invalid" << endl;
        exit(1);
    }

    // load Class_ into tmp
    auto tmp = new Class_;
    tmp->constant_pool = read_constant_pool();
    tmp->field_count = *read_int();
    DEBUG_OUT_HEX("field count : ", tmp->field_count);
    tmp->method_count = *read_int();
    DEBUG_OUT_HEX("method count : ", tmp->method_count);
    tmp->methods = read_methods(tmp->method_count);
    tmp->constructors = read_constructors();

    classes.push_back(*tmp);
}
uint8_t* Converter::read(uint32_t size) {
    auto tmp = new uint8_t[size];
    for (uint32_t i = 0; i < size; i++) {
        tmp[i] = input[position++];
    }
    return tmp;
}
uint32_t* Converter::read_int() {
    return (uint32_t*) read(4);
}
function* Converter::read_function() {
    uint32_t size = *read_int();
    DEBUG_OUT_HEX("function size : ", size);
    uint32_t* tmp = &value_placeholder.back();
    for (uint32_t i = 0; i < size; i++) {
        value_placeholder.push_back(*read_int());
    }
    return (function*) ++tmp;
}
function* Converter::read_methods(uint32_t method_count) {
    uint32_t* tmp = &pointer_placeholder.back();
    for (uint32_t i = 0; i < method_count; i++) {
        pointer_placeholder.push_back((uint32_t) read_function());
    }
    return (function*) ++tmp;
}
function* Converter::read_constructors() {
    uint32_t constructor_count = *read_int();
    DEBUG_OUT_HEX("constructor count : ", constructor_count);
    uint32_t* tmp = &pointer_placeholder.back();
    for (uint32_t i = 0; i < constructor_count; i++) {
        pointer_placeholder.push_back((uint32_t) read_function());
    }
    return (function*) ++tmp;
}
immediate* Converter::read_immediate() {
    uint32_t size = *read_int();
    DEBUG_OUT_HEX("immediate size : ", size);
    uint32_t* tmp = &value_placeholder.back();
    for (uint32_t i = 0; i < size; i++) {
        value_placeholder.push_back(*read_int());
    }
    return (immediate*) ++tmp;
}
immediate* Converter::read_constant_pool() {
    uint32_t constant_pool_count = *read_int();
    DEBUG_OUT_HEX("constant pool count : ", constant_pool_count);
    uint32_t* tmp = &pointer_placeholder.back();
    for (uint32_t i = 0; i < constant_pool_count; i++) {
        pointer_placeholder.push_back((uint32_t) read_immediate());
    }
    return (immediate*) ++tmp;
}

#undef DEBUG_OUT
#undef DEBUG_OUT_HEX