package com.conestogac.msd.roster;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MainActivity extends Activity implements OnItemSelectedListener {
    public static final String PREFS_NAME = "msd.conestogac.MyPrefsFile";
    private Spinner eyeColor=null;
    private String eyeColorSelected;
    EditText nameEditText;
    TextView birthday;
    CheckBox thinkWeAre;
    int year_x, month_x, day_x;
    static final int DIALOG_ID = 0;
    final SimpleDateFormat formatDate = new SimpleDateFormat("MMM dd,yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        eyeColor=(Spinner)findViewById(R.id.sp_eyeColor);
        birthday = (TextView)findViewById(R.id.tv_calendar);
        thinkWeAre = (CheckBox) findViewById(R.id.cb_thinkWeAre);

        if (thinkWeAre.isChecked()) {
            thinkWeAre.setChecked(false);
        }
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> eyeColors=new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.eyecolor_item));
        // Specify the layout to use when the list of choices appears
        eyeColors.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        eyeColor.setAdapter(eyeColors);
        //Register Callback
        eyeColor.setOnItemSelectedListener(this);
        nameEditText = (EditText) findViewById(R.id.et_name);

        if (year_x == 0) {
            final Calendar cal = Calendar.getInstance();
            year_x = cal.get(Calendar.YEAR);
            month_x = cal.get(Calendar.MONTH);
            day_x = cal.get(Calendar.DAY_OF_MONTH);
            birthday.setText(formatDate.format(cal.getTime()));
            Log.d("MainActivity", "year:" + year_x + "month:" + month_x + "day:" + day_x);
        }

        // Restore preferences
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String strName = settings.getString(getString(R.string.key_name), "");
        if (strName!="") {
            nameEditText.setText(strName);
        }
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        eyeColor.setSelection(position);
        eyeColorSelected = (String) eyeColor.getSelectedItem();
        Log.d("MainActivity", "onItemSelected " + eyeColorSelected);
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    protected void showCalendar(View v) {
        showDialog(DIALOG_ID);
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        if(id == DIALOG_ID) {
            return new DatePickerDialog(this, dpickerListener,  year_x, month_x, day_x);
        }
        return null;
    }

    protected void onSave(View v) {
        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(getString(R.string.key_name), nameEditText.getText().toString());
        editor.putString(getString(R.string.key_eyecolor), eyeColorSelected);
        editor.putString(getString(R.string.key_birthday), birthday.getText().toString());

        // Commit the edits!
        editor.commit();
    }

    private DatePickerDialog.OnDateSetListener dpickerListener
            = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            year_x = year;
            month_x = monthOfYear;
            day_x = dayOfMonth;
            Calendar selectedCal = new GregorianCalendar(year, monthOfYear, dayOfMonth);
            birthday.setText(formatDate.format(selectedCal.getTime()));
        }
    };
}


