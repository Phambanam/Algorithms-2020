package lesson3;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

// attention: Comparable is supported but Comparator is not
public class BinarySearchTree<T extends Comparable<T>> extends AbstractSet<T> implements CheckableSortedSet<T> {

    private static class Node<T> {
        final T value;
        Node<T> left = null;
        Node<T> right = null;

        Node(T value) {
            this.value = value;
        }
    }
    List<T> elementData = new ArrayList<>();
    private Node<T> root = null;

    private int size = 0;

    @Override
    public int size() {
        return size;
    }

    private Node<T> find(T value) {
        if (root == null) return null;
        return find(root, value);
    }

    private Node<T> find(Node<T> start, T value) {
        int comparison = value.compareTo(start.value);
        if (comparison == 0) {
            return start;
        } else if (comparison < 0) {
            if (start.left == null) return start;
            return find(start.left, value);
        } else {
            if (start.right == null) return start;
            return find(start.right, value);
        }
    }

    @Override
    public boolean contains(Object o) {
        @SuppressWarnings("unchecked")
        T t = (T) o;
        Node<T> closest = find(t);
        return closest != null && t.compareTo(closest.value) == 0;
    }

    /**
     * Добавление элемента в дерево
     * <p>
     * Если элемента нет в множестве, функция добавляет его в дерево и возвращает true.
     * В ином случае функция оставляет множество нетронутым и возвращает false.
     * <p>
     * Спецификация: {@link Set#add(Object)} (Ctrl+Click по add)
     * <p>
     * Пример
     */
    @Override
    public boolean add(T t) {
        Node<T> closest = find(t);
        int comparison = closest == null ? -1 : t.compareTo(closest.value);
        if (comparison == 0) {
            return false;
        }
        Node<T> newNode = new Node<>(t);
        if (closest == null) {
            root = newNode;
        } else if (comparison < 0) {
            assert closest.left == null;
            closest.left = newNode;
        } else {
            assert closest.right == null;
            closest.right = newNode;
        }
        elementData.add(t);
        size++;
        return true;
    }

    /**
     * Удаление элемента из дерева
     * <p>
     * Если элемент есть в множестве, функция удаляет его из дерева и возвращает true.
     * В ином случае функция оставляет множество нетронутым и возвращает false.
     * Высота дерева не должна увеличиться в результате удаления.
     * <p>
     * Спецификация: {@link Set#remove(Object)} (Ctrl+Click по remove)
     * <p>
     * Средняя
     */
    @Override
    public boolean remove(Object o) {
        if (!contains(o)) {
            System.out.println("don't find");
            return false;
        }
        @SuppressWarnings("unchecked")
        T t = (T) o;

        Node<T> node = root;
        Node<T> nodeParent = null;
        boolean left = false;

        while (node.value.compareTo(t) != 0) {
            nodeParent = node;
            if (node.value.compareTo(t) < 0) {
                left = false;
                node = node.right;
            } else {
                left = true;
                node = node.left;
            }
        }
        if (node.left == null) {
            if (node == root) root = node.right;
            else if (left) nodeParent.left = node.right;
            else nodeParent.right = node.right;
        } else if (node.right == null) {
            if (node == root) root = node.left;
            else if (left) nodeParent.left = node.left;
            else nodeParent.right = node.left;
        } else {
            Node<T> maxNode = node.left;
            Node<T> perNode = node;

            while (maxNode.right != null) {
                perNode = maxNode;
                maxNode = maxNode.right;
            }
            if (maxNode != node.left) {
                perNode.right = maxNode.left;
                maxNode.left = node.left;
            }
            maxNode.right = node.right;
            if (node == root) {
                root = maxNode;
            } else if (left) {
                nodeParent.left = maxNode;
            } else {
                nodeParent.right = maxNode;
            }
        }

        size--;
        return true;
        // трудоёмкост : O(log(n)) в средним случае, O(n) в худшем случае n- количество узлов дерева
        // ресурсоёмкост : O(1)
    }

    @Nullable
    @Override
    public Comparator<? super T> comparator() {
        return null;
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new BinarySearchTreeIterator();
    }

    public class BinarySearchTreeIterator implements Iterator<T> {
        private int location = 0;
        int  lastRet = -1;

       /**
         * Проверка наличия следующего элемента
         * <p>
         * Функция возвращает true, если итерация по множеству ещё не окончена (то есть, если вызов next() вернёт
         * следующий элемент множества, а не бросит исключение); иначе возвращает false.
         * <p>
         * Спецификация: {@link Iterator#hasNext()} (Ctrl+Click по hasNext)
         * <p>
         * Средняя
         */
        @Override
        public boolean hasNext() {
            return location != size;
        }

