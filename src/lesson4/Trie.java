package lesson4;

import java.util.*;
import kotlin.NotImplementedError;
import lesson3.BinarySearchTree;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Префиксное дерево для строк
 */
public class Trie extends AbstractSet<String> implements Set<String> {

    private static class Node {
        Map<Character, Node> children = new LinkedHashMap<>();
    }
    private Node root = new Node();

    private int size = 0;

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
            if (current == null) return null;
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

        System.out.println(root.children.keySet());

        System.out.println(element);
        System.out.println("------------------ ");

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
     *
     * Спецификация: {@link Iterator} (Ctrl+Click по Iterator)
     *
     * Сложная
     */
    @NotNull
    @Override
    public Iterator<String> iterator() {
        return new TreeIterator();
    }
    public class TreeIterator implements Iterator{
        private int location = 0;
        private Node node = null;
        private List<Node> listNode;
        private List<String> lString;
        private int count = 0;
        private TreeIterator() {
            listNode = new ArrayList<>();
            listNode.add(root);
        }
        public void addNode(Node node){
               if(node != null) {
                   addNode( node) ;
               }
               listNode.add(node);
        }
        @Override
        public boolean hasNext() {
            return location < listNode.size();
        }

        @Override
        public String next() {
            if( location == listNode.size() ) throw new IllegalStateException();

            return null;
        }

        @Override
        public void remove() {

        }
    }
}