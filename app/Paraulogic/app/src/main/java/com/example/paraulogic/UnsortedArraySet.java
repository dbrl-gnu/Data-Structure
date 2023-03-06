package com.example.paraulogic;
/*
 * @author Pau Rosado Muñoz
 */
import java.util.Iterator;

public class UnsortedArraySet<E> {
    private E[] array;
    private int n;

    // Constructor
    public UnsortedArraySet(int max) {
        this.array = (E[]) new Object[max];
        this.n = 0;
    }

    // Mètode per afegir al conjunt
    public boolean add(E elem) {
        // Recorrem l'array
        for (int i = 0; i < n; i++) {
            if (((E) this.array[i]).equals(elem)) {
                // Si ja l'hem trobat return
                return false;
            }
        }
        // Si no l'hem trobat
        if (n < this.array.length) {
            // L'afegim si cap
            this.array[this.n++] = elem;
            return true;
        }
        // Sino return false
        return false;
    }
    // Mètode per eliminar un element
    public boolean remove(E elem) {
        // Recorrem l'array
        for (int i = 0; i < n; i++) {
            if (((E) this.array[i]).equals(elem)) {
                // Si el trobam l'eliminam
                this.array[i] = this.array[--this.n];
                return true;
            }
        }
        // Sino return false
        return false;
    }

    // Mètode contains
    public boolean contains(E elem) {
        // Recorrem l'array
        for (int i = 0; i < n; i++) {
            // Si el trobam return true
            if (((E) this.array[i]).equals(elem)) {
                return true;
            }
        }
        // Si no return false
        return false;
    }

    // Mètode per saber si esta buid
    public boolean isEmpty() {
        return this.n == 0;
    }

    // Iterador
    public Iterator iterator() {
        Iterator it = new IteratorUnsortedArraySet();
        return it;
    }

    // Interfície Iterador per recórrer el conjunt
    private class IteratorUnsortedArraySet implements Iterator {
        private int idx;
        private IteratorUnsortedArraySet() {
            idx = 0;
        }
        @Override
        public boolean hasNext() {
            return idx < n;
        }
        @Override
        public Object next() {
            idx++;
            return array[idx - 1];
        }
    }
}
