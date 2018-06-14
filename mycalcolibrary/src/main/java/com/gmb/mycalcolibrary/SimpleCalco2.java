package com.gmb.mycalcolibrary;

import android.content.Context;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.gmb.mycalcolibrary.tools.AppliqueOperateur;
import com.gmb.mycalcolibrary.tools.CongruLine;
import com.gmb.mycalcolibrary.tools.EcritUserSaisie;
import com.gmb.mycalcolibrary.tools.EffaceAffiche;
import com.gmb.mycalcolibrary.tools.MonMoteur;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SimpleCalco2.OnCalcListener} interface
 * to handle interaction events.
 * Use the {@link SimpleCalco2#} factory method to
 * create an instance of this fragment.
 */
public class SimpleCalco2 extends AlertDialog implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnCalcListener mListener;

    long nbre=0;
    long exposant=1;
    long dvse=0;
    int current=0;
    ArrayList<Long> ar;
    ArrayList<CongruLine> vect;

    Button zero = null;
    Button doublezero = null;
    Button un = null;
    Button deux = null;
    Button trois = null;
    Button quatre = null;
    Button cinq = null;
    Button six = null;
    Button sept = null;
    Button huit = null;
    Button neuf = null;

    Button virgule = null;
    Button plus = null;
    Button egal = null;
    Button moins = null;
    Button fois = null;
    Button div = null;
    Button clear = null;
    Button back = null;
    Button expo = null;
    Button rm = null;
    Button ok = null;
    Button parentheseO = null;
    Button parentheseF = null;
    Button pourcent = null;

    TextView afficheur = null;
    TextView saisie = null;

    public SimpleCalco2(Context context,SimpleCalco2.OnCalcListener listener) {

        super(context);

        setView(initView(listener));
    }






    public View initView(SimpleCalco2.OnCalcListener listener) {
        // Inflate the layout for this fragment

        mListener=listener;

        final Context themeContext = getContext();
        final LayoutInflater inflater = LayoutInflater.from(themeContext);
        final View rootView = inflater.inflate(R.layout.fragment_simple_calco, null);



        //Recup�ration des vues
        zero = (Button)rootView.findViewById(R.id.zero);
        un = (Button)rootView.findViewById(R.id.un);
        deux = (Button)rootView.findViewById(R.id.deux);
        trois = (Button)rootView.findViewById(R.id.trois);
        quatre = (Button)rootView.findViewById(R.id.quatre);
        cinq = (Button)rootView.findViewById(R.id.cinq);
        six = (Button)rootView.findViewById(R.id.six);
        sept = (Button)rootView.findViewById(R.id.sept);
        huit = (Button)rootView.findViewById(R.id.huit);
        neuf = (Button)rootView.findViewById(R.id.neuf);

        plus = (Button)rootView.findViewById(R.id.plus);
        moins = (Button)rootView.findViewById(R.id.moins);
        doublezero = (Button)rootView.findViewById(R.id.doublezero);

        virgule = (Button)rootView.findViewById(R.id.virgule);
        egal = (Button)rootView.findViewById(R.id.egal);
        div = (Button)rootView.findViewById(R.id.div);
        clear = (Button)rootView.findViewById(R.id.clear);
        back = (Button)rootView.findViewById(R.id.back);
        expo = (Button)rootView.findViewById(R.id.expo);
        rm = (Button)rootView.findViewById(R.id.rm);
        ok = (Button)rootView.findViewById(R.id.ok);
        fois = (Button)rootView.findViewById(R.id.fois);

        parentheseO = (Button)rootView.findViewById(R.id.parentheseO);
        parentheseF = (Button)rootView.findViewById(R.id.parentheseF);
        pourcent = (Button)rootView.findViewById(R.id.pourcent);

        afficheur = (TextView)rootView.findViewById(R.id.afficheur);
        saisie = (TextView)rootView.findViewById(R.id.saisie);

        //attribution des listners aux diff�rents �l�ments
        EcritUserSaisie ecritval=new EcritUserSaisie(getContext(),saisie);
        zero.setOnClickListener(ecritval);
        doublezero.setOnClickListener(ecritval);
        un.setOnClickListener(ecritval);
        deux.setOnClickListener(ecritval);
        trois.setOnClickListener(ecritval);
        quatre.setOnClickListener(ecritval);
        cinq.setOnClickListener(ecritval);
        six.setOnClickListener(ecritval);
        sept.setOnClickListener(ecritval);
        huit.setOnClickListener(ecritval);
        neuf.setOnClickListener(ecritval);

        virgule.setOnClickListener(ecritval);

        AppliqueOperateur appliqueOperateur=new AppliqueOperateur(getContext(),afficheur,saisie);

        parentheseO.setOnClickListener(appliqueOperateur);
        parentheseF.setOnClickListener(appliqueOperateur);
        moins.setOnClickListener(appliqueOperateur);
        plus.setOnClickListener(appliqueOperateur);
        fois.setOnClickListener(appliqueOperateur);
        div.setOnClickListener(appliqueOperateur);
        expo.setOnClickListener(appliqueOperateur);

        pourcent.setOnClickListener(appliqueOperateur);
        ok.setOnClickListener(this);


        EffaceAffiche effaceTout=new EffaceAffiche(getContext(),afficheur,saisie,true);

        clear.setOnClickListener(effaceTout);

        EffaceAffiche effaceLeLast=new EffaceAffiche(getContext(),afficheur,saisie,false);
        back.setOnClickListener(effaceLeLast);

        MonMoteur resultat=new MonMoteur(getContext(),afficheur,saisie);
        egal.setTag(-1,""+MonMoteur.EGAL_ID);
        egal.setOnClickListener(resultat);
        rm.setTag(-1,""+MonMoteur.RM_ID);
        rm.setOnClickListener(resultat);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        
        
        return rootView;
    }


    public void closeFragment(){

        this.dismiss();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
           // mListener.onFragmentInteraction(uri);
        }
    }




    @Override
    public void onClick(View v) {


        String val=(!saisie.getText().toString().equalsIgnoreCase(""))?saisie.getText().toString():"0";
        mListener.onCalcClose(Double.parseDouble(val));
        closeFragment();
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnCalcListener {

        void onCalcClose(double val);
    }
}
