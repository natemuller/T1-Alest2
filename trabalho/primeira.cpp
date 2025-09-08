#include <stdio.h>
#include <stdlib.h>
#include <iostream>
#include <list>
#include <string>

using namespace std;

class Node {
 public:
    Node(int execTime, string name, list<Node*> childs) :
    childs(childs),
    executionTime(execTime),
    name(name) {}

    void addChild(Node* node) {
        childs.push_back(node);
    }

    list<Node*> childs;
    int executionTime;
    string name;
};

int sumExecTime(Node* node) {
    if (node == nullptr) {
        return 0;
    }
    int result = node->executionTime;
    for(Node* n : node->childs) {
        result += sumExecTime(n);
    }
    return result;

}

int main() {
    cout << "Hello" << endl;

    Node* root = new Node(80, "sim", {});
    Node* child1 = new Node(100, "nao", {});
    Node* child2 = new Node(80, "abc", {});

    root->addChild(child1);
    root->addChild(child2);

    cout << "Execution time: " << sumExecTime(root) << endl;

    

    return 0;
}