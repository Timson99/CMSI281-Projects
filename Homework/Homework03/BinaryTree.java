   /** ````````~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 *  File          : BinaryTree.java
 *  Purpose       :  
 *  Date          :  2018-11-02
 *  Author        :  Timothy Herrmann
 *  Description   :  Provided by Book. Changed to hold int frequency and char data items for purposes of Huffman Tree 
 *  Notes         :  None
 *  Warnings      :  None
 *  Exceptions    :  None
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */ 

import java.io.*;
import java.util.*; // for Stack class

class Node
{
    public char character; // data item (key)
    public int frequency; // data item
    public Node leftChild; // this node’s left child
    public Node rightChild; // this node’s right child
    
    public void displayNode() { // display ourself
        System.out.print("{");
        System.out.print(character);
        System.out.print(", ");
        System.out.print(frequency);
        System.out.print("} ");
    }
} // end class Node

public class BinaryTree
{
    private Node root; // first node of tree

    public BinaryTree() { // constructor
        root = null;  // no nodes in tree yet
    }
    
    public int getRootFrequency() {
        return root.frequency;
    }
    public Node getRoot() {
        return root;
    }
    
    public Node find(char key) { // find node with given key
        // (assumes non-empty tree)
        Node current = root; // start at root
        while(current.character != key) // while no match,
        {
            if(key < current.character) // go left?
                current = current.leftChild;
            else // or go right?
                current = current.rightChild;
                
            if(current == null) // if no child,
                return null; // didn’t find it
        }
        return current; // found it
    } // end find()

    
    public String getBinPath(Node localRoot, char key, String path, String lastPath )
    {
        if(localRoot == null) 
            return "2";
        else if(localRoot.character == key) {
            if(path.length() == 0 || path.charAt(path.length() - 1) != ' ')
                path += lastPath + " ";
            return path;
        }
        else
        {
            path += lastPath;
            if(!getBinPath(localRoot.leftChild, key, path,"0").equals("2"))
                return getBinPath(localRoot.leftChild, key, path,"0");
            if(!getBinPath(localRoot.rightChild, key, path,"1").equals("2"))
                return getBinPath(localRoot.rightChild, key, path,"1");
            return "2";
        }
    }
    
