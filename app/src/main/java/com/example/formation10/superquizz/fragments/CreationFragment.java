package com.example.formation10.superquizz.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.formation10.superquizz.broadcast.NetworkChangeReceiver;
import com.example.formation10.superquizz.model.Question;
import com.example.formation10.superquizz.R;


public class CreationFragment extends Fragment {


    private EditText editTextIntitule, editTextProposition1, editTextProposition2, editTextProposition3, editTextProposition4;
    private RadioButton btn1, btn2, btn3, btn4;
    private FloatingActionButton btnOk;

    public CreationFragmentListener listener;

    private Question q;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        registerReceiver();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       View view = inflater.inflate(R.layout.fragment_creation, container, false);
        editTextIntitule = view.findViewById(R.id.editText_intitule);
        editTextProposition1 = view.findViewById(R.id.editText_proposition1);
        editTextProposition2 = view.findViewById(R.id.editText_proposition2);
        editTextProposition3 = view.findViewById(R.id.editText_proposition3);
        editTextProposition4 = view.findViewById(R.id.editText_proposition4);
        btn1 = view.findViewById(R.id.radioButton_1);
        btn2 = view.findViewById(R.id.radioButton_2);
        btn3 = view.findViewById(R.id.radioButton_3);
        btn4 = view.findViewById(R.id.radioButton_4);

        btnOk= view.findViewById(R.id.button_ok);

        View.OnClickListener radioButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton b = (RadioButton) v;
                if (b.isChecked()) {
                    btn1.setChecked(false);
                    btn2.setChecked(false);
                    btn3.setChecked(false);
                    btn4.setChecked(false);
                    b.setChecked(true);
                }
            }
        };

        btn1.setOnClickListener(radioButtonListener);
        btn2.setOnClickListener(radioButtonListener);
        btn3.setOnClickListener(radioButtonListener);
        btn4.setOnClickListener(radioButtonListener);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                q = new Question();
                q.setIntitule(editTextIntitule.getText().toString());
                q.addProposition(editTextProposition1.getText().toString());
                q.addProposition(editTextProposition2.getText().toString());
                q.addProposition(editTextProposition3.getText().toString());
                q.addProposition(editTextProposition4.getText().toString());

                if (btn1.isChecked()) {
                    q.setBonneReponse(editTextProposition1.getText().toString());
                }
                if (btn2.isChecked()) {
                    q.setBonneReponse(editTextProposition2.getText().toString());
                }
                if (btn3.isChecked()) {
                    q.setBonneReponse(editTextProposition3.getText().toString());
                }
                if (btn4.isChecked()) {
                    q.setBonneReponse(editTextProposition4.getText().toString());
                }


                listener.createQuestion(q);
            }
        });
        // Inflate the layout for this fragment
        return view;

    }


    /**
     * Return the question to the main activity
     */
    public interface CreationFragmentListener{
        void createQuestion(Question q);
    }

    private void registerReceiver(){
        try
        {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(NetworkChangeReceiver.NETWORK_CHANGE_ACTION);
            getActivity().registerReceiver(internalNetworkChangeReceiver, intentFilter);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        try
        {
            // Make sure to unregister internal receiver in onDestroy().
            getActivity().unregisterReceiver(internalNetworkChangeReceiver);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        super.onDestroy();
    }

    /**
     * This is internal BroadcastReceiver which get status from external receiver(NetworkChangeReceiver)
     * */
    InternalNetworkChangeReceiver internalNetworkChangeReceiver = new InternalNetworkChangeReceiver();
    class InternalNetworkChangeReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent) {
        Log.d("CONNECTIVITY",intent.getStringExtra("status"));
        Toast.makeText(getActivity(), "Pas de connexion ", Toast.LENGTH_LONG).show();
        }
    }
}
