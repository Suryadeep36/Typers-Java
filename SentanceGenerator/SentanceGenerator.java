package SentanceGenerator;
import java.util.Random;

public class SentanceGenerator {
    private static final String[] subjects = {"cook", "runner", "driver", "student", "musician", "artist", "writer", "boy", "girl", "man"};
    private static final String[] verbs = {"cooked", "ate", "drank", "solved", "played", "painted", "penned", "played", "wrote", "created"};
    private static final String[] adjectives = {"best", "sweetest", "hardest", "nicest", "saddest", "happiest"};
    private static final String[] objects = {"rice", "ice-cream", "beer", "equation", "tune", "portrait", "story", "song", "poem", "riddle"};
    
    public static String generateSentence() {
        String subject = getRandomElement(subjects);
        String verb = getRandomElement(verbs);
        String adjective = getRandomElement(adjectives);
        String object = getRandomElement(objects);
        return "The " + subject + " " + verb + " the " + adjective + " " + object + ".";
    }
    public static String generateParagraph(int numberOfSentences){
        String para = "";
        for(int i = 0; i < numberOfSentences; i++){
            if(i != numberOfSentences - 1)
            para += generateSentence() + " ";
            else
            para += generateSentence();
        }
        return para;
    }
    private static String getRandomElement(String[] array) {
        Random random = new Random();
        int index = random.nextInt(array.length);
        return array[index];
    }
}
