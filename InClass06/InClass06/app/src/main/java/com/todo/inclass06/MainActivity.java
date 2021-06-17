// Assignment # In class 06.
// MainActivity.java
// Dayakar Ravuri and Anoosh Hari Group 29.


package com.todo.inclass06;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Double> countList = new ArrayList<>();
    private int executions;

    SeekBar seekBar;
    TextView seekBarSelectionText;
    Button byThread;
    Button byAsyncTask;

    Handler handler;

    ProgressBar progressBar;
    TextView currentProgress;
    TextView average;

    ListView listView;
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        byThread = findViewById(R.id.buttonByThread);
        byAsyncTask = findViewById(R.id.buttonByAsyncTask);
        seekBar = findViewById(R.id.seekBar);
        seekBarSelectionText = findViewById(R.id.seekBarSelection);
        String seekBarText;

        progressBar = findViewById(R.id.currentProgressSeekbar);
        currentProgress = findViewById(R.id.currentProgressValue);
        average = findViewById(R.id.averageText);

        listView = findViewById(R.id.listView);

        arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, countList);
        listView.setAdapter(arrayAdapter);

        updateVisibility(false);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                executions = progress;
                progressBar.setMax(executions);
                seekBarSelectionText.setText(progress + " " + getString(R.string.timeslabel));
                currentProgress.setText(countList.size() + getString(R.string.forwardslash) + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBarSelectionText.setText(seekBar.getProgress() + " " + getString(R.string.timeslabel));

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                ArrayList<Double> currentObject = (ArrayList<Double>) msg.obj;
                if (currentObject.size() != 0) {
                    double avg = 0;
                    for (double d : currentObject) {
                        avg += d;
                    }
                    average.setText(getString(R.string.averageLabel) + avg / currentObject.size());
                    currentProgress.setText(currentObject.size() + getString(R.string.forwardslash) + executions);
                    progressBar.setProgress(currentObject.size());
                    countList = currentObject;
                    arrayAdapter.notifyDataSetChanged();
                }
                if (currentObject.size() == seekBar.getProgress()) buttonStatus(true);
                return false;
            }
        });

        byThread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (seekBar.getProgress() !=0) {
                    clearContents();
                    updateVisibility(true);
                    buttonStatus(false);
                    ExecutorService poolExecutor = Executors.newFixedThreadPool(getResources().getInteger(R.integer.number_of_threads));
                    poolExecutor.execute(new BackGroundTaskByThread());
                } else {
                    Toast.makeText(MainActivity.this, getString(R.string.zero_msg), Toast.LENGTH_SHORT).show();
                }
            }
        });

        byAsyncTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (seekBar.getProgress() !=0) {
                    clearContents();
                    updateVisibility(true);
                    buttonStatus(false);
                    new BackGroundTaskByAsyncTask().execute(executions);
                } else {
                    Toast.makeText(MainActivity.this, getString(R.string.zero_msg), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void buttonStatus(boolean isVisible) {
        byThread.setEnabled(isVisible);
        byAsyncTask.setEnabled(isVisible);
        seekBar.setEnabled(isVisible);
    }

    public void updateVisibility(boolean isVisible) {
        if (isVisible) {
            currentProgress.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            average.setVisibility(View.VISIBLE);
            listView.setVisibility(View.VISIBLE);
        } else {
            //Clear contents and make invisible
            currentProgress.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            average.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.INVISIBLE);
        }
    }

    public void clearContents() {
        countList.clear();
        average.setText(getString(R.string.averageLabel) + new Double(0).toString());
        currentProgress.setText(0 + getString(R.string.forwardslash) + executions);
        progressBar.setProgress(0);
        arrayAdapter.notifyDataSetChanged();
    }

    class BackGroundTaskByAsyncTask
            extends AsyncTask<Integer, ArrayList<Double>, ArrayList<Double>> {
        ArrayList<Double> doubles = new ArrayList<>();

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(ArrayList<Double> value) {
            buttonStatus(true);
        }

        @Override
        protected void onProgressUpdate(ArrayList<Double>... values) {
            updateView(values[0]);
        }

        private void updateView(ArrayList<Double> value) {
            ArrayList<Double> currentObject = value;
            double avg = 0;
            for (double d : currentObject) {
                avg += d;
            }
            average.setText(getString(R.string.averageLabel) + avg / currentObject.size());
            currentProgress.setText(currentObject.size() + getString(R.string.forwardslash) + executions);
            progressBar.setProgress(currentObject.size());
            arrayAdapter.notifyDataSetChanged();
        }

        @Override
        protected ArrayList<Double> doInBackground(Integer... integers) {
            for (int i = 0; i < integers[0]; i++) {
                countList.add(HeavyWork.getNumber());
                publishProgress(countList);
            }
            return countList;
        }
    }

    class BackGroundTaskByThread implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < seekBar.getProgress(); i++) {
                countList.add(HeavyWork.getNumber());
                Message message = new Message();
                message.obj = countList;
                handler.sendMessage(message);
            }
        }
    }
}