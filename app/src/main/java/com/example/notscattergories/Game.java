package com.example.notscattergories;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Random;

/**
 * A class to handle the game logic of NotScattergories.
 */
public class Game {

    private int numberOfCategories;
    private int[] catIndexes;
    final private char[] LETTERS = "ABCDEFGHIJKLMNOPRSTW".toCharArray();
    private String letter;
    private int totalNumberOfCategoriesInFile;


    /**
     * Constructor used to create a new game.
     * @param numberOfCategories The numberOfCategories to be used for the game.
     * @param totalNumberOfCategoriesInFile The total number of categories in file. (This could be
     *                                      refactored into a Shared Preference).
     */
    public Game(int numberOfCategories, int totalNumberOfCategoriesInFile){
        this.numberOfCategories = numberOfCategories;
        this.totalNumberOfCategoriesInFile = totalNumberOfCategoriesInFile;
    }

    /**
     * A method to start a game.
     */
    public void start(){
        generateLetter();
        generateCategories();
    }

    /**
     * A method to generate random category indices, based on numberOfCategories class variable.
     */
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

    /**
     * A method to check whether a specific number can be found inside an array.
     * @param array Array to be searched
     * @param number Number to check against
     * @return True if array contains int. False if array does not contain int.
     */
    private boolean containsInt(int[] array, int number) {
        for(int i=0; i < array.length; i++) {
            if (array[i] == number) {
                return true;
            }
        }
        return false;
    }

    /**
     * A method to generate a random letter.
     */
    private void generateLetter() {
        Random rand = new Random();
        int index = rand.nextInt(LETTERS.length);
        letter = Character.toString(LETTERS[index]);
    }

    /**
     * Getter method for letter class variable.
     * @return letter class variable
     */
    public String getLetter(){
        return letter;
    }

    /**
     * Getter method for category indexes Array.
     * @return Category indexes array
     */
    public int[] getCategoryIndexes(){
        return catIndexes;
    }
}
