package project.minesweeper.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import project.minesweeper.MainActivity;
import project.minesweeper.R;
import project.minesweeper.model.MinesweeperModel;

/**
 * Created by Crystal on 10/1/2016.
 */
public class MinesweeperView extends View {

    private Paint paintBg;
    private Paint paintLine;
    private Paint paintText;

    public boolean flagOn = false;
    public boolean gameOn = true;

    private Bitmap bitmapFlag;
    private Bitmap bitmapBoom;
    private Bitmap bitmapWin;

    int flags = 0;

    public MinesweeperView(Context context, AttributeSet attrs) {
        super(context, attrs);

        bitmapFlag = BitmapFactory.decodeResource(getResources(),
                R.drawable.skull);
        bitmapBoom = BitmapFactory.decodeResource(getResources(),
                R.drawable.boom);
        bitmapWin = BitmapFactory.decodeResource(getResources(),
                R.drawable.win);

        paintBg = new Paint();
        paintBg.setColor(Color.rgb(255, 150, 255));
        paintBg.setStyle(Paint.Style.FILL);

        paintLine = new Paint();
        paintLine.setColor(Color.WHITE);
        paintLine.setStyle(Paint.Style.STROKE);
        paintLine.setStrokeWidth(5);

        paintText = new Paint();
        paintText.setColor(Color.BLACK);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawGameField(canvas);
        drawAction(canvas);
        if(!gameOn){
            drawEnd(canvas);
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);
        int d = w == 0 ? h : h == 0 ? w : w < h ? w : h;
        setMeasuredDimension(d, d);
    }

    private void drawGameField(Canvas canvas) {
        int boxSize = getWidth() / 5;
        // border
        canvas.drawRect(0, 0, getWidth(), getWidth(), paintBg);

        // two horizontal lines
        canvas.drawLine(0, boxSize, getWidth(), boxSize,
                paintLine);
        canvas.drawLine(0, 2 * boxSize, getWidth(), 2 * boxSize,
                paintLine);
        canvas.drawLine(0, 3 * boxSize, getWidth(), 3 * boxSize,
                paintLine);
        canvas.drawLine(0, 4 * boxSize, getWidth(), 4 * boxSize,
                paintLine);

        // two vertical lines
        canvas.drawLine(boxSize, 0, boxSize, getWidth(),
                paintLine);
        canvas.drawLine(2 * boxSize, 0, 2 * boxSize, getWidth(),
                paintLine);
        canvas.drawLine(3 * boxSize, 0, 3 * boxSize, getWidth(),
                paintLine);
        canvas.drawLine(4 * boxSize, 0, 4 * boxSize, getWidth(),
                paintLine);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Log.d("TAG_DRAW", "x: " + event.getX() + " # y: " + event.getY());

            int tX = ((int) event.getX()) / (getWidth() / 5);
            int tY = ((int) event.getY()) / (getWidth() / 5);

            if (gameOn && tX < 5 && tY < 5) {
                checkBox(tX, tY);
                invalidate();
            }
            if (flags == 3){
                gameOn = false;
                win();
            }
        }
        return true;
    }

    public void checkBox(int x, int y) {
        if (MinesweeperModel.getInstance().getField(x, y) == MinesweeperModel.EMPTY) {
            if (flagOn) {
                gameOn = false;
                gameOver();
            } else {
                MinesweeperModel.getInstance().setField(x, y, MinesweeperModel.NUM);
            }
        } else if (MinesweeperModel.getInstance().getField(x, y) == MinesweeperModel.MINE) {
            if (flagOn) {
                MinesweeperModel.getInstance().setField(x, y, MinesweeperModel.FLAG);
                flags += 1;
            } else {
                gameOn = false;
                gameOver();
            }
        }
    }

    public int mineNum(int x, int y) {
        int num = 0;
        int newX = 0;
        int newY = 0;
        for (int a = -1; a <= 1; a++) {
            for (int b = -1; b <= 1; b++) {
                newX = x + a;
                newY = y + b;
                if (newX >= 0 && newX <= 4 && newY >= 0 && newY <= 4) {
                    if (MinesweeperModel.getInstance().getField(newX, newY)
                            == MinesweeperModel.MINE || MinesweeperModel.getInstance().getField(newX, newY)
                            == MinesweeperModel.FLAG) {
                        num += 1;
                    }
                }
            }
        }
        return num;
    }

    private void drawAction(Canvas canvas) {
        int boxSize = getWidth() / 5;
        for (int x = 0; x <= 4; x++) {
            for (int y = 0; y <= 4; y++) {
                if (MinesweeperModel.getInstance().getField(x, y)
                        == MinesweeperModel.FLAG) {
                    //draw flag
                    bitmapFlag = Bitmap.createScaledBitmap(bitmapFlag, boxSize, boxSize, false);
                    canvas.drawBitmap(bitmapFlag, x * boxSize, y * boxSize, null);
                } else if (MinesweeperModel.getInstance().getField(x, y) == MinesweeperModel.NUM) {
                    //draw num
                    paintText.setTextSize(boxSize);
                    canvas.drawText("" + mineNum(x, y), x * boxSize+ 15,
                            y * boxSize + boxSize - 10, paintText);
                }
            }
        }
    }

    private void drawEnd(Canvas canvas) {
        if(flags == 3){
            bitmapWin = Bitmap.createScaledBitmap(bitmapWin, getWidth(), getWidth(), false);
            canvas.drawBitmap(bitmapWin, 0, 0, null);
        } else {
            bitmapBoom = Bitmap.createScaledBitmap(bitmapBoom, getWidth(), getWidth(), false);
            canvas.drawBitmap(bitmapBoom, 0, 0, null);
        }
    }

    public void flagButton() {
        if (flagOn) {
            flagOn = false;
            ((MainActivity) getContext()).showSimpleSnackbarMessage(
                    getResources().getString(R.string.flag_off)
            );
        } else {
            flagOn = true;
            ((MainActivity) getContext()).showSimpleSnackbarMessage(
                    getResources().getString(R.string.flag_on)
            );
        }
    }

    public void restartGame() {
        gameOn = true;
        flagOn = false;
        flags = 0;
        MinesweeperModel.getInstance().resetModel();
        invalidate();
    }

    public void gameOver() {
        if (flagOn) {
            ((MainActivity) getContext()).showSimpleSnackbarMessage(
                    getResources().getString(R.string.flag_game_over)
            );
        }
        ((MainActivity) getContext()).showSimpleSnackbarMessage(
                getResources().getString(R.string.mine_game_over)
        );
    }

    public void win() {
        ((MainActivity) getContext()).showSimpleSnackbarMessage(
                getResources().getString(R.string.win)
        );
    }
}