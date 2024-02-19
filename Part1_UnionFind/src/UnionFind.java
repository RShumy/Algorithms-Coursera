public class UnionFind {

    public int[] elements;

    // For better performance of the Quick-Union implementation
    // we have to increase the space complexity
    // and store the size of the tree each root element belongs to
    private final int[] treeSize;

    public UnionFind(int length){
        this.elements = new int[length];
        this.treeSize = new int[length];
        for(int index=0; index<length; index++){
            // initializing all element sizes to one as at the beginning
            // all elements are their own root
            treeSize[index] = 1;
            elements[index] = index;
        }
    }



/*    // Initial union implementation big O of N going through all elements
    public void union(int first, int second){
        int firstId = elements[first];
        int secondId = elements[second];
        for (int index = 0; index< length; index++)

//            if instead of firstId we had referenced elements[first]
//            then at one moment elements[index] == elements[first]
//            and some elements previously connected would not be included
//            in the future connection

            if (elements[index] == firstId) elements[index] = secondId;
    }
*/

/*    // Initial connected() implementation where we only check if the
    // value of the first element is the same as the second one
    public boolean connected (int first, int second){
        return elements[first] == elements[second];
    }
*/





    // Find the -root- of an element in Quick union implementation
    // the value of the root will always be itself
    private int root(int element){
        while (elements[element] != element)
            element = elements[element];
        return element;
    }

    // Quick -union- implementation
    public void union(int first, int second){
        int rootFirst = root(first);
        int rootSecond = root(second);
        if (rootFirst == rootSecond) return;
        // we compare if the tree size of the second root is greater
        if ( compareTreeSize(rootSecond, rootFirst) ) {
            // the union operation here will make the root of the first element
            // a child of the root of the second element
            elements[rootFirst] = rootSecond;
            // we increase the tree size of the second root
            // with the tree size of the first root
            addTreeSize( rootSecond, rootFirst );
        }
        // reversing for the other alternative
        else {
            elements[rootSecond] = rootFirst;
            addTreeSize( rootFirst, rootSecond );
        }
    }

    public boolean compareTreeSize(int first, int second){
        return treeSize[first] > treeSize[second];
    }

    public void addTreeSize(int first, int second){
        treeSize[first] += treeSize[second];
    }

    // True/false -connected- query in the Quick-Union implementation
    public boolean connected(int first, int second){
        return root(first) == root(second);
    }



}
