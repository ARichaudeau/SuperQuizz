package com.example.formation10.superquizz.api;

import android.util.Log;

import com.example.formation10.superquizz.Question;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class APIClient {

    private final OkHttpClient client = new OkHttpClient();

    private static APIClient sInstance;

    public static APIClient getInstance(){
        if (sInstance == null) {
            sInstance = new APIClient();
        }
        return sInstance;
    }

    public void getQuestions(final APIResult<List<Question>> result) {

        Request request = new Request.Builder()
                .url("http://192.168.10.38:3000/questions")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                result.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                List<Question> questions = new ArrayList<>();

                try {
                    JSONArray jsonArray = new JSONArray(response.body().string());

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject json = jsonArray.getJSONObject(i);
                        Question question = new Question();
                        question.setId(json.getInt("id"));
                        question.setIntitule(json.getString("title"));
                        question.addProposition(json.getString("answer_1"));
                        question.addProposition(json.getString("answer_2"));
                        question.addProposition(json.getString("answer_3"));
                        question.addProposition(json.getString("answer_4"));
                        switch (json.getInt("correct_answer")){
                            case 1 :
                                question.setBonneReponse(json.getString("answer_1"));
                                break;
                            case 2 :
                                question.setBonneReponse(json.getString("answer_2"));
                                break;
                            case 3 :
                                question.setBonneReponse(json.getString("answer_3"));
                                break;
                            case 4 :
                                question.setBonneReponse(json.getString("answer_4"));
                                break;
                        }

                        questions.add(question);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

          result.OnSuccess(questions);
            }
        });

        //TODO : Faire un update
        //TODO : Faire un delete
        //TODO : Faire un Create
    }

    public interface APIResult<T> {
        void onFailure(IOException e);
        void OnSuccess(T object) throws IOException;
    }
}
