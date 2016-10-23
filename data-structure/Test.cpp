#include <iostream>
#include "Stack.h"

using namespace std;

int main() {
  Stack<int> stack;
  stack.push(7);
  stack.push(5);

  cout << stack.pop();
  cout << stack.pop();

  cout << stack.empty();

  cin.get();

  return 0;
}
