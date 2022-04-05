package fr.usmb.m1isc.compilation.tp;

import javax.swing.text.html.parser.Parser;

public class ParserAssembler {

    public ParserAssembler(){}

    // parcours un Arbre binaire récurcivement
    public void parcoursArbre(Arbre arbre){
        if(arbre.getFilsGauche() == null && arbre.getFilsDroit() == null){
            System.out.println(arbre.getValeur());
        }
        else{
            parcoursArbre(arbre.getFilsGauche());
            parcoursArbre(arbre.getFilsDroit());
        }
    }
}
