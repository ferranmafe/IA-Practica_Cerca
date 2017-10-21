
import java.util.Scanner;

import static java.lang.String.valueOf;

public class Search_generator {

    private String local_search_algorithm;
    private int heuristic_function;

    private int distribution_centers_num;
    private int distrubution_centers_mult;
    private int distribution_centers_seed;

    private int petrol_stations_num;
    private int petrol_stations_seed;

    private static final int NUMBER_HEURISTICS = 3;

    Search_generator() {
        String command = "continue";

        while (command.equals("continue")) {
            enter_search_paramenters();
            initialize_search_parameters();
            command = enter_new_string();
        }
    }

    void enter_search_paramenters() {

        //Algorithm that we are going to use
        String command = "";
        while (!command.equals("HC") && !command.equals("SA")) {
            System.out.println("Enter the Search Algorithm that you want to use.");
            System.out.println("To use the Hill Climbing algorithm, type " + '"' + "HC" + '"' + ". To use the Simulated Annealing algorithm, type " + '"' + "SA" + '"' + ":");

            command = enter_new_string();
            if (!command.equals("HC") && !command.equals("SA")) {
                System.out.println("Wrong command.");
                System.out.println();
            }
            else this.local_search_algorithm = command;
        }

        System.out.println();

        //Heuristic that we are going to use
        command = "info";
        while (command.equals("info")) {
            System.out.println("Enter the kind of heuristic that you want to use (number between [0 - " + valueOf(NUMBER_HEURISTICS) +"]). If you don't know the heuristic that you can use, type " + '"' + "info" + '"' + ":");
            command = enter_new_string();

            if (command.equals("info")) {
                show_heuristics_info();
            }
            else {
                int heuristic_command;
                try {
                    heuristic_command = Integer.parseInt(command);
                }
                catch (Exception e){
                    heuristic_command = -1;
                }

                if (heuristic_command >= 0 && heuristic_command < NUMBER_HEURISTICS) {
                    this.heuristic_function = heuristic_command;
                }
                else {
                    System.out.println("Wrong command.");
                    System.out.println();
                    command = "info";
                }
            }
        }

        System.out.println();

        //Distribution centers number
        command = "";
        while (command.equals("")) {
            System.out.println("Enter the number of distribution centers that you want to have:");
            command = enter_new_string();
            int dc_num;
            try {
                dc_num = Integer.parseInt(command);
            }
            catch (Exception e) {
                dc_num = -1;
            }
            if (dc_num > -1) {
                this.distribution_centers_num = dc_num;
            }
            else {
                System.out.println("Wrong command.");
                System.out.println();
                command = "";
            }
        }

        System.out.println();

        //Distribution centers mult
        command = "";
        while (command.equals("")) {
            System.out.println("Enter the mult of distribution centers that you want to have:");
            command = enter_new_string();
            int dc_mult;
            try {
                dc_mult = Integer.parseInt(command);
            }
            catch (Exception e) {
                dc_mult = -1;
            }
            if (dc_mult > -1) {
                this.distrubution_centers_mult = dc_mult;
            }
            else {
                System.out.println("Wrong command.");
                System.out.println();
                command = "";
            }
        }

        System.out.println();

        //Distribution centers seed
        command = "";
        while (command.equals("")) {
            System.out.println("Enter the seed that you want to have for the distribution centers:");
            command = enter_new_string();
            int dc_seed;
            try {
                dc_seed = Integer.parseInt(command);
            }
            catch (Exception e) {
                dc_seed = -1;
            }
            if (dc_seed > -1) {
                this.distrubution_centers_mult = dc_seed;
            }
            else {
                System.out.println("Wrong command.");
                System.out.println();
                command = "";
            }
        }

        System.out.println();

        //Petrol Stations Num
        command = "";
        while (command.equals("")) {
            System.out.println("Enter the number of petrol stations that you want to have:");
            command = enter_new_string();
            int ps_num;
            try {
                ps_num = Integer.parseInt(command);
            }
            catch (Exception e) {
                ps_num = -1;
            }
            if (ps_num > -1) {
                this.petrol_stations_num = ps_num;
            }
            else {
                System.out.println("Wrong command.");
                System.out.println();
                command = "";
            }
        }

        System.out.println();

        //Distribution centers mult
        command = "";
        while (command.equals("")) {
            System.out.println("Enter the seed that you want to have for the petrol stations:");
            command = enter_new_string();
            int ps_seed;
            try {
                ps_seed = Integer.parseInt(command);
            }
            catch (Exception e) {
                ps_seed = -1;
            }
            if (ps_seed > -1) {
                this.petrol_stations_seed = ps_seed;
            }
            else {
                System.out.println("Wrong command.");
                System.out.println();
                command = "";
            }
        }

        System.out.println();
    }

    void initialize_search_parameters() {
        
    }

    String enter_new_string() {
        Scanner sc = new Scanner(System.in);
        String input =  sc.nextLine();
        return input;
    }


    void show_heuristics_info() {
        System.out.println("These are the kind of heuristic that you can use:");
        System.out.println("0 ->");
        System.out.println("1 ->");
        System.out.println();

    }
}
