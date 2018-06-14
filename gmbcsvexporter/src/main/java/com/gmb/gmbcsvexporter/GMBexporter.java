package com.gmb.gmbcsvexporter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.opencsv.CSVWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by GMB on 10/15/2016.
 */

public class GMBexporter {

    public static  String DEFAULT_EXPORT_DIR;
    public static  String FILE_EXTENSION=".csv";
    
    private Context context;
    private String exported_file_name;
    private String exported_title;
    private boolean openAfterExport;
    private ArrayList<entityToExport> listToExport;
    private onExportListner mlistner;
    private JSONObject json;


    
    public GMBexporter(Context con, onExportListner listner, String dirPath, String fileName, JSONObject json, String title, boolean openAfterExport){
        DEFAULT_EXPORT_DIR=dirPath;
        context=con;
        StringTokenizer tokenizer=new StringTokenizer(fileName,".");
        exported_file_name=tokenizer.nextToken()+FILE_EXTENSION;
        exported_title=title;
        this.openAfterExport=openAfterExport;
        this.mlistner=listner;
        this.json=json;

        //listFromJson(json);
    }

    public void exportTitle(String ...args){


    }



    public void exportData(){

        ExportDataToCSVTask exp=new ExportDataToCSVTask();
        exp.execute("first param");

    }



    private  void createWaitTime(long waitTime){

        long debut=System.currentTimeMillis();
        long actual=System.currentTimeMillis();

        while (actual-debut<waitTime){

            actual=System.currentTimeMillis();
        }
    }






    /*****************************Background class qui s'occupe de l'export***********************************/

    private class ExportDataToCSVTask extends AsyncTask<String, String, String> {
        private final ProgressDialog dialog = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {

            this.dialog.setMessage("Exporting Data to CSV file...");
            this.dialog.show();

            if(mlistner!=null){

                mlistner.onPreExport(DEFAULT_EXPORT_DIR,exported_file_name,openAfterExport);
            }

        }

