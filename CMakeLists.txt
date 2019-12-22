cmake_minimum_required(VERSION 3.15)
project(lisp)

set(CMAKE_CXX_STANDARD 14)

add_executable(lisp source/compiler/vm/main.cpp source/compiler/vm/lisp.h source/compiler/vm/VirtualMachine.cpp source/compiler/vm/VirtualMachine.h)