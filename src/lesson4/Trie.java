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

    public Node root = new Node();

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
        private List<String> listNode;
        private String str = "";
        private int count = 0;

        public TreeIterator() {
            listNode = new ArrayList<>();
            addString(root, 0, new StringBuilder());
        }

        public void addString(Node node, int level, StringBuilder sequence) {
            Map<Character, Node> chil = node.children;
            Object[] cha = chil.keySet().toArray();
            for (Object character : cha) {
                if ((char) character == (char) 0)
                    listNode.add(sequence.toString());
                sequence = sequence.insert(level, character);
                addString(chil.get( character), level + 1, sequence);
                sequence.deleteCharAt(level);
            }
        }

        @Override
        public boolean hasNext() {
            return location < listNode.size();
        }

        @Override
        public Object next() {
            if (location == listNode.size()) throw new IllegalStateException();
            str = listNode.get(location++);
            if (str.equals("")) throw new NoSuchElementException();
            return str;
            // трудоёмкост : O(1)
            // ресурсоёмкост : O(n) n - количество елементов в list
        }

        @Override
        public void remove() {
            if (location == 0) throw new IllegalStateException();
            count++;
            if (count >= 2)
                throw new IllegalStateException();
            else {

                Trie.this.remove(listNode.get(location - 1));
                listNode.remove(listNode.get(location - 1));
                location--;
            }
        }
        // трудоёмкост : O(log(n)) в средним случае, O(n) в худшем случае n- количество узлов дерева
        // ресурсоёмкост : O(1)
    }
}