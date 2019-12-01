
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SpellingHelp {
    private static List<SpellingHelp> WORDS = new ArrayList<SpellingHelp>();
    private static int totalQuestions;
    private static int correctAnswers = 0;
    //
    private String word;
    private String definition;
    private String example;
    private SpellingHelp(String inValue) {
        String[] parts = inValue.split("\\|");
        for (int i = 0; i < parts.length; i++) {
            parts[i] = (parts[i]).trim();
        }
        if (parts.length >= 1 && !(parts[0]).isEmpty()) {
            word = parts[0];
        }
        if (parts.length >= 2 && !(parts[1]).isEmpty()) {
            definition = parts[1];
        }
        if (parts.length >= 3 && !(parts[2]).isEmpty()) {
            example = parts[2];
        }
    }
    //
    public static void main(String[] inArgs) throws Exception {
        init(inArgs);
        Scanner scanner = new Scanner(System.in);
        for (int i = 0; i < totalQuestions; i++) {
            SpellingHelp nextWord = getWord();
            boolean answeredCorrectly = true;
            while (true) {
                sayWord(nextWord);
                System.out.print("SPELL IT: ");
                String userEntry = scanner.nextLine().trim();
                if (!userEntry.isEmpty()) {
                    if (userEntry.equalsIgnoreCase(nextWord.word)) {
                        if (answeredCorrectly) {
                            correctAnswers++;
                        }
                        System.out.println("CORRECT!");
                        break;
                    } else {
                        answeredCorrectly = false;
                        System.out.println("WRONG, IT'S SPELLED: " + nextWord.word);
                    }
                }
            }
        }
        System.out.println("TOTAL CORRECT ANSWERS - " + correctAnswers);
        System.out.println("TOTAL QUESTIONS - " + totalQuestions);
        double correctRatio = ((double)correctAnswers)/totalQuestions;
        System.out.println("PERCENTAGE CORRECT - " + (correctRatio*100) + "%");
    }
    private static void init(String[] inArgs) throws Exception {
        if (inArgs.length != 2) {
            throw new IllegalArgumentException(
                    "<word_file> and <question_count> parameters must be specified");
        }
        try (BufferedReader br = new BufferedReader(new FileReader(inArgs[0]));) {
            String nextLine;
            while ((nextLine = br.readLine()) != null) {
                nextLine = nextLine.trim();
                if (!nextLine.isEmpty()) {
                    SpellingHelp sh = new SpellingHelp(nextLine);
                    if (sh.word != null) {
                        WORDS.add(sh);
                    }
                }
            }
        }
        totalQuestions = Integer.parseInt(inArgs[1]);
    }
    private static SpellingHelp getWord() {
        int index = (int)(Math.random()*(WORDS.size()));
        return WORDS.get(index);
    }
    private static void sayWord(SpellingHelp inWord) throws Exception {
        Runtime r = Runtime.getRuntime();
        String[] thingsToSay = new String[]{inWord.word, inWord.definition, inWord.example};
        for (String nextThing : thingsToSay) {
            if (nextThing != null) {
                Process p1 = r.exec("say " + nextThing);
                p1.waitFor();
                p1.destroy();
            }
        }
    }
}
