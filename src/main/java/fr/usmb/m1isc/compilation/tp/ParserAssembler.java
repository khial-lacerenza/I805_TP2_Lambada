package fr.usmb.m1isc.compilation.tp;

import javax.swing.text.html.parser.Parser;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

public class ParserAssembler {

    WriterAsm wa = new WriterAsm("test.asm");

    public void parseIt(Arbre value) {
        wa.writeIntoFile("DATA SEGMENT\n");
        parcoursArbreDataSeg(value);
        wa.writeIntoFile("DATA ENDS\n");

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
        wa.writeIntoFile(decryptValue(arbre));

    }

    public void decryptVariable(Arbre arbre) {
        String value = arbre.getValeur().toString();
        if ("let".equals(value)) {
            wa.writeIntoFile(arbre.getFilsGauche().getValeur().toString() + " " + "DD" + "\n");
        }
    }

    // doit retourner une value au lieu d'écrire.
    public String decryptValue(Arbre arbre ) {
        String value = arbre.getValeur().toString();
        System.out.println(value);
        System.out.println(arbre.getValeur());

        if (arbre.getType() == Types.INT ||arbre.getType() == Types.IDENT ) {


            return "\tmov eax, " + value + "\n";
        }

        if (arbre.getType() == Types.PLUS) {
            return decryptValue(arbre.getFilsGauche()) +
                    "\tpush eax\n"
                    + decryptValue(arbre.getFilsDroit())
                    + "\tpop ebx\n"
                    + "\tadd eax, ebx\n";
        }

        if (arbre.getType() == Types.MINUS) {
            return decryptValue(arbre.getFilsGauche()) +
                    "\tpush eax\n"
                    + decryptValue(arbre.getFilsDroit())
                    + "\tpop ebx\n"
                    + "\tsub ebx, eax\n"
                    + "\tmov eax, ebx\n";

        }

        if (arbre.getType() == Types.UNAIRE) {
            return decryptValue(arbre.getFilsGauche()) +
                    "\tpush eax\n"
                    + "\tmov ebx, 0\n"
                    + "\tsub ebx, eax\n"
                    + "\tmov eax, ebx\n";
        }

        if (arbre.getType() == Types.MUL) {
            return decryptValue(arbre.getFilsGauche()) +
                    "\tpush eax\n"
                    + decryptValue(arbre.getFilsDroit())
                    + "\tpop ebx\n"
                    + "\tmul eax, ebx\n";
        }
        if (arbre.getType() == Types.DIV) {
            return decryptValue(arbre.getFilsGauche()) +
                    "\tpush eax\n"
                    + decryptValue(arbre.getFilsDroit())
                    + "\tpop ebx\n"
                    + "\tdiv ebx, eax\n"
                    + "\tmov eax, ebx\n";
        }
        if (arbre.getType() == Types.MOD) {
            return decryptValue(arbre.getFilsGauche()) +
                    "\tpush eax\n"
                    + decryptValue(arbre.getFilsDroit())
                    + "\tpop ebx\n"
                    + "\tpush ebx\n"
                    + "\tdiv ebx, eax\n"
                    + "\tmul eax, edx\n"
                    + "\tpop ebx\n"
                    + "\tsub ebx, eax\n"
                    + "\tmov eax, ebx\n";
        }

        if (arbre.getType() == Types.LET) {
            String res = decryptValue(arbre.getFilsDroit());
            res += "\tmov " + arbre.getFilsGauche().getValeur().toString() + ", eax\n";
            return res;
        }
        

//        if (arbre.getType() == Types.IF) {

        if (arbre.getType() == Types.SEQUENCE) {
            return decryptValue(arbre.getFilsGauche()) + decryptValue(arbre.getFilsDroit());
        }
        return "";
    }

    Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

//    public boolean isNumeric(String str) {
//        if (str == null) {
//            return false;
//        }
//        return pattern.matcher(str).matches();
//    }

}
