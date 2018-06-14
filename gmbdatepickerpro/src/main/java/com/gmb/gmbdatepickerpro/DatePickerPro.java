package com.gmb.gmbdatepickerpro;


import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gmb.gmbdatepickerpro.tools.MyDatePickGestureListnerInterface;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DatePickerPro.OnDatePickedListener} interface
 * to handle interaction events.
 */
public class DatePickerPro extends AlertDialog implements View.OnClickListener,Spinner.OnItemSelectedListener
        , MyDatePickGestureListnerInterface {


    OnDatePickedListener myListener;

    private TextView txtCurrentMonth,txtDayWeek,txtDayMonth,txtYear,txtNextMonth;
    private ImageView cmdPrevMonth;
    private ImageView cmdNextMonth;
    private GridView gridCalendarView;
    private GridCellAdapter adapter;
    private Spinner cmbDate,cmbMonth;

    private boolean modePortrait=true;


    private ImageView cmdOk,cmdCancel;

    private long dateCour=0;
    private View currentDateView=null;
    private ArrayList<Integer> listYear;
    private ArrayList<String> listMonth;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    long d1=0;
    long d2=0;

    @SuppressLint("NewApi")
    private int month, year;
    @SuppressWarnings("unused")
    @SuppressLint({ "NewApi", "NewApi", "NewApi", "NewApi" })

    private static final String dateTemplate = "MMMM yyyy";

    private final SimpleDateFormat datFor = new SimpleDateFormat(dateTemplate);

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_CALLER_ID = "callerID";
    private static final String ARG_OVERDUE_AUTHORIZED = "overdue";
    private static final String ARG_DATE_START = "dateStart";

    private int callerID=0;
    private boolean acceptOverdue=false;
    private Long dateStart;

    private GregorianCalendar cal;

    /**
     * @param context The context the dialog is to run in.
     * @param listener How the parent is notified that the date is set.
     * @param year The initial year of the dialog.
     * @param monthOfYear The initial month of the dialog.
     * @param dayOfMonth The initial day of the dialog.
     */
    public DatePickerPro(Context context, DatePickerPro.OnDatePickedListener listener, int year,
                         int monthOfYear, int dayOfMonth, boolean dateOverdueAllowed) {
        super(context);

        if(listener==null) return;
        cal = new GregorianCalendar();
        cal.set(year,monthOfYear,dayOfMonth);

        myListener=listener;
        acceptOverdue=dateOverdueAllowed;



        reloadView(cal.getTimeInMillis());



    }



    /**
     * @param context The context the dialog is to run in.
     * @param listener How the parent is notified that the date is set.
     * @param currentDate represent the initial date, in long variable
     */
    public DatePickerPro(Context context, DatePickerPro.OnDatePickedListener listener, long currentDate, boolean dateOverdueAllowed) {
        super(context);

        if(listener==null) return;

        cal = new GregorianCalendar();
        cal.setTimeInMillis(currentDate);

        myListener=listener;
        acceptOverdue=dateOverdueAllowed;



        reloadView(cal.getTimeInMillis());


    }




    public void reloadView(long curDate){

        //dateCour=checkIfOverdueAllowed(curDate);
        dateCour=curDate;
        cal = new GregorianCalendar();
        cal.setTimeInMillis(dateCour);



        setView(initView());
    }


    public View initView() {
        // Inflate the layout for this fragment


        final Context themeContext = getContext();
        final LayoutInflater inflater = LayoutInflater.from(themeContext);
        final View rootView = inflater.inflate(R.layout.custom_date_picker, null);

        TextView mode = (TextView) rootView.findViewById(R.id.txt_dimen);

        if(mode!=null) modePortrait=false;


        month=cal.get(Calendar.MONTH);
        year=cal.get(Calendar.YEAR);



        cmdPrevMonth = (ImageView) rootView.findViewById(R.id.prevMonth);
        cmdPrevMonth.setOnClickListener(this);

        txtCurrentMonth = (TextView) rootView.findViewById(R.id.currentMonth);
        txtCurrentMonth.setText(datFor.format(cal.getTime()));

        cmdNextMonth = (ImageView) rootView.findViewById(R.id.nextMonth);
        cmdNextMonth.setOnClickListener(this);

        txtDayWeek = (TextView) rootView.findViewById(R.id.txt_day_week);
        cmbMonth = (Spinner) rootView.findViewById(R.id.cmb_month);
        txtDayMonth = (TextView) rootView.findViewById(R.id.txt_day_month);
        txtYear = (TextView) rootView.findViewById(R.id.txt_year);
        txtNextMonth = (TextView) rootView.findViewById(R.id.txt_next_month);

        cmdCancel = (ImageView) rootView.findViewById(R.id.cmd_cancel);
        cmdOk = (ImageView) rootView.findViewById(R.id.cmd_oK);

        cmbDate = (Spinner) rootView.findViewById(R.id.cmb_date);
        // Creating adapter for spinner
        int lastYear=cal.get(Calendar.YEAR)+100;
        listYear=new ArrayList<>();
        for(int i=1900;i<=lastYear;i++){
            listYear.add(i);
        }

        int monthCur=cal.get(Calendar.MONTH);
        int curYear=cal.get(Calendar.YEAR);
        cal.set(Calendar.MONTH,Calendar.JANUARY);
        SimpleDateFormat df=new SimpleDateFormat("MMMM");
        listMonth=new ArrayList<>();
        while (cal.get(Calendar.YEAR)==curYear){

            listMonth.add(df.format(cal.getTime()));
            cal.add(Calendar.MONTH,1);
        }
        cal.set(Calendar.YEAR,curYear);
        cal.set(Calendar.MONTH,monthCur);


        ArrayAdapter<Integer> dataAdapter = new ArrayAdapter<Integer>(getContext(), R.layout.my_spinner_view,listYear );
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(getContext(), R.layout.my_spinner_view,listMonth );

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Drop down layout style - list view with radio button
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        cmbDate.setAdapter(dataAdapter);
        cmbMonth.setAdapter(dataAdapter2);
        int spinnerPosition = dataAdapter.getPosition(cal.get(Calendar.YEAR));
        cmbDate.setSelection(spinnerPosition);
        cmbDate.setOnItemSelectedListener(this);


        spinnerPosition = dataAdapter2.getPosition(df.format(cal.getTime()));
        cmbMonth.setSelection(spinnerPosition);
        cmbMonth.setOnItemSelectedListener(this);


        dateCour=cal.getTimeInMillis();




        gridCalendarView = (GridView) rootView.findViewById(R.id.grid_calendar);


        cmdOk.setOnClickListener(this);
        cmdCancel.setOnClickListener(this);
        //txtYear.setOnClickListener(this);

// Initialised
        adapter = new GridCellAdapter(getContext(),
                R.id.lytCustomDay, dateCour,acceptOverdue,this);
        adapter.notifyDataSetChanged();
        gridCalendarView.setAdapter(adapter);

        setField(cal.getTimeInMillis());

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        txtNextMonth.setVisibility(View.GONE);
        return rootView;
    }


    private void setField(long newDate){

        //dateCour=checkIfOverdueAllowed(newDate);
        dateCour=newDate;
        //Log.e("MyEasyDatePick","setFiel called avec date->"+new Date(dateCour));
        GregorianCalendar calen=new GregorianCalendar();
        calen.setTimeInMillis(dateCour);

        SimpleDateFormat daF=new SimpleDateFormat("EEEE");

        txtDayWeek.setText(daF.format(calen.getTime()));

        daF.applyPattern("MMMM");

        int spinnerPosition = listMonth.indexOf(daF.format(calen.getTime()));
        cmbMonth.setSelection(spinnerPosition);


        daF.applyPattern("dd");

        txtDayMonth.setText(daF.format(calen.getTime()));

        daF.applyPattern("yyyy");

        spinnerPosition = listYear.indexOf(calen.get(Calendar.YEAR));
        cmbDate.setSelection(spinnerPosition);
        txtYear.setText(daF.format(calen.getTime()));

        daF.applyPattern("MMMM yyyy");

        txtCurrentMonth.setText(daF.format(calen.getTime()));


        daF.applyPattern("MMMM yyyy");
        calen.add(Calendar.MONTH,1);

        txtNextMonth.setText(daF.format(calen.getTime()));

    }




    public void close(){

        dismiss();

    }

    /**
     *
     * @param timeParam
     */
    private void setGridCellAdapterToDate(long timeParam) {

        timeParam=checkIfOverdueAllowed(timeParam,false);
        cal.setTimeInMillis(timeParam);

        adapter = new GridCellAdapter(getContext(),
                R.id.lytCustomDay, timeParam,acceptOverdue,this);
        txtCurrentMonth.setText(datFor.format(cal.getTime()));
        adapter.notifyDataSetChanged();
        gridCalendarView.setAdapter(adapter);
    }






    @Override
    public void onClick(View v) {
        if (v == cmdPrevMonth) {


            GregorianCalendar calen=new GregorianCalendar();
            calen.setTimeInMillis(getFirstDayPreviousMonth(dateCour));


            cal.set(Calendar.YEAR,calen.get(Calendar.YEAR));
            cal.set(Calendar.MONTH,calen.get(Calendar.MONTH));

            dateCour=cal.getTimeInMillis();

            //Log.d(tag, "Setting Prev Month in GridCellAdapter: " + "Month: "        + month + " Year: " + year);


            dateCour=checkIfOverdueAllowed(dateCour,true);

            setField(dateCour);
            setGridCellAdapterToDate(dateCour);

        }
        if (v == cmdNextMonth) {
            GregorianCalendar calen=new GregorianCalendar();
            calen.setTimeInMillis(getFirstDayNextMonth(dateCour));



            cal.set(Calendar.YEAR,calen.get(Calendar.YEAR));
            cal.set(Calendar.MONTH,calen.get(Calendar.MONTH));

            dateCour=cal.getTimeInMillis();

            //Log.d(tag, "Setting Prev Month in GridCellAdapter: " + "Month: "        + month + " Year: " + year);


            dateCour=checkIfOverdueAllowed(dateCour,false);

            setField(dateCour);
            setGridCellAdapterToDate(dateCour);

        }


        if(v==txtYear){

            cmbDate.setVisibility(View.VISIBLE);
            txtNextMonth.setVisibility(View.GONE);
            gridCalendarView.setVisibility(View.GONE);

            cmbDate.performClick();

            //cal.set(Calendar.YEAR,(Integer) cmbDate.getSelectedItem());
            //dateCour=cal.getTimeInMillis();

        }







        if(v==cmdCancel){
            close();
        }

        if(v==cmdOk){

            myListener.datePicked(dateCour);
            close();
        }

    }


    private long checkIfOverdueAllowed(long newDate,boolean showMessage){

        dateCour=newDate;
        if(!acceptOverdue){

            if(compareDate(dateCour,System.currentTimeMillis(),true)<=0){
                if(showMessage) Toast.makeText(getContext(),R.string.no_overdue_allowed,Toast.LENGTH_SHORT).show();
                dateCour=System.currentTimeMillis();
                cal.setTimeInMillis(dateCour);
            }
        }

        return dateCour;
    }




    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        //Log.e("MyEasyDate","onItemSelected voici le choix->"+parent.getSelectedItem());

        if(parent.getId()==R.id.cmb_date){
                cal.setTimeInMillis(dateCour);
                cal.set(Calendar.YEAR, (Integer) parent.getSelectedItem());
                dateCour = cal.getTimeInMillis();


                //cmbDate.setVisibility(View.GONE);
                //txtNextMonth.setVisibility(View.VISIBLE);
                gridCalendarView.setVisibility(View.VISIBLE);

                dateCour = checkIfOverdueAllowed(dateCour,true);

                setField(dateCour);
                setGridCellAdapterToDate(dateCour);


        }
            else if(parent.getId()==R.id.cmb_month){

                cal.setTimeInMillis(dateCour);
                cal.set(Calendar.MONTH, getMonthSelect(position));
                dateCour = cal.getTimeInMillis();


                //cmbDate.setVisibility(View.GONE);
                //txtNextMonth.setVisibility(View.VISIBLE);
                gridCalendarView.setVisibility(View.VISIBLE);

                dateCour = checkIfOverdueAllowed(dateCour,true);

                setField(dateCour);
                setGridCellAdapterToDate(dateCour);


        }

    }


    private int getMonthSelect(int choix){

        int res=cal.JANUARY;

        switch (choix){

            case 0:
                res=cal.JANUARY;
                break;

            case 1:
                res=cal.FEBRUARY;
                break;

            case 2:
                res=cal.MARCH;
                break;

            case 3:
                res=cal.APRIL;
                break;

            case 4:
                res=cal.MAY;
                break;

            case 5:
                res=cal.JUNE;
                break;

            case 6:
                res=cal.JULY;
                break;

            case 7:
                res=cal.AUGUST;
                break;

            case 8:
                res=cal.SEPTEMBER;
                break;

            case 9:
                res=cal.OCTOBER;
                break;

            case 10:
                res=cal.NOVEMBER;
                break;

            case 11:
                res=cal.DECEMBER;
                break;

        }

        return res;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

        //cmbDate.setVisibility(View.GONE);
        //txtNextMonth.setVisibility(View.VISIBLE);
        gridCalendarView.setVisibility(View.VISIBLE);
    }



    @Override
    public void setNewDate(long newDate) {

        //Log.e("MyEasyPicker ", "About to set new date->: " + new Date(newDate));
        dateCour=newDate;
        dateCour=checkIfOverdueAllowed(dateCour,false);

        setField(dateCour);
        setGridCellAdapterToDate(dateCour);
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


    public interface OnDatePickedListener {

        void datePicked(long datePicked);
    }





















    /***************************************INNER CLASS GridCellAdapter************************************************************/










    // Inner Class
    public class GridCellAdapter extends BaseAdapter implements View.OnClickListener,View.OnTouchListener {
        private static final String tag = "MyGridCellAdapter";
        private final Context _context;

        private boolean acceptOverdue=false;
        private GestureDetector mGestureDetector;
        DatePickerPro fragment;



        //private final List<String> list;
        private final List<HoldEventsForAday> list;
        private static final int DAY_OFFSET = 1;

        private int daysInMonth;
        private int currentDayOfMonth;
        private int currentWeekDay;
        private RelativeLayout lytGrillCell;
        private TextView txtCenterInfos,txtTopLeft,txtTopRight,txtBottomLeft,txtBottomRight;
        private final HashMap<String, HoldEventsForAday> eventsPerMonthMap=null;


        // Days in Current Month
        public GridCellAdapter(Context context, int textViewResourceId,
                               long newDate,boolean acceptOverdue,DatePickerPro fragment) {
            super();
            this._context = context;
            this.acceptOverdue=acceptOverdue;
            this.fragment=fragment;
            //this.list = new ArrayList<String>();
            this.list = new ArrayList<HoldEventsForAday>();
            Log.e(tag, "==> Passed in Date FOR Month: " + month + " "               + "Year: " + year);

            GregorianCalendar cal=new GregorianCalendar();
            cal.setTimeInMillis(newDate);
            setCurrentDayOfMonth(cal.get(Calendar.DAY_OF_MONTH));
            setCurrentWeekDay(cal.get(Calendar.DAY_OF_WEEK));
            /*Log.d(tag, "New Calendar:= " + cal.getTime().toString());
            Log.d(tag, "CurrentDayOfWeek :" + getCurrentWeekDay());
            Log.d(tag, "CurrentDayOfMonth :" + getCurrentDayOfMonth());*/




// Print Month
            printMonth21(newDate);


        }





        public HoldEventsForAday getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }







        private void printMonth21(long dateChoose) {

            int trailingSpaces = 0;
            int daysInPrevMonth = 0;
            int prevMonth = 0;
            int prevYear = 0;
            int nextMonth = 0;
            int nextYear = 0;
            int yy=0;

            GregorianCalendar cal=new GregorianCalendar();
            cal.setTimeInMillis(dateChoose);

            int curDateMonth=cal.get(Calendar.DAY_OF_MONTH);

            dateCour=cal.getTimeInMillis();

            yy=cal.get(Calendar.YEAR);
            int currentMonth = cal.get(Calendar.MONTH);
            String currentMonthName = getMonthAsString(cal.getTimeInMillis());


            daysInMonth = getNumberOfDaysInMonth(cal.getTimeInMillis());



            //Log.d(tag, "Current Month: " + " " + currentMonthName + " having "             + daysInMonth + " days.");


            cal.add(Calendar.MONTH,-1);
            prevMonth=cal.get(Calendar.MONTH);
            daysInPrevMonth=getNumberOfDaysInMonth(cal.getTimeInMillis());
            prevYear=cal.get(Calendar.YEAR);

            cal.add(Calendar.MONTH,2);
            nextMonth=cal.get(Calendar.MONTH);
            nextYear=cal.get(Calendar.YEAR);

            cal.setTimeInMillis(dateCour);



            int currentDay=cal.get(Calendar.DAY_OF_MONTH);
            cal.set(Calendar.DAY_OF_MONTH,1);
            int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK);
            cal.set(Calendar.DAY_OF_MONTH,currentDay);

            trailingSpaces = currentWeekDay-1;


            //insert days title;
            String[] tabWeek=getContext().getResources().getStringArray(R.array.week_label);
            for (int i=0;i<7;i++){

                HoldEventsForAday buf=new HoldEventsForAday(cal.getTimeInMillis(),getContext().getString(R.string.week_title));
                buf.setDayWeekName(tabWeek[i]);
                list.add(buf);

            }


// Trailing Month days
            cal.set(Calendar.MONTH,prevMonth);
            cal.set(Calendar.YEAR,prevYear);
            for (int i = 0; i < trailingSpaces; i++) {

                int buf=daysInPrevMonth - trailingSpaces + DAY_OFFSET+i;

                /*Log.d(tag,
                        "PREV MONTH:= "
                                + prevMonth
                                + " => "
                                + getMonthAsString(prevMonth)
                                + " "
                                + String.valueOf((daysInPrevMonth
                                - trailingSpaces + DAY_OFFSET)
                                + i));*/

                cal.set(Calendar.DAY_OF_MONTH,buf);

                HoldEventsForAday bufEvt=new HoldEventsForAday(cal.getTimeInMillis(),getContext().getString(R.string.past_month_print_color));
                bufEvt.setDayWeekName("empty");
                list.add(bufEvt);
            }

// Current Month Days
            cal.set(Calendar.MONTH,currentMonth);
            cal.set(Calendar.YEAR,yy);
            for (int i = 1; i <= daysInMonth; i++) {
                //Log.d(currentMonthName, String.valueOf(i) + " "  + getMonthAsString(currentMonth) + " " + yy);


                cal.set(Calendar.DAY_OF_MONTH, i);

                if (i == curDateMonth) {

                    list.add(new HoldEventsForAday(cal.getTimeInMillis(),getContext().getString(R.string.current_day_month_print_color)));
                } else {

                    list.add(new HoldEventsForAday(cal.getTimeInMillis(),getContext().getString(R.string.current_month_print_color)));
                }
            }


// Leading Month days
            cal.set(Calendar.MONTH,nextMonth);
            cal.set(Calendar.YEAR,nextYear);
            //Log.d(tag, "NEXT MONTH:= " + getMonthAsString(nextMonth));
            int tailMax=42-list.size();
            for (int i = 0; i < tailMax; i++) {
                int buf=i+1;


                cal.set(Calendar.DAY_OF_MONTH, buf);

                HoldEventsForAday bufEvt=new HoldEventsForAday(cal.getTimeInMillis(),getContext().getString(R.string.past_month_print_color));
                bufEvt.setDayWeekName("empty");
                list.add(bufEvt);
            }
        }



        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;

            ////Log.e(tag,"MyCustomCalendarFragment getview pos 1");

            if (row == null) {
                LayoutInflater inflater = (LayoutInflater) _context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.fragment_custom_day_in_calendar, parent, false);
            }

