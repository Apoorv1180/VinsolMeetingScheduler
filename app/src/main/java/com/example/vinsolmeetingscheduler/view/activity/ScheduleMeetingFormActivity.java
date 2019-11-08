package com.example.vinsolmeetingscheduler.view.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.vinsolmeetingscheduler.R;
import com.example.vinsolmeetingscheduler.service.model.ScheduleMeetingResponse;
import com.example.vinsolmeetingscheduler.util.Constant;
import com.example.vinsolmeetingscheduler.viewmodel.ScheduleMeetingViewModel;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ScheduleMeetingFormActivity extends AppCompatActivity implements View.OnClickListener {

    //view
    LinearLayout previous_button_click;
    Button selectMeetingDate;
    Button selectStartTime;
    Button selectEndTime;
    EditText meetingDescription;
    Button submit;

    //view model
    ScheduleMeetingViewModel scheduleMeetingViewModel;

    //variables
    private DatePickerDialog datePickerDialog;
    private Calendar currentTime = Calendar.getInstance();
    private Calendar currentTimeEnd = Calendar.getInstance();
    private Calendar pickedDate = Calendar.getInstance();
    private ArrayList<Long> startTimeinMilliseconds = new ArrayList<>();
    private ArrayList<Long> endTimeinMilliseconds = new ArrayList<>();
    private String date;
    private Date intentDate;
    private int month, day, year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_meeting_form);
        getIntentData();
        initViewModel();
        initToolBar();
        initView();
        initListeners();
    }

    private void initListeners() {
        selectMeetingDate.setOnClickListener(this);
        selectStartTime.setOnClickListener(this);
        selectEndTime.setOnClickListener(this);
        submit.setOnClickListener(this);
        previous_button_click.setOnClickListener(this);
    }

    private void initView() {
        selectMeetingDate = findViewById(R.id.meeting_date);
        selectStartTime = findViewById(R.id.start_time);
        selectEndTime = findViewById(R.id.end_time);
        meetingDescription = findViewById(R.id.meeting_description);
        submit = findViewById(R.id.submit);
        submit.setEnabled(false);
    }

    private void getIntentData() {
        date = getIntent().getStringExtra(getString(R.string.date_intent_extra));
        intentDate = Constant.convertStringToDate(date);
        Calendar calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

    }

    private void initToolBar() {
        getSupportActionBar().setCustomView(R.layout.custom_bar_layout_meeting_schedule);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        View view = getSupportActionBar().getCustomView();
        previous_button_click = view.findViewById(R.id.back_click);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.meeting_date:
                Calendar newCalendar = Calendar.getInstance();
                checkDisplayVaidations(newCalendar);
                datePickerDialog.show();
                break;
            case R.id.start_time:
                setStartTimePicker();
                break;
            case R.id.end_time:
                setEndTimePicker();
                break;
            case R.id.submit:
                checkSubmitValidations();
                break;
            case R.id.back_click:
                finish();
        }
    }

    private void checkSubmitValidations() {
        if (TextUtils.isEmpty(meetingDescription.getText())) {
            meetingDescription.setError(getResources().getString(R.string.enter_valid_date));
        } else {
            meetingDescription.setError(null);

            if (currentTimeEnd.getTimeInMillis() <= currentTime.getTimeInMillis()) {
                selectEndTime.setError(getResources().getString(R.string.select_valid_time));
                selectStartTime.setError(getResources().getString(R.string.select_valid_time));
                selectEndTime.setText(getString(R.string.select_Valid_date));
                selectStartTime.setText(getString(R.string.select_Valid_date));

            } else {
                selectEndTime.setText(getString(R.string.select_Valid_date));
                selectStartTime.setText(getString(R.string.select_Valid_date));
                selectEndTime.setError(null);
                selectStartTime.setError(null);
            }            if (checkMeetingLogic(currentTime.getTimeInMillis(), currentTimeEnd.getTimeInMillis(), startTimeinMilliseconds, endTimeinMilliseconds)) {
               Snackbar.make(previous_button_click, getString(R.string.slots_available), Snackbar.LENGTH_LONG).show();
            } else
                Snackbar.make(previous_button_click, getString(R.string.slots_unavailable), Snackbar.LENGTH_LONG).show();
        }
    }

    private void setEndTimePicker() {
        TimePickerDialog mTimePickerEnd;
        mTimePickerEnd = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                currentTimeEnd.set(year, month, day, selectedHour, selectedMinute);
                if (currentTimeEnd.getTimeInMillis() <= currentTime.getTimeInMillis()) {
                    selectEndTime.setText(getString(R.string.select_Valid_date));
                    selectEndTime.setError(getResources().getString(R.string.select_valid_time));
                } else {
                    selectEndTime.setText(selectedHour + ":" + selectedMinute);
                    selectEndTime.setError(null);
                }


            }
        }, 0, 0, true);//Yes 24 hour time
        mTimePickerEnd.setTitle(getResources().getString(R.string.select_time));
        mTimePickerEnd.show();
    }

    private void setStartTimePicker() {
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                selectStartTime.setText(selectedHour + ":" + selectedMinute);
                currentTime.set(year, month, day, selectedHour, selectedMinute);

            }
        }, 0, 0, true);//Yes 24 hour time
        mTimePicker.setTitle(getResources().getString(R.string.select_time));
        mTimePicker.show();
    }

    private void checkDisplayVaidations(Calendar newCalendar) {
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar currentDate = getCalendarValuesForPickedDate(year, monthOfYear, dayOfMonth);

                if (pickedDate.getTimeInMillis() >= currentDate.getTimeInMillis()) {
                    submit.setEnabled(true);
                    selectMeetingDate.setText(Constant.convertDateToString(pickedDate.getTime()));
                    observeViewModel(scheduleMeetingViewModel, Constant.convertDateToString(pickedDate.getTime()));
                } else {
                    selectMeetingDate.setText(getResources().getString(R.string.select_valid_date));
                    selectMeetingDate.setError(getResources().getString(R.string.select_valid_date));
                }
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    @NotNull
    private Calendar getCalendarValuesForPickedDate(int year, int monthOfYear, int dayOfMonth) {
        pickedDate = Calendar.getInstance();
        pickedDate.set(year, monthOfYear, dayOfMonth);
        pickedDate.set(Calendar.HOUR_OF_DAY, 0);
        pickedDate.set(Calendar.MINUTE, 0);
        pickedDate.set(Calendar.SECOND, 0);
        pickedDate.set(Calendar.MILLISECOND, 0);
        Date dateFr = Constant.convertStringToDate(date);
        Calendar currentDate = Calendar.getInstance();
        currentDate.setTime(dateFr);
        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        currentDate.set(Calendar.MILLISECOND, 0);
        return currentDate;
    }

    private boolean checkMeetingLogic(long startSlot, long endSlot, ArrayList<Long> time, ArrayList<Long> endTimeSlots) {
        boolean isScheduleAvailable = true;
        for (int index = 0; index < startTimeinMilliseconds.size(); index++) {

            if (
                    (startSlot > time.get(index) && startSlot < endTimeSlots.get(index))
                            ||
                            (endSlot > time.get(index) && endSlot < endTimeSlots.get(index))
                            ||
                            (time.get(index) > startSlot && time.get(index) < endSlot)
                            ||
                            (endTimeSlots.get(index) > startSlot && endTimeSlots.get(index) < endSlot)
            ) {
                isScheduleAvailable = false;
                return isScheduleAvailable;
            }

        }
        return isScheduleAvailable;
    }

    private void initViewModel() {
        scheduleMeetingViewModel = ViewModelProviders.of(this).get(ScheduleMeetingViewModel.class);
        observeViewModel(scheduleMeetingViewModel, Constant.getDate(this));
    }

    private void observeViewModel(ScheduleMeetingViewModel scheduleMeetingViewModel, String date) {
        scheduleMeetingViewModel.getMeetingResponse(date).observe(this, new Observer<List<ScheduleMeetingResponse>>() {
            @Override
            public void onChanged(List<ScheduleMeetingResponse> scheduleMeetingResponses) {
                if (scheduleMeetingResponses != null && scheduleMeetingResponses.size() != 0) {
                    for (int i = 0; i < scheduleMeetingResponses.size(); i++) {
                        startTimeinMilliseconds.add(Constant.getTimeInMilliseconds(pickedDate, scheduleMeetingResponses.get(i).getStartTime()));
                        endTimeinMilliseconds.add(Constant.getTimeInMilliseconds(pickedDate, scheduleMeetingResponses.get(i).getEndTime()));
                        selectMeetingDate.setError(null);
                    }
                } else {
                    selectMeetingDate.setError(getResources().getString(R.string.no_data));
                    Snackbar.make(previous_button_click, getString(R.string.no_data), Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }
}



