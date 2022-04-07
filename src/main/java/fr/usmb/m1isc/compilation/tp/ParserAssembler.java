package fr.usmb.m1isc.compilation.tp;

import javax.swing.text.html.parser.Parser;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;

public class ParserAssembler {

    public ParserAssembler(){}
    WriterAsm wa = new WriterAsm("test.asm");

    public void parseIt(Arbre value) {
        wa.writeIntoFile("DATA SEGMENT");
        parcoursArbreDataSeg(value);
        wa.writeIntoFile("DATA ENDS");

        wa.writeIntoFile("CODE SEGMENT");
        parcoursArbre(value);
        wa.writeIntoFile("CODE ENDS");
    }

    public void parcoursArbreDataSeg(Arbre arbre) {
        if(arbre.getFilsGauche() != null && arbre.getFilsDroit() != null){
            parcoursArbreDataSeg(arbre.getFilsGauche());
            parcoursArbreDataSeg(arbre.getFilsDroit());
        }
        decryptVariable(arbre);
    }

    // parcours un Arbre binaire récurcivement
    public void parcoursArbre(Arbre arbre){
        if(arbre.getFilsGauche() != null && arbre.getFilsDroit() != null){
            parcoursArbre(arbre.getFilsGauche());
            parcoursArbre(arbre.getFilsDroit());
        }
    }

    public void decryptVariable(Arbre arbre) {
        String value = arbre.getValeur().toString();
        if("let".equals(value)){
            wa.writeIntoFile(arbre.getFilsGauche().getValeur().toString() + " " + "DD");
        }
    }

    public void decryptValue(Arbre arbre){
        String value = arbre.getValeur().toString();

        if("let".equals(value)){
            wa.writeIntoFile("mov eax, "+ arbre.getFilsDroit().getValeur());
            wa.writeIntoFile("mov "+ arbre.getFilsGauche().getValeur() + ", eax");
            wa.writeIntoFile("push eax");
        }
        else if ("+".equals(value)){
            wa.writeIntoFile("mov eax, "+ arbre.getFilsGauche().getValeur());
            wa.writeIntoFile("add eax, "+ arbre.getFilsDroit().getValeur());
            wa.writeIntoFile("push eax");
        } else if ("-".equals(value)) {
            wa.writeIntoFile("mov eax, "+ arbre.getFilsGauche().getValeur());
            wa.writeIntoFile("sub eax, "+ arbre.getFilsDroit().getValeur());
            wa.writeIntoFile("push eax");
        } else if ("*".equals(value)) {
            wa.writeIntoFile("mov eax, "+ arbre.getFilsGauche().getValeur());
            wa.writeIntoFile("mul eax, "+ arbre.getFilsDroit().getValeur());
            wa.writeIntoFile("push eax");
        } else if ("/".equals(value)) {
            wa.writeIntoFile("mov eax, "+ arbre.getFilsGauche().getValeur());
            wa.writeIntoFile("div eax, "+ arbre.getFilsDroit().getValeur());
            wa.writeIntoFile("push eax");
        } else if ("mod".equals(value) || "%".equals(value) || "MOD".equals(value)) {
            System.out.println("%");
        }
    }

}
