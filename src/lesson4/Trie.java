package lesson4;

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
        private String lastElement  ;
        private Queue<String> elementData = new LinkedList<>();
        public TreeIterator(){
            addString(root,0,new StringBuilder());
        }
        public void addString(Node node, int level, StringBuilder sequence) {
            Map<Character, Node> chil = node.children;
            Object[] cha = chil.keySet().toArray();
            for (Object character : cha) {
                if ((char) character == (char) 0)
                    elementData.add(sequence.toString());
                sequence = sequence.insert(level, character);
                addString(chil.get( character), level + 1, sequence);
                sequence.deleteCharAt(level);
            }
        }
        @Override
        public boolean hasNext() {
            return elementData.peek() != null;
        }

        @Override
        public Object next() {
            if(elementData.peek() == null) throw new IllegalStateException();
            lastElement = elementData.remove();
            return  lastElement;
        }

        @Override
        public void remove() {
            if(lastElement == null) throw new IllegalStateException();
            if( !Trie.this.remove(lastElement)) throw new IllegalStateException();
        }

    }
}