/** Class representing a Trie.
 * 25th Aug 2021
 * @param <T> The generic type of the data eventually contained by a node of this tree.
 * @author Matteo Ferfoglia*/
public class Trie<T> {

    /** The root node. */
    private final TrieNode<T> rootNode;

    /** Constructor. Creates an empty Trie.*/
    public Trie() {
        this(null);
    }

    /** Constructor. Creates a Trie having as root the given node.
     * @param root The {@link TrieNode} to use as root for the instance to create.
     *             If the given node is null, a new empty node will be used as root.*/
    public Trie(TrieNode<T> root) {
        this.rootNode = root==null ? new TrieNode<>() : root;
    }

    /**
     * @return the subtree having the desired prefix as root node. It might be empty
     *          if no results are available
     */
    public Trie<T> findByPrefix(String prefix) {
        return new Trie<>(rootNode.findByPrefix(prefix.toCharArray()));
    }

    /**
     * @param word The word to find in this instance.
     * @return the data associated with the given string to find, if it is
     *          present, null otherwise.
     * @throws NotFoundException if the given word is not found in this instance.
     */
    public T get(String word) throws NotFoundException {
        try {
            return rootNode.findByPrefix(word.toCharArray()).get();
        } catch (NullPointerException e) {
            throw new NotFoundException();
        }
    }

    /** Inserts new data associated with the given word in this data structure.
     * @param word The new word to insert.
     * @param data The data associated with the word.
     * @return the data previously associated with the same word, if it was already
     * present in this instance, null otherwise.*/
    public T insert (final String word, T data) {
        return rootNode.insert(word.toCharArray(), data);
    }

    @Override
    public String toString() {
        return rootNode.exploreSubtree().toString();
    }

    /** @return the size (number of node containing some data) of this instance. */
    public int size() {
        return rootNode.exploreSubtree().size();
    }
}
