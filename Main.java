import Analyzers.Analyzer;
import Analyzers.DirectoryAnalyzer;
import Analyzers.FileAnalyzer;
import java.io.File;
import java.util.Scanner;

public class Main {

    public static void main(String[] args){
        File fl;
        boolean hidden = false;
        if (args.length > 0){
            fl = new File(args[0]);
        }else{
            Scanner input = new Scanner(System.in);
            System.out.println("Please enter a directory or a file address: ");
            fl = new File(input.nextLine());
            System.out.println("Including hidden files and directories? [Y/N]");
            hidden = input.nextLine().equalsIgnoreCase("y");
        }
        startAnalyzing(fl,hidden);
    }

    private static void startAnalyzing(File fl, boolean hidden){
        Analyzer anlz = null;
        if (fl.isDirectory())
            anlz = new DirectoryAnalyzer(fl,hidden);
        else if (fl.isFile())
            anlz = new FileAnalyzer(fl);
        else
            System.out.println("The address is invalid!");
        printResult(anlz);
    }

    private static void printResult(Analyzer an){
        if (an != null){
            System.out.println("_______________________________________________________________________________");
            System.out.println(an.toString());
            System.out.print("------------------------------------------------------------------------------");
        }
    }
}
