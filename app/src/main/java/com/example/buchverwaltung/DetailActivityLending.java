package com.example.buchverwaltung;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

public class DetailActivityLending extends AppCompatActivity {

    /*
    private static final String Tag = "DetailActivityLending";

    private TextView selectPlannedEndDate;
    private TextView selectActualDate;
    private DatePickerDialog.OnDateSetListener actualDateSetListener;
    private DatePickerDialog.OnDateSetListener plannedDateSetListener;
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_lending);


        /*
                                Needed for the DatePickers...



        selectActualDate = (TextView) findViewById(R.id.detailLendingTextViewActualDate);

        selectActualDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        DetailActivityLending.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        actualDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });

        plannedDateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                String date = month + "/" + day + "/" + year;
                selectPlannedEndDate.setText(date);
            }
        };

        selectPlannedEndDate = (TextView) findViewById(R.id.detailLendingTextViewPlannedDate);


        selectPlannedEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        DetailActivityLending.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        plannedDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });

        actualDateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                String date = month + "/" + day + "/" + year;
                selectActualDate.setText(date);
            }
        };

         */
    }
}