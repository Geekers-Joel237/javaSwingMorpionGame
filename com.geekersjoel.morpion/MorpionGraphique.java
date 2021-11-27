import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.*;


public class MorpionGraphique {

    JPanel panneauFinal;
    JPanel panneauSuperieur;
    JPanel panneauCases;
    JPanel panneauInferieur;
    JButton nouvellePartie;
    JButton cases[];
    JLabel text1;
    JLabel text2;
    JPanel panneauScores;
    JLabel victoire;
    JLabel Defaite;
    JLabel nul;
    JButton scoreVictoire;
    JButton scoreDefaite;
    JButton scoreNul;

    public MorpionGraphique(){
        panneauFinal = new JPanel();
        BorderLayout disposition1 = new BorderLayout();
        panneauFinal.setLayout(disposition1);

        panneauSuperieur = new JPanel();
        GridLayout disposition2 = new GridLayout(1,1);
        panneauSuperieur.setLayout(disposition2);
        nouvellePartie = new JButton(" Nouvelle Partie ");
        panneauSuperieur.add(nouvellePartie);
        panneauFinal.add("North", panneauSuperieur);

        panneauCases = new JPanel();
        GridLayout disposition3 = new GridLayout(3,3);
        panneauCases.setLayout(disposition3);
        //Cree un tableau pour stocker les references des 9 boutons
        cases = new JButton[9];

        //Instancie les boutons, stocke leurs references dans le
        //tableau ,peint les
        //boutons en orange et les ajoute au panneau central
        for(int i = 0; i < 9;i++){
            cases[i] = new JButton();
            cases[i].setBackground(Color.ORANGE);
            panneauCases.add(cases[i]);
        }
        panneauFinal.add("Center", panneauCases);

        panneauInferieur = new JPanel();
        GridLayout disposition4 = new GridLayout(2,1);
        panneauInferieur.setLayout(disposition4);
        text1 = new JLabel(" A vous de jouer !");
        text2 = new JLabel(" Jeu Demarre ");
        panneauInferieur.add(text1);
        panneauInferieur.add(text2);

        panneauScores = new JPanel();
        GridLayout disposition5 = new GridLayout(2,3,5,5);
        panneauScores.setLayout(disposition5);
        victoire = new JLabel("Victoires :");
        Defaite = new JLabel("Defaites :");
        nul = new JLabel("Nuls :");
        scoreVictoire = new JButton();
        scoreDefaite = new JButton();
        scoreNul = new JButton();
        panneauScores.add(victoire);
        panneauScores.add(Defaite);
        panneauScores.add(nul);
        panneauScores.add(scoreVictoire);
        panneauScores.add(scoreDefaite);
        panneauScores.add(scoreNul);


        panneauFinal.add("South", panneauInferieur);
        panneauFinal.add("South", panneauScores);

        JFrame myFrame = new JFrame("MORPION");
        myFrame.setContentPane(panneauFinal);
        myFrame.pack();
        myFrame.setBackground(Color.CYAN);
        myFrame.setVisible(true);
        

    }
}
