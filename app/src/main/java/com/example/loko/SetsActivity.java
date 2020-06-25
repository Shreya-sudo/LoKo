package com.example.loko;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SetsActivity extends AppCompatActivity {
private GridView gridView;
private Toolbar toolbar;
private Dialog LoadingDialogue;
public static int CATEGORY_ID;
private FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sets);
        toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String Title= getIntent().getStringExtra("CAT");
        getSupportActionBar().setTitle(Title);
        gridView= findViewById(R.id.GridView);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        firestore=FirebaseFirestore.getInstance();
        loadset();
        CATEGORY_ID=getIntent().getIntExtra("CATEGORY_ID",1);

        LoadingDialogue= new Dialog(SetsActivity.this);
         LoadingDialogue.setContentView(R.layout.loading_progressbar);
         LoadingDialogue.setCancelable(false);
         LoadingDialogue.getWindow().setBackgroundDrawableResource(R.drawable.progress_background);
         LoadingDialogue.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
         LoadingDialogue.show();
    }
    public void loadset(){
        {

            firestore.collection("QUIZ").document("CAT"+String.valueOf(CATEGORY_ID)).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot doc= task.getResult();
                        if(doc.exists()){

                            long sets= (long) doc.get("SETS");
                            setAdapter adapter= new setAdapter(Integer.valueOf((int)sets));
                            gridView.setAdapter(adapter);

                        }
                        else
                        {
                            Toast.makeText(SetsActivity.this, "NO SET DOCUMENT EXISTS", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                    else{
                        Toast.makeText(SetsActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                    LoadingDialogue.cancel();
                }
            });

        }

    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            SetsActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}