package com.example.formation10.superquizz;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.widget.ProgressBar;

public class DelayTask extends AsyncTask<Void, Integer, String> {
    int count = 0;
    OnDelayTaskListener listener;

    public DelayTask (OnDelayTaskListener listener){
        super();
        this.listener=listener;
    }

    @Override
    protected void onPreExecute() {
        listener.onWillStart();
    }

    @Override
    protected String doInBackground(Void... params) {

        float durationSec = 60;
        float stepMs = 200;
        float totalProgress = 100;

        while (count < durationSec) {
            SystemClock.sleep((int)stepMs);
            count++;
            publishProgress((int)(count * stepMs * totalProgress / (durationSec * 1000)));
        }
        return "Complete";
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        listener.onProgress(values[0]);
    }

    public interface OnDelayTaskListener{
        void onWillStart();
        void onProgress(int progress);
    }
}
