package com.gmb.mycalcolibrary.tools;

import android.content.Context;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by GMB on 10/7/2016.
 */

public class EcritUserSaisie implements View.OnClickListener {

    TextView ecranDeSaisie;
    Context context;

    public EcritUserSaisie(Context context, TextView ecrantCourant){

        this.context=context;
        ecranDeSaisie=ecrantCourant;
    }

    @Override
    public void onClick(View v) {

        Vibrator vb = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vb.vibrate(20);

        Button buf=(Button) v;

        String text="";
        String old=ecranDeSaisie.getText().toString();
        String newChar=buf.getText().toString();

        if(newChar.equalsIgnoreCase("+-")){

            newChar="-";
        }

        String valPos=new String("0123456789()00.-");

        if(valPos.contains(newChar)){

            if (newChar.equalsIgnoreCase("-")) {
                if (old.startsWith(newChar)) {
                    text=old.substring(1);
                } else {
                    text=newChar + old;
                }
            } else {

                text=(old + newChar);
            }

        }
        else{

            text=old;
        }

        ecranDeSaisie.setText(text);
    }
}
