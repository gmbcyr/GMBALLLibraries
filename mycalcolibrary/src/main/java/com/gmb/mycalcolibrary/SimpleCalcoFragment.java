package com.gmb.mycalcolibrary;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * {@link SimpleCalcoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SimpleCalcoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SimpleCalcoFragment extends DialogFragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

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

    public SimpleCalcoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SimpleCalcoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SimpleCalcoFragment newInstance(String param1, String param2) {
        SimpleCalcoFragment fragment = new SimpleCalcoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_simple_calco, container, false);


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
        
        
        
        return rootView;
    }


    public void closeFragment(){

        //getFragmentManager().popBackStack();

        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();

       //getActivity().finish();


        /*Intent intent = new Intent(getApplicationContext(), FirstActivity.class);
intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
intent.putExtra("EXIT", true);
startActivity(intent);






            if (getIntent().getExtras() != null && getIntent().getExtras().getBoolean("EXIT", false)) {
    finish();
}*/
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
           // mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    public interface OnFragmentInteractionListener {

        void onCalcClose(double val);
    }
}
