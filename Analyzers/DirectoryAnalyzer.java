package Analyzers;

import java.io.File;
import java.util.HashMap;
import java.util.Objects;

public class DirectoryAnalyzer implements Analyzer{

    private final File dir;
    private final HashMap<String,TypeData> data = new HashMap<>();
    private final boolean hidden;

    public DirectoryAnalyzer(File directory, boolean hid){
        this.dir = directory;
        this.hidden = hid;
        this.startAnalyzing();
    }

    @Override
    public void startAnalyzing(){
        for (String add : Objects.requireNonNull(dir.list())){
            if (add.startsWith(".") && !hidden)
                continue;
            File fl = new File(dir.getAbsolutePath() + "/" + add);
            if (fl.isDirectory()){
                DirectoryAnalyzer da = new DirectoryAnalyzer(fl,hidden);
                this.mergeData(da.getData());
            }else {
                FileAnalyzer fa = new FileAnalyzer(fl);
                TypeData td = data.get(fa.getFileType());
                if (td != null){
                    td.addFile(fa);
                }else{
                    td = new TypeData(fa.getFileType());
                    td.addFile(fa);
                    data.put(fa.getFileType(),td);
                }
            }
        }
    }

    private void mergeData(HashMap<String,TypeData> data2){
        for (String key : data2.keySet()){
            if (data.containsKey(key)) {
                data.get(key).merge(data2.get(key));
            }else{
                data.put(key,data2.get(key));
            }
        }
    }

    public HashMap<String,TypeData> getData(){
        return this.data;
    }

    @Override
    public String toString(){
        long totalLines = 0;
        long totalWords = 0;
        long totalSize = 0;
        long totalFilesCount = 0;
        StringBuilder out = new StringBuilder("| File type\t|Size (B)\t|Words\t\t|Lines\t\t|Files count\t|\n" +
                "---------------------------------------------------------------------------------\n");
        for (String key : data.keySet()){
            TypeData td = data.get(key);
            out.append(td.toString()).append("\n");
            totalLines += td.getLines();
            totalWords += td.getWords();
            totalSize += td.getSize();
            totalFilesCount += td.getFilesCount();
        }
        out.append("---------------------------------------------------------------------------------\n")
                .append("| Total\t\t|")
                .append(totalSize).append("\t\t|")
                .append(totalWords).append("\t\t|")
                .append(totalLines).append("\t\t|")
                .append(totalFilesCount).append("\t\t|");
        return out.toString();
    }
}
