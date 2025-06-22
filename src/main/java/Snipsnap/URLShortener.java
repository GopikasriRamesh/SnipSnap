package Snipsnap;

import java.util.HashMap;
import java.util.Random;

/**
 * URLShortener class handles the logic of shortening long URLs
 * into unique short codes and retrieving the original URLs.
 */
public class URLShortener {
    private HashMap<String, String> shortToLong;
    private HashMap<String, String> longToShort;
    private final String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private final int CODE_LENGTH = 6;
    private Random rand;

    /**
     * Constructor loads existing mappings from file (if any),
     * and initializes HashMaps for storing URLs.
     */
    public URLShortener() {
        // Load saved mappings from file
        shortToLong = FileManager.loadFromFile();

        // Rebuild longToShort reverse map
        longToShort = new HashMap<>();
        for (String shortCode : shortToLong.keySet()) {
            String longURL = shortToLong.get(shortCode);
            longToShort.put(longURL, shortCode);
        }

        rand = new Random();
    }

    public String shortenURL(String longURL) {
        if(longURL==null || longURL.trim().isEmpty()){
            return "Error:URL cannot be empty!";
        }
        // If URL already exists, return its short code
        if (longToShort.containsKey(longURL)) {
            return longToShort.get(longURL);
        }

        // Generate a new unique short code
        String shortCode = generateShortCode();
        while (shortToLong.containsKey(shortCode)) {
            shortCode = generateShortCode(); // avoid collision
        }

        // Store both mappings
        shortToLong.put(shortCode, longURL);
        longToShort.put(longURL, shortCode);

        // Save to file
        FileManager.saveToFile(shortToLong);

        return shortCode;
    }

    public String getOriginalURL(String shortCode) {
        if(shortCode==null||shortCode.trim().isEmpty()){
            return "Error:Short code cannot be empty!";
        }
        String longURL=shortToLong.get(shortCode);
        if(longURL==null){
            return "No matching URL found for this short code!!";
        }
        return longURL;
    }

    private String generateShortCode() {
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(chars.charAt(rand.nextInt(chars.length())));
        }
        return code.toString();
    }
}
