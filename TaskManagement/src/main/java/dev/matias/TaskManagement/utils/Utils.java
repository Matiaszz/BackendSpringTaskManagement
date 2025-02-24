package dev.matias.TaskManagement.utils;

import java.io.IOException;

public class Utils {
    public static void clearScreen() throws IOException, InterruptedException {
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")){
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            return;
        }
        new ProcessBuilder("clear").inheritIO().start().waitFor();
    }
}
