/* -*- C++ -*- */

// LinkedList.h
// Created At: 2016-10-22
// Created By: Phong Nguyen
// Version: 1.0.SNAPSHOT

// Linked List:
// a linked list is a linear collection of data elements, called nodes, each pointing
// to the next node by means of a pointer.
// ----------        ----------        ----------
// - Data   -        - Data   -        - Data   -
// ----------        ----------        ----------
// - Pointer- - - -> - Pointer- - - -> - Pointer-
// ----------        ----------        ----------
template <class T>
class LinkedList {
private:
  struct Node {
    T data;
    Node *next;
  };

  Node *root;
  Node *last;

public:
  LinkedList() {
    root = new Node;
    last = root;
  }

  ~LinkedList() {
    delete root;
  }

  void insert(T data) {
    last->data = data;

    Node *next;
    next = new Node;
    last->next = next;

    last = next;
  }

  T get(int index) {
    Node *conductor;
    conductor = root;

    int i = 0;
    while(conductor->next != NULL && i != index) {
      conductor = conductor->next;
      i++;
    }

    return conductor->data;
  }
};
