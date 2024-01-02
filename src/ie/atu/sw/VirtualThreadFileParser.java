package ie.atu.sw;

import static java.lang.System.out;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Executors;
import java.util.Map;

public class VirtualThreadFileParser {
    private static int line = 0;

    private Collection<String> words = new ConcurrentLinkedDeque<>();
    private Map<String, Integer> lexicon; 

    public void go(String book, String lexicon) throws Exception {

        try (var pool = Executors.newVirtualThreadPerTaskExecutor()) {
            Files.lines(
                Paths.get(book)
            )
            .forEach(
                text -> pool.execute(
                    () -> process(text, ++line)
                )
            );
        }
        //out.println(words);
        out.println(words.size());
    }

    public void process(String text, int line) {
        Arrays.stream(text.split("\\s+")).forEach(w -> words.add(w));
    }

    public static void main(String[] args) throws Exception {
        new VirtualThreadFileParser().go("./shakespeare.txt");
        out.println("Lines: " + line);
    }
}