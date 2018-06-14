package com.gmb.gmbcsvexporter;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gmb.gmbcsvexporter.dirselector.MyDirChooser;
import com.gmb.gmbcsvexporter.dirselector.MyFileSelectListner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ExportParam.OnExportParamListner} interface
 * to handle interaction events.
 * Use the {@link ExportParam#} factory method to
 * create an instance of this fragment.
 */
public class ExportParam extends AlertDialog {
    // TODOdone: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ALL_INFOS = "allInfos";
    private static final String ARG_ID = "id";
    public static  long WAIT_TIME = 1000;

    // TODOdone: Rename and change types of parameters
    private boolean allInfos,isNotif,isCat,groupByStatement;
    private long id;
    Context context;

    private OnExportParamListner mListener;
    public static final String EXPORT_FILE_PREF_NAME = "com.gmb.exporter.";


    EditText txtName;
    TextView txtDir;
    CheckBox chkOpen;
    LinearLayout tblField;

    Button cmdSave,cmdCancel;
    ArrayList<entityToExport> listToExport;
    String exportTitle;
    String idExport;
    boolean useGeneralParam=true;
    int PRIVATE_MODE = 0;
    ArrayList<Integer> listIndexSelect;
    String[] metaData;

    boolean isDataAvailable=false;



    private View.OnClickListener selectField=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(listIndexSelect==null) listIndexSelect=new ArrayList<>();

            CheckBox cb=(CheckBox) tblField.findViewById(v.getId());

            if(cb==null) return;

            if(cb.isChecked()){

                if(!listIndexSelect.contains(cb.getId())) listIndexSelect.add(cb.getId());
            }
            else{

                if(listIndexSelect.contains(cb.getId())) listIndexSelect.remove(listIndexSelect.indexOf(cb.getId()));
            }
        }
    };



    public ExportParam(Context context, OnExportParamListner listner, String idExport) {

        super(context);
        this.context=context;

        this.idExport=idExport;
        this.mListener=listner;

        this.setView(this.initView());
    }


    public ExportParam(Context context, OnExportParamListner listner, String idExport,ArrayList<entityToExport> list, String exportTitle) {

        super(context);
        this.context=context;

        this.idExport=idExport;
        this.mListener=listner;

        setMyData(list,exportTitle,null);

        this.setView(this.initView());
    }


    public ExportParam(Context context, OnExportParamListner listner, String idExport,ArrayList<entityToExport> list, String exportTitle,String[] metaData) {

        super(context);
        this.context=context;

        this.idExport=idExport;
        this.mListener=listner;

        setMyData(list,exportTitle,metaData);

        this.setView(this.initView());
    }

    private void setMyData(ArrayList<entityToExport> list, String exportTitle,String[] metaData){

        this.exportTitle=exportTitle;
        this.listToExport=(list!=null)? list:new ArrayList<entityToExport>();
        this.metaData=(metaData!=null)? metaData:new String[]{};

        listIndexSelect=new ArrayList<>();

        if(listToExport.size()>0){

            int size=listToExport.get(0).getFieldTitle().length;
            if(listToExport!=null && size>0){

                for(int i=0;i<size;i++){
                    listIndexSelect.add(i);
                }

                isDataAvailable=true;
            }

            loadParam();
        }
        else{

            isDataAvailable=false;
        }
    }

    private boolean isUseGeneralParam(){

        SharedPreferences pref = context.getSharedPreferences(EXPORT_FILE_PREF_NAME+idExport, PRIVATE_MODE);

        if(pref!=null && !pref.getBoolean(getContext().getString(R.string.default_pref_use_gen_export_param),true))
            useGeneralParam=false;
        else
            useGeneralParam=true;


        return useGeneralParam;
    }

    public View initView() {
        // Inflate the layout for this fragment
        Context themeContext = this.getContext();
        LayoutInflater inflater = LayoutInflater.from(themeContext);
        View rootView = inflater.inflate(R.layout.exporter_params, null);



        txtName = (EditText) rootView.findViewById(R.id.txt_file_name);
        txtDir = (TextView) rootView.findViewById(R.id.txt_file_dir);
        chkOpen=(CheckBox) rootView.findViewById(R.id.chk_open);
        //horizontalScroll=(HorizontalScrollView) rootView.findViewById(R.id.scroll_field);
        tblField=(LinearLayout) rootView.findViewById(R.id.lyt_field_dispo);

        isUseGeneralParam();

        cmdSave=(Button) rootView.findViewById(R.id.cat_cmdSave);
        cmdCancel=(Button) rootView.findViewById(R.id.cat_cmdCancel);


        loadParam();


        txtDir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDirSelector();
            }
        });

        cmdSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });
        cmdCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cancel();
            }
        });

        return rootView;
    }


    public boolean isDataAvailable() {
        return isDataAvailable;
    }

    public void setDataAvailable(boolean dataAvailable) {
        isDataAvailable = dataAvailable;
    }

    private  JSONObject dataToJson(){



        if(listToExport==null || listToExport.size()==0) return null;

        JSONObject jsOb=new JSONObject();




        try{


            jsOb.put("idExport",idExport);
            jsOb.put("fileName", txtName.getText().toString());
            jsOb.put("fileDir", txtDir.getText().toString());

            ArrayList<String> lst=new ArrayList<>();

            for (entityToExport c :listToExport) {
                JSONObject js=getSelectedFields(c);
                if(js!=null) lst.add(js.toString());
            }

            JSONArray jArr=new JSONArray(lst);

            jsOb.putOpt("list",jArr);
            jsOb.putOpt("listTitle",getSelectedFieldsTitle(listToExport.get(0)));
            jsOb.putOpt("listMeta",getMetaAsJsonArray());


        }
        catch (Exception e){

            jsOb=null;
            //System.out.println("ShowCaseCell objectToJson error->"+e.getMessage());
        }




        return jsOb;
    }


    private  JSONArray getSelectedFieldsTitle(entityToExport entity){




        JSONArray jArr;


       /* private int displayOrder=1;
    private int displayTime=5;
    private boolean showOk=true;
    String okPosition="24-24";*/


        try{


            int tail=entity.getFieldValue().length;
            ArrayList<String> lst=new ArrayList<>();

            for(int i=0;i<tail;i++){

                if(listIndexSelect.contains(i)){

                    lst.add(entity.getFieldTitle()[i]);
                }
            }

             jArr=new JSONArray(lst);


        }
        catch (Exception e){

            jArr=null;
            //System.out.println("ShowCaseCell objectToJson error->"+e.getMessage());
        }




        return jArr;
    }


    private  JSONArray getMetaAsJsonArray(){




        JSONArray jArr;


       /* private int displayOrder=1;
    private int displayTime=5;
    private boolean showOk=true;
    String okPosition="24-24";*/

        if(metaData==null) return null;

        try{


            int tail=metaData.length;
            ArrayList<String> lst=new ArrayList<>();

            for(int i=0;i<tail;i++){

                lst.add(metaData[i]);

            }

            jArr=new JSONArray(lst);


        }
        catch (Exception e){

            jArr=null;
            //System.out.println("ShowCaseCell objectToJson error->"+e.getMessage());
        }




        return jArr;
    }


    private  JSONObject getSelectedFields(entityToExport entity){




        JSONObject jsOb=new JSONObject();


       /* private int displayOrder=1;
    private int displayTime=5;
    private boolean showOk=true;
    String okPosition="24-24";*/


        try{


            int tail=entity.getFieldValue().length;

            for(int i=0;i<tail;i++){

                if(listIndexSelect.contains(i)){

                    jsOb.put(entity.getFieldTitle()[i],entity.getFieldValue()[i]);
                }
            }


        }
        catch (Exception e){

            jsOb=null;
            //System.out.println("ShowCaseCell objectToJson error->"+e.getMessage());
        }




        return jsOb;
    }

    private void saveCurrentParam(){


        SharedPreferences pref = context.getSharedPreferences(EXPORT_FILE_PREF_NAME+idExport, PRIVATE_MODE);
        SharedPreferences.Editor editor=pref.edit();


        editor.putBoolean(getContext().getString(R.string.default_pref_use_gen_export_param),false);
        editor.putBoolean(getContext().getString(R.string.chk_default_pref_open_after_export),chkOpen.isChecked());

        editor.putString(getContext().getString(R.string.txt_default_file_name),txtName.getText().toString());
        editor.putString(getContext().getString(R.string.txt_default_file_dir),txtDir.getText().toString());

        int size=listIndexSelect.size();
        StringBuilder builder=new StringBuilder();
        String[] title=listToExport.get(0).getFieldTitle();
        for(int i=0;i<size;i++){

            builder.append(title[listIndexSelect.get(i)]+context.getString(R.string.my_title_separator));
        }

        editor.putString(getContext().getString(R.string.txt_default_fields_selected),builder.toString());


        editor.commit();





    }



    private void loadParam(){




        SharedPreferences pref;
        if(useGeneralParam){

            pref= PreferenceManager.getDefaultSharedPreferences(getContext());

        }
        else{

            pref = context.getSharedPreferences(EXPORT_FILE_PREF_NAME+idExport, PRIVATE_MODE);
        }


        txtName.setText(pref.getString(getContext().getString(R.string.txt_default_file_name),"ExportedFile"));
        txtDir.setText(pref.getString(getContext().getString(R.string.txt_default_file_dir),""));

        chkOpen.setChecked(pref.getBoolean(getContext().getString(R.string.chk_default_pref_open_after_export),true));

        selectionToView(pref.getString(getContext().getString(R.string.txt_default_fields_selected),""));


    }



    private void selectionToView(String selection){

        if(listToExport==null || listToExport.size()<1) return;

        String[] title=listToExport.get(0).getFieldTitle();

        tblField.removeAllViewsInLayout();
        boolean selectAll=false;
        if(selection==null || selection.equalsIgnoreCase("")) selectAll=true;

        LinearLayout.LayoutParams lP=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);

        int size=title.length;

        for(int i=0;i<size;i++){

            LinearLayout tr=new LinearLayout(context);
            tr.setOrientation(LinearLayout.VERTICAL);
            lP.setMargins(5,2,5,2);
            tr.setPadding(2,2,2,2);
            tr.setLayoutParams(lP);


            TextView tv=new TextView(context);
            tv.setText(title[i]);
            LinearLayout.LayoutParams lT=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0,0.6f);
            lT.setMargins(0,5,5,5);
            tv.setGravity(View.TEXT_ALIGNMENT_CENTER);
            tv.setPadding(2,2,2,2);

            lT.gravity = Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL;

            tv.setGravity(lT.gravity);
            tv.setLayoutParams(lT);

            CheckBox cb=new CheckBox(context);
            cb.setId(i);
            LinearLayout.LayoutParams lC=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0,0.4f);
            lC.setMargins(0,5,5,5);
            cb.setGravity(View.TEXT_ALIGNMENT_CENTER);
            cb.setPadding(2,2,2,2);
            cb.setChecked(selectAll || selection.contains(title[i]));
            cb.setOnClickListener(selectField);
            lC.gravity = Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL;
            cb.setGravity(lC.gravity);
            cb.setLayoutParams(lC);

            if(i%2==0){
                tr.setBackgroundColor(context.getResources().getColor(R.color.export_back1));
                //cb.setBackgroundColor(context.getResources().getColor(R.color.export_back1));
            }
            else{
                tr.setBackgroundColor(context.getResources().getColor(R.color.export_back2));
                //cb.setBackgroundColor(context.getResources().getColor(R.color.export_back2));
            }
            tr.addView(tv);
            tr.addView(cb);

            tblField.addView(tr);

        }

    }






    private void onDirSelected(String dir){

        txtDir.setText(dir);
    }

    private void showDirSelector(){

        MyFileSelectListner listne=new MyFileSelectListner() {
            @Override
            public void onChosenDir(String dirChoose) {

                onDirSelected(dirChoose);
            }
        };

        MyDirChooser dirC=new MyDirChooser(context,listne);

        dirC.chooseDirectory("");
    }

    private void validate() {


        try{

            //String category = (String) mCategory.getSelectedItem();
            String nom = txtName.getText().toString();

            if(nom.equalsIgnoreCase("")){

                Toast.makeText(context,context.getString(R.string.must_fill_out_field),Toast.LENGTH_LONG).show();
                return;
            }

            if(txtDir.getText().toString().equalsIgnoreCase("")){

                Toast.makeText(context,context.getString(R.string.must_fill_select_dir),Toast.LENGTH_LONG).show();
                return;
            }


            if(listIndexSelect.size()<1){

                Toast.makeText(context,context.getString(R.string.must_select_least_one),Toast.LENGTH_LONG).show();
                return;
            }

            saveCurrentParam();

            GMBexporter.onExportListner exListner=new GMBexporter.onExportListner() {
                @Override
                public void onPreExport(String dir, String file, boolean openAfterExport) {

                    if(mListener!=null){

                        mListener.onPreExport(dir,file,openAfterExport,Uri.parse(dir+File.separator+file));
                    }
                }

                @Override
                public void onPostExport(String dir, String file, boolean openAfterExport, String exportResult) {

                    if(mListener!=null){

                        mListener.onPostExport(dir,file,openAfterExport,exportResult,Uri.parse(dir+File.separator+file));
                    }
                }
            };

            GMBexporter exp=new GMBexporter(getContext(),exListner,txtDir.getText().toString(),nom,dataToJson(),exportTitle,chkOpen.isChecked());
            exp.exportData();


            this.dismiss();
        }
        catch (Exception ex){


            Log.e("ExportParam","onSave error->"+ex.getMessage());
        }

    }



    public void setExportData(ArrayList<entityToExport> list, String exportTitle,String[] metaData) {


        setMyData(list,exportTitle,metaData);
    }



    private void cancelMethod() {

        //if(allInfos) getBackToList();

        //String category = (String) mCategory.getSelectedItem();
       /* String nom = txtName.getText().toString();

        if(nom.equalsIgnoreCase("")){

            Toast.makeText(getActivity().getApplicationContext(),"The name can't be empty",Toast.LENGTH_LONG).show();
            return;
        }


        DaoSession daoSession = ((MyBbmApplication) getActivity().getApplicationContext()).getDaoSession();

        CategoryDao bufDao = daoSession.getCategoryDao();

        Category buf=new Category();
        buf.setNom(nom);
        buf.setNomaffiche(nom);
        buf.setCh1("1");
        buf.setCh2("ch2");
        buf.setCh3("ch3");

        bufDao.insertOrReplace(buf);

        mListener.onMyCategoryDialog(nom);*/

        this.dismiss();

    }



    public void export(ArrayList<entityToExport> list, String exportTitle, String fileName,String[] metaDat) {

        setMyData(list,exportTitle,metaDat);
        txtName.setText(fileName);

        validate();
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnExportParamListner {
        // TODOdone: Update argument type and name
        public void onPreExport(String dir, String fileName, boolean openAfterExport, Uri file);

        //public void onExport();

        public void onPostExport(String dir, String fileName, boolean openAfterExport, String exportResult, Uri file);

    }



}