    public void insert(char id, int dd) {
        Node newNode = new Node(); // make new node
        newNode.character = id; // insert data
        newNode.frequency = dd;
        if(root==null) // no node in root
            root = newNode;
        else { // root occupied
            Node current = root; // start at root
            Node parent;
            
            while(true) // (exits internally)
            {
                parent = current;
                if(id < current.character) // go left?
                {
                    current = current.leftChild;
                    if(current == null) // if end of the line,
                    { // insert on left
                        parent.leftChild = newNode;
                        return;
                    }
                } // end if go left
                else // or go right?
                {
                    current = current.rightChild;
                    if(current == null) // if end of the line
                    { // insert on right
                        parent.rightChild = newNode;
                        return;
                    }
                } // end else go right
            } // end while
        } // end else not root
    } // end insert()
// -------------------------------------------------------------
    public boolean delete(int key) // delete node with given key
    { // (assumes non-empty list)
        Node current = root;
        Node parent = root;
        boolean isLeftChild = true;
        while(current.character != key) // search for node
        {
            parent = current;
            if(key < current.character) { // go left?
                isLeftChild = true;
                current = current.leftChild;
            }
            else { // or go right?
                isLeftChild = false;
                current = current.rightChild;
            }
            if(current == null) // end of the line,
            return false; // didn’t find it
        } // end while
        // found node to delete
        // if no children, simply delete it
        if(current.leftChild==null && current.rightChild==null) {
            if(current == root) // if root,
                root = null; // tree is empty
            else if(isLeftChild)
                parent.leftChild = null; // disconnect
            else // from parent
                parent.rightChild = null;
        }
        else if(current.rightChild==null) // if no right child, replace with left subtree
            if(current == root)
                root = current.leftChild;
        else if(isLeftChild)
            parent.leftChild = current.leftChild;
        else
            parent.rightChild = current.leftChild;
        // if no left child, replace with right subtree
        else if(current.leftChild==null)
            if(current == root)
                root = current.rightChild;
        else if(isLeftChild)
            parent.leftChild = current.rightChild;
        else
            parent.rightChild = current.rightChild;
        else // two children, so replace with inorder successor
        {
        // get successor of node to delete (current)
            Node successor = getSuccessor(current);
        // connect parent of current to successor instead
        if(current == root)
            root = successor;
        else if(isLeftChild)
            parent.leftChild = successor;
        else
            parent.rightChild = successor;
            // connect successor to current’s left child
        successor.leftChild = current.leftChild;
        } // end else two children
        // (successor cannot have a left child)
        return true; // success
    } // end delete()
// -------------------------------------------------------------
// returns node with next-highest value after delNode
// goes to right child, then right child’s left descendents
    private Node getSuccessor(Node delNode)
    {
        Node successorParent = delNode;
        Node successor = delNode;
        Node current = delNode.rightChild; // go to right child
        while(current != null) // until no more
        { // left children,
            successorParent = successor;
            successor = current;
            current = current.leftChild; // go to left child
        }
        // if successor not
        if(successor != delNode.rightChild) // right child,
        { // make connections
            successorParent.leftChild = successor.rightChild;
            successor.rightChild = delNode.rightChild;
        }
        return successor;
    }
// -------------------------------------------------------------
    public void traverse(int traverseType)
    {
        switch(traverseType)
        {
            case 1: System.out.print("\nPreorder traversal: ");
                    preOrder(root);
                    break;
            case 2: System.out.print("\nInorder traversal: ");
                    inOrder(root);
            break;
            case 3: System.out.print("\nPostorder traversal: ");
                    postOrder(root);
                    break;
        }
        System.out.println();
    }
    
    private void preOrder(Node localRoot)
    {
        if(localRoot != null)
        {
            System.out.print(localRoot.character + " ");
            preOrder(localRoot.leftChild);
            preOrder(localRoot.rightChild);
        }
    }

    private void inOrder(Node localRoot)
    {
        if(localRoot != null)
        {
            inOrder(localRoot.leftChild);
            System.out.print(localRoot.character + " ");
            inOrder(localRoot.rightChild);
        }
    }

    private void postOrder(Node localRoot)
    {
        if(localRoot != null)
        {
            postOrder(localRoot.leftChild);
            postOrder(localRoot.rightChild);
            System.out.print(localRoot.character + " ");
        }
    }
// -------------------------------------------------------------
    public void displayTree()
    {
        Stack<Node> globalStack = new Stack<Node>();
        globalStack.push(root);
        int nBlanks = 32;
        boolean isRowEmpty = false;
        System.out.println("......................................................");
        while(isRowEmpty==false)
        {
            Stack<Node> localStack = new Stack<Node>();
            isRowEmpty = true;
            for(int j=0; j<(nBlanks); j++) {
                System.out.print(" ");
            }
            while(globalStack.isEmpty()==false)
            {
                boolean spLong = true;
                Node temp = (Node)globalStack.pop();
                if(temp != null)
                {
                    System.out.print(temp.character);
                    localStack.push(temp.leftChild);
                    localStack.push(temp.rightChild);
                    
                    if(temp.leftChild != null || temp.rightChild != null)
                        isRowEmpty = false;
                }
                else {
                    System.out.print("--");
                    localStack.push(null);
                    localStack.push(null);
                }
                for(int j=0; j<(nBlanks*2-3); j++) {
                        System.out.print(" ");
                }
                if(spLong && nBlanks != 0)
                    System.out.print(" ");
                
                spLong = !spLong;
                    
            } // end while globalStack not empty
            System.out.println();
            nBlanks /= 2;
            while(localStack.isEmpty()==false) {
                globalStack.push( localStack.pop() );
            }
        } // end while isRowEmpty is false
        System.out.println("......................................................");
    } // end displayTree()

} 













