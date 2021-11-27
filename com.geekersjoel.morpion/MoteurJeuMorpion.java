import java.awt.Color;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JOptionPane;

public class MoteurJeuMorpion implements Action{
    
    //Reference a MorpionGraphique
    MorpionGraphique parent;

    int casesLibresRestantes = 9;
    int scoreVictoire = 0,scoreDefaite = 0,scoreNul = 0;

    public MoteurJeuMorpion(MorpionGraphique parent){
        this.parent = parent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub

        //Recuper la source de l'evenement
        JButton boutonClique = (JButton) e.getSource();

        //s'agit il du bouton noubelle partie
        if(boutonClique == parent.nouvellePartie){
            for(int i = 0; i < 9;++i){
                //Rendre les boutons cliquables
                parent.cases[i].setEnabled(true);
                parent.cases[i].setText("");
                parent.cases[i].setBackground(Color.GREEN);
            }
            casesLibresRestantes = 9 ;
            parent.text1.setText("A vous de jouer !");
            parent.nouvellePartie.setEnabled(false);
            // return;
        }
        
        String gagnant = "";

        //S'agit il de l'une des cases
        for(int i = 0; i < 9; ++i){
            if(boutonClique == parent.cases[i]){
                if (!parent.cases[i].getText().equals("")) {
                    parent.cases[i].setBackground(Color.RED);
                    int pos_i = i;
                    Timer chrono = new Timer();
                    chrono.schedule(new TimerTask() {
                    
                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            JOptionPane.showConfirmDialog(null, "Je t'ai a l'oeil petit tricheur", "Tentative de triche Detectee", JOptionPane.PLAIN_MESSAGE);
                            parent.cases[pos_i].setBackground(Color.GREEN);
                        }
                        
                    }, 1000);
                } else {
                    parent.cases[i].setText("X");

                    //On cherche le gagnant apres chaque coup de l'user
                    gagnant = chercherGagnant();

                    //Si gagnant trouve alors terminer la partie
                    if (!"".equals(gagnant)) {
                        terminerLaPartie();
                    }else{
                        //passe la main a l'ordi
                        coupOrdinateur();
                        //On cherche aussi un gagnant apres chaaque coup de l'ordi
                        gagnant = chercherGagnant();
                        //Si gagnant trouve alors terminer la partie
                        if (!"".equals(gagnant)) {
                            terminerLaPartie();
                        }
                        
                    }
                    break;

                }
                
                
            }
        }

