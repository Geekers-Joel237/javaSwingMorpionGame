public class MainMorpion {
    public static void main(String[] args) {
        //Instances de mes classes MorpionGraphique et MoteurJeuMorpion
        MorpionGraphique morpionGraphique = new MorpionGraphique();
        MoteurJeuMorpion moteurJeuMorpion = new MoteurJeuMorpion(morpionGraphique);

        //Enregistrement des compoants graphiques aux actions de moteurJeuMorpion
        morpionGraphique.nouvellePartie.addActionListener(moteurJeuMorpion);
        for(int i = 0; i < 9;i++){
            morpionGraphique.cases[i].addActionListener(moteurJeuMorpion);
        }
    }
}
