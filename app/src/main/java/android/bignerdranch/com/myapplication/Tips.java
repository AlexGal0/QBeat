package android.bignerdranch.com.myapplication;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Tips extends FragmentActivity{

    private LinearLayout container;
    private ProgressBar progressBar;
    private ProgressBar progressBar2;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tips);

        container = findViewById(R.id.container_tips);
        progressBar = findViewById(R.id.progress_bar_tip);
        progressBar2 = findViewById(R.id.progress_bar_tips_2);


        progressBar.setVisibility(View.VISIBLE);
        progressBar2.setVisibility(View.VISIBLE);

        progressBar2.setProgress(0);


        DataBase.getDataBase().db.collection(References.TIPS_REFERENCE).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int n = queryDocumentSnapshots.getDocumentChanges().size();

                container.setVisibility(View.GONE);
                for(DocumentSnapshot doc: queryDocumentSnapshots.getDocuments()){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                        progressBar2.setProgress(progressBar2.getProgress() + 100/n, true);
                    else
                        progressBar2.setProgress(progressBar2.getProgress() + 100/n);

                    TextView textView = new TextView(Tips.this);
                    textView.setText("Tip #" + (container.getChildCount() + 1) +":\t" + doc.get("tip") + "");
                    container.addView(textView);
                }
                container.setVisibility(View.VISIBLE);
                progressBar2.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
            }
        });

    }
}
