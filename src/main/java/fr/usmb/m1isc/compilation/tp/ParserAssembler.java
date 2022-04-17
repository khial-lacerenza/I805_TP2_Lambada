package fr.usmb.m1isc.compilation.tp;

import javax.swing.text.html.parser.Parser;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

public class ParserAssembler {

    WriterAsm wa = new WriterAsm("test.asm");
    private static int nbWhile = 1;
    private static int nbIf = 1;
    private static int nbCondi = 1;

    private ArrayList<String> listeVariable = new ArrayList<String>();

    public void parseIt(Arbre value) {
        wa.writeIntoFile("DATA SEGMENT\n");
        parcoursArbreDataSeg(value);
        wa.writeIntoFile("DATA ENDS\n");

        wa.writeIntoFile("CODE SEGMENT\n");
        parcoursArbre(value);
        wa.writeIntoFile("CODE ENDS");
    }

    public void parcoursArbreDataSeg(Arbre arbre) {
        decryptVariable(arbre);
    }

    // parcours un Arbre binaire récurcivement
    public void parcoursArbre(Arbre arbre) {
        wa.writeIntoFile(decryptValue(arbre));

    }

    public void decryptVariable(Arbre arbre) {
        String value = arbre.getValeur().toString();
        if (arbre.getType() == Types.LET && !listeVariable.contains(value)) {
            if (!listeVariable.contains(arbre.getFilsGauche().getValeur().toString())) {
                listeVariable.add(arbre.getFilsGauche().getValeur().toString());
                wa.writeIntoFile(arbre.getFilsGauche().getValeur().toString() + " " + "DD" + "\n");
            }
        }
        if (arbre.getFilsGauche() != null) {
            decryptVariable(arbre.getFilsGauche());
        }
        if (arbre.getFilsDroit() != null) {
            decryptVariable(arbre.getFilsDroit());
        }
    }

    // doit retourner une value au lieu d'écrire.
    public String decryptValue(Arbre arbre ) {
        String value = arbre.getValeur().toString();


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
                    + "\tmov ecx, ebx\n"
                    + "\tdiv ebx, eax\n"
                    + "\tmul eax, ebx\n"
                    + "\tsub ecx, eax\n"
                    + "\tmov eax, ecx\n";
        }

        if (arbre.getType() == Types.LET) {
            String res;
            if (arbre.getFilsDroit().getType() == Types.INPUT) {
                res = "\tin eax\n";
            }else{
                res = decryptValue(arbre.getFilsDroit());
            }

            res += "\tmov " + arbre.getFilsGauche().getValeur().toString() + ", eax\n";
            return res;
        }

        if (arbre.getType() == Types.WHILE) {
            int cpt = nbWhile++;
            return "debut_while_" + cpt + ":\n"
                    + decryptValue(arbre.getFilsGauche())
                    + "\tjz sortie_while_" + cpt + "\n"
                    + decryptValue(arbre.getFilsDroit())
                    + "\tjmp debut_while_" + cpt + "\n"
                    + "sortie_while_" + cpt + ":\n";

        }

        if (arbre.getType() == Types.GT) {
            int cpt = nbCondi++;
            return decryptValue(arbre.getFilsGauche()) +
                    "\tpush eax\n"
                    + decryptValue(arbre.getFilsDroit())
                    + "\tpop ebx\n"
                    + "\tsub eax, ebx\n"
                    + "\tjle faux_gt_" + cpt + "\n"
                    + "\tmov eax, 1\n"
                    + "\tjmp sortie_gt_" + cpt + "\n"
                    + "faux_gt_" + cpt + ":\n"
                    + "\tmov eax, 0\n"
                    + "sortie_gt_" + cpt + ":\n";
        }

        if (arbre.getType() == Types.GTE) {
            int cpt = nbCondi++;
            return decryptValue(arbre.getFilsGauche()) +
                    "\tpush eax\n"
                    + decryptValue(arbre.getFilsDroit())
                    + "\tpop ebx\n"
                    + "\tsub eax, ebx\n"
                    + "\tjl faux_gte_" + cpt + "\n"
                    + "\tmov eax, 1\n"
                    + "\tjmp sortie_gte_" + cpt + "\n"
                    + "faux_gte_" + cpt + ":\n"
                    + "\tmov eax, 0\n"
                    + "sortie_gte_" + cpt + ":\n";
        }

        if (arbre.getType() == Types.EQUAL) {
            int cpt = nbCondi++;
            return decryptValue(arbre.getFilsGauche()) +
                    "\tpush eax\n"
                    + decryptValue(arbre.getFilsDroit())
                    + "\tpop ebx\n"
                    + "\tsub eax, ebx\n"
                    + "\tjnz faux_equal_" + nbCondi + "\n"
                    + "\tmov eax, 1\n"
                    + "\tjmp sortie_equal_" + nbCondi + "\n"
                    + "faux_equal_" + nbCondi + ":\n"
                    + "\tmov eax, 0\n"
                    + "sortie_equal_" + nbCondi + ":\n";
        }

        if (arbre.getType() == Types.OUTPUT) {
            return decryptValue(arbre.getFilsGauche()) +
                    "\tout eax\n";
        }

        if (arbre.getType() == Types.OR) {
            int cpt = nbCondi++;
            return decryptValue(arbre.getFilsGauche()) +
                    "\tjnz sortie_or_" + cpt + "\n"
                    + decryptValue(arbre.getFilsDroit())
                    + "sortie_or_" + cpt + ":\n";
        }

        if (arbre.getType() == Types.AND) {
            int cpt = nbCondi++;
            return decryptValue(arbre.getFilsGauche()) +
                    "\tjz sortie_and_" + cpt + "\n"
                    + decryptValue(arbre.getFilsDroit())
                    + "sortie_and_" + cpt + ":\n";
        }

        if (arbre.getType() == Types.NOT) {
            int cpt = nbCondi++;
            return decryptValue(arbre.getFilsGauche()) +
                    "\tjnz sortie_not_" + cpt + "\n"
                    + "\tmov eax, 1\n"
                    + "\tjmp sortie_not_" + cpt + ":\n"
                    + "sortie_not_" + cpt + ":\n"
                    + "\tmov eax, 0\n"
                    + "sortie_not_" + cpt + ":\n";
        }

        if (arbre.getType() == Types.IF) {
            int cpt = nbIf++;
            return decryptValue(arbre.getFilsGauche()) +
                    "\tjz else_" + cpt + "\n"
                    + decryptValue(arbre.getFilsDroit().getFilsGauche())
                    + "\tjmp sortie_if_" + cpt + "\n"
                    + "else_" + cpt + ":\n"
                    + decryptValue(arbre.getFilsGauche().getFilsDroit())
                    + "sortie_if_" + cpt + ":\n";
        }

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
