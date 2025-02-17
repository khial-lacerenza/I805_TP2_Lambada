package fr.usmb.m1isc.compilation.tp;

import java_cup.runtime.Symbol;

public class Arbre {
    private Arbre filsGauche;
    private Arbre filsDroit;
    private Object valeur;

    private Types type;


    public Arbre() {
    }

    public Arbre(Object valeur) {
        this.valeur = valeur;
    }

    public Arbre(Object valeur, Types type) {
        this.valeur = valeur;
        this.type = type;
    }

    public Arbre(Arbre a) {
        this.filsGauche = a.filsGauche;
        this.filsDroit = a.filsDroit;
        this.valeur = a.valeur;
        this.type = a.type;
    }

    public Arbre(Arbre filsGauche, Object valeur, Types type) {
        this.filsGauche = filsGauche;
        this.valeur = valeur;
        this.type = type;
    }

    public Arbre(Object valeur, Arbre filsDroit, Types type) {
        this.valeur = valeur;
        this.filsDroit = filsDroit;
        this.type = type;
    }

    public Arbre(Arbre filsGauche, Object valeur, Arbre filsDroit, Types type) {
        this.valeur = valeur;
        this.filsGauche = filsGauche;
        this.filsDroit = filsDroit;
        this.type = type;
    }

    public Arbre getFilsGauche() {
        return filsGauche;
    }

    public void setFilsGauche(Arbre filsGauche) {
        this.filsGauche = filsGauche;
    }

    public Arbre getFilsDroit() {
        return filsDroit;
    }

    public void setFilsDroit(Arbre filsDroit) {
        this.filsDroit = filsDroit;
    }

    public Object getValeur() {
        return valeur;
    }

    public void setValeur(Object valeur) {
        this.valeur = valeur;
    }

    public Types getType() {
        return type;
    }

    public void setType(Types type) {
        this.type = type;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(valeur).append(" ");
        sb.append(type).append(" ");
        if (filsGauche != null) {
            if (filsGauche.getFilsGauche() != null && filsGauche.getFilsDroit() != null) {
                sb.append("(").append(filsGauche).append(")");
            } else {
                sb.append(filsGauche);
            }
        }
        if (filsDroit != null) {
            if ((filsDroit.getFilsGauche() != null && filsDroit.getFilsDroit() != null)) {
                sb.append("(").append(filsDroit).append(")");
            } else {
                sb.append(filsDroit);
            }
        }

        return sb.toString();
    }
}


