package net.itca.checker;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import net.itca.datastructures.BloomFilter;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by dylan on 19.01.18.
 */
public class DictionaryLoader {

    private static boolean hasLoaded = false;
    private static BloomFilter dictionary;

    private DictionaryLoader() {

    }

    public static BloomFilter loadDictionary() {
        if (hasLoaded) {
            System.out.println("Already loaded the dictionary");
            return dictionary;
        }

        String file = "/home/dylan/Development/Code/java/SpellChecker/resources/nederlands3.txt";

        try {
            List<String> lines = Files.readAllLines(Paths.get(file), Charset.forName("iso-8859-1"));
            dictionary = new BloomFilter(lines.size());
            lines.forEach(word -> dictionary.add(word.toLowerCase()));
            return dictionary;
        } catch (IOException ioe) {
            throw new RuntimeException("IOE", ioe);
        }
    }
}
