import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;

public class Launcher {

    public static final Scanner scanner = new Scanner(System.in);
    private static final int CHOICE_AMOUNT = 10;
    private static final int MENU_WIDTH = 42;
    public static int nbRunners = 1;
    private static Runners runners;
    private static final String TIME_FORMAT = "HH:mm:ss";
    private static final Random RANDOM = new Random();
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(TIME_FORMAT);

    public static void main(String[] args) {
        try {
            int menuChoice;
            nbRunners = chooseRunnersAmount();
            runners = new Runners(nbRunners);
            while ((menuChoice = menuChoiceScanner()) != -1) {
                Runner runner;
                switch (menuChoice) {
                    case 1:
                        if (runners.isEmpty())
                            System.out.println("There are no runners for you to see. Come back another day !\n");
                        else System.out.println(runners);
                        break;
                    case 2:
                        System.out.println(runners.classementsToString());
                        break;
                    case 3:
                        if (runners.isEmpty())
                            System.out.println("There are no runners for you to see. Come back another day !\n");
                        else {
                            runner = chooseRunner();
                            runner.arrive();
                        }
                        break;
                    case 4:
                        if (runners.isEmpty())
                            System.out.println("There are no runners for you to see. Come back another day !\n");
                        else {
                            runner = chooseRunner();
                            runner.setStatus(RunnerStatus.ABANDON);
                        }
                        break;
                    case 5:
                        if (runners.isEmpty())
                            System.out.println("There are no runners for you to see. Come back another day !\n");
                        else {
                            runner = chooseRunner();
                            runner.setStatus(RunnerStatus.DISQUALIFIED);
                        }
                        break;
                    case 6:
                        if (runners.isEmpty())
                            System.out.println("There are no runners for you to see. Come back another day !\n");
                        else {
                            runner = chooseRunner();
                            if (runner.getStatus() != RunnerStatus.FINISHED)
                                System.out.println(runner.getPrenom() + " " + runner.getNom() + " (n°" + runner.getNumDossard() + ") hasn't finished yet !\n");
                            else {
                                Date arrivalTime = chooseTime(runner);
                                runner.setTemps(arrivalTime);
                                System.out.println("Successfully added a time to the runner !");
                            }
                        }
                        break;
                    case 7:
                        if (runners.isEmpty())
                            System.out.println("There are no runners for you to see. Come back another day !\n");
                        else {
                            runner = chooseRunner();
                            if (runner.getStatus() != RunnerStatus.FINISHED)
                                System.out.println(runner.getPrenom() + " " + runner.getNom() + " (n°" + runner.getNumDossard() + ") hasn't finished yet !\n");
                            else System.out.println(runner.getPrenom() + " " +
                                    runner.getNom() +
                                    " (n°" + runner.getNumDossard() + ") had a time of " + DATE_FORMAT.format(runner.getTemps()));
                        }
                        break;
                    case 8:
                        if (!runners.hasAtLeastTwoRunners())
                            System.out.println("There are not enough runners for you to see. Wait a little !\n");
                        else {
                            runner = chooseRunner();
                            Runner secondRunner = chooseAnotherRunner(runner);
                            if (runner.getStatus() != RunnerStatus.FINISHED)
                                System.out.println(runner.getPrenom() + " " + runner.getNom() + " (n°" + runner.getNumDossard() + ") hasn't finished yet !\n");
                            else if (secondRunner.getStatus() != RunnerStatus.FINISHED)
                                System.out.println(secondRunner.getPrenom() + " " + secondRunner.getNom() + " (n°" + secondRunner.getNumDossard() + ") hasn't finished yet !\n");
                            else {
                                Date difference;
                                if (runner.getTemps().compareTo(secondRunner.getTemps()) > 0)
                                    difference = new Date(runner.getTemps().getTime() - secondRunner.getTemps().getTime());
                                else
                                    difference = new Date(secondRunner.getTemps().getTime() - runner.getTemps().getTime());
                                System.out.println("The time difference between them is of : " + DATE_FORMAT.format(difference));
                            }
                        }
                        break;
                    case 9:
                        if (runners.isFull())
                            System.out.println("Runners list is full. Come back another day !\n");
                        else {
                            runners.addRunner(getNewRunner());
                            System.out.println("Successfully added new runner\n");
                        }
                        break;
                    default:
                        return;
                }
            }
        } catch (ExitException ignored) {
        }
    }

    private static Date chooseTime(Runner runner) {
        Date timeBefore = runners.getTimeBefore(runner);
        Date timeAfter = runners.getTimeAfter(runner);
        if (timeAfter.getMinutes() == 0 && timeAfter.getSeconds() == 0 && timeAfter.getHours() == 0)
            System.out.println("The runner time has to be after : " + DATE_FORMAT.format(timeBefore));
        else
            System.out.println("The runner time has to be between " + DATE_FORMAT.format(timeBefore) + " and " + DATE_FORMAT.format(timeAfter));
        System.out.print("Choose arrival hour : ");
        String hourInput = scanner.nextLine();
        System.out.print("Choose arrival minutes : ");
        String minutesInput = scanner.nextLine();
        System.out.print("Choose arrival seconds : ");
        String secondsInput = scanner.nextLine();
        int hour, minutes, seconds;
        while (!hourInput.matches("\\d+") ||
                !minutesInput.matches("\\d+") ||
                !secondsInput.matches("\\d+") ||
                (hour = Integer.parseInt(hourInput)) < 0 ||
                (minutes = Integer.parseInt(minutesInput)) < 0 ||
                minutes > 59 ||
                (seconds = Integer.parseInt(secondsInput)) < 0 ||
                seconds > 59 ||
                !isInTheTimeInterval(hour, minutes, seconds, timeBefore, timeAfter)) {

            System.out.println("Incorrect time of arrival !\n");
            System.out.print("Choose arrival hour : ");
            hourInput = scanner.nextLine();
            System.out.print("Choose arrival minutes : ");
            minutesInput = scanner.nextLine();
            System.out.print("Choose arrival seconds : ");
            secondsInput = scanner.nextLine();
        }
        Date date = new Date();
        date.setHours(hour);
        date.setMinutes(minutes);
        date.setSeconds(seconds);
        return date;
    }

