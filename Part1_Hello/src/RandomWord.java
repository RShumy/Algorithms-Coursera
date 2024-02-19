import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

public class RandomWord {
    public static void main(String[] args) {
        int index = 0;
        String champion_word = "";

        while ( !StdIn.isEmpty() ){
            String current_word = StdIn.readString();

            if ( StdRandom.bernoulli((double)1/++index) ) {
                champion_word = current_word;
            }
        }
        System.out.println(champion_word);
    }
}
