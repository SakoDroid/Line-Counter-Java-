package Analyzers;

import java.io.*;
import java.util.regex.*;

public class FileAnalyzer implements Analyzer{

    private final File file;
    private long linesCount = 0, wordsCount = 0,blankLineCount = 0,bracketLineCount = 0, size = 0;
    private String fileType = "";

    public FileAnalyzer(File fl){
        this.file = fl;
        this.size = fl.length();
        this.startAnalyzing();
    }

    @Override
    public void startAnalyzing(){
        this.determineFileType();
        try(BufferedReader bf = new BufferedReader(new FileReader(file))){
            String line;
            while((line = bf.readLine()) != null){
                linesCount++;
                if (line.isEmpty())
                    blankLineCount ++;
                else if (line.trim().equals("{") || line.trim().equals("}"))
                    bracketLineCount ++;
                wordsCount += line.split(" ").length;
            }
        }catch (Exception ex){
            System.out.println(ex.toString());
        }
    }

    private void determineFileType(){
        String ext = "";
        Pattern ptn = Pattern.compile("\\.\\w+");
        Matcher mc = ptn.matcher(file.getName());
        if (mc.find())
            ext = mc.group();
        fileType = Type.getInstance().getType(ext);
    }

    public long getLinesCount(){
        return this.linesCount;
    }

    public long getWordsCount(){
        return this.wordsCount;
    }

    public long getSize(){
        return this.size;
    }

    public long getBlankLineCount() {
        return this.blankLineCount;
    }

    public long getBracketLineCount() {
        return this.bracketLineCount;
    }

    public String getFileType(){
        return this.fileType;
    }

    @Override
    public String toString(){
        return "| " + fileType + "\t\t|" + size + "\t\t|" + wordsCount +  "\t\t|" + linesCount + "\t\t|" + blankLineCount
                + "\t\t|" + bracketLineCount + "\t\t|1\t\t|";
    }
}
