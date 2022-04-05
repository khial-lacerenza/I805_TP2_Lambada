package fr.usmb.m1isc.compilation.tp;

public class FileWriter {

    private final String filename;

    public FileWriter(String filename) {
        this.filename = filename;
    }

    public void write(String content) {
        try {
            java.io.FileWriter fw = new java.io.FileWriter(filename + ".asm");
            fw.write(content);
            fw.close();
        } catch (Exception e) {
            System.out.println("Erreur lors de la création du fichier");
        }
    }

}
