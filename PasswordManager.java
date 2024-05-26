import java.util.Scanner;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class PasswordManager {
    private static final int MAX_WRONG_ATTEMPTS = 3;
    private static final Scanner scanner = new Scanner(System.in);
    private static final SecureRandom random = new SecureRandom();
    private static String registeredPhoneNumber;
    private static int wrongAttemptsCount = 0;
    private static final Map<String, String> websitePasswords = new HashMap<>();

    public static void main(String[] args) {
        System.out.println("Welcome to Password Manager!");

        while (true) {
            System.out.println("\n1. Add Password");
            System.out.println("2. Edit Password");
            System.out.println("3. Forgot Password");
            System.out.println("4. Enter Password");
            System.out.println("5. Search Password");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    addPassword();
                    break;
                case 2:
                    editPassword();
                    break;
                case 3:
                    forgotPassword();
                    break;
                case 4:
                    enterPassword();
                    break;
                case 5:
                    searchPassword();
                    break;
                case 6:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void addPassword() {
        System.out.print("Enter website name: ");
        String website = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (isWeakPassword(password)) {
            System.out.println("Warning: Your password is weak. We suggest a strong password:Example@123");
            System.out.print("Enter password: ");
            password = scanner.nextLine();
            
            System.out.println(password);
        }

        websitePasswords.put(website, password);
        System.out.println("Password added successfully for " + website);
    }

    private static void editPassword() {
        System.out.print("Enter website name: ");
        String website = scanner.nextLine();

        System.out.print("Enter current password: ");
        String currentPassword = scanner.nextLine();

        if (!isCorrectPassword(website, currentPassword)) {
            System.out.println("Incorrect password. Access denied.");
            wrongAttemptsCount++;
            if (wrongAttemptsCount >= MAX_WRONG_ATTEMPTS) {
                System.out.println("Your website will be locked if the password entered again is wrong.");
                return;
            }
            return;
        }

        System.out.print("Enter new password: ");
        String newPassword = scanner.nextLine();

        if (isWeakPassword(newPassword)) {
            System.out.println("Warning: Your password is weak. We suggest a strong password:");
            System.out.print("Enter new password: ");
            newPassword = scanner.nextLine();
            
            System.out.println(newPassword);
        }

        websitePasswords.put(website, newPassword);
        System.out.println("Password updated successfully for " + website);
    }

    private static void forgotPassword() {
        System.out.print("Enter website name for which you want to set password: ");
        String website = scanner.nextLine();
        
        System.out.print("Enter your registered phone number: ");
        String phoneNumber = scanner.nextLine();

        // Validate the phone number
        if (phoneNumber.matches("[0-9]+") && phoneNumber.length() == 10) {
            String otp = generateOTP();
            System.out.println("An OTP has been sent to your registered phone number: " + otp);
            
            System.out.print("Enter OTP: ");
            String enteredOtp = scanner.nextLine();
            if (enteredOtp.equals(otp)) {
                System.out.println("OTP validated successfully. Enter new password:");
                String newPassword = scanner.nextLine();
                websitePasswords.put(website, newPassword);
                System.out.println("Password updated successfully for " + website);
            } else {
                System.out.println("Invalid OTP. Please try again.");
            }
        } else {
            System.out.println("Invalid phone number.");
            forgotPassword();
        }
    }

    private static boolean isWeakPassword(String password) {
        return !(password.matches("^(?=.[A-Z])(?=.[0-9])(?=.[!@#$%^&()-_=+]).+$"));
    }

    private static boolean isCorrectPassword(String website, String enteredPassword) {
        String savedPassword = websitePasswords.get(website);
        return savedPassword != null && savedPassword.equals(enteredPassword);
    }

    private static String generateOTP() {
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    private static void enterPassword() {
        System.out.print("Enter website name: ");
        String website = scanner.nextLine();
        String savedPassword = websitePasswords.get(website);

        if (savedPassword == null) {
            System.out.println("No password set for this website. Please set the password first.");
            return;
        }

        int attempts = 0;
        while (true) {
            System.out.print("Enter password: ");
            String enteredPassword = scanner.nextLine();

            if (enteredPassword.equals(savedPassword)) {
                System.out.println("Password accepted. Access granted.");
                break;
            } else {
                attempts++;
                if (attempts >= MAX_WRONG_ATTEMPTS) {
                    System.out.println("Warning: You have entered the wrong password " + MAX_WRONG_ATTEMPTS +
                            " times. Your website will be locked if the password is entered incorrectly again.");
                    forgotPassword();
                    break;
                } else {
                    System.out.println("Incorrect password. Please try again.");
                }
            }
        }
    }
    
    private static void searchPassword() {
        System.out.print("Enter website name: ");
        String website = scanner.nextLine();
        String password = websitePasswords.get(website);
        
        if (password != null) {
            System.out.println("Password for " + website + ": " + password);
        } else {
            System.out.println("No password found for " + website);
        }
    }
}