        //On gere les affichages en fonction de qui gagne
        if (gagnant.equals("X")){
            ++scoreVictoire;
            this.parent.text1.setText("Vous avez gagne!");
            this.parent.text2.setText("Partie Terminee !");
            this.parent.scoreVictoire.setText(String.valueOf(scoreVictoire).toString());
        }else if (gagnant.equals("O")) {
            ++scoreDefaite;
            this.parent.text1.setText("Vous avez perdu!");
            this.parent.text2.setText("Partie Terminee !");
            this.parent.scoreDefaite.setText(String.valueOf(scoreDefaite).toString());
        }else if(gagnant.equals("T")){
            ++scoreNul;
            this.parent.text1.setText("Vous Feriez mieux la prochaine fois!");
            this.parent.text2.setText("Partie Nulle !");
            this.parent.scoreNul.setText(String.valueOf(scoreNul).toString());
        }

    }

    //Cette methode applique un ensemble de regles afin de trouver
    //le meilleur coup pour l'ordi. Si un bon coup ne peut etre trouve,
    //elle choisit une case au hasard
    private void coupOrdinateur() {
        int caseSelectionnee;

        //L'ordi essaie d'abord de trouver une case vide pres de 2
        //cases marquees "O" pour gagner
        caseSelectionnee = trouverCaseVide("O");

        //S'il n'ya pas deux "O" alignes ,essaie au moins d'empecher
        //l'adversaire d'aligner trois "X" en placant un "O" pres de 2 "X"
        if (caseSelectionnee == -1) {
            caseSelectionnee = trouverCaseVide("X");
        }
        //Si la case vaut toujours -1 , essaie d'occuper la case centrale
        if (caseSelectionnee == -1 && (parent.cases[4].getText().equals("")))  {
            caseSelectionnee = 4;
        }
        //Pas de chance avec la case centrale non plus...
        //Choisit une case au hasard
        if (caseSelectionnee == -1) {
            caseSelectionnee = choisirCaseAuHasard();
        }
        parent.cases[caseSelectionnee].setText("O");
    }


    //Cette methode selectionne une case vide quelconque
    //@return le numero de la case choisie au hasard
    private int choisirCaseAuHasard() {
        boolean caseVideTrouvee = false;
        int caseSelectionnee = -1;

        do {
            caseSelectionnee = (int)(Math.random()*9);
            if (parent.cases[caseSelectionnee].getText().equals("")) {
                caseVideTrouvee = true;
            }
        } while (!caseVideTrouvee);
        return caseSelectionnee;
    }

    //Cette methode examine chaque ligne ,colonne et diagonale pour
    //voir si elle contient deux cases avec le mm label et une case vide.
    //@param string "X" pour l'utilisateur ou "O" pour l'ordi
    //@return numero de la case vide a utiliser ou -1 si la
    //recherche est infructueuse
    private int trouverCaseVide(String string) {
        int poids[] = new int[9];
        for(int i = 0;i<9;++i){
            if (parent.cases[i].getText().equals("O")) {
                poids[i] = -1;
            }else if(parent.cases[i].getText().equals("X")){
                poids[i] = 1;
            }else{
                poids[i] = 0;
            }
        }
        int deuxPoids = string.equals("O") ? -2 : 2;

        //Regarde si la ligne 1 a 2 cases identiques et une vide
        if (poids[0]+poids[1]+poids[2] == deuxPoids) {
            if (poids[0] == 0) {
                return 0;
            }else if(poids[1] == 0){
                return 1;
            }else{
                return 2;
            }
        }

        //Regarde si la ligne 2 a 2 cases identiques et une vide
        if (poids[3]+poids[4]+poids[5] == deuxPoids) {
            if (poids[3] == 0) {
                return 3;
            }else if(poids[5] == 0){
                return 5;
            }else{
                return 4;
            }
        }

        //Regarde si la ligne 3 a 2 cases identiques et une vide
        if (poids[6]+poids[7]+poids[8] == deuxPoids) {
            if (poids[6] == 0) {
                return 6;
            }else if(poids[7] == 0){
                return 7;
            }else{
                return 8;
            }
        }

        //Regarde si la colonne 1 a 2 cases identiques et une vide
        if (poids[0]+poids[3]+poids[6] == deuxPoids) {
            if (poids[0] == 0) {
                return 0;
            }else if(poids[3] == 0){
                return 3;
            }else{
                return 6;
            }
        }

        //Regarde si la colonne 2 a 2 cases identiques et une vide
        if (poids[1]+poids[4]+poids[7] == deuxPoids) {
            if (poids[1] == 0) {
                return 1;
            }else if(poids[4] == 0){
                return 4;
            }else{
                return 7;
            }
        }

        //Regarde si la colonne 3 a 2 cases identiques et une vide
        if (poids[2]+poids[5]+poids[8] == deuxPoids) {
            if (poids[2] == 0) {
                return 2;
            }else if(poids[5] == 0){
                return 5;
            }else{
                return 8;
            }
        }

        //Regarde si la diagonale 1 a 2 cases identiques et une vide
        if (poids[0]+poids[4]+poids[8] == deuxPoids) {
            if (poids[0] == 0) {
                return 0;
            }else if(poids[4] == 0){
                return 4;
            }else{
                return 8;
            }
        }

        //Regarde si la colonne 2 a 2 cases identiques et une vide
        if (poids[2]+poids[4]+poids[6] == deuxPoids) {
            if (poids[2] == 0) {
                return 2;
            }else if(poids[4] == 0){
                return 4;
            }else{
                return 6;
            }
        }

        //Il n'ya pas de caases alignees identiques
        return -1;
    }

    //Desactive les cases et active le bouton Nouvelle Partie
    private void terminerLaPartie() {
        parent.nouvellePartie.setEnabled(true);
        for (int i = 0; i < 9; i++) {
            parent.cases[i].setEnabled(false);
        }
    }

    //Cette methode est appelee a chaque coup joue pour
    //voir s'il y'a un gagnant.  Elle verifie pour chaque 
    //ligne,colonne et diagonale ,s,il y'a 3 symboles identiques 
    //return "X":Termine "O":Partie nulle "T":Pas fini 
    private String chercherGagnant() {
        String leGagnant = "";
        casesLibresRestantes--;
        //verifie la ligne 1 - elements 0 ,1 et 2 du tableau
        if (!parent.cases[0].getText().equals("") && 
            parent.cases[0].getText().equals(parent.cases[1].getText()) &&  
            parent.cases[0].getText().equals(parent.cases[2].getText()) ) {
            leGagnant = parent.cases[0].getText();
            momtrerGagnant(0,1,2);
        }
        //verifie la ligne 2 - elements 3 ,4 et 5 du tableau
        else if (!parent.cases[3].getText().equals("") && 
                parent.cases[3].getText().equals(parent.cases[4].getText()) &&  
                parent.cases[3].getText().equals(parent.cases[5].getText()) ) {
            leGagnant = parent.cases[3].getText();
            momtrerGagnant(3,4,5);
        }
        //verifie la ligne 3 - elements 6 ,7 et 8 du tableau
        else if (!parent.cases[6].getText().equals("") && 
                parent.cases[6].getText().equals(parent.cases[7].getText()) &&  
                parent.cases[6].getText().equals(parent.cases[8].getText()) ) {
            leGagnant = parent.cases[6].getText();
            momtrerGagnant(5,7,8);
        }
        //verifie la colone 1 - elements 0 ,3 et 6 du tableau
        else if (!parent.cases[0].getText().equals("") && 
                parent.cases[0].getText().equals(parent.cases[3].getText()) &&  
                parent.cases[0].getText().equals(parent.cases[6].getText()) ) {
            leGagnant = parent.cases[0].getText();
            momtrerGagnant(0,3,6);
        }
        //verifie la colone 2 - elements 1 ,4 et 7 du tableau
        else if (!parent.cases[1].getText().equals("") && 
                parent.cases[1].getText().equals(parent.cases[4].getText()) &&  
                parent.cases[1].getText().equals(parent.cases[7].getText()) ) {
            leGagnant = parent.cases[1].getText();
            momtrerGagnant(1,4,7);
        }
        //verifie la colone 3 - elements 2 ,5 et 8 du tableau
        else if (!parent.cases[2].getText().equals("") && 
                parent.cases[2].getText().equals(parent.cases[5].getText()) &&  
                parent.cases[2].getText().equals(parent.cases[8].getText()) ) {
            leGagnant = parent.cases[2].getText();
            momtrerGagnant(2,5,8);
        }
        //verifie la diagonale 1 - elements 0 ,4 et 8 du tableau
        else if (!parent.cases[0].getText().equals("") && 
                parent.cases[0].getText().equals(parent.cases[4].getText()) &&  
                parent.cases[0].getText().equals(parent.cases[8].getText()) ) {
            leGagnant = parent.cases[0].getText();
            momtrerGagnant(0,4,8);
        }
        //verifie la diagonale 2 - elements 2 ,4 et 6 du tableau
        else if (!parent.cases[2].getText().equals("") && 
                parent.cases[2].getText().equals(parent.cases[4].getText()) &&  
                parent.cases[2].getText().equals(parent.cases[6].getText()) ) {
            leGagnant = parent.cases[2].getText();
            momtrerGagnant(2,4,6);
        }
        else if(casesLibresRestantes == 0){
            return "T";//partie nulle
        }
        return leGagnant;
    }

    //Cette methode affiche la ligne gagnante en surbrillance
    private void momtrerGagnant(int i, int j, int k) {
        parent.cases[i].setBackground(Color.CYAN);
        parent.cases[j].setBackground(Color.CYAN);
        parent.cases[k].setBackground(Color.CYAN);
    }

    @Override
    public Object getValue(String key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void putValue(String key, Object value) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setEnabled(boolean b) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean isEnabled() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        // TODO Auto-generated method stub
        
    }
}
