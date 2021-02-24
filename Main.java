import Analyzers.Analyzer;
import Analyzers.DirectoryAnalyzer;
import Analyzers.FileAnalyzer;
import java.io.File;
import java.util.Scanner;

public class Main {

    public static void main(String[] args){
        File fl = null;
        boolean hidden = false;
        String exception = "";
        if (args.length > 0){
            if (!args[0].startsWith("-")) {
                if (!args[0].isEmpty())
                    fl = new File(args[0]);
                else
                    exception = "!!! Please enter an address!";
            }
            else{
                 for (int i = 1 ; i < args[0].length() ; i++){
                    if (args[0].charAt(i) == 'h')
                        hidden = true;
                    else
                        exception = "!!! Unrecognized flag : \"" + args[0].charAt(i) + "\"";
                }
                if (exception.isEmpty()){
                    if (!args[1].isEmpty())
                        fl = new File(args[1]);
                    else
                        exception = "!!! Please enter an address!";
                }
            }
        }else{
            Scanner input = new Scanner(System.in);
            System.out.println("Please enter a directory or a file address: ");
            fl = new File(input.nextLine());
            System.out.println("Including hidden files and directories? [Y/N]");
            hidden = input.nextLine().equalsIgnoreCase("y");
        }
        if (exception.isEmpty())
            startAnalyzing(fl,hidden);
        else
            System.out.println(exception);
    }

    private static void startAnalyzing(File fl, boolean hidden){
        Analyzer anlz = null;
        if (fl.isDirectory())
            anlz = new DirectoryAnalyzer(fl,hidden);
        else if (fl.isFile())
            anlz = new FileAnalyzer(fl);
        else
            System.out.println("!!! The address is invalid!");
        printResult(anlz);
    }

    private static void printResult(Analyzer an){
        if (an != null){
            System.out.println("________________________________________________________________________________");
            System.out.println(an.toString());
            System.out.println("---------------------------------------------------------------------------------");
        }
    }
}
