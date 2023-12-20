import java.util.Scanner;

public class PalindromeCheck {
    public static boolean check(String input) {
//        input = input.toLowerCase().replace(" ", "").strip();
        input = input.replaceAll("\\W", "").toLowerCase();
        if (input.isEmpty() || input.length() == 1) {
            return true;
        }
        boolean lastAndFirst = input.substring(0, 1).equals(input.substring(input.length() - 1));
        return lastAndFirst && check(input.substring(1, input.length() - 1));
    }

    public static void main(String[] args) {
        String[] invalid = {"man", "dab", "catocat"};
        String[] valid = {"mom", "dad", "tacocat"};
        String[] validComplex = {"never odd or even", "Able was I ere I saw Elba.", "A man, a plan, a canal: Panama!"};

        for (String s : invalid) {
            boolean checked = check(s);
            System.out.printf("String \"%s\" check: %b (should be false)\n", s, checked);
        }
        for (String s : valid) {
            boolean checked = check(s);
            System.out.printf("String \"%s\" check: %b (should be true)\n", s, checked);
        }
        for (String s : validComplex) {
            boolean checked = check(s);
            System.out.printf("String \"%s\" check: %b (should be true)\n", s, checked);
        }

        System.out.println("\nNow that the automated checks are done, you can try it yourself!");

        Scanner scan = new Scanner(System.in);
        while (true) {
            System.out.print("Enter your string to check or type \"/e\" to exit: ");
            String s = scan.next().strip();
            if (s.equals("/e")) {
                break;
            }
            System.out.printf("Checking \"%s\"...\n", s);
            boolean result = check(s);
            System.out.printf("\"%s\" is%s a palindrome.\n", s.replaceAll("\\W", "").toLowerCase(), result ? "" : " NOT");
        }
    }
}
