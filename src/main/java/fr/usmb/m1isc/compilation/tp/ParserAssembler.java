package fr.usmb.m1isc.compilation.tp;

import javax.swing.text.html.parser.Parser;

public class ParserAssembler {

    public ParserAssembler(){}
    FileWriter fw = new FileWriter("test.asm");

    // parcours un Arbre binaire récurcivement
    public void parcoursArbre(Arbre arbre){
        if(arbre.getFilsGauche() == null && arbre.getFilsDroit() == null){
            System.out.println(arbre.getValeur());
        }
        else{
            parcoursArbre(arbre.getFilsGauche());
            decryptValue(arbre.getFilsGauche());
            parcoursArbre(arbre.getFilsDroit());
            decryptValue(arbre.getFilsDroit());
        }
    }

    public void decryptValue(Arbre arbre){
        Object value = arbre.getValeur();

        if ("+".equals(value)) {
            fw.write("mov eax, "+ arbre.getFilsGauche() +"\n");
            fw.write("add eax, "+ arbre.getFilsDroit() + "\n");
        } else if ("-".equals(value)) {
            System.out.println("-");
        } else if ("*".equals(value)) {
            System.out.println("*");
        } else if ("/".equals(value)) {
            System.out.println("/");
        } else if ("mod".equals(value) || "%".equals(value) || "MOD".equals(value)) {
            System.out.println("%");
        }
        else {
            System.out.println(value);
        }
    }

}
