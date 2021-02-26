package Analyzers;

import java.io.*;

public class GitCloner {

    public GitCloner(String repoAddress){
        this.startCloning(repoAddress);
    }

    private void startCloning(String add){
        try{
            String wd = System.getProperty("user.dir");
            String temp = wd + "/Temp";
            File fl = new File(temp);
            if (!fl.isDirectory())
                fl.mkdir();
            ProcessBuilder pb = new ProcessBuilder("sh",wd + "/assets/git-clone.sh",add,wd + "/Temp");
            Process p = pb.start();
            new Piper(p.getInputStream(), System.out);
            new Piper(p.getErrorStream(), System.err);
            new Piper(System.in, p.getOutputStream());
            p.waitFor();
        }catch (Exception ex){
            System.out.println(ex.toString());
        }
    }

    private static class Piper extends Thread{

        private final InputStream is;
        private final OutputStream os;

        public Piper(InputStream in, OutputStream out){
            this.is = in;
            this.os = out;
            this.start();
        }

        @Override
        public void run(){
            int i;
            try {
                while ((i = is.read()) != -1)
                    os.write(i);
            }catch (Exception ex){
                System.out.println(ex.toString());
            }
        }
    }
}
