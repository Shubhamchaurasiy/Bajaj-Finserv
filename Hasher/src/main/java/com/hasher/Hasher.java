package com.hasher;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Random;

import org.json.JSONObject;
import org.json.JSONTokener;

public class Hasher {
	
	
	public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("provide args");
            return;
        }

        String prnNumber = args[0].trim().toLowerCase();
        String jsonFilePath = args[1];

        try (FileInputStream fis = new FileInputStream(jsonFilePath)) {
            JSONObject jsonObject = new JSONObject(new JSONTokener(fis));
            String destinationValue = findDestination(jsonObject);

            if (destinationValue != null) {
                String randomString = generateRandomString(8);
                String inputString = prnNumber + destinationValue + randomString;
                String hash = md5Hash(inputString);

                System.out.println(hash + ";" + randomString);
            } else {
                System.out.println("Key 'destination' not found in the JSON file.");
            }
        } catch (IOException e) {
            System.err.println("Error reading the JSON file: " + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Error generating MD5 hash: " + e.getMessage());
        }
    }

    private static String findDestination(JSONObject jsonObject) {
        Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            Object value = jsonObject.get(key);

            if (key.equals("destination")) {
                return value.toString();
            }

            if (value instanceof JSONObject) {
                String found = findDestination((JSONObject) value);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }

    private static String generateRandomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    private static String md5Hash(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] digest = md.digest(input.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

}
