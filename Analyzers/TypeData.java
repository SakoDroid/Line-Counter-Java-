package Analyzers;

public class TypeData {

    private long lines = 0;
    private long words = 0;
    private long size = 0;
    private int filesCount = 0;
    private final String type;

    public TypeData(String type){
        this.type = type;
    }

    public long getSize(){
        return this.size;
    }

    public long getLines(){
        return this.lines;
    }

    public long getWords(){
        return this.words;
    }

    public int getFilesCount(){
        return this.filesCount;
    }

    public void merge(TypeData td){
        this.lines += td.lines;
        this.words += td.words;
        this.size += td.size;
        this.filesCount += td.filesCount;
    }

    public void addFile(FileAnalyzer fa){
        this.lines += fa.getLinesCount();
        this.words += fa.getWordsCount();
        this.size += fa.getSize();
        filesCount ++;
    }

    @Override
    public String toString(){
        return "| " + type + "\t\t|" + size + "\t\t|" + words +  "\t\t|" + lines + "\t\t|" + filesCount + "\t\t|";
    }
}
