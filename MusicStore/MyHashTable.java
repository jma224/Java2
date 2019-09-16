//* Jingyan Ma *//

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class MyHashTable<K,V> implements Iterable<HashPair<K,V>>{
    // num of entries to the table
    private int numEntries;
    // num of buckets
    private int numBuckets;
    // load factor needed to check for rehashing
    private static final double MAX_LOAD_FACTOR = 0.75;
    // ArrayList of buckets. Each bucket is a LinkedList of HashPair
    private ArrayList<LinkedList<HashPair<K,V>>> buckets;

    // constructor
    public MyHashTable(int initialCapacity) {
        // ADD YOUR CODE BELOW THIS
        this.numEntries=0;
        this.numBuckets=initialCapacity;
        this.buckets=new ArrayList<>(numBuckets);
        for(int i=0;i<numBuckets;i++){
            this.buckets.add(new LinkedList<>());
        }
        //ADD YOUR CODE ABOVE THIS
    }

    public int size() {
        return this.numEntries;
    }

    public int numBuckets() {
        return this.numBuckets;
    }

    /**
     * Returns the buckets variable. Useful for testing  purposes.
     */
    public ArrayList<LinkedList< HashPair<K,V> > > getBuckets(){
        return this.buckets;
    }
    /**
     * Given a key, return the bucket position for the key.
     */
    public int hashFunction(K key) {
        int hashValue = Math.abs(key.hashCode())%this.numBuckets;
        return hashValue;
    }
    /**
     * Takes a key and a value as input and adds the corresponding HashPair
     * to this HashTable. Expected average run time  O(1)
     */
    public V put(K key, V value) {
        //  ADD YOUR CODE BELOW HERE
        if(key==null){
            throw new NullPointerException("The key cannot be null!");
        }
        int h_value = hashFunction(key);
        LinkedList<HashPair<K,V>> list = this.buckets.get(h_value);
        if (!list.isEmpty()){
            for(HashPair<K,V> hp: list){
                if (hp.getKey().equals(key)){
                    V value2 = hp.getValue();
                    hp.setValue(value);
                    return value2;
                }
            }
        }
        HashPair newhp = new HashPair(key,value);
        list.add(newhp);
        this.numEntries += 1;
        double newFactor = (double)this.numEntries/this.numBuckets;
        if(newFactor>MAX_LOAD_FACTOR){
            rehash();
        }
        //  ADD YOUR CODE ABOVE HERE
        return null;
    }


    /**
     * Get the value corresponding to key. Expected average runtime = O(1)
     */

    public V get(K key) {
        //ADD YOUR CODE BELOW HERE
        int h_value = hashFunction(key);
        LinkedList<HashPair<K,V>> list = this.buckets.get(h_value);
        if (!list.isEmpty()){
            for(HashPair<K,V> hp: list){
                if (hp.getKey().equals(key)){
                    return hp.getValue();
                }
            }
        }
        return null;
        //ADD YOUR CODE ABOVE HERE
    }

    /**
     * Remove the HashPair corresponding to key. Expected average runtime O(1)
     */

    public V remove(K key) {
        //ADD YOUR CODE BELOW HERE
        int h_value = hashFunction(key);
        LinkedList<HashPair<K,V>> list = this.buckets.get(h_value);
        if (!list.isEmpty()){
            for(HashPair<K,V> hp: list){
                if (hp.getKey().equals(key)){
                    V value = hp.getValue();
                    list.remove(hp);
                    this.numEntries -= 1;
                    return value;
                }
            }
        }
        return null;
        //ADD YOUR CODE ABOVE HERE
    }


    // Method to double the size of the hashtable if load factor increases
    // beyond MAX_LOAD_FACTOR.
    // Made public for ease of testing.
    public void rehash() {
        //ADD YOUR CODE BELOW HERE
        ArrayList<LinkedList<HashPair<K,V>>> oldBuckets = this.buckets;
        this.buckets = new ArrayList<LinkedList<HashPair<K,V>>>(2*numBuckets);
        this.numBuckets *= 2;
        this.numEntries = 0;
        for(int i=0;i<numBuckets;i++){
            this.buckets.add(new LinkedList<>());
        }
        for (int i=0; i<oldBuckets.size();i++){
            LinkedList<HashPair<K,V>> list = oldBuckets.get(i);
            if (!list.isEmpty()){
                for (HashPair<K,V> hp: list){
                    put(hp.getKey(), hp.getValue());
                }
            }
        }
        //ADD YOUR CODE ABOVE HERE
    }


    /**
     * Return a list of all the keys present in this hashtable.
     */
    public ArrayList<K> keys() {
        //ADD YOUR CODE BELOW HERE
        ArrayList<K> arraylist = new ArrayList<>();
        for(LinkedList<HashPair<K,V>> list:buckets){
            if (!list.isEmpty()){
                for (HashPair<K,V> hp: list){
                    arraylist.add(hp.getKey());
                }
            }
        }
        return arraylist;
        //ADD YOUR CODE ABOVE HERE
    }


    /**
     * Returns an ArrayList of unique values present in this hashtable.
     * Expected average runtime is O(n)
     */
    public ArrayList<V> values() {
        //ADD CODE BELOW HERE
        //ArrayList<V> arraylist = new ArrayList<>();
        MyHashTable<K,V> table = new MyHashTable<>(numBuckets);
        for(HashPair<K,V>hp:this) {
            table.put((K) hp.getValue(), null);
        }
        return (ArrayList<V>) table.keys();
        //ADD CODE ABOVE HERE
    }


    @Override
    public MyHashIterator iterator() {
        return new MyHashIterator();
    }

    private class MyHashIterator implements Iterator<HashPair<K,V>> {
        private LinkedList<HashPair<K,V>> entries;

        private MyHashIterator() {
            //ADD YOUR CODE BELOW HERE
            this.entries = new LinkedList<>();
            for(LinkedList<HashPair<K,V>> ll: buckets){
                for(HashPair<K,V> hp: ll){
                    this.entries.add(hp);
                }
            }
            //ADD YOUR CODE ABOVE HERE
        }

        @Override
        public boolean hasNext() {
            //ADD YOUR CODE BELOW HERE
            return (!entries.isEmpty());
            //ADD YOUR CODE ABOVE HERE
        }

        @Override
        public HashPair<K,V> next() {
            //ADD YOUR CODE BELOW HERE
            HashPair<K,V> hp = entries.removeFirst();
            return hp;
            //ADD YOUR CODE ABOVE HERE
        }

    }
}
