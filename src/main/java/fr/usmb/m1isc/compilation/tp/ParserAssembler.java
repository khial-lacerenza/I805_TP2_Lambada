package fr.usmb.m1isc.compilation.tp;

import javax.swing.text.html.parser.Parser;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

public class ParserAssembler {

    WriterAsm wa = new WriterAsm("test.asm");

    public void parseIt(Arbre value) {
//        wa.writeIntoFile("DATA SEGMENT\n");
//        parcoursArbreDataSeg(value);
//        wa.writeIntoFile("DATA ENDS\n");

        wa.writeIntoFile("CODE SEGMENT\n");
        parcoursArbre(value);
        wa.writeIntoFile("CODE ENDS");
    }

    public void parcoursArbreDataSeg(Arbre arbre) {
        if (arbre.getFilsGauche() != null && arbre.getFilsDroit() != null) {
            parcoursArbreDataSeg(arbre.getFilsGauche());
            parcoursArbreDataSeg(arbre.getFilsDroit());
        }
        decryptVariable(arbre);
    }

    // parcours un Arbre binaire récurcivement
    public void parcoursArbre(Arbre arbre) {
        if (arbre.getFilsGauche() != null) {
            parcoursArbre(arbre.getFilsGauche());
        }
        if(arbre.getFilsDroit() != null) {
            parcoursArbre(arbre.getFilsDroit());
        }
        // on doit ecire ici
        wa.writeIntoFile(decryptValue(arbre));
    }

    public void decryptVariable(Arbre arbre) {
        String value = arbre.getValeur().toString();
        if ("let".equals(value)) {
            wa.writeIntoFile(arbre.getFilsGauche().getValeur().toString() + " " + "DD" + "\n");
        }
    }

    // doit retourner une value au lieu d'écrire.
    public String decryptValue(Arbre arbre) {
        String value = arbre.getValeur().toString();
        System.out.println(value);
        if ("let".equals(value)) {
            return
                    "mov eax, " + decryptValue(arbre.getFilsDroit()) + "\n" +
                            "mov " + decryptValue(arbre.getFilsGauche()) + ", eax" + "\n" +
                            "push eax" + "\n";
        } else if ("+".equals(value)) {
            return
                    "mov eax, " + decryptValue(arbre.getFilsGauche()) + "\n" +
                            "add eax, " + decryptValue(arbre.getFilsDroit()) + "\n" +
                            "push eax" + "\n";
        } else if ("-".equals(value)) {
            return
                    "mov eax, " + decryptValue(arbre.getFilsGauche()) + "\n" +
                            "sub eax, " + decryptValue(arbre.getFilsDroit()) + "\n" +
                            "push eax" + "\n";
        } else if ("*".equals(value)) {
            return
                    "mov eax, " + decryptValue(arbre.getFilsGauche()) + "\n" +
                            "mul eax, " + decryptValue(arbre.getFilsDroit()) + "\n" +
                            "push eax" + "\n";
        } else if ("/".equals(value)) {
            return
                    "mov eax, " + decryptValue(arbre.getFilsGauche()) + "\n" +
                            "div eax, " + decryptValue(arbre.getFilsDroit()) + "\n" +
                            "push eax" + "\n";
        }else {
            return value;
        }

    }

    Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

    public boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        return pattern.matcher(strNum).matches();
    }

}
