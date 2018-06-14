package com.gmb.mycalcolibrary.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gmb.mycalcolibrary.R;

import java.util.ArrayList;

/**
 * Created by GMB on 10/7/2016.
 */

public class MonMoteur implements View.OnClickListener {

    private static final int PRIVATE_MODE=0;
    public static final int EGAL_ID=2131493002;
    public static final int RM_ID=2131493017;
    private  final String PREF_NAME;
    private final String RESULT_CALCUL_VAR_NAME="resultCal";

    Context context;
    TextView afficheur,saisie;
    long nbre=0;
    long exposant=1;
    long dvse=0;
    int current=0;
    ArrayList<Long> ar;
    ArrayList<CongruLine> vect;


    public MonMoteur(Context context, TextView afficheur, TextView saisie){

        this.context=context;
        this.afficheur=afficheur;
        this.saisie=saisie;

        PREF_NAME=context.getPackageName();
    }


    public String identifieOperateur(String chaine){

        String result="";
        int ouvre=0;
        int ferme=0;

        //recherche des operateurs
        for(int i=0;i<chaine.length();i++){

            char[] t={chaine.charAt(i)};
            String test=new String(t);
            if(test.equalsIgnoreCase(context.getResources().getString(R.string.plus))
                    || test.equalsIgnoreCase(context.getResources().getString(R.string.fois))
                    || test.equalsIgnoreCase(context.getResources().getString(R.string.moins))
                    || test.equalsIgnoreCase(context.getResources().getString(R.string.div))){


                result=result+i+"_"+chaine.charAt(i)+"#";
            }
        }

        //recherche des parentheses
        for(int i=0;i<chaine.length();i++){
            char[] t={chaine.charAt(i)};
            String test=new String(t);
            if(test.equalsIgnoreCase(context.getResources().getString(R.string.parentheseO)) ||
                    test.equalsIgnoreCase(context.getResources().getString(R.string.parentheseF))){


                result=result+"$"+i+"_"+chaine.charAt(i);

            }
        }
        return result+"%"+compteParenthese(chaine);
    }


    public String compteParenthese(String chaine){

        String result="";
        int ouvre=0;
        int ferme=0;


        //recherche des parentheses
        for(int i=0;i<chaine.length();i++){
            char[] t={chaine.charAt(i)};
            String test=new String(t);
            if(test.equalsIgnoreCase(context.getResources().getString(R.string.parentheseO))
                    || test.equalsIgnoreCase(context.getResources().getString(R.string.parentheseF))){

                if(test.equalsIgnoreCase(context.getResources().getString(R.string.parentheseO))) ouvre++;
                else ferme++;
            }
        }

        System.out.println("COmpte Parenthese voici mon retour->"+ouvre+"_"+ferme);
        return ouvre+"_"+ferme;
    }


    public String extraitChaine(String chaine){

        String result="";
        int ouvre=0;
        int ferme=0;
        boolean trouve=false;

        int last=chaine.length();

        //recherche des parentheses
        for(int i=0;i<last;i++){

            char[] t={chaine.charAt(i)};
            String test=new String(t);

            if(i!=0 && i!=(last-1)){

                result=result+chaine.charAt(i);
            }

            if(test.equalsIgnoreCase(context.getResources().getString(R.string.parentheseO)) ||
                    test.equalsIgnoreCase(context.getResources().getString(R.string.parentheseF))){

                if(test.equalsIgnoreCase(context.getResources().getString(R.string.parentheseO))){
                    trouve=true;
                    ouvre++;
                }
                else ouvre--;
            }

            if(trouve && ouvre==0) break;
        }
        System.out.println("ExtraitChaine, voici mon retour->"+result);
        return result;
    }


    public int trouvePosFermante(String chaine,int maPos){

        int result=maPos;
        int ouvre=0;
        int ferme=0;
        boolean trouve=false;

        int last=chaine.length();

        //recherche des parentheses
        for(int i=maPos;i<last;i++){

            char[] t={chaine.charAt(i)};
            String test=new String(t);


            if(test.equalsIgnoreCase(context.getResources().getString(R.string.parentheseO)) ||
                    test.equalsIgnoreCase(context.getResources().getString(R.string.parentheseF))){

                if(test.equalsIgnoreCase(context.getResources().getString(R.string.parentheseO))){
                    trouve=true;
                    ouvre++;
                }
                else ouvre--;
            }

            if(trouve && ouvre==0){
                result=i;
                break;
            }
        }
        System.out.println("TrouveMaFermante dans chaine->"+chaine+", je suis la pos->"+maPos+" c'est � la pos->"+result);
        return result;
    }


