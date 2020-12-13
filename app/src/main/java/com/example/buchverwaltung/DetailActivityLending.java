package com.example.buchverwaltung;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class DetailActivityLending extends AppCompatActivity {

    private static final String Tag = "DetailActivityLending";

    TextView selectPlannedEndDate, selectActualDate, nameView, commentView;
    DatePickerDialog.OnDateSetListener actualDateSetListener, plannedDateSetListener;
    Button confirmButton, deleteButton, isBackButton;
    Lending lending;
    DataBaseHelper dataBaseHelper;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_lending);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent i = getIntent();
        context = getApplicationContext();
        dataBaseHelper = new DataBaseHelper(DetailActivityLending.this);

        deleteButton = findViewById(R.id.detailLendingButtonDelete);
        confirmButton = findViewById(R.id.detailLendingButtonConfirm);
        isBackButton = findViewById(R.id.detailLendingButtonGiveBack);
        nameView = findViewById(R.id.detailLendingEditTextLender);
        commentView = findViewById(R.id.detailLendingEditTextComment);
        selectActualDate = findViewById(R.id.detailLendingTextViewActualDate);
        selectPlannedEndDate = findViewById(R.id.detailLendingTextViewPlannedDate);

        //Needed for the DatePickers...
        handleDateSetters();

        // create a new lending
        if(i.hasExtra("bookId")) {
            createLending(i);
        }

        // edit an existing lending
        if(i.hasExtra("lendingId")) {
            handleExistingLending(i);
        }
    }

    private void handleDateSetters() {
        actualDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                String date =  day + "/" + month + "/" + year;
                selectActualDate.setText(date);
            }
        };
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
                String date =  day + "/" + month + "/" + year;
                selectPlannedEndDate.setText(date);
            }
        };
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
    }

    private void createLending(Intent i) {
        deleteButton.setVisibility(Button.GONE);
        isBackButton.setVisibility(Button.GONE);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lending = new Lending(i.getIntExtra("bookId",1),String.valueOf(nameView.getText()),String.valueOf(selectActualDate.getText()),String.valueOf(selectPlannedEndDate.getText()),false,String.valueOf(commentView.getText()));
                dataBaseHelper.addLending(lending);

                Intent iBackToDetailBook = new Intent(DetailActivityLending.this, DetailActivityBook.class);
                iBackToDetailBook.putExtra("id", lending.getBook_id());
                startActivity(iBackToDetailBook);
            }
        });
    }

    private void handleExistingLending(Intent i) {
        lending = dataBaseHelper.getLending(i.getIntExtra("lendingId",0));

        nameView.setText(lending.getLender());
        commentView.setText(lending.getComment());
        selectActualDate.setText(lending.getStart());
        selectPlannedEndDate.setText(lending.getPlanned_end());

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmLending();
            }
        });

        isBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lending = new Lending(lending.getId(),lending.getBook_id(),String.valueOf(nameView.getText()),String.valueOf(selectActualDate.getText()),String.valueOf(selectPlannedEndDate.getText()),true,String.valueOf(commentView.getText()));
                dataBaseHelper.editLending(lending);
                Intent iBackToDetailBook3 = new Intent(DetailActivityLending.this, DetailActivityBook.class);
                iBackToDetailBook3.putExtra("id", lending.getBook_id());
                startActivity(iBackToDetailBook3);
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(DetailActivityLending.this)
                        .setTitle(R.string.dialogDeleteLending)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Toast.makeText(DetailActivityLending.this, "Yaay", Toast.LENGTH_SHORT).show();
                                dataBaseHelper.remLending(lending.getId());
                                Intent iBackToDetailBook4 = new Intent(DetailActivityLending.this, DetailActivityBook.class);
                                iBackToDetailBook4.putExtra("id", lending.getBook_id());
                                startActivity(iBackToDetailBook4);
                            }})
                        .setNegativeButton(android.R.string.cancel, null).show();

            }
        });
        // hide returned button
        if(lending.getIsBack()) {
            isBackButton.setVisibility(Button.GONE);
            selectActualDate.setClickable(false);
            selectPlannedEndDate.setClickable(false);
        }
    }

    private void confirmLending() {
        lending = new Lending(lending.getId(),lending.getBook_id(),String.valueOf(nameView.getText()),String.valueOf(selectActualDate.getText()),String.valueOf(selectPlannedEndDate.getText()),lending.getIsBack(),String.valueOf(commentView.getText()));
        dataBaseHelper.editLending(lending);

        Intent iBackToDetailBook2 = new Intent(DetailActivityLending.this, DetailActivityBook.class);
        iBackToDetailBook2.putExtra("id", lending.getBook_id());
        startActivity(iBackToDetailBook2);
    }
}