public class Main {

    public static void main(String[] args) {

        Trie<String> t = new Trie<>();

        String str = "a";
        try {
            t.get("a");
        } catch (NotFoundException e) {
            System.err.println("\"" + str + "\" not found");
        }

        for(String s : new String[]{"cane", "cavallo", "cavia", "albero","alberone", "cavolo"}) {
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

    }
}