        private String[] getValFromJson(JSONObject json,String[] title){

            if(json==null || title==null) return new String[]{};

            int tail=title.length;
            String[] res=new String[tail];

            for(int i=0;i<tail;i++){

                try {
                    res[i]=json.getString(title[i]);
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }

            return res;

        }

        private String[] getSimpleArrayFromJsonArray(JSONArray jsonArr){

            if(jsonArr==null) return new String[]{};

            int tail=jsonArr.length();
            String[] title=new String[tail];

            try{

                for(int i=0;i<tail;i++){

                    title[i]=jsonArr.getString(i);
                }
            }
            catch (Exception ex){

                title=null;
            }

            return title;

        }

        protected String doInBackground(final String... args) {

/*
            // String csv = android.os.Environment.getExternalStorageDirectory().getAbsolutePath()+ File.pathSeparator+"ASCdata"+File.pathSeparator+"ASCinfo.csv";
            File root = Environment.getExternalStorageDirectory();
            //File dir    =   new File (root.getAbsolutePath() + File.pathSeparator+"ASCdata"+File.pathSeparator);
            File dir = new File("sdcard/ADEdata");
            if (!dir.exists()) dir.mkdirs();

            String csv = "/sdcard/ADEdata/ADEinfo.csv";
            System.out.println("Main this is CSV to write->" + csv);
            CSVWriter writer = new CSVWriter(new FileWriter(csv), ',');*/

            /*File exportDir = new File(Environment.getExternalStorageDirectory(), "ADEdata");

            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }

            File file = new File(exportDir, "ADEinfo.csv");*/

            //SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(context);

            //String fileName = (shared.getString(getString(R.string.expo_file_name), "ADEinfo"));


            //File dir = new File("sdcard/ADEdata");
            //File dir = new File(Environment.getExternalStorageDirectory(),DEFAULT_EXPORT_DIR);
            File dir = new File(DEFAULT_EXPORT_DIR);
            if (!dir.exists()) dir.mkdirs();

            //String csv = "/sdcard/ADEdata/"+fileName+".csv";
            String csv = dir.getAbsolutePath()+File.separator+exported_file_name;


            try {

                //if(file.exists()) file.delete();


                // file.createNewFile();

                //CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
                CSVWriter csvWrite = new CSVWriter(new FileWriter(csv), ',');

            /*//ormlite core method
            List<person> listdata=dbhelper.GetDataPerson();
            Person person=null;

            // this is the Column of the table and same for Header of CSV file
            String arrStr1[] ={"First Name", "Last Name", "Address", "Email"};
            csvWrite.writeNext(arrStr1);*/

                //TODO : check if for each single entity, the title tab and value tab get the same size.

                JSONArray jsonArr= json.getJSONArray("list");

                String[] title=getSimpleArrayFromJsonArray(json.getJSONArray("listTitle"));
                String[] metaData=getSimpleArrayFromJsonArray(json.getJSONArray("listMeta"));



                if (jsonArr != null && jsonArr.length() > 0) {

                    //on met la title first
                    String[] bufV=getValFromJson(new JSONObject(jsonArr.getString(0)),title);

                    if(title.length!=bufV.length) return "0-"+context.getString(R.string.title_value_array_size);

                    String[] titleExport=new String[]{exported_title};
                    csvWrite.writeNext(titleExport);

                    if(metaData!=null && metaData.length>0){

                        //On write touts les metaData si exist
                        int tail=metaData.length;
                        for (int i=0;i<tail;i++) {

                            csvWrite.writeNext(new String[]{metaData[i]});
                            //Log.e("GMBexporter","DoInBackground, CmdList ExportToCsv voici une ligne MetaData->"+ metaData[i]);
                        }
                    }


                    csvWrite.writeNext(title);

                    //csvWrite.writeAll(dataSpeech);
                    int tail=jsonArr.length();
                    for (int i=0;i<tail;i++) {
                        JSONObject js=new JSONObject(jsonArr.getString(i));
                        //buf=listToExport.get(index).getFieldValue();
                        bufV=getValFromJson(js,title);
                        if(title.length!=bufV.length){
                            csvWrite.close();
                            return "0-"+context.getString(R.string.title_value_array_size);
                        }
                        csvWrite.writeNext(bufV);

                        //Log.e("GMBexporter","DoInBackground, CmdList ExportToCsv voici une ligne autre methode->"+ Arrays.deepToString(entity.getFieldValue()));
                    }


                }
                else return "0-"+context.getString(R.string.empty_data_to_export);

                csvWrite.close();
                return "1-"+context.getString(R.string.export_succes);
            } catch (IOException e) {
                Log.e("GMBexporter", e.getMessage(), e);
                return "0-"+context.getString(R.string.export_failed);
            } catch (Exception e) {
                Log.e("GMBexporter", e.getMessage(), e);
                return "0-"+context.getString(R.string.export_failed);

            }
        }

        protected String doInBackgroundOld(final String... args) {

/*
            // String csv = android.os.Environment.getExternalStorageDirectory().getAbsolutePath()+ File.pathSeparator+"ASCdata"+File.pathSeparator+"ASCinfo.csv";
            File root = Environment.getExternalStorageDirectory();
            //File dir    =   new File (root.getAbsolutePath() + File.pathSeparator+"ASCdata"+File.pathSeparator);
            File dir = new File("sdcard/ADEdata");
            if (!dir.exists()) dir.mkdirs();

            String csv = "/sdcard/ADEdata/ADEinfo.csv";
            System.out.println("Main this is CSV to write->" + csv);
            CSVWriter writer = new CSVWriter(new FileWriter(csv), ',');*/

            /*File exportDir = new File(Environment.getExternalStorageDirectory(), "ADEdata");

            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }

            File file = new File(exportDir, "ADEinfo.csv");*/

            //SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(context);

            //String fileName = (shared.getString(getString(R.string.expo_file_name), "ADEinfo"));


            //File dir = new File("sdcard/ADEdata");
            //File dir = new File(Environment.getExternalStorageDirectory(),DEFAULT_EXPORT_DIR);
            File dir = new File(DEFAULT_EXPORT_DIR);
            if (!dir.exists()) dir.mkdirs();

            //String csv = "/sdcard/ADEdata/"+fileName+".csv";
            String csv = dir.getAbsolutePath()+File.separator+exported_file_name;


            try {

                //if(file.exists()) file.delete();


                // file.createNewFile();

                //CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
                CSVWriter csvWrite = new CSVWriter(new FileWriter(csv), ',');

            /*//ormlite core method
            List<person> listdata=dbhelper.GetDataPerson();
            Person person=null;

            // this is the Column of the table and same for Header of CSV file
            String arrStr1[] ={"First Name", "Last Name", "Address", "Email"};
            csvWrite.writeNext(arrStr1);*/

                //TODO : check if for each single entity, the title tab and value tab get the same size.


                if (listToExport != null && listToExport.size() > 0) {
                    int dataTail=listToExport.size();
                    //on met la title first
                    String[] bufT=listToExport.get(0).getFieldTitle();
                    String[] bufV=listToExport.get(0).getFieldValue();

                    if(bufT.length!=bufV.length) return "0-"+context.getString(R.string.title_value_array_size);

                    String[] titleExport=new String[]{exported_title};
                    csvWrite.writeNext(titleExport);

                    csvWrite.writeNext(bufT);

                    //csvWrite.writeAll(dataSpeech);
                    for (entityToExport entity:listToExport) {
                        //buf=listToExport.get(index).getFieldValue();
                        csvWrite.writeNext(entity.getFieldValue());

                        //Log.e("GMBexporter","DoInBackground, CmdList ExportToCsv voici une ligne autre methode->"+ Arrays.deepToString(entity.getFieldValue()));
                    }

                    createWaitTime(ExportParam.WAIT_TIME);

                }
                else return "0-"+context.getString(R.string.empty_data_to_export);

                csvWrite.close();

                return "1-"+context.getString(R.string.export_succes);
            } catch (IOException e) {
                Log.e("GMBexporter", e.getMessage(), e);
                return "0-"+context.getString(R.string.export_failed);
            }
        }

        @Override
        protected void onPostExecute(final String result) {

            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }

            if(mlistner!=null){


                mlistner.onPostExport(DEFAULT_EXPORT_DIR,exported_file_name,openAfterExport,result);
            }


        }

    }


