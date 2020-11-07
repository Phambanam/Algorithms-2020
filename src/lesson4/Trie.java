package lesson4;

import lesson3.BinarySearchTree;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Префиксное дерево для строк
 */
public class Trie extends AbstractSet<String> implements Set<String> {

    static class Node {
        Character c;
        Map<Character, Node> children = new LinkedHashMap<>();
        boolean isLeaf = false;
    }

    private Node root = new Node();

    private int size = 0;
    List<String> elementData = new ArrayList<>();
    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        root.children.clear();
        size = 0;
    }

    private String withZero(String initial) {
        return initial + (char) 0;
    }

    @Nullable
    private Node findNode(String element) {
        Node current = root;
        for (char character : element.toCharArray()) {
            if (current == null) {
                return null;
            }
            current = current.children.get(character);
        }
        return current;
    }

    @Override
    public boolean contains(Object o) {
        String element = (String) o;
        return findNode(withZero(element)) != null;
    }

    @Override
    public boolean add(String element) {
        Node current = root;
        boolean modified = false;
        for (char character : withZero(element).toCharArray()) {
            Node child = current.children.get(character);
            if (child != null) {
                current = child;
            } else {
                modified = true;
                Node newChild = new Node();
                current.children.put(character, newChild);
                current = newChild;
            }
        }
        if (modified) {
            size++;
        }
        elementData.add(element);
        current.isLeaf = true;
        return modified;
    }

    @Override
    public boolean remove(Object o) {
        String element = (String) o;
        Node current = findNode(element);
        if (current == null) return false;
        if (current.children.remove((char) 0) != null) {
            size--;
            return true;
        }
        return false;
    }


    /**
     * Итератор для префиксного дерева
     * <p>
     * Спецификация: {@link Iterator} (Ctrl+Click по Iterator)
     * <p>
     * Сложная
     */
    @NotNull
    @Override
    public Iterator<String> iterator() {
        return new TreeIterator();
    }

    public class TreeIterator implements Iterator {
        private int location = 0;
        int  lastRet = -1;

        @Override
        public boolean hasNext() {
            return location != size;
        }

        @Override
        public Object next() {
            int i = location;
            if(i >= size) throw new IllegalStateException();
            List<String> elementData = Trie.this.elementData;
            location = i + 1;
            return elementData.get(lastRet = i );
        }

        @Override
        public void remove() {
            if (lastRet < 0)
                throw new IllegalStateException();
           Trie.this.remove(elementData.get(lastRet ));
            elementData.remove(lastRet );
            location = lastRet;
            lastRet = -1;
        }

        // трудоёмкост : O(log(n)) в средним случае, O(n) в худшем случае n- количество узлов дерева
        // ресурсоёмкост : O(1)
    }
}