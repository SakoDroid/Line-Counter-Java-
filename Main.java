import Analyzers.*;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;
import java.util.Date;

public class Main {

    private static long startTime;
    private static float wholeTime;
    private static boolean save = false;
    private static String saveLocation;
    private static final String header = "________________________________________________________________________________________________________________________________\n"+
            "| File type\t|Size (B)\t|Words\t\t|Lines\t\t|Blank lines\t|Bracket lines\t|Pure lines\t|Files count\t|\n" +
            "---------------------------------------------------------------------------------------------------------------------------------";
    private static final String breakLine = "---------------------------------------------------------------------------------------------------------------------------------";

    public static void main(String[] args){
        File fl = null;
        boolean hidden = false;
        boolean git = false;
        String exception = "";
        if (args.length > 0){
            if (!args[0].startsWith("-")) {
                if (!args[0].isEmpty())
                    fl = new File(args[0]);
                else
                    exception = "Error!\n=> Please enter an address!\n(For help type akolc --help to see the full documentation)";
            }
            else{
                if (args[0].equals("--help")){
                    exception = "end.";
                    printHelp();
                }else if (args[0].equals("--version"))
                    System.out.println("Ako line counter version 1.0.0");
                else{
                    int mode = 1;
                    for (int i = 1; i < args[0].length(); i++) {
                        switch(args[0].charAt(i)){
                            case 'h' -> hidden = true;
                            case 'l' -> mode = 1;
                            case 'g' -> mode = 2;
                            case 's' -> {
                                save = true;
                                saveLocation = args[2];
                            }
                            default -> exception = "Error!\n=> Unrecognized flag : \"" + args[0].charAt(i) + "\"";
                        }
                    }
                    if (exception.isEmpty()) {
                        if (mode == 1) {
                            if (!args[1].isEmpty())
                                fl = new File(args[1]);
                            else
                                exception = "Error!\n=> Please enter an address!";
                        } else {
                            new GitCloner(args[1]);
                            git = true;
                        }
                    }
                }
            }
        }else{
            Scanner input = new Scanner(System.in);
            System.out.println("Please select : (1 or 2)\n1) Local directory or file\n2) Git repository");
            int mode = input.nextInt();
            if (mode == 1){
                input = new Scanner(System.in);
                System.out.println("Please enter a directory or a file address: ");
                fl = new File(input.nextLine());
            }else if (mode == 2){
                input = new Scanner(System.in);
                System.out.println("Enter the repository address : ");
                new GitCloner(input.nextLine());
                git = true;
            }else
                exception = "Error!\n=> Invalid selection!";
            if (exception.isEmpty()){
                input = new Scanner(System.in);
                System.out.println("Including hidden files and directories? [Y/N]");
                hidden = input.nextLine().equalsIgnoreCase("y");
                System.out.println("Do you want to save the result in a file? [Y/N]");
                save = input.nextLine().equalsIgnoreCase("y");
                if (save) {
                    System.out.println("Please enter the address to save the file :");
                    saveLocation = input.nextLine();
                }
            }
        }
        if (exception.isEmpty())
            startAnalyzing((git ? new File(System.getProperty("user.dir") + "/Temp") : fl),hidden);
        else
            System.out.println(exception);
    }

    private static void startAnalyzing(File fl, boolean hidden){
        System.out.println(fl.getAbsolutePath());
        startTime = new Date().getTime();
        Analyzer anlz = null;
        if (fl.isDirectory())
            anlz = new DirectoryAnalyzer(fl,hidden);
        else if (fl.isFile())
            anlz = new FileAnalyzer(fl);
        else
            System.out.println("Error!\n=> The address is invalid!");
        printResult(anlz);
    }

    private static void printResult(Analyzer an){
        if (an != null){
            System.out.println(header);
            System.out.println(an.toString());
            System.out.println(breakLine);
            wholeTime = (new Date().getTime() - startTime) / 1000.0F;
            System.out.println("Statistics :    T = " + wholeTime + " s || " + (an.getTotalFilesCount() / wholeTime) + " files/s || " +
                    (an.getTotalLinesCount() / wholeTime) + " lines/s");
        }
        if (save)
            saveResult(an);
        endProgram();
    }

    private static void printHelp(){
        System.out.println("Ako line counter documentation : \nProper format for calling the program : \n\n" +
                "       akolc [options] [local address/git repo address] [file address for saving result]\n\nOptions : \n\n1) -l : This option indicates that the given address" +
                " is a local address.\n2) -g : This option indicates that the given address is a git repository address.\n3)" +
                " -h : This option tell the program to include hidden files and directories in the counting process.\n4) -s : This option can be used" +
                "to save the result of the analyzing in a file. The file address should be mentioned as the third argument.\n\n" +
                "*** Note 1 : If no option is set, the program will assume that the given address is a local address, so for analyzing a" +
                "git repo you should definitely use \"-g\" option.\n\n*** Note 2 : akolc command can be used directly without any " +
                "option or address. In that case program will ask you what you want to do.");
    }

    private static void endProgram(){
        File temp = new File(System.getProperty("user.dir") + "/Temp");
        if (temp.isDirectory()){
            try{
                ProcessBuilder pb = new ProcessBuilder("rm","-rfd",System.getProperty("user.dir") + "/Temp");
                pb.start().waitFor();
            }catch (Exception ex){
                System.out.println(ex.toString());
            }
        }
        Runtime.getRuntime().halt(130);
    }

    private static void saveResult(Analyzer an){
        try (FileWriter fw = new FileWriter(saveLocation)){
            fw.write("Result for analyzing " + an.getAddress() + " at " + new Date().toString() + " :\n\n");
            fw.write(header);
            fw.write(an.toString());
            fw.write(breakLine);
            fw.write("\n\nThe analyzing took " + wholeTime + " seconds to complete. " + an.getTotalFilesCount() + " files" +
                    " and " + an.getTotalLinesCount() + " lines were scanned which means that " + (an.getTotalFilesCount() / wholeTime) +
                    " files and " + (an.getTotalLinesCount() / wholeTime) + " lines were scanned in one second.");
        }catch (Exception ex){
            System.out.println(ex.toString());
        }
    }
}
