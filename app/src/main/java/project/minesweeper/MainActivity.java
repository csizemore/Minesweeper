package project.minesweeper;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import project.minesweeper.view.MinesweeperView;

public class MainActivity extends AppCompatActivity {

    private LinearLayout layoutContent;
    private MinesweeperView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layoutContent = (LinearLayout) findViewById(R.id.layoutContent);
        gameView =
                (MinesweeperView) findViewById(R.id.gameView);

        Button btnFlag = (Button) findViewById(R.id.btnFlag);
        btnFlag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gameView.gameOn){
                    gameView.flagButton();
                }
            }
        });

        Button btnRestart = (Button) findViewById(R.id.btnRestart);
        btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameView.restartGame();
            }
        });
    }

    public void showSimpleSnackbarMessage(String message) {
        Snackbar.make(layoutContent, message, Snackbar.LENGTH_LONG).show();
    }
}