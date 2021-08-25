import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {

        Trie<String> t = new Trie<>();

        String s1 = "a";
        try {
            t.get("a");
        } catch (NotFoundException e) {
            System.err.println("\"" + s1 + "\" not found");
        }

        for(String s : new String[]{"cane", "cavallo", "cavia", "albero","alberone", "cavolo", "cani", "22n", "22p", "22n"}) {
            t.insert(s, "_"+s+"_");
        }
        System.out.println(t + "\tSize: " + t.size());

        // search by key
        try {
            System.out.println(t.get("cane"));                      // _cane_
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        // insert when already present
        System.out.println(t.insert("cane","_cane2_")); // _cane_
        System.out.println(t + "\tSize: " + t.size());

        // Find by prefix
        System.out.println(t.findByPrefix("ca"));




        ///////////////////// Comparison with HashTable /////////////////////

        Hashtable<String, String> hashTable = new Hashtable<>();
        Trie<String> trie = new Trie<>();
        long start, stop;

        // Generates keys and values (duplicates might be present)
        List<String> keys = new ArrayList<>();
        List<String> values = new ArrayList<>();
        int N = 2000000;  // number of string to generate
        int length = 10;
        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        Random rnd = new Random();
        rnd.setSeed(0);
        for(int j=0; j<N; j++) {
            StringBuilder sb = new StringBuilder(length);
            for (int i = 0; i < length; i++) {
                sb.append(AB.charAt(rnd.nextInt(AB.length())));
            }
            String str = sb.toString();
            keys.add(str);
            values.add(str+"_");
        }

        // insertion
        start = System.nanoTime();
        for(int i=0; i<keys.size(); i++) {
            hashTable.put(keys.get(i), values.get(i));
        }
        stop = System.nanoTime();
        System.out.println("INSERTION in HashTable: " + (stop-start)/1000.0 + " us, " + keys.size() + " elements, " + hashTable.size() + " distinct values");
        start = System.nanoTime();
        for(int i=0; i<keys.size(); i++) {
            trie.insert(keys.get(i), values.get(i));
        }
        stop = System.nanoTime();
        System.out.println("INSERTION in Trie: " + (stop-start)/1000.0 + " us, " + keys.size() + " elements, " + hashTable.size() + " distinct values");


        // System.out.println(trie.allEntries());

        // search
        String s;
        start = System.nanoTime();
        for(int i=0; i<keys.size(); i++) {
            s = hashTable.get(keys.get(i));
            if(s==null || !s.equals(values.get(i))) {
                throw new RuntimeException("Error! Wrong value found");
            }
        }
        stop = System.nanoTime();
        System.out.println("SEARCH in HashTable: " + (stop-start)/1000.0 + " us, " + keys.size() + " elements with duplicates, " + hashTable.size() + " distinct values");

        start = System.nanoTime();
        for(int i=0; i<keys.size(); i++) {
            try {
                trie.get(keys.get(i));
            } catch (NotFoundException e) {
                throw new RuntimeException("Error! Wrong value found: \"" + values.get(i) + "\" expected, but not found");
            }
        }
        stop = System.nanoTime();
        System.out.println("SEARCH in Trie: " + (stop-start)/1000.0 + " us, " + keys.size() + " elements with duplicates, " + hashTable.size() + " distinct values");


        // Search by prefix

        String prefixToFind = "abc";

        start = System.nanoTime();
        Hashtable<String, String> hashtablePrefix = new Hashtable<>();
        String data;
        for(String aKey : hashTable.keySet()) {
            if((data = hashTable.get(aKey)).startsWith(prefixToFind)) {
                hashtablePrefix.put(aKey, data);
            }
        }
        stop = System.nanoTime();
        System.out.println("SEARCH in HashTable by prefix: " + (stop-start)/1000.0 + " us, " +
                hashtablePrefix.entrySet().stream().sorted(Map.Entry.comparingByKey()).collect(Collectors.toList()) +   // needs sorting, too
                "\n\t" + hashtablePrefix.size() + " elements");

        start = System.nanoTime();
        Trie<String> triePrefix = trie.findByPrefix(prefixToFind);
        stop = System.nanoTime();
        System.out.println("SEARCH in Trie by prefix: " + (stop-start)/1000.0 + " us, " + triePrefix +
                "\n\t" + triePrefix.size() + " elements");


    }
}

/* OUTPUT

    "a" not found
    [_albero_, _alberone_, _22p_, _22n_, _cavallo_, _cavia_, _cavolo_, _cani_, _cane_]	Size: 9
    _cane_
    _cane_
    [_albero_, _alberone_, _22p_, _22n_, _cavallo_, _cavia_, _cavolo_, _cani_, _cane2_]	Size: 9
    [_cavallo_, _cavia_, _cavolo_, _cani_, _cane2_]
    INSERTION in HashTable: 499113.8 us, 2000000 elements, 2000000 distinct values
    INSERTION in Trie: 3229138.9 us, 2000000 elements, 2000000 distinct values
    SEARCH in HashTable: 179547.6 us, 2000000 elements with duplicates, 2000000 distinct values
    SEARCH in Trie: 2065444.3 us, 2000000 elements with duplicates, 2000000 distinct values
    SEARCH in HashTable by prefix: 373358.2 us, [abc5892wOg=abc5892wOg_, abc6R7Jatf=abc6R7Jatf_, abcL3tcG66=abcL3tcG66_, abcOAttnrD=abcOAttnrD_, abcOQM2WdG=abcOQM2WdG_, abccU5MmFe=abccU5MmFe_, abcgN5wFvg=abcgN5wFvg_, abchAaKAAF=abchAaKAAF_, abclGGxkKB=abclGGxkKB_]
        9 elements
    SEARCH in Trie by prefix: 3.0 us, [abcL3tcG66_, abc6R7Jatf_, abclGGxkKB_, abc5892wOg_, abchAaKAAF_, abcgN5wFvg_, abcOAttnrD_, abcOQM2WdG_, abccU5MmFe_]
        9 elements


 */