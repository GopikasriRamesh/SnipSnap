package Snipsnap;

import java.io.*;
import java.util.HashMap;

public class FileManager {
    private static final String FILE_NAME = "shortly_data.txt";

    // Save shortCode → longURL mappings
    public static void saveToFile(HashMap<String, String> shortToLong) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (String shortCode : shortToLong.keySet()) {
                String longURL = shortToLong.get(shortCode);
                writer.write(shortCode + "=" + longURL);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("❌ Error saving to file: " + e.getMessage());
        }
    }

    // Load shortCode → longURL mappings
    public static HashMap<String, String> loadFromFile() {
        HashMap<String, String> map = new HashMap<>();
        File file = new File(FILE_NAME);
        if (!file.exists()) return map;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    map.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            System.out.println("❌ Error loading from file: " + e.getMessage());
        }

        return map;
    }
}
