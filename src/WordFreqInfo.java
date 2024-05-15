import java.util.*;

public class WordFreqInfo {
    public String word;
    public int occurCt;
    public ArrayList<Freq> followList;

    public WordFreqInfo(String word, int count) {
        this.word = word;
        this.occurCt = count;
        this.followList = new ArrayList<Freq>();
    }


    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append( "'"+word+"' appears in total ");
        sb.append(occurCt + " times. ");
        for (Freq f : followList)
            sb.append(f.toString());

        return sb.toString();
    }

    public void incrementFrequency() {
        this.occurCt++; //i added this myself
    }

    public String getNextWord() {
        ArrayList<String> words = new ArrayList<>();
        for (Freq f : followList) {
            if (f != null) {
                for (int j = 0; j < f.followCt; j++) {
                    words.add(f.follow);
                }
            }
        }
        if (words.size() > 0) {
            Random random = new Random();
            int randomIndex = random.nextInt(words.size());
            return words.get(randomIndex);
        }
        return "error";
    }


    public void updateFollows(String follow) {
        //System.out.println("updateFollows " + word + " " + follow);
        for (Freq f : followList) {
            if (follow.compareTo(f.follow) == 0) {
                f.followCt++;
                return;
            }
        }
        followList.add(new Freq(follow, 1));
    }

    public static class Freq {
        String follow;
        int followCt;

        public Freq(String follow, int ct) {
            this.follow = follow;
            this.followCt = ct;
        }

        public String toString() {
            return "'"+follow + "' appears after " + followCt + ", ";
        }

        public boolean equals(Freq f2) {
            return this.follow.equals(f2.follow);
        }
    }


}

