package com.example.notscattergories;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Random;

public class Game {

    private int timerDuration;
    private int numberOfCategories;

    private int[] catIndexes;

    final private char[] LETTERS = "ABCDEFGHIJKLMNOPRSTW".toCharArray();

    private String letter;

    private int totalNumberOfCategoriesInFile;



    public Game(int timerDuration, int numberOfCategories, int totalNumberOfCategoriesInFile){
        this.timerDuration = timerDuration;
        this.numberOfCategories = numberOfCategories;
        this.totalNumberOfCategoriesInFile = totalNumberOfCategoriesInFile;
    }

    public void start(){
        generateLetter();
        generateCategories();
    }

    private void generateCategories(){
        catIndexes = new int[numberOfCategories]; //Create based on numberOfCategories var
        Arrays.fill(catIndexes, -1); // fill with -1 to start.

        int length = catIndexes.length;
        int index = 0;
        Random rand = new Random();

        while (catIndexes[length - 1] == -1) {
            int num = rand.nextInt(totalNumberOfCategoriesInFile);
            if (!containsInt(catIndexes, num)) {
                catIndexes[index] = num;
                index++;
            }
        }
    }

    private boolean containsInt(int[] array, int number) {
        for(int i=0; i < array.length; i++) {
            if (array[i] == number) {
                return true;
            }
        }
        return false;
    }

    private void generateLetter() {
        Random rand = new Random();
        int index = rand.nextInt(LETTERS.length);
        letter = Character.toString(LETTERS[index]);
    }

    public String getLetter(){
        return letter;
    }

    public int[] getCategoryIndexes(){
        return catIndexes;
    }
}
