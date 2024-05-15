import java.io.FileNotFoundException;
public class PoetryMain {

public static void main(String[] args) throws FileNotFoundException {

    System.out.println("Hashing Poetry - Jonny Jackson A02364754");
    String files[] = {"poems/Lester.txt", "poems/green.txt", "poems/Nose.txt", "poems/Zebra.txt"};
    String startWords[] = {"lester", "sam", "nose", "are"};
    int poemLength[] = {20,30,30,50 };
    boolean printTable []= {true,true,false, true};


    for (int i = 0; i < 4; i++) {
        WritePoetry poem = new WritePoetry(files[i]);
        System.out.println(poem.writePoem(startWords[i], poemLength[i], printTable[i]));
    }
}

}