    public int trouvePosOuvrante(String chaine,int maPos){

        int result=maPos;
        int ouvre=0;
        int ferme=0;
        boolean trouve=false;

        int last=chaine.length();

        //recherche des parentheses
        for(int i=maPos;i>=0;i--){

            char[] t={chaine.charAt(i)};
            String test=new String(t);


            if(test.equalsIgnoreCase("(") ||
                    test.equalsIgnoreCase(")")){

                if(test.equalsIgnoreCase(")")){
                    trouve=true;
                    ferme++;
                }
                else ferme--;
            }

            if(trouve && ferme==0){
                result=i;
                break;
            }
        }
        System.out.println("TrouveMaOuvrante dans chaine->"+chaine+", je suis la pos->"+maPos+" c'est � la pos->"+result);
        return result;
    }


    public String evalueModulo(String dividende,String exposan,String diviseur){

        String retour="E";

        System.out.println("EvalueModulo voici les val : \ndividende->"+dividende+" \nexposanst->"+exposant+" \ndiv->"+diviseur);
        try {
            dvse= Long.parseLong(diviseur);
            //TxtSaisie.setText("E");

            nbre= Long.parseLong(dividende);
            exposant= Long.parseLong(exposan);
            ar=new ArrayList<Long>();
            current=0;

            //TxtPreced.setText("(("+numFor.format(nbre)+")^("

            long c0 = 1, r0 = 1;

            long r0c0 = c0*r0;

            long d=dvse;

            System.out.println("FrmTableMongo3 evalue voici les operateurs->" +
                    "\ndiv->"+nbre+
                    "\nDvseur->"+d);

            if(nbre<=0 || exposant<=0 || d <=0 ){

                String txt="Les diff�rents op�rateurs doivent �tre Strictement positifs";
                //JOptionPane.showMessageDialog(this, txt, "Tableau Mongo Mongo", JOptionPane.INFORMATION_MESSAGE);

                return retour;
            }

                 /*if(nbre<=d ){

                     JOptionPane.showMessageDialog(this, "Le diviseur doit �tre inf�rieur au dividende", "Tableau Mongo Mongo", JOptionPane.INFORMATION_MESSAGE);
                     return;
                 }*/

            vect=new ArrayList<CongruLine>();



            long qi=(r0c0-r0c0%d)/d;

            long nqi=d*qi;

            long r=r0c0-nqi;
            long c=0;
            long rici=1;
            long rb=nbre%d;
            CongruLine buf=new CongruLine(0, r0, c0, r0c0, d, qi, nqi, r, nbre);
            vect.add(buf);

            for(long i=1;i<=exposant;i++){

                c=r;
                rici=rb*c;
                qi=(rici-rici%d)/d;
                nqi=d*qi;
                r=rici-nqi;

                buf=new CongruLine(i, rb, c, rici, d, qi, nqi, r, nbre);
                vect.add(buf);

            }

            //TxtSaisie.setText(numFor.format(r));
            retour= new Double(r).toString();


        } catch (Exception e) {

            System.err.println("FrmTableMongo evalueModulo error->"+e.getMessage());

        }

        String res= retour;

        if(res.endsWith(".0")){

            return res.substring(0, res.length()-2);
        }
        else{
            return res;
        }
    }

