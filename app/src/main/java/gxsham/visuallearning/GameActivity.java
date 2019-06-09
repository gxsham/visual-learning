package gxsham.visuallearning;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class GameActivity extends AppCompatActivity {

    private Integer totalHits;
    private Integer totalElements;
    private ArrayList<GameElement> gameElements;
    private Integer points;
    private Integer counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        points = 0;
        counter = 0;

        final EditText userInput = findViewById(R.id.userInput);
        userInput.setEnabled(true);
        userInput.setVisibility(View.VISIBLE);
        final Button checkResult = findViewById(R.id.checkResult);
        final TextView correntAnswers = findViewById(R.id.correctAnswers);
        final TextView previousWords = findViewById(R.id.previousWords);
        correntAnswers.setText(Integer.toString(points));

        totalHits = getIntent().getIntExtra("totalHits", 0);
        totalElements = getIntent().getIntExtra("totalElements", 0);

        gameElements = getIntent().getParcelableArrayListExtra("gameElements");

        String url = gameElements.get(counter).getImageUrl();
        ImageView imageView = findViewById(R.id.imageView);
        Glide.with(getApplicationContext())
                .load(url) // Remote URL of image.
                .into(imageView); //ImageView to set.

        checkResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] tags = gameElements.get(counter).getTags().toLowerCase().split(", ");
                List<String> arrayList = Arrays.asList(tags);
                StringTokenizer input = new StringTokenizer(userInput.getText().toString(), ",");
                while(input.hasMoreTokens()){
                    if(arrayList.contains(input.nextToken().trim().toLowerCase())){
                        points++;
                    }
                }

                correntAnswers.setText(Integer.toString(points) + " Points");
                previousWords.setText(gameElements.get(counter).getTags());
                counter ++;
                if(counter == totalElements){
                    userInput.setEnabled(false);
                    userInput.setVisibility(View.INVISIBLE);
                    checkResult.setText("Finish");
                    checkResult.setBackgroundColor(Color.GREEN);
                    checkResult.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            MainActivity.PointsMain += points;
                            finish();
                        }
                    });
                }
                else{
                    ImageView imageView = findViewById(R.id.imageView);
                    Glide.with(getApplicationContext())
                            .load(gameElements.get(counter).ImageUrl) // Remote URL of image.
                            .into(imageView); //ImageView to set.
                }
            }
        });

    }
}
