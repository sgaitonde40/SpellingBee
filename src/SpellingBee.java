import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Spelling Bee
 *
 * This program accepts an input of letters. It prints to an output file
 * all English words that can be generated from those letters.
 *
 * For example: if the user inputs the letters "doggo" the program will generate:
 * do
 * dog
 * doggo
 * go
 * god
 * gog
 * gogo
 * goo
 * good
 *
 * It utilizes recursion to generate the strings, mergesort to sort them, and
 * binary search to find them in a dictionary.
 *
 * @author Zach Blick, [ADD YOUR NAME HERE]
 *
 * Written on March 5, 2023 for CS2 @ Menlo School
 *
 * DO NOT MODIFY MAIN OR ANY OF THE METHOD HEADERS.
 */
public class SpellingBee {

    private String letters;
    private ArrayList<String> words;
    public static final int DICTIONARY_SIZE = 143091;
    public static final String[] DICTIONARY = new String[DICTIONARY_SIZE];

    public SpellingBee(String letters) {
        this.letters = letters;
        words = new ArrayList<String>();
    }

    // TODO: generate all possible substrings and permutations of the letters.
    //  Store them all in the ArrayList words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void generate() {
        // TODO: YOUR CODE HERE — Call your recursive method!
        generation("", letters);
    }

    public void generation(String ans, String original) {
        words.add(ans);
        if(original.length() == 0) {
            return;
        }
        for(int i = 0; i < original.length(); i++) {
            generation(ans + original.substring(i,i + 1), original.substring(0, i) + original.substring(i + 1));
        }
    }

    // TODO: Apply mergesort to sort all words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void sort() {
        // YOUR CODE HERE
        words = mergesort(words, 0, words.size() - 1);
        System.out.println(words);
    }

    public ArrayList<String> mergesort(ArrayList<String> s, int low, int high) {
        if((high - low) == 0) {
            ArrayList<String> x = new ArrayList<>();
            x.add(s.get(low));
            return x;
        }
        int middle = (high + low) / 2;
        ArrayList<String> arr1 = mergesort(s, low, middle);
        ArrayList<String> arr2 = mergesort(s,middle + 1, high);
        return merge(arr1, arr2);
    }

    public ArrayList<String> merge(ArrayList<String> one, ArrayList<String> two) {
        ArrayList<String> merged = new ArrayList<>();
        int i = 0, j = 0;
        while(i < one.size() && j < two.size()) {
            if(one.get(i).compareTo(two.get(j)) < 0) {
                merged.add(i+j, one.get(i));
                i++;
            }
            else {
                merged.add(i+j, two.get(j));
                j++;
            }
        }
        while(j < two.size()) {
            merged.add(i+j, two.get(j));
            j++;
        }
        while(i < one.size()) {
            merged.add(i+j, one.get(i));
            i++;
        }
        return merged;
    }

    // Removes duplicates from the sorted list.
    public void removeDuplicates() {
        int i = 0;
        while (i < words.size() - 1) {
            String word = words.get(i);
            if (word.equals(words.get(i + 1)))
                words.remove(i + 1);
            else
                i++;
        }
    }

    // TODO: For each word in words, use binary search to see if it is in the dictionary.
    //  If it is not in the dictionary, remove it from words.
    public void checkWords() {
        // YOUR CODE HERE
        ArrayList<String> validwords = new ArrayList<>();
        for(int i = 0; i < words.size(); i++) {
            String word = words.get(i);
            if(found(word)) {
                validwords.add(word);
            }
        }
        words = validwords;
        System.out.println(validwords);
    }

    public boolean found(String s) {
        int left = 0;
        int right = DICTIONARY_SIZE;
        while(right >= left) {
            int middle = (left + right) / 2;
            if(DICTIONARY[middle].equals(s)) {
                return true;
            }
            else if(DICTIONARY[middle].compareTo(s) < 0) {
                left = middle + 1;
            }
            else {
                right = middle - 1;
            }
        }
        return false;
    }

    // Prints all valid words to wordList.txt
    public void printWords() throws IOException {
        File wordFile = new File("Resources/wordList.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(wordFile, false));
        for (String word : words) {
            writer.append(word);
            writer.newLine();
        }
        writer.close();
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public SpellingBee getBee() {
        return this;
    }

    public static void loadDictionary() {
        Scanner s;
        File dictionaryFile = new File("Resources/dictionary.txt");
        try {
            s = new Scanner(dictionaryFile);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open dictionary file.");
            return;
        }
        int i = 0;
        while(s.hasNextLine()) {
            DICTIONARY[i++] = s.nextLine();
        }
    }

    public static void main(String[] args) {

        System.out.println("a".compareTo("b"));

        // Prompt for letters until given only letters.
        Scanner s = new Scanner(System.in);
        String letters;
        do {
            System.out.print("Enter your letters: ");
            letters = s.nextLine();
        }
        while (!letters.matches("[a-zA-Z]+"));

        // Load the dictionary
        SpellingBee.loadDictionary();

        // Generate and print all valid words from those letters.
        SpellingBee sb = new SpellingBee(letters);
        sb.generate();
        sb.sort();
        sb.removeDuplicates();
        sb.checkWords();
        try {
            sb.printWords();
        } catch (IOException e) {
            System.out.println("Could not write to output file.");
        }
        s.close();
    }
}