    public String calcul(String chaine,int appelNum){

        float result=0;
        int ouvre=0;


        ArrayList<Object> operand=new ArrayList<Object>();


        System.out.println("\n\nCalcul avec appelNum->"+appelNum+" et  chaine->"+chaine);
        try{


            /*******************Verrifie si contient mod pour ne pas �lever � des puissances fortes******************/

            if(chaine.contains("M")){

                int posM=chaine.indexOf("M");

                boolean trouve=false;
                String test="^";
                String finF=")";
                String finO="(";
                String valExpo="";
                int i=posM-1;
                String courant="";
                String valPos="0123456789";

                while(!trouve && !test.equalsIgnoreCase(courant) && !finF.equalsIgnoreCase(courant)
                        && !finO.equalsIgnoreCase(courant) && i>=0){

                    i--;
                    if (i>=0) {
                        courant = chaine.substring(i, i + 1);
                    }
                    System.out.println("calcul, cas Mod, voici valExpo->"+valExpo+" pour i->"+i+" courant->"+courant);
                    if(valPos.contains(courant)){

                        valExpo=courant+valExpo;

                        System.out.println("calcul, cas Mod, voici valExpo->"+valExpo+" pour i->"+i);
                    }

                    if(courant.equalsIgnoreCase(test)){

                        trouve=true;
                        //System.out.println("calcul, cas Mod, voici valExpo->"+valExpo+" trouve expo � la pos i->"+i);
                    }



                }

                if(trouve){

                    System.out.println("calcul expo a �t� trouve � la pos->"+i);

                    String div=calcul(chaine.substring(trouvePosOuvrante(chaine, i-1), i),appelNum+1);

                    i=posM+4;
                    courant=chaine.substring(i, i+1);
                    String dseur=courant;
                    i++;
                    while(valPos.contains(courant) && i<chaine.length()){

                        courant=chaine.substring(i, i+1);
                        if (valPos.contains(courant)) {
                            dseur = dseur + courant;
                        } else {

                            break;
                        }

                        i++;


                    }

                    return evalueModulo(div, valExpo, dseur);
                }
                else{//on n'a pas � elever � une puissance forte

                    System.out.println("calcul expo n'a pas �t� trouve � la pos->"+i);

                    String div=calcul(chaine.substring(trouvePosOuvrante(chaine, posM-1), posM),appelNum+1);
                    valExpo="1";

                    i=posM+4;
                    courant=chaine.substring(i, i+1);
                    String dseur=courant;
                    i++;
                    while(valPos.contains(courant) && i<chaine.length()){

                        courant=chaine.substring(i, i+1);
                        if (valPos.contains(courant)) {
                            dseur = dseur + courant;
                        } else {

                            break;
                        }

                        i++;


                    }


                    return evalueModulo(div, valExpo, dseur);
                }
            }
            int last=chaine.length();
            String buf="";
            //recherche des parentheses
            for(int i=0;i<last;i++){

                char[] t={chaine.charAt(i)};
                String test=new String(t);
                System.out.println("Calcul avec appelNum->"+appelNum+" et   chaine pos 2 avec i->"+i+" char->"+test);
                if(!test.equalsIgnoreCase("(") &&
                        !test.equalsIgnoreCase(")") &&
                        !test.equalsIgnoreCase("+") &&
                        !test.equalsIgnoreCase("-") &&
                        !test.equalsIgnoreCase("*") &&
                        !test.equalsIgnoreCase("/") &&
                        !test.equalsIgnoreCase("%") &&
                        !test.equalsIgnoreCase("^") &&
                        !test.equalsIgnoreCase("M")){


                    buf=buf+chaine.charAt(i);

                    System.out.println("Calcul avec appelNum->"+appelNum+" et   chaine pos 20 avec i->"+i+" char->"+test+" et buf->"+buf);

                    if(i+1==last){
                        System.out.println("Calcul avec appelNum->"+appelNum+" et   chaine pos 201 avec i->"+i+" char->"+test+" et buf->"+buf);
                        if (buf!="") {
                            operand.add(buf);
                            buf = "";
                        }
                    }


                }
                else if(test.equalsIgnoreCase("+") ||
                        test.equalsIgnoreCase("-") ||
                        test.equalsIgnoreCase("*") ||
                        test.equalsIgnoreCase("/") ||
                        test.equalsIgnoreCase("%") ||
                        test.equalsIgnoreCase("^") ||
                        test.equalsIgnoreCase("M")){

                    System.out.println("Calcul avec appelNum->"+appelNum+" et   chaine pos 21 avec i->"+i+" char->"+test);
                    if (buf!="") {
                        operand.add(buf);
                        buf = "";
                    }
                    char[] tab={chaine.charAt(i)};
                    operand.add(new String(tab));
                    System.out.println("Calcul avec appelNum->"+appelNum+" et   chaine pos 21 apres insert operateur avec i->"+i+" char->"+test);
                }
                else if(test.equalsIgnoreCase("(") ){

                    System.out.println("Calcul avec appelNum->"+appelNum+" et   chaine pos 22 avec i->"+i+" char->"+test);
                    int dep=i;
                    i=trouvePosFermante(chaine,i);
                    int newPos=i;
                    //operand.add(calcul(extraitChaine(chaine.substring(dep,newPos))));
                    operand.add(calcul(chaine.substring(dep+1,newPos),appelNum+1));
                    if (buf!="") {
                        operand.add(buf);
                        buf = "";
                    }





                }
                else if(test.equalsIgnoreCase(")") ){

                    System.out.println("Calcul avec appelNum->"+appelNum+" et   chaine pos 23 avec i->"+i+" char->"+test);
                    //operand.add(calcul(extraitChaine(chaine.substring(i))));
                    if (buf!="") {
                        operand.add(buf);
                        buf = "";
                    }
                }





            }

            System.out.println("\nCalcul avec appelNum->"+appelNum+" et    chaine pos 3 avec taille de operand->"+operand.size());

            //nous allons effectuer les op�rations en fonction de la priorit� des operateur
            System.out.println("\nCalcul avec appelNum->"+appelNum+" et   chaine pos EXPO avant for");

            //Gestion des operateurs unaires Exple: %
            for(int i=0;i<operand.size();i++){
                System.out.println("\nCalcul avec appelNum->"+appelNum+" et   chaine pos EXPO avec i->"+i);
                String op=(String) operand.get(i);
                System.out.println("\nCalcul avec appelNum->"+appelNum+" et   chaine pos EXPO avec i->"+i+" char->"+op);
                if(op.equalsIgnoreCase("%")){

                    String op1=(String) operand.get(i-1);
                    String op2=""+100;
                    System.out.println("\nCalcul avec appelNum->"+appelNum+" et   chaine pos EXPO avec i->"+i+" char->"+op+" op1->"+op1+" op2->"+op2);
                    //float res=new Float(op1).floatValue()*new Float(op2).floatValue();

                    //double res = Math.pow(Double.valueOf(op1),Double.valueOf(op2));
                    double res = Double.valueOf(op1)/Double.valueOf(op2);

                    operand.set(i-1, new Double(res).toString());

                    //operand.remove(i+1);
                    operand.remove(i);
                    i=0;


                }
            }


            //Gestion des Expo
            for(int i=0;i<operand.size();i++){
                System.out.println("\nCalcul avec appelNum->"+appelNum+" et   chaine pos EXPO avec i->"+i);
                String op=(String) operand.get(i);
                System.out.println("\nCalcul avec appelNum->"+appelNum+" et   chaine pos EXPO avec i->"+i+" char->"+op);
                if(op.equalsIgnoreCase("^")){

                    String op1=(String) operand.get(i-1);
                    String op2=(String) operand.get(i+1);
                    System.out.println("\nCalcul avec appelNum->"+appelNum+" et   chaine pos EXPO avec i->"+i+" char->"+op+" op1->"+op1+" op2->"+op2);
                    //float res=new Float(op1).floatValue()*new Float(op2).floatValue();

                    double res = Math.pow(Double.valueOf(op1),Double.valueOf(op2));

                    operand.set(i-1, new Double(res).toString());

                    operand.remove(i+1);
                    operand.remove(i);
                    i=0;


                }
            }



            //Gestion des Multiplications
            for(int i=0;i<operand.size();i++){

                String op=(String) operand.get(i);

                if(op.equalsIgnoreCase("*")){

                    String op1=(String) operand.get(i-1);
                    String op2=(String) operand.get(i+1);
                    System.out.println("Calcul avec appelNum->"+appelNum+" et   chaine pos multi avec i->"+i+" char->"+op+" op1->"+op1+" op2->"+op2);
                    //float res=new Float(op1).floatValue()*new Float(op2).floatValue();

                    double res = Double.valueOf(op1)*Double.valueOf(op2);

                    operand.set(i-1, new Double(res).toString());

                    operand.remove(i+1);
                    operand.remove(i);
                    i=0;


                }
            }

            //Gestion des Divisions
            for(int i=0;i<operand.size();i++){

                String op=(String) operand.get(i);

                if(op.equalsIgnoreCase("/")){
                    String op1=(String) operand.get(i-1);
                    String op2=(String) operand.get(i+1);
                    System.out.println("Calcul avec appelNum->"+appelNum+" et   chaine pos DIVISION avec i->"+i+" char->"+op+" op1->"+op1+" op2->"+op2);
                    //float res=new Float(op1).floatValue()*new Float(op2).floatValue();

                    double res=0;
                    if (op2!="0") {
                        res = Double.valueOf(op1) / Double.valueOf(op2);
                    }

                    operand.set(i-1, new Double(res).toString());

                    operand.remove(i+1);
                    operand.remove(i);
                    i=0;


                }
            }


            //Gestion des Additions
            for(int i=0;i<operand.size();i++){

                String op=(String) operand.get(i);

                if(op.equalsIgnoreCase("+")){
                    String op1=(String) operand.get(i-1);
                    String op2=(String) operand.get(i+1);
                    System.out.println("Calcul avec appelNum->"+appelNum+" et   chaine pos ADDITIONS 0 avec i->"+i+" char->"+op+" op1->"+op1+" op2->"+op2);
                    //float res=new Float(op1).floatValue()*new Float(op2).floatValue();

                    double res= Double.valueOf(op1)+ Double.valueOf(op2);

                    System.out.println("Calcul avec appelNum->"+appelNum+" et   chaine pos ADDITIONS 1 avec i->"+i+" char->"+op+" op1->"+op1+" op2->"+op2);

                    operand.set(i-1, new Double(res).toString());
                    System.out.println("Calcul avec appelNum->"+appelNum+" et   chaine pos ADDITIONS 2 avec i->"+i+" char->"+op+" op1->"+op1+" op2->"+op2);

                    operand.remove(i+1);
                    operand.remove(i);
                    i=0;
                    System.out.println("Calcul avec appelNum->"+appelNum+" et   chaine pos ADDITIONS 3 avec i->"+i+" char->"+op+" op1->"+op1+" op2->"+op2);



                }
            }



            //Gestion des Soustractions
            for(int i=0;i<operand.size();i++){

                String op=(String) operand.get(i);

                if(op.equalsIgnoreCase("-")){
                    String op1=(String) operand.get(i-1);
                    String op2=(String) operand.get(i+1);
                    System.out.println("Calcul avec appelNum->"+appelNum+" et   chaine pos SOUSTRACTIONS avec i->"+i+" char->"+op+" op1->"+op1+" op2->"+op2);
                    //float res=new Float(op1).floatValue()*new Float(op2).floatValue();

                    double res= Double.valueOf(op1)- Double.valueOf(op2);


                    operand.set(i-1, new Double(res).toString());

                    operand.remove(i+1);
                    operand.remove(i);
                    i=0;



                }
            }



        }
        catch(Exception exp){

            System.err.println("Calcul appelNum->"+appelNum+" et   Error->"+exp);
        }

        System.out.println("Calcul voici appelNum->"+appelNum+" et   mon retour->"+operand.get(0)+" avec taille de operand->"+operand.size());
        //return Double.valueOf((String)operand.get(0));
        String res= operand.get(0).toString();

        if(res.endsWith(".0")){

            return res.substring(0, res.length()-2);
        }
        else{
            return res;
        }
    }


