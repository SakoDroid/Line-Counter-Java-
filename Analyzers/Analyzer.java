package Analyzers;

public interface Analyzer {

    void startAnalyzing();

    String toString();

    float getTotalFilesCount();

    float getTotalLinesCount();
}