        /**
         * Получение следующего элемента
         * <p>
         * Функция возвращает следующий элемент множества.
         * Так как BinarySearchTree реализует интерфейс SortedSet, последовательные
         * вызовы next() должны возвращать элементы в порядке возрастания.
         * <p>
         * Бросает NoSuchElementException, если все элементы уже были возвращены.
         * <p>
         * Спецификация: {@link Iterator#next()} (Ctrl+Click по next)
         * <p>
         * Средняя
         */
        @Override
        public T next() {
            int i = location;
            if(i >= size) throw new IllegalStateException();
            List<T> elementData = BinarySearchTree.this.elementData;
            location = i + 1;
            return elementData.get(lastRet = i );
            // трудоёмкост : O(1)
            // ресурсоёмкост : O(n) n - количество елементов в list
        }

        /**
         * Удаление предыдущего элемента
         * <p>
         * Функция удаляет из множества элемент, возвращённый крайним вызовом функции next().
         * <p>
         * Бросает IllegalStateException, если функция была вызвана до первого вызова next() или же была вызвана
         * более одного раза после любого вызова next().
         * <p>
         * Спецификация: {@link Iterator#remove()} (Ctrl+Click по remove)
         * <p>
         * Сложная
         */
        @Override
        public void remove() {
            if (lastRet < 0)
                throw new IllegalStateException();
            BinarySearchTree.this.remove(elementData.get(lastRet ));
            elementData.remove(lastRet );
            location = lastRet;
            lastRet = -1;
        }
    }

    /**
     * Подмножество всех элементов в диапазоне [fromElement, toElement)
     * <p>
     * Функция возвращает множество, содержащее в себе все элементы дерева, которые
     * больше или равны fromElement и строго меньше toElement.
     * При равенстве fromElement и toElement возвращается пустое множество.
     * Изменения в дереве должны отображаться в полученном подмножестве, и наоборот.
     * <p>
     * При попытке добавить в подмножество элемент за пределами указанного диапазона
     * должен быть брошен IllegalArgumentException.
     * <p>
     * Спецификация: {@link SortedSet#subSet(Object, Object)} (Ctrl+Click по subSet)
     * (настоятельно рекомендуется прочитать и понять спецификацию перед выполнением задачи)
     * <p>
     * Очень сложная (в том случае, если спецификация реализуется в полном объёме)
     */
    int sizeSubSet(T from, T toEnd) {
        count = 0;
        sizeSubSet(from, toEnd, root);
        return count;
    }

    private int count;

    private void sizeSubSet(T from, T toEnd, Node<T> node) {
        if (node != null) {
            sizeSubSet(from, toEnd, node.left);
            if (from == null) {
                if (node.value.compareTo(toEnd) < 0) count++;
            } else if (toEnd == null) {
                if (node.value.compareTo(from) >= 0) count++;
            } else {
                if (node.value.compareTo(from) >= 0 && node.value.compareTo(toEnd) < 0) count++;
            }
            sizeSubSet(from, toEnd, node.right);
        }
    }

    @NotNull
    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) {
        return new mySortedSet<>(this,
                false, fromElement,
                false, toElement);
    }

    /**
     * Подмножество всех элементов строго меньше заданного
     * <p>
     * Функция возвращает множество, содержащее в себе все элементы дерева строго меньше toElement.
     * Изменения в дереве должны отображаться в полученном подмножестве, и наоборот.
     * <p>
     * При попытке добавить в подмножество элемент за пределами указанного диапазона
     * должен быть брошен IllegalArgumentException.
     * <p>
     * Спецификация: {@link SortedSet#headSet(Object)} (Ctrl+Click по headSet)
     * (настоятельно рекомендуется прочитать и понять спецификацию перед выполнением задачи)
     * <p>
     * Сложная
     */
    @NotNull
    @Override
    public SortedSet<T> headSet(T toElement) {
        return new mySortedSet<>(this,
                true, null, false,
                toElement);
    }

    /**
     * Подмножество всех элементов нестрого больше заданного
     * <p>
     * Функция возвращает множество, содержащее в себе все элементы дерева нестрого больше toElement.
     * Изменения в дереве должны отображаться в полученном подмножестве, и наоборот.
     * <p>
     * При попытке добавить в подмножество элемент за пределами указанного диапазона
     * должен быть брошен IllegalArgumentException.
     * <p>
     * Спецификация: {@link SortedSet#tailSet(Object)} (Ctrl+Click по tailSet)
     * (настоятельно рекомендуется прочитать и понять спецификацию перед выполнением задачи)
     * <p>
     * Сложная
     */

    @NotNull
    @Override
    public SortedSet<T> tailSet(T fromElement) {
        return new mySortedSet<>(this, false, fromElement, true, null);
    }

    @Override
    public T first() {
        if (root == null) throw new NoSuchElementException();
        Node<T> current = root;
        while (current.left != null) {
            current = current.left;
        }
        return current.value;

    }

    @Override
    public T last() {
        if (root == null) throw new NoSuchElementException();
        Node<T> current = root;
        while (current.right != null) {
            current = current.right;
        }
        return current.value;
    }

    public int height() {
        return height(root);
    }

    private int height(Node<T> node) {
        if (node == null) return 0;
        return 1 + Math.max(height(node.left), height(node.right));
    }

    public boolean checkInvariant() {
        return root == null || checkInvariant(root);
    }

    private boolean checkInvariant(Node<T> node) {
        Node<T> left = node.left;
        if (left != null && (left.value.compareTo(node.value) >= 0 || !checkInvariant(left))) return false;
        Node<T> right = node.right;
        return right == null || right.value.compareTo(node.value) > 0 && checkInvariant(right);
    }
}

