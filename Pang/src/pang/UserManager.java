package pang;

import java.io.*;

public class UserManager {
    private static final String USER_FILE = "users.csv";
    private static String loggedInUser = null;

    // Kayıt işlemi:
    public static boolean register(String username, String password) {
        File file = new File(USER_FILE);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            // Kullanıcı zaten kayıtlı mı?
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 2 && parts[0].equals(username)) {
                        return false; // kayıtlı
                    }
                }
            }

            // Kayıt yapılır
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
                writer.write(username + "," + password);
                writer.newLine();
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Giriş işlemi
    public static boolean login(String username, String password) {
        File file = new File(USER_FILE);
        if (!file.exists()) return false;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2 && parts[0].equals(username) && parts[1].equals(password)) {
                    loggedInUser = username;
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isLoggedIn() {
        return loggedInUser != null;
    }

    public static String getLoggedInUser() {
        return loggedInUser;
    }

    public static void logout() {
        loggedInUser = null;
    }
}
