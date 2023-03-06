package com.example.paraulogic;

import java.util.Iterator;
import java.util.Stack;

/*
 * @author Pau Rosado Muñoz
 */
public class BSTmapping<K extends Comparable, V> {
    private Node root;
    // Contructor
    public BSTmapping() {
        this.root = null;
    }

    // Mètode per saber si l'arbre es buid
    public boolean isEmpty() {
        return root == null;
    }

    // Afegir element a l'arbre mitjançant la funció recursiva
    public V put(K key, V value) {
        Find f = new Find(null);
        this.root = putRec(key, value, root, f);
        return f.value;
    }
    private Node putRec(K key, V value, Node current, Find f) {
        // Cas en el que no hi era
        if (current == null) {
            return new Node(key, value);
        } else {
            // Cas en el que el trobam
            if (current.key.equals(key)) {
                f.value = (V) current.value;
                current.value = value;
                return current;
            }
            // Casos en el que encara no l'hem trobat
            if (key.compareTo(current.key) < 0) {
                current.left = putRec(key, value, current.left, f);
            } else {
                current.right = putRec(key, value, current.right, f);
            }
            return current;
        }
    }

    // Eliminar element amb clau 'key' mitjançant funció recursiva
    public V remove(K key) {
        Find f = new Find(null);
        this.root = removeRec(key, root, f);
        return root.value;
    }
    private Node removeRec(K key, Node current, Find f) {
        // Cas en el que no hi era
        if (current == null) {
            return null;
        }
        // Cas en el que el trobam
        if (current.key.equals(key)) {
            // Eliminam el node
            f.value = (V) current.value;
            // Comprovam els seus fills i els reordenam
            if (current.right != null && current.left != null) {
                Node parent = current;
                Node plowest = current.right;
                while (plowest.left != null) {
                    parent = plowest;
                    plowest = plowest.left;
                }
                plowest.left = current.left;
                if (plowest != current.right) {
                    parent.left = plowest.right;
                    plowest.right = current.right;
                }
                return plowest;
            } else if (current.left == null && current.right != null) {
                return current.right;
            } else if (current.left != null && current.right == null) {
                return current.left;
            }
        }
        // Casos en el que encara no l'hem trobat
        if (key.compareTo(current.key) < 0) {
            current.left = removeRec(key, current.left, f);
        } else {
            current.right = removeRec(key, current.right, f);
        }
        return current;
    }

    // Obternir un element amb clau 'key' mitjançant la funció recursiva
    public V get(K key) {
        Node n = getRec (key, root);
        if (n == null) {
            return null;
        } else {
            return n.value;
        }
    }
    private Node getRec(K key, Node current) {
        // Cas en el que no hi era
        if (current == null) {
            return null;
        } else {
            // Cas en el que el trobam
            if (current.key.equals(key)) {
                return current;
            }
            // Casos en el que encara no l'hem trobat
            if (key.compareTo(current.key) < 0) {
                return getRec(key, current.left);
            } else {
                return getRec(key, current.right);
            }
        }
    }

    // Iterador de l'arbre
    public Iterator iterator() {
        Iterator it = new IteradorBSTmapping();
        return it;
    }
    // Interfície Iterador per poder recollir els nodes de l'arbre
    private class IteradorBSTmapping implements Iterator {
        // Pila per poder retornar els nodesde de menor a major
        private Stack<Node> pila;
        public IteradorBSTmapping() {
            Node n;
            pila = new Stack();
            if (root != null) {
                n = root;
                while (n.left != null) {
                    pila.push(n);
                    n = n.left;
                }
                pila.push(n);
            }
        }
        @Override
        public boolean hasNext() {
            return !pila.isEmpty();
        }
        // Extreim els nodes en inordre
        @Override
        public Object next() {
            Node n = pila.pop();
            Pair pair = new Pair(n.key, n.value);
            if (n.right != null) {
                n = n.right;
                while (n.left != null) {
                    pila.push(n);
                    n = n.left;
                }
                pila.push(n);
            }
            return pair;
        }
    }

    /*
     * Declaració de classes AUXILIARS
     */

    // Classe Node per poder formar l'arbre
    private class Node {
        private V value;
        private K key;
        private Node left,right;

        // Contructor
        public Node (K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    // Classe per poder localitzar els nodes
    private class Find {
        V value;
        // Contructor
        public Find(V v) {
            value = v;
        }
    }

    // Classe per poder retornar parelles clau-valor
    protected class Pair {
        V value;
        K key;
        // Constructor
        public Pair(K k, V v) {
            value = v;
            key = k;
        }
        // Getters
        public V getValue() {
            return value;
        }
        public K getKey() {
            return key;
        }
    }

}
