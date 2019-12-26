#include <iostream>
#include <fstream>

#include "Converter.h"

#ifdef ON_DEBUG

#define DEBUG_OUT(message, target) cout << "debug | " << message << target << endl

#else

#define DEBUG_OUT(message, target)

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
        uint8_t byte = 0;
        fin.read((char*) &byte, sizeof(uint8_t));
        input.push_back(byte);
    }
    fin.close();

    // check if magic is valid or not
    uint32_t magic = *read_int();
    if (magic != 0xdeadbeef) {
        cout << "error | file is invalid" << endl;
        exit(1);
    }
    uint32_t entry_point = *read_int();
    DEBUG_OUT("entry point : ", entry_point);

    // load Class_ into c
    auto c = new Class_;
    vector<uint32_t> tmp = vector<uint32_t>();
    c->constant_pool = (immediate*)tmp.back() + 1;
    uint32_t constant_pool_count = *read_int();
    DEBUG_OUT("constant pool count : ", constant_pool_count);
    read_immediate(c->constant_pool); // TODO
    c->methods = (function*) tmp.back() + 1;
    uint32_t method_count = *read_int();
    DEBUG_OUT("method count : ", method_count);
    read_function(c->methods); // TODO
    c->constructors = (function*) tmp.back() + 1;
    uint32_t constructor_count = *read_int();
    DEBUG_OUT("constructor count : ", constructor_count);
    read_function(c->constructors); // TODO
    c->fields = (immediate*) tmp.back() + 1;
    uint32_t field_count = *read_int();
    DEBUG_OUT("field count : ", field_count);

    classes.push_back(*c);
}
uint8_t* Converter::read(const int32_t length) {
    auto bytes = new uint8_t[length];
    for (int i = 0; i < length; i++) {
        bytes[i] = input[position + i];
    }
    position += length;
    return bytes;
}
uint32_t* Converter::read_int() {
    auto bytes = read(4);
    DEBUG_OUT("read_int called and returned : ", *(uint32_t*)bytes);
    return (uint32_t*)bytes;
}
function* Converter::read_function(function* malloced) {
    uint32_t size = *read_int();
    for (uint32_t i = 0; i < size; i++) {
        malloced[i] = *(function*) read_int();
    }
    return malloced;
}
immediate* Converter::read_immediate(immediate* malloced) {
    uint32_t size = *read_int();
    for (uint32_t i = 0; i < size; i++) {
        malloced[i] = *(immediate*) read_int();
    }
    return malloced;
}
#undef DEBUG_OUT