import Analyzers.*;
import java.io.File;
import java.util.Scanner;
import java.util.Date;

public class Main {

    private static long startTime;

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
                        if (args[0].charAt(i) == 'h')
                            hidden = true;
                        else if (args[0].charAt(i) == 'l')
                            mode = 1;
                        else if (args[0].charAt(i) == 'g')
                            mode = 2;
                        else
                            exception = "Error!\n=> Unrecognized flag : \"" + args[0].charAt(i) + "\"";
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
                System.out.println("Please enter a directory or a file address: ");
                fl = new File(input.nextLine());
                System.out.println("Including hidden files and directories? [Y/N]");
                hidden = input.nextLine().equalsIgnoreCase("y");
            }else if (mode == 2){
                input = new Scanner(System.in);
                System.out.println("Enter the repository address : ");
                new GitCloner(input.nextLine());
                git = true;
            }else
                exception = "Error!\n=> Invalid selection!";
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
            System.out.println("________________________________________________________________________________________________________________________________\n"+
                    "| File type\t|Size (B)\t|Words\t\t|Lines\t\t|Blank lines\t|Bracket lines\t|Pure lines\t|Files count\t|\n" +
                    "---------------------------------------------------------------------------------------------------------------------------------");
            System.out.println(an.toString());
            System.out.println("---------------------------------------------------------------------------------------------------------------------------------");
            float wholeTime = (new Date().getTime() - startTime) / 1000.0F;
            System.out.println("Statistics :    T = " + wholeTime + " s || " + (an.getTotalFilesCount() / wholeTime) + " files/s || " +
                    (an.getTotalLinesCount() / wholeTime) + " lines/s");
        }
        endProgram();
    }

    private static void printHelp(){
        System.out.println("Ako line counter documentation : \nProper format for calling the program : \n\n" +
                "       akolc [options] [local address/git repo address]\n\nOptions : \n\n    1) -l : This option indicates that the given address" +
                " is a local address.\n    2) -g : This option indicates that the given address is a git repository address.\n    3)" +
                " -h : This option tell the program to include hidden files and directories in the counting process.\n\n" +
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
}
