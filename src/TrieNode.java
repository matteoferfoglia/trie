import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Optional;

/** Class representing a node of the {@link Trie}.
 * Note: children are saved with an {@link Hashtable}, this means the order
 * does not matter (O(1) for access a child).
 * 25th Aug 2021
 * @param <T> The generic type of the data eventually contained by an instance of this class.
 * @author Matteo Ferfoglia
 */
public class TrieNode<T> {

    /** The data eventually associated with this node. */
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private Optional<T> data;

    /** Children of this node*/
    private final Hashtable<Character, TrieNode<T>> children;

    /** Constructor.*/
    public TrieNode(final T data) {
        this.data = Optional.ofNullable(data);
        this.children = new Hashtable<>();
    }

    /** Constructor. Creates a new node with an empty key ('') and without data. */
    public TrieNode() {
        this(null);
    }

    /** @return the data associated with this node if it is present, null otherwise. */
    public T get() {
        return data.orElse(null);
    }

    /**
     * @return the {@link TrieNode} corresponding to the desired word
     * @throws NullPointerException If the desired node is not found.
     */
    public TrieNode<T> findByPrefix(char[] word) throws NullPointerException {
        if(word.length==0) {
            return this;
        } else {
            char[] subArray = new char[word.length-1];
            System.arraycopy(word, 1, subArray, 0, subArray.length);
            return this.children.get(word[0]).findByPrefix(subArray);
        }
    }

    /**
     * Inserts a new word in the data structure. If the word is already present, it
     * will be replaced and the old data (if present) will be returned.
     * @param word The new word to insert in the sub-trie having this node as root node.
     * @param data The data associated with the node.
     * @return the previously contained data, if it was already associated to the given word, null otherwise.
     */
    public T insert(char[] word, T data) {

        // Find the parent of the new node to insert
        TrieNode<T> parent = this,
                    oldParent = this;
        int i = 0;
        for(; i<word.length; i++) {
            try {
                parent = parent.findByPrefix(Arrays.copyOfRange(word, i, i + 1));
                if(parent==oldParent) {
                    break;
                } else {
                    oldParent = parent;
                }
            } catch (NullPointerException ne) {
                // node not found
                break;
            }
        }
        if(i==word.length) {
            // the word was already present
            T oldData = parent.get();   // the old data will be returned
            parent.data = Optional.ofNullable(data);
            return oldData;
        } else {
            for(; i<word.length-1; i++ ) {
                parent.children.put(word[i], new TrieNode<>(null) );
                parent = parent.children.get(word[i]);
            }
            parent.children.put(word[i], new TrieNode<>(data));
            return null;
        }
    }

    /** @return a list of all data contained in this node (if present) and in its children. */
    public List<T> exploreSubtree() {
        List<T> list = new ArrayList<>();
        data.ifPresent(list::add);
        for(TrieNode<T> aChild : children.values()) {
            list.addAll(aChild.exploreSubtree());
        }
        return list;
    }
}
