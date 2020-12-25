package com.example.questionandanswerdemo.Repositary;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.questionandanswerdemo.DataLoad.DataLoadListener;
import com.example.questionandanswerdemo.ViewDetails.QuestionView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FirebaseRepo2 {
    static FirebaseRepo2 instanse;
    private ArrayList<QuestionView> questionViews = new ArrayList<>();
    static Context mcontext;
    static DataLoadListener dataLoadListener;
    public  FirebaseRepo2 getInstance(Context context){
        mcontext=context;
        if (instanse==null){
            instanse=new FirebaseRepo2();
        }
        dataLoadListener= (DataLoadListener) mcontext;
        return instanse;
    }
    public MutableLiveData<ArrayList<QuestionView>> getQuestion(){
        loadquestions();
        MutableLiveData<ArrayList<QuestionView>> question=new MutableLiveData<>();
        question.setValue(questionViews);
        return question;
    }


    private void loadquestions() {
        DatabaseReference firebase= FirebaseDatabase.getInstance().getReference().child("Question");
        firebase.keepSynced(true);

        firebase.orderByChild("time").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                questionViews.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        QuestionView questionView=dataSnapshot1.getValue(QuestionView.class);
                        questionViews.add(questionView);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}