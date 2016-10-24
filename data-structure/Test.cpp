#include <iostream>
#include "Stack.h"

using namespace std;

int main() {
  Stack<int> stack;
  stack.push(7);
  stack.push(5);
  stack.push(18);
  stack.push(3);
  stack.push(9);
  stack.push(1);
  stack.push(20);

  stack.sort();

  while(!stack.empty()) {
    cout << stack.pop() << " ";
  }

  return 0;
}
