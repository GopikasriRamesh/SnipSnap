package Snipsnap;

import java.sql.SQLOutput;
import java.util.Scanner;

public class Main {
    public static void main (String [] args){
        Scanner sc= new Scanner(System.in);
        URLShortener shortener=new URLShortener();

        System.out.println("Welcome to snipsnap-java version");

        while(true) {
            System.out.println("\nChoose An Option:");
            System.out.println("1.Shorten URL");
            System.out.println("2.Get Original URL");
            System.out.println("3.Exit");

            String input = sc.nextLine();
            int choice;

            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Please enter a valid number (1, 2, or 3)");
                continue;
            }


            switch(choice){
                case 1:
                    System.out.println("Enter Long URL:");
                    String longURL=sc.nextLine();
                    String shortCode= shortener.shortenURL(longURL);
                    System.out.println("Short URL code:"+shortCode);
                    break;

                case 2:
                    System.out.println("Enter Short Code:");
                    String code=sc.nextLine();
                    String originalURL=shortener.getOriginalURL(code);
                    if(originalURL!=null) {
                        System.out.println("Original URL:" + originalURL);
                    }else{
                        System.out.println("Invalid short code.no match found!!");
                    }
                    break;

                case 3:
                    System.out.println("exiting...Thank you for using SnipSnap");
                    return;

                default:
                    System.out.println("Invalid choice...");
                    }


            }
        }

        }

