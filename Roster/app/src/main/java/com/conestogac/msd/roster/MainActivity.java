package com.conestogac.msd.roster;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class MainActivity extends Activity implements OnItemSelectedListener {
    public static final String PREFS_NAME = "msd.conestogac.MyPrefsFile";
    private EditText nameEditText;
    //Checkbox
    private CheckBox thinkWeAre;
    //Spinner
    private Spinner eyeColor=null;
    private String eyeColorSelected;
    //Calendar
    private TextView birthday;
    private int year_x, month_x, day_x;
    public static final int DIALOG_ID = 0;
    public final SimpleDateFormat formatDate = new SimpleDateFormat("MMM dd, yyyy");
    private boolean isThinkweare;
    //radio
    public  RadioGroup radioGroup;
    private Integer index_ShirtSize, index_eyeColor;
    //Seekbar
    private SeekBar pantsize_seek;
    private SeekBar shirtsize_seek;
    private SeekBar shoesize_seek;
    private TextView pantsize_text;
    private TextView shirtsize_text;
    private TextView shoesize_text;
    //Person's Name
    private String strName;
    //Size from seekbar
    private Integer pantSize, shirtSize, shoeSize;

   @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_main);
       eyeColor=(Spinner)findViewById(R.id.sp_eyeColor);
       birthday = (TextView)findViewById(R.id.tv_calendar);
       thinkWeAre = (CheckBox) findViewById(R.id.cb_thinkWeAre);
       radioGroup= (RadioGroup)findViewById(R.id.radioGroup);

       setActionBarTitle();
       restorePreferences();
       updateEditText();
       initCalendarView();
       updateRadioButtonChecked();
       initSpinnerDropDown();
       initSeekBar();
       updateSeekBarText();
    }

    private void setActionBarTitle() {
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getActionBar().setCustomView(R.layout.action_bar);
    }

    private void SetCustomTitle(String title)
    {
        TextView textViewTitle = (TextView) findViewById(R.id.action_bar_title);
        Log.d("MainActivity",title);
        textViewTitle.setText(title);
    }

    private void updateEditText() {
        nameEditText = (EditText) findViewById(R.id.et_name);
        if (strName != "") {
            nameEditText.setText(strName);
        }
    }

    private void initCalendarView() {
        if (year_x == 0) {
            final Calendar cal = Calendar.getInstance();
            year_x = cal.get(Calendar.YEAR);
            month_x = cal.get(Calendar.MONTH);
            day_x = cal.get(Calendar.DAY_OF_MONTH);
            Log.d("MainActivity", "year:" + year_x + "month:" + month_x + "day:" + day_x);
        }
        Calendar selectedCal = new GregorianCalendar(year_x, month_x, day_x);
        birthday.setText(formatDate.format(selectedCal.getTime()));
    }

    private void updateRadioButtonChecked() {
        ((RadioButton)radioGroup.getChildAt(index_ShirtSize)).setChecked(true);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                View radioButton = radioGroup.findViewById(i);
                index_ShirtSize = radioGroup.indexOfChild(radioButton);
                Log.d("Main_Activity", "Radio Button:" + String.valueOf(index_ShirtSize));
            }
        });
    }

    private void initSpinnerDropDown() {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> eyeColors=new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.eyecolor_item));
        // Specify the layout to use when the list of choices appears
        eyeColors.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        eyeColor.setAdapter(eyeColors);
        eyeColor.setSelection(index_eyeColor);
        //Register Callback
        eyeColor.setOnItemSelectedListener(this);
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        isThinkweare = ((CheckBox) view).isChecked();
    }

    private void initSeekBar() {
        pantsize_seek = (SeekBar) findViewById(R.id.sb_pantSize);
        pantsize_text = (TextView) findViewById(R.id.tv_seekPantSize);
        shirtsize_seek = (SeekBar) findViewById(R.id.sb_shirtSize);
        shirtsize_text = (TextView) findViewById(R.id.tv_seekShirtSize);
        shoesize_seek = (SeekBar) findViewById(R.id.sb_shoeSize);
        shoesize_text = (TextView) findViewById(R.id.tv_seekShoeSize);
    }

    private void updateSeekBarText() {
        shirtsize_seek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                shirtSize = progress;
                shirtsize_text.setText(String.valueOf(progress));
            }
        });
        shirtsize_seek.setProgress(shirtSize);
        shirtsize_text.setText(String.valueOf(shirtSize));

        pantsize_seek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pantSize = progress;
                pantsize_text.setText(String.valueOf(progress));
            }
        });
        pantsize_seek.setProgress(pantSize);
        pantsize_text.setText(String.valueOf(pantSize));

        shoesize_seek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                shoeSize = progress;
                shoesize_text.setText(String.valueOf(progress + 4));
            }
        });
        shoesize_seek.setProgress(shoeSize);
        shoesize_text.setText(String.valueOf(shoeSize + 4));
    }

    private DatePickerDialog.OnDateSetListener dpickerListener =
            new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            year_x = year;
            month_x = monthOfYear;
            day_x = dayOfMonth;
            Calendar selectedCal = new GregorianCalendar(year, monthOfYear, dayOfMonth);
            birthday.setText(formatDate.format(selectedCal.getTime()));
        }
    };

    //Spinner Drop Down with Adapter View
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        eyeColor.setSelection(position);
        eyeColorSelected = (String) eyeColor.getSelectedItem();
        Log.d("MainActivity", "onItemSelected " + eyeColorSelected);
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO
    }
    //Birthday OnClick
    public void onShowCalendar(View v) {
        showDialog(DIALOG_ID);
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        if(id == DIALOG_ID) {
            return new DatePickerDialog(this, dpickerListener,  year_x, month_x, day_x);
        }
        return null;
    }
    private void restorePreferences() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        strName = settings.getString(getString(R.string.key_name), "");

        isThinkweare = settings.getBoolean(getString(R.string.key_thinkweare), false);
        thinkWeAre.setChecked(isThinkweare);

        index_eyeColor = settings.getInt(getString(R.string.key_eyecolor), 0);

        year_x = settings.getInt(getString(R.string.key_birthday_y), 2015);
        month_x = settings.getInt(getString(R.string.key_birthday_m), 9);
        day_x = settings.getInt(getString(R.string.key_birthday_d),6);

        index_ShirtSize = settings.getInt(getString(R.string.key_shirtsize_rb), 2);
        shirtSize = settings.getInt(getString(R.string.key_shirtsize_sb),0);
        pantSize = settings.getInt(getString(R.string.key_pantsize),0);
        shoeSize = settings.getInt(getString(R.string.key_shoesize),0);
    }
    //Save Button OnClick
    public void onSave() {

        Log.d("MainActivity", String.valueOf(eyeColor.getSelectedItemPosition()));

        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(getString(R.string.key_name), nameEditText.getText().toString());
        editor.putBoolean(getString(R.string.key_thinkweare), isThinkweare);
        editor.putInt(getString(R.string.key_eyecolor), eyeColor.getSelectedItemPosition());
        editor.putInt(getString(R.string.key_birthday_y), year_x);
        editor.putInt(getString(R.string.key_birthday_m), month_x);
        editor.putInt(getString(R.string.key_birthday_d), day_x);
        editor.putInt(getString(R.string.key_shirtsize_rb), index_ShirtSize);

        editor.putInt(getString(R.string.key_shirtsize_sb), shirtSize);
        editor.putInt(getString(R.string.key_pantsize), pantSize);
        editor.putInt(getString(R.string.key_shoesize), shoeSize);
        // Commit the edits!
        editor.commit();
        Toast.makeText(this, getString(R.string.saved), Toast.LENGTH_SHORT).show();
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
        if (id == R.id.action_save) {
            onSave();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    

}


