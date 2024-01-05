package ie.atu.sw;

import static java.lang.System.out;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Executors;
import java.util.stream.Stream;
import java.util.Map;

public class VirtualThreadFileParser {
    private Collection<String> words = new ConcurrentLinkedDeque<>();
    private Map<String, Integer> lexicon;

    public void go(String inputFile, String inputLexicon) throws Exception {
        lexicon = new HashMap<>();

        // Process the input text file.
        try (var pool = Executors.newVirtualThreadPerTaskExecutor()) {
            Files.lines(
                Paths.get(inputFile)
            )
            .forEach(
                text -> pool.execute(
                    () -> processInputFile(text)
                )
            );
        }
        //out.println(words);
        out.println(words.size() + " words in inputted text file. ");

        // Process the lexicon file.
        try (var pool = Executors.newVirtualThreadPerTaskExecutor()) {
            Files.lines(
                Paths.get(inputLexicon)
            )
            .forEach(
                text -> pool.execute(
                    () -> processLexiconFile(text)
                )
            );
        }
        out.println(lexicon.size() + " words in lexicon. ");

        // Determine the sentiment of the text.
        getSentiment();
    }

    public void processInputFile(String text) {
        Arrays.stream(text.split("\\s+")).forEach(w -> words.add(w));
    }

    // Process the lexicon file.
    public void processLexiconFile(String text) {
        String[] parts = text.split(",");
        if (parts.length == 2) {
            String key = parts[0];
            try {
                int value = Integer.parseInt(parts[1]);
                lexicon.put(key, value);
                //System.out.println("Added " + key + " with value " + value);
            } 
            catch (NumberFormatException e) {
                System.err.println("Invalid integer value: " + parts[1]);
            }
        }
    }

    public void getSentiment() {
        // Determine the sentiment of the text using the lexicon's second column.
        int sentiment = 0;
        for (String word : words) {
            if (lexicon.containsKey(word)) {
                int value = lexicon.get(word);
                sentiment += value;
            }
        }

        // Print the sentiment of the text.
        if (sentiment > 0) {
            out.println("Positive sentiment detected. " + sentiment);
        } 
        else if (sentiment < 0) {
            out.println("Negative sentiment detected. " + sentiment);
        } 
        else {
            out.println("Neutral sentiment detected." + sentiment);
        }
    }

    public static void main(String[] args) throws Exception {
        //new VirtualThreadFileParser().go("./shakespeare.txt");
        //out.println("Lines: " + line);
    }
}