    public interface onExportListner {

        public void onPreExport(String dir,String file,boolean openAfterExport);

        public void onPostExport(String dir,String file,boolean openAfterExport,String exportResult);
    }







    


/*********************************Class pour send un email en background*****************************************************************/

   /* private class SendEmailInBack extends AsyncTask<String, Boolean, Boolean> {
        private final ProgressDialog dialog = new ProgressDialog(CmdList.this);

        @Override
        protected void onPreExecute() {

            this.dialog.setMessage("Please Wait while Sending Email...");

            this.dialog.show();

        }

        protected Boolean doInBackground(final String... args) {





            File dir = new File("sdcard/ADEdata");
            if (!dir.exists()) dir.mkdirs();



            try {


                SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(context);
                String emailTo = (shared.getString(getString(R.string.email_to_name), ""));
                String subj = (shared.getString(getString(R.string.email_subject_name), "CSV data"));


                String msg = (shared.getString(getString(R.string.email_msg_name), "Exported data as CSV"));
                String fileName = (shared.getString(getString(R.string.expo_file_name), "ADEinfo"));

                fileName=fileName+".csv";

                File file = new File(dir,fileName);

                if(!emailTo.isEmpty() && !subj.isEmpty() && !msg.isEmpty() && file.exists()){

                    Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                    emailIntent.setType("vnd.android.cursor.dir/email");
                    emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{emailTo});
                    emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subj);
                    emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, msg);
                    emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                    startActivity(Intent.createChooser(emailIntent, "Use one App below to send your email..."));
                }
                else{
                    return false;
                }





                return true;
            } catch (Exception e) {
                Log.e("GMBexporter", e.getMessage(), e);
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }

            Toast toast;
            if (success) {

                toast=Toast.makeText(context,"Choose the email address to use!",Toast.LENGTH_LONG);


            } else {

                toast=Toast.makeText(context,"Email failed!",Toast.LENGTH_LONG);


            }
            toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }

    }*/








}
