package fr.usmb.m1isc.compilation.tp;

import java.io.*;

public class WriterAsm {

    String name;

    public WriterAsm(String name) {
        this.name = name;
    }

    FileWriter fw = null;
    BufferedWriter bw = null;
    PrintWriter pw = null;

    public void writeIntoFile(String content) {

        try {
            FileWriter f = new FileWriter(name, true);
            BufferedWriter b = new BufferedWriter(f);
            PrintWriter p = new PrintWriter(b);
            p.println(content);

            p.close();
            b.close();
            f.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
