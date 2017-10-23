
import IA.Gasolina.CentrosDistribucion;
import IA.Gasolina.Gasolineras;
import aima.search.framework.*;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;

import java.util.*;

import static java.lang.String.valueOf;

public class SearchGenerator {

    private String local_search_algorithm;
    private int heuristic_function;
    private int successors_function;
    private int initial_distribution;

    private int distribution_centers_num;
    private int distrubution_centers_mult;
    private int distribution_centers_seed;

    private int petrol_stations_num;
    private int petrol_stations_seed;

    private static final int NUMBER_HEURISTICS = 5;
    private static final int NUMBER_SUCCESSORS = 3;
    private static final int NUMBER_INITIAL_DISTRIBUTIONS = 3;

    SearchGenerator(String start_command) {
        String command = "continue";
        if (start_command.equals("start")) {
            while (command.equals("continue")) {
                enter_search_parameters();
                generate_search();
                command = enter_new_string();
            }
        }
        else if (start_command.equals("fast start")) {
            while (command.equals("continue")) {
                enter_search_parameters_one_line();
                generate_search();
                command = enter_new_string();
            }
        }
    }

    void enter_search_parameters() {

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
            System.out.println("Enter the kind of heuristic that you want to use (number between [0 - " + (valueOf(NUMBER_HEURISTICS - 1)) +"]). If you don't know the heuristic that you can use, type " + '"' + "info" + '"' + ":");
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

        //Successors function that we are going to use
        command = "info";
        while (command.equals("info")) {
            System.out.println("Enter the kind of successors that you want to use (number between [0 - " + valueOf(NUMBER_SUCCESSORS - 1) +"]). If you don't know the successors that you can use, type " + '"' + "info" + '"' + ":");
            command = enter_new_string();

            if (command.equals("info")) {
                show_successors_info();
            }
            else {
                int successors_command;
                try {
                    successors_command = Integer.parseInt(command);
                }
                catch (Exception e){
                    successors_command = -1;
                }

                if (successors_command >= 0 && successors_command < NUMBER_SUCCESSORS) {
                    this.successors_function = successors_command;
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
                this.distribution_centers_seed = dc_seed;
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
        //Heuristic that we are going to use
        command = "info";
        while (command.equals("info")) {
            System.out.println("Enter the initial distribution that you want to use (number between [0 - " + valueOf(NUMBER_INITIAL_DISTRIBUTIONS - 1) +"]). If you don't know the initial distributions that you can use, type " + '"' + "info" + '"' + ":");
            command = enter_new_string();

            if (command.equals("info")) {
                show_distributions_info();
            }
            else {
                int distributions_command;
                try {
                    distributions_command = Integer.parseInt(command);
                }
                catch (Exception e){
                    distributions_command = -1;
                }

                if (distributions_command >= 0 && distributions_command < NUMBER_HEURISTICS) {
                    this.initial_distribution = distributions_command;
                }
                else {
                    System.out.println("Wrong command.");
                    System.out.println();
                    command = "info";
                }
            }
        }
        System.out.println();
    }

    void enter_search_parameters_one_line() {
        boolean validCommand = false;
        while (!validCommand) {
            validCommand = true;
            System.out.println("Usage: [Algorithm] [Heuristic] [Successors function] [DC num] [DC mult] [DC seed] [PS num] [PS seed] [initial distribution]");
            String command = enter_new_string();
            String[] parts = command.split(" ");

            if (!parts[0].equals("HC") && !parts[0].equals("SA")) {
                validCommand = false;
            }
            else this.local_search_algorithm = parts[0];

            try {
                this.heuristic_function = Integer.parseInt(parts[1]);
                if (this.heuristic_function < 0 || this.heuristic_function >= this.NUMBER_HEURISTICS) {
                    validCommand = false;
                }
            }
            catch (Exception e) {
                validCommand = false;
            }

            try {
                this.successors_function = Integer.parseInt(parts[2]);
                if (this.successors_function < 0 || this.successors_function >= this.NUMBER_SUCCESSORS) {
                    validCommand = false;
                }
            }
            catch (Exception e) {
                validCommand = false;
            }

            try {
                this.distribution_centers_num = Integer.parseInt(parts[3]);
                if (this.distribution_centers_num < 0) {
                    validCommand = false;
                }
            }
            catch (Exception e) {
                validCommand = false;
            }

            try {
                this.distrubution_centers_mult = Integer.parseInt(parts[4]);
                if (this.distrubution_centers_mult < 0) {
                    validCommand = false;
                }
            }
            catch (Exception e) {
                validCommand = false;
            }

            try {
                this.distribution_centers_seed = Integer.parseInt(parts[5]);
                if (this.distribution_centers_seed < 0) {
                    validCommand = false;
                }
            }
            catch (Exception e) {
                validCommand = false;
            }

            try {
                this.petrol_stations_num = Integer.parseInt(parts[6]);
                if (this.petrol_stations_num < 0) {
                    validCommand = false;
                }
            }
            catch (Exception e) {
                validCommand = false;
            }

            try {
                this.petrol_stations_seed = Integer.parseInt(parts[7]);
                if (this.petrol_stations_seed < 0) {
                    validCommand = false;
                }
            }
            catch (Exception e) {
                validCommand = false;
            }

            try {
                this.initial_distribution = Integer.parseInt(parts[2]);
                if (this.initial_distribution < 0 || this.initial_distribution >= this.NUMBER_INITIAL_DISTRIBUTIONS) {
                    validCommand = false;
                }
            }
            catch (Exception e) {
                validCommand = false;
            }

            if (!validCommand) {
                System.out.println();
                System.out.println("Wrong command. Please, try again.");
            }
        }
    }

    void generate_search() {
        System.out.println("Starting to generate the search ...");

        try {
            Date d1,d2;
            Calendar c1, c2;
            CentrosDistribucion centros_distribucion = new CentrosDistribucion(this.distribution_centers_num, this.distrubution_centers_mult, this.distribution_centers_seed);
            Gasolineras gasolineras = new Gasolineras(this.petrol_stations_num, this.petrol_stations_seed);

            SuccessorFunction successor = null;
            HeuristicFunction heuristic = null;
            Problem problem = null;
            Search search = null;

            switch (this.heuristic_function) {
                case 0:
                    heuristic = new HeuristicFunction1();
                    break;
                case 1:
                    heuristic = new HeuristicFunction2();
                    break;
                case 2:
                    heuristic = new HeuristicFunction3();
                    break;
                case 3:
                    heuristic = new HeuristicFunction4();
                    break;
                case 4:
                    heuristic = new HeuristicFunction5();
                    break;

                default:
                    heuristic = new HeuristicFunction1();
            }

            switch (this.successors_function) {
                case 0:
                    successor = new SuccesorFunction();
                    break;

                default:
                    successor = new SuccesorFunction();
            }

            State initial_state = new State(gasolineras, centros_distribucion);

            switch (this.initial_distribution) {
                case 0:
                    initial_state.emptyTrips();
                    break;
                case 1:
                    initial_state.greedyTrips();
                    break;
                case 2:
                    initial_state.randomTrips();
                    break;
                default:
                    initial_state.emptyTrips();
            }

            if (this.local_search_algorithm.equals("HC")) {
                search = new HillClimbingSearch();
            }
            /*else if (this.local_search_algorithm.equals("SA")) {
                search = new SimulatedAnnealingSearch(steps, stiter, k, lamb);
            }*/

            problem = new Problem(initial_state, successor, new IAGoalTest(), heuristic);
            d1=new Date();
            SearchAgent agent = new SearchAgent(problem,search);
            d2=new Date();
            c1 = Calendar.getInstance();
            c2 = Calendar.getInstance();
            c1.setTime(d1);
            c2.setTime(d2);
            long m = c2.getTimeInMillis() - c1.getTimeInMillis();

            // We print the results of the search
            System.out.println();
            //printActions(agent.getActions());
            printInstrumentation(agent.getInstrumentation());

            // You can access also to the goal state using the
            // method getGoalState of class Search
            System.out.println("Benefits: " + -((State)search.getGoalState()).getHeuristic1());
            System.out.println("Benefits (with losses): " + -((State)search.getGoalState()).getHeuristic2());

            System.out.println ("Execution time: " + m + "ms");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printInstrumentation(Properties properties) {
        Iterator keys = properties.keySet().iterator();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            String property = properties.getProperty(key);
            System.out.println(key + " : " + property);
        }

    }

    private static void printActions(List actions) {
        for (int i = 0; i < actions.size(); i++) {
            String action = (String) actions.get(i);
            System.out.println(action);
        }
    }

    String enter_new_string() {
        Scanner sc = new Scanner(System.in);
        String input =  sc.nextLine();
        return input;
    }


    void show_heuristics_info() {
        System.out.println("These are the kind of heuristic that you can use:");
        System.out.println("0 -> Heuristic that only takes into consideration the benefits and the travel costs");
        System.out.println("1 -> Heuristic that takes into consideration the benefits, the travel costs and the losses");
        System.out.println("2 -> Heuristic that works like heuristic 1 but giving more importance to the benefits");
        System.out.println("3 -> Heuristic that works like heuristic 1 but giving more importance to the travel costs");
        System.out.println("4 -> Heuristic that works like heuristic 1 but giving more importance to the losses");
        System.out.println();

    }


    void show_successors_info() {
        System.out.println("These are the kind of successors that you can use:");
        System.out.println("0 ->");
        System.out.println("1 ->");
        System.out.println();
    }


    void show_distributions_info() {
        System.out.println("These are the kind of initial distributions that you can use:");
        System.out.println("0 ->");
        System.out.println("1 ->");
        System.out.println();
    }
}
