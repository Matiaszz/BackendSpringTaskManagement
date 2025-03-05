package dev.matias.TaskManagement.utils;


public class Utils {
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
