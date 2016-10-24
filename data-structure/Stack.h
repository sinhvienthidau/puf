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
  T* _data;
  // the current size of stack.
  int _index;

public:
  // a new stack will be created, and size will be zero.
  Stack();
  // a new element will be put on top of stack.
  void push(const T& element);
  // take the top element out of stack, and decrease the size.
  // WARN: pop using pointer will be not bounded by size, this function should
  // used with empty function to check for element existance.
  T& pop();
  // get the top element data, still keep this element in stack.
  T& top();
  // check whether a stack is empty.
  bool empty();
  // return the size of stack.
  int size();
  // sort a stack by using 2 additional stacks.
  void sort();
};

// ---------------------------------------- //
// - Stack Interface Implementation         //
// ---------------------------------------- //
template <class T>
Stack<T>::Stack() {
  _data = new T[100];
  _index = -1;
}

template <class T>
void Stack<T>::push(const T& element) {
  _data[++_index] = element;
}

template <class T>
T& Stack<T>::pop() {
  return _data[_index--];
}

template <class T>
T& Stack<T>::top() {
  return _data[_index];
}

template <class T>
bool Stack<T>::empty() {
  return _index < 0;
}

template <class T>
int Stack<T>::size() {
  return _index + 1;
}

// -------------------------------------------- //
// - Stack Interface non-common Implementation  //
// -------------------------------------------- //
template <class T>
void Stack<T>::sort() {
  Stack<T> _sorted;
  Stack<T> _temp;

  // process all elements in orgin stack for transfering to sorted stack.
  while(!empty()) {
    // deciate place in sorted stack to push
    // all remaining unbalance element will be transfered to temp stack
    while(!_sorted.empty() && this->top() < _sorted.top()) {
      _temp.push(_sorted.pop());
    }

    // push orgin element to sorted stack, and then rebalance sorted stack
    // by moving all element from merge node to sorted stack.
    _sorted.push(this->pop());
    while(!_temp.empty()) {
      _sorted.push(_temp.pop());
    }
  }

  // pull elements from sorted stack to orgin stack.
  while(!_sorted.empty()) {
    this->push(_sorted.pop());
  }
}
