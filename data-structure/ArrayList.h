/* -*- C++ -*- */

// ArrayList.h
// Created At: 2016-10-22
// Created By: Phong Nguyen
template <class T>
class ArrayList {
private:
  T *arrays;

public:
  ArrayList() {
    arrays = new T;
  }

  ~ArrayList() {
    delete[] arrays;
  }

  void insert(int index, T data) {
    arrays[index] = data;
  }

  T get(int index) {
    return arrays[index];
  }
};