    private static boolean isInTheTimeInterval(int hour, int minutes, int seconds, Date timeBefore, Date timeAfter) {
        if (hour < timeBefore.getHours()) return false;
        if (timeAfter.getMinutes() == 0 && timeAfter.getSeconds() == 0 && timeAfter.getHours() == 0) {
            if (hour > timeBefore.getHours()) return true;
            if (minutes < timeBefore.getMinutes()) return false;
            if (minutes > timeBefore.getMinutes()) return true;
            return seconds > timeBefore.getSeconds();
        } else {
            if (hour > timeAfter.getHours()) return false;
            boolean beforeIsCorrect = false;
            boolean afterIsCorrect = false;
            if (hour == timeBefore.getHours()) {
                if (minutes > timeBefore.getMinutes()) beforeIsCorrect = true;
                else if (minutes < timeBefore.getMinutes()) beforeIsCorrect = false;
                else beforeIsCorrect = seconds > timeBefore.getSeconds();
            } else {
                beforeIsCorrect = true;
                if (hour == timeAfter.getHours()) {
                    if(minutes < timeAfter.getMinutes()) afterIsCorrect = true;
                    else if(minutes > timeAfter.getMinutes()) afterIsCorrect = false;
                    else afterIsCorrect = seconds < timeAfter.getSeconds();
                } else afterIsCorrect = true;
            }
            return beforeIsCorrect && afterIsCorrect;
        }
    }

    private static Runner chooseRunner() {
        System.out.print("Runner dossard number : ");
        int nbDossard;
        String dossardWritten = scanner.nextLine();
        while (!dossardWritten.matches("\\d+") || !runners.hasDossardNum(nbDossard = Integer.parseInt(dossardWritten))) {
            System.out.print("Invalid runner dossard.\n" + runners.toString() + "\nPlease choose again : ");
            dossardWritten = scanner.nextLine();
        }
        return runners.getRunnerByDossard(nbDossard);
    }

    private static Runner chooseAnotherRunner(Runner runner) {
        System.out.print("Other runner dossard number : ");
        int nbDossard;
        String dossardWritten = scanner.nextLine();
        while (!dossardWritten.matches("\\d+") || !runners.hasDossardNum(nbDossard = Integer.parseInt(dossardWritten)) || nbDossard == runner.getNumDossard()) {
            System.out.print("Invalid runner dossard.\n" + runners.toString() + "\nPlease choose again : ");
            dossardWritten = scanner.nextLine();
        }
        return runners.getRunnerByDossard(nbDossard);
    }

    private static int chooseRunnersAmount() {
        System.out.print("Choose a max amount of runners : ");
        int nbRunners;
        String amount = scanner.nextLine();
        while (!amount.matches("\\d+") || (nbRunners = Integer.parseInt(amount)) < 1) {
            System.out.println("Invalid runners amount. Please choose again : ");
            amount = scanner.nextLine();
        }
        return nbRunners;
    }

    private static Runner getNewRunner() {
        System.out.print("New runner last name : ");
        String lastName = scanner.nextLine();
        while (lastName.matches("[a-zA-Z]*\\d+[a-zA-Z]*")) {
            System.out.print("Invalid runner name.\nPlease choose again : ");
            lastName = scanner.nextLine();
        }
        System.out.print("New runner first name : ");
        String firstName = scanner.nextLine();
        while (firstName.matches("[a-zA-Z]*\\d+[a-zA-Z]*")) {
            System.out.print("Invalid runner name.\nPlease choose again : ");
            firstName = scanner.nextLine();
        }
        int nbDossard = RANDOM.nextInt(nbRunners) + 1;
        while (runners.hasDossardNum(nbDossard))
            nbDossard = RANDOM.nextInt(nbRunners) + 1;
        return new Runner(lastName, firstName, nbDossard);
    }

    private static int menuChoiceScanner() throws ExitException {
        displayMenu();
        String menuChoice = scanner.nextLine();
        int nbChoice;
        while (!menuChoice.matches("\\d+") || (nbChoice = Integer.parseInt(menuChoice)) <= 0 || nbChoice > CHOICE_AMOUNT) {
            System.out.println("Invalid choice from the menu. Please choose another option :\n");
            displayMenu();
            menuChoice = scanner.nextLine();
        }
        if (nbChoice == CHOICE_AMOUNT) throw new ExitException();
        return nbChoice;
    }

    private static void displayMenu() {
        System.out.print(new String(new char[MENU_WIDTH]).replace("\0", "*"));
        System.out.println("\n\t\tWelcome to the Efrei race!\n");
        System.out.println("1) Display runners");
        System.out.println("2) Display leaderboard");
        System.out.println("3) Save an arrival entry");
        System.out.println("4) Save a quitting entry");
        System.out.println("5) Save a disqualification entry");
        System.out.println("6) Save the racing time of a runner");
        System.out.println("7) Find a runner's time");
        System.out.println("8) Get the time difference between runners");
        System.out.println("9) Add runner to table");
        System.out.println("10) Quit\n");
        System.out.print(new String(new char[MENU_WIDTH]).replace("\0", "*").concat("\n"));
    }

}
