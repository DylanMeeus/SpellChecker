package net.itca.datastructures;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by dylan on 19.01.18.
 */
public class BloomFilter {

    private int arraySize; // to avoid the virtual call to 'm' (performance)
    private boolean[] m;
    private static final String SHA256 = "SHA-256";
    private static final String SHA1 = "SHA-1";
    private static final String MD5 = "md5";
    private Set<String> actualData = new HashSet<>(); // kept for testing purposes, this'd be in the database or whatever normally if we want to push down spacecomplexity.
    public BloomFilter(int size) {
        m = new boolean[size];
        this.arraySize = size;
    }

    public void add(String element) {
        // get the indices
        actualData.add(element);
        int[] indices = getIndices(element);
        for (int index : indices) {
            m[index] = true;
        }
    }

    public boolean contains(String element){
        int falseInM = 0;
        for (boolean b : m) {
            falseInM += !b ? 1 : 0;
        }
        System.out.println("false in M: " + falseInM);

        int[] indices = getIndices(element);
        for (int index : indices) {
            if (!m[index]) {
                return false;
            }
        }
        // at this point, we have not returned this we have a match (or hash collision, so we check our actual data)
        System.out.println("Checking Set");
        return actualData.contains(element);
    }

    private int[] getIndices(String element){
        // get the indices
        try {
            int firstIndex = sha1index(element);
            int secondIndex = sha256index(element);
            int thirdIndex = md5index(element);
            return new int[]{
                    firstIndex, secondIndex, thirdIndex
            };

        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException("Hashing algorithm not found (is it supported on your JVM?)", ex);
        }
    }

    private int sha256index(String element) throws NoSuchAlgorithmException{
        MessageDigest md = MessageDigest.getInstance(SHA256);
        return hashIndexResult(md, element);
    }

    private int sha1index(String element) throws NoSuchAlgorithmException{
        MessageDigest md = MessageDigest.getInstance(SHA1);
        return hashIndexResult(md, element);
    }

    private int md5index(String element) throws NoSuchAlgorithmException{
        MessageDigest md = MessageDigest.getInstance(MD5);
        return hashIndexResult(md, element);
    }

    private int hashIndexResult(MessageDigest md, final String element) {
        md.update(element.getBytes());
        byte[] result = md.digest();
        BigInteger bigInteger = new BigInteger(result);
        BigInteger bigIndex = bigInteger.mod(BigInteger.valueOf(arraySize));
        return bigIndex.intValue(); // has to fit in integer, because our "size" is an integer :-)
    }

}
