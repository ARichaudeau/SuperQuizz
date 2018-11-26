package com.example.formation10.superquizz.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.formation10.superquizz.Question;

import java.util.ArrayList;
import java.util.List;

public class QuestionsDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "";
    private static QuestionsDatabaseHelper Instance;

    // Infos de la base de données
    private static final String DATABASE_NAME = "qcm";
    private static final int DATABASE_VERSION = 1;

    // Nom de la table
    private static final String TABLE_QCM = "qcm";

    // Colonnes de la table qcm
    private static final String KEY_QUESTION_ID = "question_id";
    private static final String KEY_INTITULE = "intitule";
    private static final String KEY_PROPOSITION_1 = "proposition_1";
    private static final String KEY_PROPOSITION_2 = "proposition_2";
    private static final String KEY_PROPOSITION_3 = "proposition_3";
    private static final String KEY_PROPOSITION_4 = "proposition_4";
    private static final String KEY_BONNE_REPONSE = "bonne_reponse";
    private static final String KEY_REPONSE_SAISIE = "reponse_saisie";


    private QuestionsDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    // Méthode appelée lorsque la connection de la base de données est configurée
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
    }

    // Méthode appelée lorsque la base de données qcm est créée pour la première fois
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_QCM_TABLE = "CREATE TABLE " + TABLE_QCM +
                "(" +
                KEY_QUESTION_ID + " INTEGER PRIMARY KEY, " +
                KEY_INTITULE + " VARCHAR, " +
                KEY_PROPOSITION_1 + " VARCHAR, " +
                KEY_PROPOSITION_2 + " VARCHAR, " +
                KEY_PROPOSITION_3 + " VARCHAR, " +
                KEY_PROPOSITION_4 + " VARCHAR, " +
                KEY_BONNE_REPONSE + " VARCHAR, " +
                KEY_REPONSE_SAISIE + " VARCHAR" +
                ")";
        db.execSQL(CREATE_QCM_TABLE);
    }

    // Méthode appelée lorsque la base de données a besoin d'être mise à jour
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_QCM);
            onCreate(db);
        }
    }


    // ******* Singleton *******
    public static synchronized QuestionsDatabaseHelper getInstance(Context context) {
        if (Instance == null) {
            Instance = new QuestionsDatabaseHelper(context.getApplicationContext());
        }
        return Instance;
    }


    /**
     * ******* CRUD *******
     * CREATE : Insérer une question dans la base de données
     * @param question qui sera creé dans la base
     */
    public void addQuestion(Question question) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_QUESTION_ID, question.getId());
            values.put(KEY_INTITULE, question.getIntitule());
            values.put(KEY_PROPOSITION_1, question.getPropositions().get(0));
            values.put(KEY_PROPOSITION_2, question.getPropositions().get(1));
            values.put(KEY_PROPOSITION_3, question.getPropositions().get(2));
            values.put(KEY_PROPOSITION_4, question.getPropositions().get(3));
            values.put(KEY_BONNE_REPONSE, question.getBonneReponse());

            db.insertOrThrow(TABLE_QCM, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Erreur lors de l'insertion d'une question");
        } finally {
            db.endTransaction();
        }
    }

    /**
     * ******* CRUD *******
     * READ : Lire toutes les questions de la base de données
     * @param
     */
    public List<Question> getAllQuestions() {
        List<Question> questions = new ArrayList<>();
        String QUESTIONS_SELECT_QUERY =
                String.format("SELECT * FROM %s",
                        TABLE_QCM);
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(QUESTIONS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Question newQuestion = new Question();
                    newQuestion.setId(cursor.getInt(cursor.getColumnIndex(KEY_QUESTION_ID)));
                    newQuestion.setIntitule(cursor.getString(cursor.getColumnIndex(KEY_INTITULE)));
                    newQuestion.addProposition(cursor.getString(cursor.getColumnIndex(KEY_PROPOSITION_1)));
                    newQuestion.addProposition(cursor.getString(cursor.getColumnIndex(KEY_PROPOSITION_2)));
                    newQuestion.addProposition(cursor.getString(cursor.getColumnIndex(KEY_PROPOSITION_3)));
                    newQuestion.addProposition(cursor.getString(cursor.getColumnIndex(KEY_PROPOSITION_4)));
                    newQuestion.setBonneReponse(cursor.getString(cursor.getColumnIndex(KEY_BONNE_REPONSE)));

                    questions.add(newQuestion);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Erreur lors de l'insertion des questions dans la base de données");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return questions;
    }


    public void synchroniseDatabaseQuestions(List<Question> serverQuestions) {


        List<Question> databaseQuestions = getAllQuestions();


        // Here we will choose if we need to add or to update the question return by the server
        for (Question serverQuestion : serverQuestions) {
            boolean found = false;
            for (Question dataBaseQuestion : databaseQuestions) {
                if (serverQuestion.getId() == dataBaseQuestion.getId()) {
                    found = true;
                    break;
                }
            }

            if (found) {
                SQLiteDatabase db = getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(KEY_INTITULE, serverQuestion.getIntitule());
                values.put(KEY_PROPOSITION_1,serverQuestion.getPropositions().get(0));
                values.put(KEY_PROPOSITION_2, serverQuestion.getPropositions().get(1));
                values.put(KEY_PROPOSITION_3, serverQuestion.getPropositions().get(2));
                values.put(KEY_PROPOSITION_4, serverQuestion.getPropositions().get(3));
                values.put(KEY_BONNE_REPONSE, serverQuestion.getBonneReponse());
                db.update(TABLE_QCM, values, KEY_QUESTION_ID + " = ?",
                    new String[]{String.valueOf(serverQuestion.getId())});

            } else {
                SQLiteDatabase db = getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(KEY_QUESTION_ID, serverQuestion.getId());
                values.put(KEY_INTITULE, serverQuestion.getIntitule());
                values.put(KEY_PROPOSITION_1, serverQuestion.getPropositions().get(0));
                values.put(KEY_PROPOSITION_2, serverQuestion.getPropositions().get(1));
                values.put(KEY_PROPOSITION_3, serverQuestion.getPropositions().get(2));
                values.put(KEY_PROPOSITION_4, serverQuestion.getPropositions().get(3));
                values.put(KEY_BONNE_REPONSE, serverQuestion.getBonneReponse());

                db.insert(TABLE_QCM, null, values);
            }
        }

        // Now we want to delete the question if thy are not on the server anymore
        for (Question dataBaseQuestion : databaseQuestions) {
            boolean found = false;
            for (Question serverQuestion : serverQuestions) {
                if (serverQuestion.getId() == dataBaseQuestion.getId()) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                //TODO DELETE database question from the database
            }
        }

    }

}