final class mySortedSet<T extends Comparable<T>> extends AbstractSet<T> implements CheckableSortedSet<T> {

    final BinarySearchTree<T> m;
    final T eStart, eEnd;
    final boolean fromStart, toEnd;

    mySortedSet(BinarySearchTree<T> m,
                boolean fromStart, T eStart,
                boolean toEnd, T eEnd) {
        this.m = m;
        this.fromStart = fromStart;
        this.eStart = eStart;
        this.toEnd = toEnd;
        this.eEnd = eEnd;
    }

    final boolean myCompare(Object o) {
        @SuppressWarnings("unchecked")
        T t = (T) o;
        if (eStart != null && eEnd != null) {
            return t.compareTo(eStart) >= 0 && t.compareTo(eEnd) < 0;
        } else {
            if (eStart == null) {
                return t.compareTo(eEnd) < 0;
            } else return t.compareTo(eStart) >= 0;
        }
    }

    @Override
    public boolean add(T t) {
        if (!myCompare(t)) throw new IllegalArgumentException();
        return myCompare(t) && m.add(t);
    }

    @Override
    public boolean remove(Object o) {
        if (!myCompare(o)) throw new IllegalArgumentException();
        return myCompare(o) && m.remove(o);
    }

    @Override
    public boolean contains(Object o) {
        return myCompare(o) && m.contains(o);
    }

    @Nullable
    @Override
    public Iterator<T> iterator() {
        return null;
    }

    @Override
    public int size() {
        return m.sizeSubSet(eStart, eEnd);
    }

    @Nullable
    @Override
    public Comparator<? super T> comparator() {
        return null;
    }



    @NotNull
    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) {
        if (fromElement.compareTo(toElement) > 0) throw new NoSuchElementException();
        return new mySortedSet<>(m, false, fromElement,
                false, toElement);
    }

    @NotNull
    @Override
    public SortedSet<T> headSet(T toElement) {
        return new mySortedSet<>(m, fromStart, eStart, false, toElement);
    }

    @NotNull
    @Override
    public SortedSet<T> tailSet(T fromElement) {
        return new mySortedSet<>(m, false, fromElement, toEnd, eEnd);
    }

    @Override
    public T first() {
        if (size() == 0) throw new NoSuchElementException();
        if (eStart == null) {
            return m.first();
        } else if (toEnd) {
            return eStart;
        } else
            {
            Iterator<T> bIterator = m.iterator();
            T current = null;
            while (bIterator.hasNext()) {
                current = bIterator.next();
                if (current.compareTo(eStart) >= 0) {
                    break;
                }
            }
            return current;
        }
    }

    @Override
    public T last() {
        if (size() == 0) throw new NoSuchElementException();
        if (eEnd == null) {
            return m.last();
        } else {
            Iterator<T> bIterator = m.iterator();
            T current;
            T sCurrent = null;
            while (bIterator.hasNext()) {
                current = bIterator.next();
                if (current.compareTo(eEnd) >= 0) {
                    break;
                }
                sCurrent = current;
            }
            return sCurrent;
        }
    }


    @Override
    public int height() {
        return 0;
    }

    @Override
    public boolean checkInvariant() {
        return false;
    }
}