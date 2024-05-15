import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class WritePoetry {
    String fileName;
    HashTable<String,WordFreqInfo> hashTable = new HashTable<>();
    public WritePoetry(String fileName) {
        this.fileName=fileName;
        //printFullPoem();
        generateHashTable();
    }
    public void printFullPoem() {
        try {
            File txtFile = new File(this.fileName);
            Scanner myReader = new Scanner(txtFile);
            while (myReader.hasNextLine()) {
                String line = myReader.nextLine();
                System.out.println(line);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    public void printHashTable() {
        StringBuilder tableString=new StringBuilder();
        tableString.append("The hash table:\n"+this.hashTable.toString(1000));
        System.out.println(tableString.toString());
    }
    public String writePoem(String startWord, int poemLength, boolean printTable) {
        if(printTable) {
            printHashTable();
        }


        StringBuilder s = new StringBuilder();
        boolean poemIsFinished = false;
        int wordsInPoem=1;
        ArrayList<String> poemWords = new ArrayList<>();
        poemWords.add(startWord);
        while(!poemIsFinished) {
            String lastWord = poemWords.getLast();
            if(lastWord.equals("\n")) {
                lastWord=poemWords.get(poemWords.size()-2); //get second to last
            }
            String nextWord = null;
            try {
                nextWord = hashTable.find(lastWord).getNextWord();
            } catch (Exception e) { //if its null
                nextWord=".";
            }
            poemWords.add(nextWord);
            wordsInPoem++;
            if(wordsInPoem>poemLength) {
                if(nextWord.equals(".") || nextWord.equals("?") || nextWord.equals("!")) {
                    poemIsFinished=true;
                }
            }
            if(nextWord.equals(".") || nextWord.equals("?") || nextWord.equals("!") || nextWord.equals(",")) {
                poemWords.add("\n");
            }
        }

        for(int i=0; i<poemWords.size(); i++) {
            s.append(poemWords.get(i)+" ");
        }


        return s.toString();
        //im not sure if this should private instead of public
    }


    public void generateHashTable() {
        //put each of the words in the poem in an arrayList "words"
        ArrayList<String> words=new ArrayList<>();
        try {
            File txtFile = new File(this.fileName);
            Scanner myReader = new Scanner(txtFile);
            while (myReader.hasNextLine()) {
                String line = myReader.nextLine();
                String[] wordsInLine = line.split(" ");
                //convert to lower case
                for (int i = 0; i < wordsInLine.length; i++) {
                    if (!wordsInLine[i].isEmpty()) {
                        words.add(wordsInLine[i].toLowerCase());
                    }
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }


        //put data into the hashTable
        for(int i=0; i<words.size()-1; i++) {
            String word=words.get(i);
            if(hashTable.contains(word)) {
                //increment the words value
                ((WordFreqInfo) hashTable.find(word)).incrementFrequency();
            }
            else {
                WordFreqInfo wfi = new WordFreqInfo(word,1);
                hashTable.insert(word,wfi);
            }
            //add to the following list
            if(i+2<words.size()) {
                String followingWord = words.get(i + 1);
                ((WordFreqInfo) hashTable.find(word)).updateFollows(followingWord);
            }
        }
    }

}