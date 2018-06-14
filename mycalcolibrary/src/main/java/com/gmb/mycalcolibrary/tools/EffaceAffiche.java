package com.gmb.mycalcolibrary.tools;

import android.content.Context;
import android.os.Vibrator;
import android.view.View;
import android.widget.TextView;

/**
 * Created by GMB on 10/7/2016.
 */

public class EffaceAffiche implements View.OnClickListener {

    Context context;
    TextView afficheur,saisie;
    boolean effaceToutaffiche;

    public EffaceAffiche(Context context, TextView afficheur, TextView saisie, boolean effaceTout){

        this.context=context;
        this.afficheur=afficheur;
        this.saisie=saisie;
        this.effaceToutaffiche=effaceTout;
    }


    @Override
    public void onClick(View v) {

        Vibrator vb = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vb.vibrate(20);

        if(effaceToutaffiche){

            saisie.setText("");
            afficheur.setText("");
        }

        else {

            String buf=saisie.getText().toString();

            try{
                buf=buf.substring(0, buf.length()-1);
            }
            catch (Exception exp){
                buf="";
                System.err.println("effaceLeLast Error->"+exp);
            }

            saisie.setText(buf);

        }
    }


}
