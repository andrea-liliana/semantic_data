import java.util.InputMismatchException;
import java.util.Scanner;

class Read {

	public static int readInt() {
        @SuppressWarnings("resource")
		Scanner scanner = new Scanner(System. in);
        try {
        	int input = scanner.nextInt();
        	return input;
		} catch (InputMismatchException e) {
			e.getMessage();
		}
		return 0;
        
        
	}
	public static String readLine() {
        @SuppressWarnings("resource")
		Scanner scanner = new Scanner(System. in);
        String input = scanner.nextLine();
        return input;
	}
}