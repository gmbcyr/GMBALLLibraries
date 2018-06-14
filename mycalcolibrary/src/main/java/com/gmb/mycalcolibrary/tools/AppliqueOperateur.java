package com.gmb.mycalcolibrary.tools;

import android.content.Context;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by GMB on 10/7/2016.
 */

public class AppliqueOperateur implements View.OnClickListener {

    Context context;
    TextView afficheur,saisie;



    public AppliqueOperateur(Context context, TextView afficheur, TextView saisie){

        this.context=context;
        this.afficheur=afficheur;
        this.saisie=saisie;
    }

    public boolean fermePossible(String chaine){

        boolean result=false;
        int ouvre=0;
        int ferme=0;
        boolean trouve=false;

        int last=chaine.length();

        //recherche des parentheses
        for(int i=0;i<last;i++){

            char[] t={chaine.charAt(i)};
            String test=new String(t);


            if(test.equalsIgnoreCase("(") ||
                    test.equalsIgnoreCase(")")){

                if(test.equalsIgnoreCase("(")){
                    trouve=true;
                    ouvre++;
                }
                else ferme++;
            }


        }

        if(ouvre>ferme)  result=true;
        System.out.println("fermePossible dans chaine->"+chaine+", c'est � la pos->"+result);
        return result;
    }


    @Override
    public void onClick(View v) {

        Vibrator vb = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vb.vibrate(20);

        String init=afficheur.getText().toString();
        String buf=saisie.getText().toString();
        Button but=(Button) v;
        String operateur=but.getText().toString();


        if(buf.equalsIgnoreCase("") && (init.endsWith("*")
                || (init.endsWith("^"))
                || (init.endsWith("+"))
                || (init.endsWith("-"))
                || (init.endsWith("%"))
                || (init.endsWith("/"))) && !operateur.equalsIgnoreCase("(")){

            if (operateur.equalsIgnoreCase(")") ) {
                if (fermePossible(init)) {
                    afficheur.setText(init.substring(0, init.length() - 1) + operateur);
                }

            }

            else if(operateur.equalsIgnoreCase("%") ) {//on peut mettre un autre operateur sur % mais pas lui meme
                afficheur.setText(init.substring(0, init.length() - 1) + operateur);
            }

            else {
                if (init.endsWith("%")) {
                    afficheur.setText(init + operateur);
                }
                else{

                    afficheur.setText(init.substring(0, init.length() - 1) + operateur);
                }
            }
            return;
        }


        if(init.endsWith("(") && buf.equalsIgnoreCase("")){

            return;
        }

        if(!buf.equalsIgnoreCase("")  && operateur.equalsIgnoreCase("(")){

            return;
        }

        if (operateur.equalsIgnoreCase("mod.")) {
            if (init.endsWith("=")) {
                afficheur.setText("("+buf+")" + operateur);
                saisie.setText("");
            } else {
                afficheur.setText("("+init + buf+")" + operateur);
                saisie.setText("");
            }
        } else if(operateur.equalsIgnoreCase("^")) {

            if (init.endsWith("=")) {
                afficheur.setText("("+buf+")" + operateur);
                saisie.setText("");
            } else {
                if (buf.equalsIgnoreCase("")) {
                    System.out.println("appliqueOperateur cas expo avec buf vide");
                    if (init.endsWith(")")) {
                        System.out.println("appliqueOperateur cas expo sur init");
                        afficheur.setText(init + operateur);
                        saisie.setText("");
                    } else {
                        afficheur.setText( "(" + init + ")" + operateur);
                        saisie.setText("");
                    }
                } else {

                    afficheur.setText(init + "(" + buf + ")" + operateur);
                    saisie.setText("");
                }
            }
        }
        else if(operateur.equalsIgnoreCase(")")) {

            if (fermePossible(init+buf)) {
                if (init.endsWith("=")) {
                    afficheur.setText("(" + buf + ")" + operateur);
                    saisie.setText("");
                } else {
                    if (buf.equalsIgnoreCase("")) {

                        afficheur.setText(init + operateur);
                        saisie.setText("");

                    } else {

                        afficheur.setText(init + buf + operateur);
                        saisie.setText("");
                    }
                }
            }
        }

        else {

            if (init.endsWith("=")) {// cas ou on effectue un calcul sur un resultat precedant
                afficheur.setText(buf + operateur);
                saisie.setText("");
            } else {
                if (buf.equalsIgnoreCase("")) {/*cas ou le champ de saisie est vide. on ne met pas deux operateur successivement
                    sauf si l'un d'eux est %*/

                    afficheur.setText(init + operateur);
                    saisie.setText("");

                } else {// on ajoute la saisie courant et l'operateur a tout le precedant saisie

                    afficheur.setText(init + buf + operateur);
                    saisie.setText("");
                }
            }
        }
    }
}
