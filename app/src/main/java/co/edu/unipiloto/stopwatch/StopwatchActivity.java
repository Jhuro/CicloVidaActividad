package co.edu.unipiloto.stopwatch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class StopwatchActivity extends AppCompatActivity {

    private int seconds = 0;
    private int stopwatchSeconds = 0;
    private int lap = 1;
    private boolean running;
    private ListView lapsList;
    private ArrayList<String> laps;
    private int secondsLastLap = -1;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stopwatch);
        lapsList = (ListView) findViewById(R.id.laps_list);
        laps = new ArrayList<>();
        adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, laps);
        lapsList.setAdapter(adapter);

        if(savedInstanceState != null){
            seconds = savedInstanceState.getInt("seconds");
            running = savedInstanceState.getBoolean("running");
            lap = savedInstanceState.getInt("lap");
            secondsLastLap = savedInstanceState.getInt("secondsLastLap");
            ArrayList<String> savedLaps= savedInstanceState.getStringArrayList("laps");
            for(String lap: savedLaps){
                laps.add(lap);
            }
            adapter.notifyDataSetChanged();
        }
        runTimer();
    }

    private void runTimer(){
        final TextView timeView = (TextView) findViewById(R.id.time_view);
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                stopwatchSeconds = seconds;
                timeView.setText(stringTime(stopwatchSeconds));
                if(running){
                    seconds++;
                }
                handler.postDelayed(this, 1000);
            }
        });
    }

    public void onClickStart(View view){
        running = true;
    }

    public void onClickStop(View view){
        running = false;
    }

    public void onClickReset(View view){
        running = false;
        seconds = 0;
        lap = 1;
        laps.clear();
        secondsLastLap = -1;
        adapter.notifyDataSetChanged();
    }

    public void onClickLap(View view){
        if(running){
            String lapTime;
            int secondsLap = stopwatchSeconds;

            if(secondsLastLap!=-1){
                lapTime = stringTime(secondsLap - secondsLastLap);
            }else{
                lapTime = stringTime(secondsLap);
            }

            laps.add("Lap " + lap + ":  +"  + lapTime + "       " + stringTime(secondsLap));

            adapter.notifyDataSetChanged();
            secondsLastLap = secondsLap;
            lap++;
        }
    }

    public String stringTime(int seconds){

        int hours = seconds/3600;
        int minutes = (seconds%3600)/60;
        int secs = seconds%60;
        String time = String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, secs);
        return time;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("seconds", seconds);
        savedInstanceState.putInt("lap", lap);
        savedInstanceState.putStringArrayList("laps", laps);
        savedInstanceState.putInt("secondsLastLap", secondsLastLap);
        savedInstanceState.putBoolean("running", running);
    }
}