import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hi!");
        System.out.println("Welcome to the " + '"' + "Petrol Stations Path Generator 2.0" + '"' + ", the best path planner generator in this square metre :)");
        System.out.println();
        System.out.println("To start using the program, type " + '"' + "start" + '"' + ". For the fast usage, type " + '"' + "fast start" + '"' +". Otherwhise, type " + '"' + "exit" + '"' + ":");

        Scanner sc = new Scanner(System.in);
        String command = sc.nextLine();

        while (!command.equals("start") && !command.equals("exit") && !command.equals("fast start")) {
            System.out.println();
            System.out.println("Wrong command. Please, try again:");
            command = sc.nextLine();
        }
        if (command.equals("start") || command.equals("fast start")) {
            System.out.println();
            SearchGenerator search_generator = new SearchGenerator(command);
        }
        System.out.println("Bye! :D");
    }
}
