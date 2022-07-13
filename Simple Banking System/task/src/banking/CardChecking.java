package banking;

public class CardChecking {

    public static boolean isValidCardNumber(String cardNumber) {
        char last = cardNumber.charAt(cardNumber.length() - 1);
        String number = cardNumber.substring(0, cardNumber.length() - 1);
        return (getBINAndAccountIdentificationSum(number) + Integer.parseInt(String.valueOf(last))) % 10 == 0;
    }

    public static int generateChecksum(String cardNumber) {
        int sum = getBINAndAccountIdentificationSum(cardNumber);
        int check = 0;
        for (int i = 0; i < 10; i++) {
            if ((sum + i) % 10 == 0) {
                check = i;
                break;
            }
        }
        return check;
    }

    public static int getBINAndAccountIdentificationSum(String cardNumber) {
        String number = cardNumber;
        int tally = 0;
        int num;
        for (int i = 0; i < number.length(); i++) {
            num = Integer.parseInt(String.valueOf(number.charAt(i)));

            if (i % 2 == 0) {
                num *= 2;
                if (num > 9) {
                    num -= 9;
                }
            }
            tally += num;
        }

        return tally;
    }
}
