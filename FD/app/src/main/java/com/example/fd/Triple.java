package com.wonbin.fad;
import java.util.*;

/**
 *
 * @param <T1> String, allergyType
 * @param <T2> ArrayList<String>, Synonyms
 * @param <T3> ArrayList<String>, Hidden Types
 */
public class Triple<T1, T2, T3> {
    private String T1;
    private ArrayList<String> T2 = new ArrayList<String>();
    private ArrayList<String> T3 = new ArrayList<String>();

    public String getAllergyType() {
        return this.T1;
    }

    public ArrayList<String> getSynonyms() {
        return this.T2;
    }

    public ArrayList<String> getHidden() {
        return this.T3;
    }

    public void insert(String T1, ArrayList<String> T2, ArrayList<String> T3) {
        this.T1 = T1;
        this.T2 = T2;
        this.T3 = T3;
    }
}