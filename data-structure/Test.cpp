#include <iostream>
#include "LinkedList.h"

using namespace std;

int main() {
  LinkedList<int> linkedList;
  linkedList.insert(7);
  linkedList.insert(5);
  linkedList.insert(8);

  cout << linkedList.get(2);

  cin.get();

  return 0;
}