// Get a reference to the Day gridcell
            lytGrillCell = (RelativeLayout) row.findViewById(R.id.lytCustomDay);




// ACCOUNT FOR SPACING

            //Log.d(tag, "Current Day: " + getCurrentDayOfMonth());
            /*String[] day_color = list.get(position).split("-");
            String theday = day_color[0];
            String themonth = day_color[2];
            String theyear = day_color[3];*/

            HoldEventsForAday holdEvent=list.get(position);



// Set the Day GridCell
            txtCenterInfos = (TextView) row
                    .findViewById(R.id.centerInfos);

            if(holdEvent.getDayWeekName().equalsIgnoreCase("NBRE")){

                lytGrillCell.setOnClickListener(this);
                //txtCenterInfos.setOnTouchListener(this);
                if(!acceptOverdue){

                    if(holdEvent.date1<System.currentTimeMillis()){

                        lytGrillCell.setEnabled(false);
                    }

                }
                txtCenterInfos.setText("" + holdEvent.getDayMonth());

            }
            else if(holdEvent.getDayWeekName().equalsIgnoreCase("empty")){

                lytGrillCell.setEnabled(false);
                txtCenterInfos.setText("");
            }
            else txtCenterInfos.setText(holdEvent.getDayWeekName());

            String theTag=""+position+"-"+holdEvent.getDate1()+"-"+holdEvent.getDate2()+"-"+(new Date(holdEvent.getDate1()).toString());
            txtCenterInfos.setTag(theTag);
            lytGrillCell.setTag(theTag);

            //Log.e(tag, "Setting GridCell " + theTag);

            if (holdEvent.getDayPrintColor().equalsIgnoreCase(getContext().getString(R.string.past_month_print_color))) {
                txtCenterInfos.setTextColor(getContext().getResources()
                        .getColor(R.color.lightgray02));
            }
            if (holdEvent.getDayPrintColor().equalsIgnoreCase(getContext().getString(R.string.current_month_print_color))) {
                txtCenterInfos.setTextColor(getContext().getResources().getColor(
                        R.color.datepic_blue_grey_900));
            }
            if (holdEvent.getDayPrintColor().equalsIgnoreCase(getContext().getString(R.string.next_month_print_color))) {
                txtCenterInfos.setTextColor(getContext().getResources().getColor(R.color.lightgray02));
            }

            if (holdEvent.getDayPrintColor().equalsIgnoreCase(getContext().getString(R.string.current_day_month_print_color))) {
               /* LayoutParams params = textView.getLayoutParams();
                params.height = 70;
                pf.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));*/


                //txtCenterInfos.setLayoutParams(new RelativeLayout.LayoutParams(ViewPager.LayoutParams.MATCH_PARENT, GridLayout.LayoutParams.MATCH_PARENT));;

                //txtCenterInfos.setBackground(Drawable.createFromXml((R.drawable.customshapecurrentday));

                txtCenterInfos.setTextColor(getContext().getResources().getColor(R.color.darkgreen));



            }

            if (holdEvent.getDayPrintColor().equalsIgnoreCase(getContext().getString(R.string.week_title))) {
               /* LayoutParams params = textView.getLayoutParams();
                params.height = 70;
                pf.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));*/


                txtCenterInfos.setLayoutParams(new RelativeLayout.LayoutParams(ViewPager.LayoutParams.MATCH_PARENT, GridLayout.LayoutParams.MATCH_PARENT));;

                //txtCenterInfos.setBackground(Drawable.createFromXml((R.drawable.customshapecurrentday));

                txtCenterInfos.setBackgroundColor(getContext().getResources().getColor(R.color.datepic_dark));
                txtCenterInfos.setGravity(Gravity.CENTER);
                txtCenterInfos.setTextColor(getContext().getResources().getColor(R.color.datepic_white));

            }


            /*MyDatePickGestureListner gestureListner=new MyDatePickGestureListner(getContext(),fragment,this);

            mGestureDetector=new GestureDetector(getContext(),gestureListner);*/



            return row;
        }


        public String getContentCell(){

            return lytGrillCell.getTag().toString();
        }

        @Override
        public void onClick(View view) {
            Log.e("MyEasyDatePic","onCLick one evt occurs->"+view.toString());
            String date_month_year = (String) view.getTag();
            //selectedDayMonthYearButton.setText("Selected: " + date_month_year);
            ////Log.e("Selected date", date_month_year);
            try {
                //Date parsedDate = dateFormatter.parse(date_month_year);
                Log.d(tag, "Parsed Date: " + date_month_year);

                String[] tabData=date_month_year.split("-");

                long d1=Long.parseLong(tabData[1]);

                cal.setTimeInMillis(d1);
                dateCour=d1;


                setField(dateCour);


                if(currentDateView!=null){


                    //currentDateView.setBackgroundColor(Color.TRANSPARENT);
                    // currentDateView.setBackgroundColor(Color.argb(100, 200, 200, 200));
                    currentDateView.setBackgroundResource(R.drawable.datepic_back_custom_day);

                }

                currentDateView=view;


                currentDateView.setBackgroundResource(R.drawable.datepic_back_sub_title_no_radius);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }




        public int getCurrentDayOfMonth() {
            return currentDayOfMonth;
        }

        private void setCurrentDayOfMonth(int currentDayOfMonth) {
            this.currentDayOfMonth = currentDayOfMonth;
        }

        public void setCurrentWeekDay(int currentWeekDay) {
            this.currentWeekDay = currentWeekDay;
        }

        public int getCurrentWeekDay() {
            return currentWeekDay;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            //return false;
            return mGestureDetector.onTouchEvent(event);
        }
    }








    private class HoldEventsForAday{



        String keyEvent;
        long date1,date2;
        int dayMonth,nbreEventIn,nbreEventOut,nbreEventRsce,nbreEventUnpaid;
        String dayPrintColor;
        String dayWeekName="NBRE";




        public HoldEventsForAday(long dateStart,String dayPrintingColor){

            GregorianCalendar cal=new GregorianCalendar();
            cal.setTimeInMillis(dateStart);
            /*cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);*/
            date1=cal.getTimeInMillis();



            keyEvent=""+date1;

            cal.set(Calendar.HOUR_OF_DAY,23);
            cal.set(Calendar.MINUTE,59);

            date2=cal.getTimeInMillis();
            dayMonth=cal.get(Calendar.DAY_OF_MONTH);
            nbreEventIn=0;
            nbreEventOut=nbreEventRsce=nbreEventUnpaid=nbreEventIn;

            keyEvent=date1+"_"+date2;

            dayPrintColor=dayPrintingColor;
            dayWeekName="NBRE";


        }




        public long getDate1() {
            return date1;
        }


        public long getDate2() {
            return date2;
        }

        public int getDayMonth() {
            return dayMonth;
        }




        public String getDayPrintColor() {
            return dayPrintColor;
        }


        public String getDayWeekName() {
            return dayWeekName;
        }

        public void setDayWeekName(String dayWeekName) {
            this.dayWeekName = dayWeekName;
        }
    }




    /*****************************Some importants and static method for date use**************************************************/

    public static long getLastDayMonth(long datCour) {

        //Log.e("MyBbmAppli", "getLastDayMonth voici ce que je recois->" + new Date(datCour));

        GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeInMillis(datCour);
        int curMont = cal.get(Calendar.MONTH);
        int lastDay = cal.get(Calendar.DAY_OF_MONTH);

        while (cal.get(Calendar.MONTH) == curMont) {

            lastDay = cal.get(Calendar.DAY_OF_MONTH);
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }

        //Remise au mois dont on faisait la boucle car a la fin, on a change de mois et pour certains cas l annee
        cal.add(Calendar.DAY_OF_MONTH, -1);

        //cal.set(Calendar.MONTH,curMont);
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);

        //Log.e("MyBbmAppli", "getLastDayMonth voici la date  de retour->" + new Date(cal.getTimeInMillis()));

        return cal.getTimeInMillis();

    }


    public static int getNumberOfDaysInMonth(long dateJour) {

        //getFirstDayMonth(dateJour);

        GregorianCalendar cal = new GregorianCalendar();

        cal.setTimeInMillis(dateJour);

        cal.set(Calendar.DAY_OF_MONTH, 1);
        ////Log.e("CustomCalendar", "getNumberOfDaysOfMonth first day du mois->" + cal.get(Calendar.DAY_OF_MONTH));
        cal.add(Calendar.MONTH, 1);
        cal.add(Calendar.DAY_OF_MONTH, -1);

        ////Log.e("CustomCalendar", "getNumberOfDaysOfMonth last day du mois->" + cal.get(Calendar.DAY_OF_MONTH));
        return cal.get(Calendar.DAY_OF_MONTH);
    }


    public static long getFirstDayMonth(long datCour) {

        //Log.e("MyBbmAppli", "getFirstDayMonth voici ce que je recois->" + new Date(datCour));

        GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeInMillis(datCour);
        int curMont = cal.get(Calendar.MONTH);
        int firstDay = cal.get(Calendar.DAY_OF_MONTH);

        while (cal.get(Calendar.MONTH) == curMont) {

            firstDay = cal.get(Calendar.DAY_OF_MONTH);
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }

        //Remise au mois dont on faisait la boucle car a la fin, on a change de mois et pour certains cas l annee
        cal.add(Calendar.DAY_OF_MONTH, 1);

        //cal.set(Calendar.MONTH,curMont);
        cal.set(Calendar.DAY_OF_MONTH, firstDay);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);

        // Log.e("MyBbmAppli", "getFirstDayMonth voici la date  de retour->" + new Date(cal.getTimeInMillis()));

        return cal.getTimeInMillis();
    }


    public static long getFirstDayWeek(long datCour) {

        //Log.e("MyBbmAppli", "getFirstDayWeek voici ce que je recois->" + new Date(datCour));

        GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeInMillis(datCour);
        int curWeek = cal.get(Calendar.WEEK_OF_YEAR);

        while (cal.get(Calendar.WEEK_OF_YEAR) == curWeek) {

            cal.add(Calendar.DAY_OF_MONTH, -1);
        }

        //Remise au week dont on faisait la boucle car a la fin, on a change de week,mois out annee
        cal.add(Calendar.DAY_OF_MONTH, 1);


        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);

        //Log.e("MyBbmAppli", "getFirstDayWeek voici la date  de retour->" + new Date(cal.getTimeInMillis()));

        return cal.getTimeInMillis();
    }

    public static long getLastDayWeek(long datCour) {

        //Log.e("MyBbmAppli", "getLastDayWeek voici ce que je recois->" + new Date(datCour));

        GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeInMillis(datCour);
        int curWeek = cal.get(Calendar.WEEK_OF_YEAR);

        while (cal.get(Calendar.WEEK_OF_YEAR) == curWeek) {

            cal.add(Calendar.DAY_OF_MONTH, 1);
        }

        //Remise au week dont on faisait la boucle car a la fin, on a change de week,mois out annee
        cal.add(Calendar.DAY_OF_MONTH, -1);


        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);

        //Log.e("MyBbmAppli", "getLastDayWeek voici la date  de retour->" + new Date(cal.getTimeInMillis()));

        return cal.getTimeInMillis();
    }
    public static long getFirstDayPreviousMonth(long datCour) {

        GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeInMillis(datCour);
        cal.add(Calendar.MONTH, -1);

        // Log.e("MyBbmAppli","getFirstDayLastMonth voici la date a traiter->"+new Date(cal.getTimeInMillis()));

        return getFirstDayMonth(cal.getTimeInMillis());

    }

    public static long getLastDayPreviousMonth(long datCour) {

        GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeInMillis(datCour);
        cal.add(Calendar.MONTH, -1);

        //Log.e("MyBbmAppli","getLastDayLastMonth voici la date a traiter->"+new Date(cal.getTimeInMillis()));
        return getLastDayMonth(cal.getTimeInMillis());

    }


    public static long getFirstDayNextMonth(long datCour) {

        GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeInMillis(datCour);
        cal.add(Calendar.MONTH, 1);

        //Log.e("MyBbmAppli","getFirstDayNextMonth voici la date a traiter->"+new Date(cal.getTimeInMillis()));
        return getFirstDayMonth(cal.getTimeInMillis());

    }

    public static long getFirstDayNextYear(long datCour) {

        GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeInMillis(datCour);
        cal.add(Calendar.YEAR, 1);
        cal.set(Calendar.MONTH, Calendar.JANUARY);

        //Log.e("MyBbmAppli","getFirstDayNextMonth voici la date a traiter->"+new Date(cal.getTimeInMillis()));
        return getFirstDayMonth(cal.getTimeInMillis());

    }

    public static long getLastDayNextMonth(long datCour) {

        GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeInMillis(datCour);
        cal.add(Calendar.MONTH, 1);

        //Log.e("MyBbmAppli","getLastDayNextMonth voici la date a traiter->"+new Date(cal.getTimeInMillis()));
        return getLastDayMonth(cal.getTimeInMillis());

    }


    public static long getFirstDayPreviousYear(long datCour) {

        GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeInMillis(datCour);
        cal.add(Calendar.YEAR, -1);
        cal.set(Calendar.MONTH, Calendar.JANUARY);

        //Log.e("MyBbmAppli","getFirstDayNextMonth voici la date a traiter->"+new Date(cal.getTimeInMillis()));
        return getFirstDayMonth(cal.getTimeInMillis());

    }


    public static long getLastDayPreviousYear(long datCour) {

        GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeInMillis(datCour);
        cal.add(Calendar.YEAR, -1);
        cal.set(Calendar.MONTH, Calendar.DECEMBER);

        //Log.e("MyBbmAppli","getFirstDayNextMonth voici la date a traiter->"+new Date(cal.getTimeInMillis()));
        return getLastDayMonth(cal.getTimeInMillis());

    }


    public static long getFirstDayYear(long datCour) {

        GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeInMillis(datCour);
        cal.set(Calendar.MONTH, Calendar.JANUARY);

        //Log.e("MyBbmAppli","getFirstDayNextMonth voici la date a traiter->"+new Date(cal.getTimeInMillis()));
        return getFirstDayMonth(cal.getTimeInMillis());

    }


    public static long getLastDayYear(long datCour) {

        GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeInMillis(datCour);
        cal.set(Calendar.MONTH, Calendar.DECEMBER);

        //Log.e("MyBbmAppli","getFirstDayNextMonth voici la date a traiter->"+new Date(cal.getTimeInMillis()));
        return getLastDayMonth(cal.getTimeInMillis());

    }


    public static String getMonthShortAsString(long dateJour) {

        SimpleDateFormat daFor = new SimpleDateFormat("MMM.");

        return daFor.format(new Date(dateJour));
    }


    public static String getMonthAsString(long dateJour) {

        SimpleDateFormat daFor = new SimpleDateFormat("MMMM");

        return daFor.format(new Date(dateJour));
    }

    public static String getMonthAndYearAsString(long dateJour) {

        SimpleDateFormat daFor = new SimpleDateFormat("MMMM yyyy");

        return daFor.format(new Date(dateJour));
    }

    public static String getWeekDayAsString(long dateJour) {
        SimpleDateFormat daFor = new SimpleDateFormat("EEEEE");

        return daFor.format(new Date(dateJour));
    }

    public static String getWeekDayShortAsString(long dateJour) {
        SimpleDateFormat daFor = new SimpleDateFormat("EEE");

        return daFor.format(new Date(dateJour));
    }

    public static String getWeekDayOneLetterAsString(long dateJour) {
        SimpleDateFormat daFor = new SimpleDateFormat("E");

        return daFor.format(new Date(dateJour));
    }

    public static int compareDate(long date1,long date2,boolean withoutTime) {


        int res=0;
        if(withoutTime){

            GregorianCalendar cal=new GregorianCalendar();
            cal.setTimeInMillis(date1);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            Date d1=cal.getTime();

            cal.setTimeInMillis(date2);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            Date d2=cal.getTime();

            res=d1.compareTo(d2);

        }
        else{

            Date d1=new Date(date1);
            Date d2=new Date(date2);
            res=d1.compareTo(d2);
        }



        return res;
    }
}
