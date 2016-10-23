/* -*- C++ -*- */

// Stack.h
// Phong Nguyen
// 23-10-2016
// 1.0-SNAPSHOT

// ---------------------------------------- //
// - Stack Interface                        //
// ---------------------------------------- //
template <class T>
class Stack {
private:
  // store data of a stack by pointer as array.
  T* data;
  // the current size of stack.
  int size;

public:
  // a new stack will be created, and size will be zero.
  Stack();
  // deconstruct stack, will delete all data in pointer.
  ~Stack();
  // a new element will be put on top of stack.
  void push(T element);
  // take the top element out of stack, and decrease the size.
  // WARN: pop using pointer will be not bounded by size, this function should
  // used with empty function to check for element existance.
  T pop();
  // get the top element data, still keep this element in stack.
  T top();
  // check whether a stack is empty.
  bool empty();
};

// ---------------------------------------- //
// - Stack Interface Implementation         //
// ---------------------------------------- //
template <class T>
Stack<T>::Stack() {
  data = new T;
  size = 0;
}

template <class T>
Stack<T>::~Stack() {
  delete[] data;
}

template <class T>
void Stack<T>::push(T element) {
  data[size++] = element;
}

template <class T>
T Stack<T>::pop() {
  return data[--size];
}

template <class T>
T Stack<T>::top() {
  return data[size - 1];
}

template <class T>
bool Stack<T>::empty() {
  return size <= 0;
}
