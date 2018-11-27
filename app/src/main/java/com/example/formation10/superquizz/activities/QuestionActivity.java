package com.example.formation10.superquizz.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.formation10.superquizz.asyncTask.DelayTask;
import com.example.formation10.superquizz.model.Question;
import com.example.formation10.superquizz.R;

import java.util.ArrayList;

public class QuestionActivity extends AppCompatActivity implements DelayTask.OnDelayTaskListener {

    private TextView textViewScreen;
    private Button btn1, btn2, btn3, btn4;
    private String reponse;
    private ProgressBar pb;
    static ArrayList<Question> questionList = new ArrayList<Question>();
    private Question q;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        textViewScreen = findViewById(R.id.textView_screen);
        btn1 = findViewById(R.id.button_digit_response_1);
        btn2 = findViewById(R.id.button_digit_response_2);
        btn3 = findViewById(R.id.button_digit_response_3);
        btn4 = findViewById(R.id.button_digit_response_4);

        q = getIntent().getParcelableExtra("Question");
        textViewScreen.setText(q.getIntitule());
        btn1.setText(q.getPropositions().get(0));
        btn2.setText(q.getPropositions().get(1));
        btn3.setText(q.getPropositions().get(2));
        btn4.setText(q.getPropositions().get(3));
        q.setBonneReponse(q.getBonneReponse());

        pb = findViewById(R.id.progressBar1);
        DelayTask delayTask = new DelayTask(this);
        delayTask.execute();
    }

    /*
    Lancement de l'activité TrueResponseActivity ou FalseResponseActivity en fonction du bouton sélectionné
     */
    public void onResponseClicked(View v){
        Button btn = (Button) v;
        reponse = btn.getText().toString();
        if (reponse.equals(q.getBonneReponse())){
            Intent intent = new Intent(this,TrueResponseActivity.class);
            this.startActivity(intent);
        } else {
            Intent intent = new Intent(this,FalseResponseActivity.class);
            this.startActivity(intent);
        }
    }

    @Override
    public void onWillStart() {
        pb.setVisibility(ProgressBar.VISIBLE);
    }

    @Override
    public void onProgress(int progress) {
        pb.setProgress(progress);
    }

}
