package com.example.loko;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static com.example.loko.SetsActivity.CATEGORY_ID;

public class QuestionActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseFirestore firestore;
    private TextView question, number, timer;
    private TextView optiona, optionb, optionc, optiond;
    private List<Question> questionList;
    private LinearLayout layout;
    private int count = 0;
    private int quetionNum;
    private int score;
    private CountDownTimer countdown;
    private int SET_NUM;
    private Dialog LoadingDialogue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        question = findViewById(R.id.question);
        number = findViewById(R.id.num);
        optiona = findViewById(R.id.optiona);
        optionb = findViewById(R.id.optionb);
        optionc = findViewById(R.id.optionc);
        optiond = findViewById(R.id.optiond);
        timer = findViewById(R.id.timer);
        optiona.setOnClickListener((View.OnClickListener) this);
        optionb.setOnClickListener((View.OnClickListener) this);
        optionc.setOnClickListener((View.OnClickListener) this);
        optiond.setOnClickListener((View.OnClickListener) this);

        LoadingDialogue= new Dialog(QuestionActivity.this);
        LoadingDialogue.setContentView(R.layout.loading_progressbar);
        LoadingDialogue.setCancelable(false);
        LoadingDialogue.getWindow().setBackgroundDrawableResource(R.drawable.progress_background);
        LoadingDialogue.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        LoadingDialogue.show();


        SET_NUM= getIntent().getIntExtra("SET_NUM",1);
        firestore=FirebaseFirestore.getInstance();

        getQuesionList();
        score=0;

    }
    private void getQuesionList() {

        questionList = new ArrayList<>();
        firestore.collection("Quiz").document("CAT"+String.valueOf(CATEGORY_ID))
        .collection("SET"+String.valueOf(SET_NUM)).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                QuerySnapshot questions= task.getResult();
                if(task.isSuccessful()){
                for(QueryDocumentSnapshot doc: questions){
                    questionList.add(new Question(doc.getString("QUESTION"),doc.getString("A")
                    ,doc.getString("B"),doc.getString("C"),doc.getString("D"),Integer.valueOf(doc.getString("ANSWER"))));
                }
                    setQuestion();}
                else {
                Toast.makeText(QuestionActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
            }
                    LoadingDialogue.cancel();
            }
        });

    }

    private void setQuestion() {
        timer.setText(String.valueOf(10));
        question.setText(questionList.get(0).getQuestion());
        optiona.setText(questionList.get(0).getOptiona());
        optionb.setText(questionList.get(0).getOptionb());
        optionc.setText(questionList.get(0).getOptionc());
        optiond.setText(questionList.get(0).getOptiond());

        number.setText(String.valueOf(1) + "/" + String.valueOf(questionList.size()));
        countdown.start();
        quetionNum = 0;
            startTimer();
    }

    private void startTimer() {
        countdown = new CountDownTimer(12000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(millisUntilFinished<10)
                timer.setText(String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                changeQuestion();
            }
        };
        countdown.start();
    }


    @Override
    public void onClick(View v) {
        int selectedoption = 0;
        switch (v.getId()) {
            case R.id.optiona:
                selectedoption = 1;
                break;
            case R.id.optionb:
                selectedoption = 2;
                break;
            case R.id.optionc:
                selectedoption = 3;
                break;
            case R.id.optiond:
                selectedoption = 4;
                break;
            default:
        }
        countdown.cancel();
        checkanswer(selectedoption,v);
    }

    private void checkanswer(int selectedoption,View view) {
        if (selectedoption == questionList.get(quetionNum).getCorrect()) {
            //right answer
            ((TextView)view).setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
            score++;
        } else {
            //wronganswer
            ((TextView)view).setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            switch(questionList.get(quetionNum).getCorrect()){
                case 1:
                    optiona.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;
                case 2:
                    optionb.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;
                case 3:
                    optionc.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;
                case 4:
                    optionc.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;
            }
        }
        Handler handler= new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        },2000);

    }

    private void changeQuestion() {
        if (quetionNum < questionList.size() - 1) {

            quetionNum++;

            playAnim(question, 0,0);
            playAnim(optiona, 0,1);
            playAnim(optionb, 0,2);
            playAnim(optionc, 0,3);
            playAnim(optiond, 0,4);

            number.setText(String.valueOf(quetionNum+1)+ "/"+String.valueOf(questionList.size()));

            timer.setText(String.valueOf(10));
            startTimer();


        } else {
            //go to score}
            Intent intent = new Intent(QuestionActivity.this, Score.class);
            intent.putExtra("SCORE",String.valueOf(score)+"/"+String.valueOf(questionList.size()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            //QuestionActivity.this.finish();


        }
    }
    private void playAnim(final View view, final int value, final int viewNum){
        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500).setStartDelay(100).setInterpolator(new DecelerateInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if(value==0){
                            switch(viewNum) {
                                case 0:
                                    ((TextView)view).setText(questionList.get(quetionNum).getQuestion());
                                    break;
                                case 1:
                                    ((TextView)view).setText(questionList.get(quetionNum).getOptiona());
                                    break;
                                case 2:
                                    ((TextView)view).setText(questionList.get(quetionNum).getOptionb());
                                    break;
                                case 3:
                                    ((TextView)view).setText(questionList.get(quetionNum).getOptionc());
                                    break;
                                case 4:
                                    ((TextView)view).setText(questionList.get(quetionNum).getOptiond());
                                    break;
                            }
                            if(viewNum!=0)
                            {
                                ((Button)view).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ab6f92")));
                            }
                            playAnim(view,1,viewNum);
                        }

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
    }
    public void onBackPressed() {
        super.onBackPressed();

        countdown.cancel();
    }
}