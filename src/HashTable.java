
// QuadraticProbing Hash table class
//
// CONSTRUCTION: an approximate initial size or default of 101
//
// ******************PUBLIC OPERATIONS*********************
// bool insert( x )       --> Insert x
// bool remove( x )       --> Remove x
// bool contains( x )     --> Return true if x is present
// void makeEmpty( )      --> Remove all items


/**
 * Probing table implementation of hash tables.
 * Note that all "matching" is based on the equals method.
 * @author Mark Allen Weiss
 */
public class HashTable<K, V>
{
    /**
     * Construct the hash table.
     */
    public HashTable( )
    {
        this( DEFAULT_TABLE_SIZE );
    }

    /**
     * Construct the hash table.
     * @param size the approximate initial size.
     */
    public HashTable( int size )
    {
        allocateArray( size );
        doClear( );
    }

    /**
     * Insert into the hash table. If the item is
     * already present, do nothing.
     * Implementation issue: This routine doesn't allow you to use a lazily deleted location.  Do you see why?
     * @param key the key to insert.
     * @param value the value to insert.
     */

    public boolean insert(K key, V value)
    {
        // Insert key-value pair as active
        int currentPos = findPos(key);
        if (isActive(currentPos))
            return false;

        array[currentPos] = new HashEntry<>(key, value, true);
        currentActiveEntries++;

        // Rehash; see Section 5.5
        if (++occupiedCt > array.length / 2)
            rehash();

        return true;
    }


    public String toString(int limit)
    {
        StringBuilder sb = new StringBuilder();
        int ct = 0;
        for (int i = 0; i < array.length && ct < limit; i++)
        {
            if (array[i] != null && array[i].isActive)
            {
                sb.append(i + ": " + array[i].key + " => " + array[i].element + "\n");
                ct++;
            }
        }
        return sb.toString();
    }

    /**
     * Expand the hash table.
     */
    private void rehash( )
    {
        HashEntry<K,V> [ ] oldArray = array;

        // Create a new double-sized, empty table
        allocateArray( 2 * oldArray.length );
        occupiedCt = 0;
        currentActiveEntries = 0;

        // Copy table over
        for( HashEntry<K,V> entry : oldArray )
            if( entry != null && entry.isActive )
                insert( entry.key,entry.element );
    }

    /**
     * Method that performs quadratic probing resolution.
     * @param key the item to search for.
     * @return the position where the search terminates.
     * Never returns an inactive location.
     */
    private int findPos( K key )
    {
        int offset = doubleHash(key);
        int currentPos = myhash( key );

        while( array[ currentPos ] != null &&
                !array[ currentPos ].key.equals( key ) )
        {
            currentPos += offset;  // Compute ith probe
            offset += 2;
            if( currentPos >= array.length )
                currentPos -= array.length;
        }

        return currentPos;
    }

    /**
     * Calculate the offset for double hashing.
     * @param key the item to calculate the offset for.
     * @return the offset for double hashing.
     */
    private int doubleHash(K key) {
        int hashVal = key.hashCode();

        //  prime number smaller than the table size
        int prime=1;
        int secondHash = prime - (hashVal % prime);

        return secondHash;


    }

    /**
     * Remove from the hash table.
     * @param key the item to remove.
     * @return true if item removed
     */
    public boolean remove( K key )
    {
        int currentPos = findPos( key );
        if( isActive( currentPos ) )
        {
            array[ currentPos ].isActive = false;
            currentActiveEntries--;
            return true;
        }
        else
            return false;
    }

    /**
     * Get current size.
     * @return the size.
     */
    public int size( )
    {
        return currentActiveEntries;
    }

    /**
     * Get length of internal table.
     * @return the size.
     */
    public int capacity( )
    {
        return array.length;
    }

    /**
     * Find an item in the hash table.
     * @param key the item to search for.
     * @return true if item is found
     */
    public boolean contains( K key )
    {
        int currentPos = findPos( key );
        return isActive( currentPos );
    }

    /**
     * Find an item in the hash table.
     * @param key the item to search for.
     * @return the matching item.
     */
    public V find( K key )
    {
        int currentPos = findPos( key );
        if (!isActive( currentPos )) {
            return null;
        }
        else {
            return array[currentPos].element;
        }
    }

    /**
     * Return true if currentPos exists and is active.
     * @param currentPos the result of a call to findPos.
     * @return true if currentPos is active.
     */
    private boolean isActive( int currentPos )
    {
        return array[ currentPos ] != null && array[ currentPos ].isActive;
    }

    /**
     * Make the hash table logically empty.
     */
    public void makeEmpty( )
    {
        doClear( );
    }

    private void doClear( )
    {
        occupiedCt = 0;
        for( int i = 0; i < array.length; i++ )
            array[ i ] = null;
    }

    private int myhash( K key )
    {
        int hashVal = key.hashCode( );

        hashVal %= array.length;
        if( hashVal < 0 )
            hashVal += array.length;

        return hashVal;
    }

    private static class HashEntry<K, V>
    {
        public K  key;   // the key
        public V element; // associated object
        public boolean isActive;  // false if marked deleted

        public HashEntry( K k, V e )
        {
            this( k, e, true );
        }

        public HashEntry( K k, V e, boolean i )
        {
            key=k;
            element  = e;
            isActive = i;
        }
    }

    private static final int DEFAULT_TABLE_SIZE = 101;

    private HashEntry<K,V> [ ] array; // The array of elements
    private int occupiedCt;         // The number of occupied cells: active or deleted
    private int currentActiveEntries;                  // Current size

    /**
     * Internal method to allocate array.
     * @param arraySize the size of the array.
     */
    private void allocateArray( int arraySize )
    {
        array = new HashEntry[ nextPrime( arraySize ) ];
    }

    /**
     * Internal method to find a prime number at least as large as n.
     * @param n the starting number (must be positive).
     * @return a prime number larger than or equal to n.
     *
     */
    private static int nextPrime( int n )
    {
        if( n % 2 == 0 )
            n++;

        for( ; !isPrime( n ); n += 2 )
            ;

        return n;
    }

    /**
     * Internal method to test if a number is prime.
     * Not an efficient algorithm.
     * @param n the number to test.
     * @return the result of the test.
     */
    private static boolean isPrime( int n )
    {
        if( n == 2 || n == 3 )
            return true;

        if( n == 1 || n % 2 == 0 )
            return false;

        for( int i = 3; i * i <= n; i += 2 )
            if( n % i == 0 )
                return false;

        return true;
    }


    // Simple main
    public static void main( String [ ] args ) {
        //HashTable<String,WordFreqInfo> x = new HashTable<>();
        //    x.insert("sam",6);
        //    x.insert("am",7);
        //    x.insert("i",1);
        //    System.out.println(x.toString(10));
        //    if(x.contains("am")) {
        //        System.out.println("SAM EXISTS");
        //    } else {
        //        System.out.println("Sam doesnt exist sorry buddy");
        //    }
        //    System.out.println(x.find("sam"));

    }
}