    private   void saveResultCalcul(String result){


        SharedPreferences pref=context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        SharedPreferences.Editor editor=pref.edit();

        //int nbreHandle=pref.getInt(NBRE_NOTIF_HANDLE,0)+nbre;
        editor.putString(RESULT_CALCUL_VAR_NAME,result);
        editor.commit();

        Toast.makeText(context,context.getText(R.string.result_saved),Toast.LENGTH_LONG).show();

    }

    private   double getResultCalcul(){


        SharedPreferences pref=context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        SharedPreferences.Editor editor=pref.edit();

        double val=Double.parseDouble(pref.getString(RESULT_CALCUL_VAR_NAME,"0"));

        Toast.makeText(context,context.getText(R.string.result_restored),Toast.LENGTH_LONG).show();
        return val;

    }


    @Override
    public void onClick(View v) {

        Vibrator vb = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vb.vibrate(20);

        String tag=(String) v.getTag(-1);
        //switch (v.getId()){
        switch (Integer.parseInt(tag)){

            //case R.id.egal:
            case MonMoteur.EGAL_ID:


                String init=afficheur.getText().toString();
                String buf=saisie.getText().toString();


                afficheur.setText(init+buf+"=");
                String val=calcul(init+buf,0);
                saisie.setText(val);

                saveResultCalcul(val);
                break;


            //case R.id.rm:
            case MonMoteur.RM_ID:


                //Log.e("MonMoteur","MonMoteur voici rm id->"+R.id.rm);
                saisie.setText(""+getResultCalcul());
                break;
        }
    }
}
