package com.konradplonka.fuelcalculator.fragments.dialogs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.konradplonka.fuelcalculator.R;
import com.konradplonka.fuelcalculator.other.DatabaseHelper;
import com.nbsp.materialfilepicker.MaterialFilePicker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import static com.konradplonka.fuelcalculator.other.DatabaseHelper.EXPORT_DICTIONARY;

public class ExportDialog extends DialogFragment {
    private DatabaseHelper databaseHelper;

    private ImageButton exportImageButton;
    private LinearLayout progressBarLinearLayout;
    private TextView progressBarTextView;
    private ImageView progressBarImageView;
    private EditText fileNameEditText;
    private View dividerFileName;
    private TextView exportMessage;
    private Button showDatabaseButton;
    private ImageButton backImageButton;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_export, container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        databaseHelper = new DatabaseHelper(getContext());

        initializeViewElements(view);
        setFileNameText();
        onAcceptFileNameClick();
        onShowDatabaseButtonClick();
        onBackImageButtonClick();

        return view;
    }

    private void onBackImageButtonClick() {
        backImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void onShowDatabaseButtonClick() {
        showDatabaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialFilePicker()
                        .withActivity((Activity) getContext())
                        .withRequestCode(2)
                        .withFilter(Pattern.compile(".*\\.db$")) // Filtering files and directories by file name using regexp
                        .withFilterDirectories(true) // Set directories filterable (false by default)
                        .withHiddenFiles(true) // Show hidden files and folders
                        .withPath(getContext().getExternalFilesDir(null) + "/" + EXPORT_DICTIONARY)
                        .start();
            }
        });

    }

    private void onAcceptFileNameClick() {
       exportImageButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(databaseHelper.exportDatabase(fileNameEditText.getText().toString(), getContext())){
                   buttonFinished();
               }

           }
       });
    }

    private void initializeViewElements(View view) {
        exportImageButton = (ImageButton) view.findViewById(R.id.export_imageButton);
        progressBarLinearLayout = (LinearLayout) view.findViewById(R.id.progressBar_linearLayout);
        progressBarTextView = (TextView) view.findViewById(R.id.progressBar_textView);
        progressBarImageView = (ImageView) view.findViewById(R.id.progressBar_imageView);
        fileNameEditText = (EditText) view.findViewById(R.id.fileName_editText);
        dividerFileName = (View) view.findViewById(R.id.divider_fileName);
        exportMessage = view.findViewById(R.id.export_message);
        showDatabaseButton = view.findViewById(R.id.show_database_button);
        backImageButton = view.findViewById(R.id.back_button);

    }

    private void buttonFinished(){
        progressBarLinearLayout.setAnimation(getAnimation());
        progressBarImageView.setAnimation(getAnimation());
        progressBarTextView.setAnimation(getAnimation());



        progressBarLinearLayout.setVisibility(View.VISIBLE);
        progressBarImageView.setVisibility(View.VISIBLE);
        progressBarTextView.setText(R.string.finished);

        exportMessage.setAnimation(getAnimation());
        exportMessage.setText(R.string.database_has_been_exported_correctly);

        showDatabaseButton.setAnimation(getAnimation());
        showDatabaseButton.setVisibility(View.VISIBLE);


        fileNameEditText.setVisibility(View.GONE);
        dividerFileName.setVisibility(View.GONE);


    }

    private Animation getAnimation(){
        return AnimationUtils.loadAnimation(getContext(), R.anim.fade_in_animation);
    }

    private void setFileNameText(){
        fileNameEditText.setText(getFileNameText());
    }

    private String getFileNameText(){
        return String.format("%s_%s", "backup", getDate());
    }

    private String getDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd_MM_yyyy");
        String date = simpleDateFormat.format(new Date());

        return date;
    }
}
