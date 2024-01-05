package ie.atu.sw;

import static java.lang.System.out;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Executors;
import java.util.stream.Stream;
import java.util.Map;
import java.io.FileWriter;

public class VirtualThreadFileParser {
    private Collection<String> words = new ConcurrentLinkedDeque<>();
    private Map<String, Integer> lexicon;
    private String outputText;

    public void go(String inputFile, String inputLexicon, String outputFile) throws Exception {
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
        outputText = words.size() + " words in inputted text file. ";
        // Write to the output file.
        writeSentiment(outputFile, outputText, 1);

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
        outputText = lexicon.size() + " words in lexicon. ";
        writeSentiment(outputFile, outputText, 0);

        // Determine the sentiment of the text.
        getSentiment(outputFile);
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

    public void getSentiment(String outputFile) {
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
            outputText = "Positive sentiment detected. " + sentiment;
            writeSentiment(outputFile, outputText, 0);
        } 
        else if (sentiment < 0) {
            out.println("Negative sentiment detected. " + sentiment);
            outputText = "Negative sentiment detected. " + sentiment;
            writeSentiment(outputFile, outputText, 0);
        } 
        else {
            out.println("Neutral sentiment detected." + sentiment);
            outputText = "Neutral sentiment detected." + sentiment;
            writeSentiment(outputFile, outputText, 0);
        }
    }

    // Write the sentiment to the output file.
    public void writeSentiment(String outputFile, String toWrite, int appendOrOverwrite) {
        try {
            FileWriter fileWriter;
            if (appendOrOverwrite == 0) {
                fileWriter = new FileWriter(outputFile, true); 
            } else {
                fileWriter = new FileWriter(outputFile); 
            }
            PrintWriter writer = new PrintWriter(fileWriter);
            writer.println(toWrite);
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file. ");
        }
    }

    public static void main(String[] args) throws Exception {
        //new VirtualThreadFileParser().go("./shakespeare.txt");
        //out.println("Lines: " + line);
    }
}