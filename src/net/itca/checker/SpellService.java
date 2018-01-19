package net.itca.checker;

import net.itca.datastructures.BloomFilter;

import java.util.Objects;

/**
 * Created by dylan on 19.01.18.
 */
public class SpellService {

    private static SpellService service;
    private BloomFilter dictionary;
    private SpellService(){
        dictionary = DictionaryLoader.loadDictionary();
    }

    public static SpellService getSpellService(){
        if (service == null) {
            service = new SpellService();
        }
        return service;
    }

    public boolean correctSpelling(String word){
        Objects.requireNonNull(word);
        return dictionary.contains(word);
    }

}

