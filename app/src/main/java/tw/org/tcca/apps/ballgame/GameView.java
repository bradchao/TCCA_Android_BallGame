package tw.org.tcca.apps.ballgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

public class GameView extends View {
    private Bitmap ball;
    private float viewW, viewH, ballW, ballH;
    private LinkedList<BallTask> balls = new LinkedList<>();

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(Color.BLACK);

        ball = BitmapFactory.decodeResource(context.getResources(), R.drawable.ball);

        timer.schedule(new RefreshTask(), 0, 17);
    }

    private boolean isInit; // false
    private void init(){
        isInit = true;
        viewW = getWidth(); viewH = getHeight();
        ballW = viewW / 12; ballH = ballW;
        Matrix matrix = new Matrix();
        matrix.postScale(ballW / ball.getWidth(), ballH / ball.getHeight());
        ball = Bitmap.createBitmap(ball, 0, 0, ball.getWidth(), ball.getHeight(), matrix, false);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isInit) init();
        for (BallTask ballTask : balls) {
            canvas.drawBitmap(ball, ballTask.x, ballTask.y, null);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float ballX = event.getX() - ballW/2;
        float ballY = event.getY() - ballH/2;
        BallTask ballTask = new BallTask(ballX, ballY);
        balls.add(ballTask);
        timer.schedule(ballTask, 500, 30);
        return false; //super.onTouchEvent(event);
    }

    private Timer timer = new Timer();
    private class RefreshTask extends TimerTask {
        @Override
        public void run() {
            invalidate();
        }
    }
    private class BallTask extends TimerTask {
        float x, y, dx, dy;
        BallTask(float x, float y){
            dx = dy = 16;
            this.x = x; this.y = y;
        }
        @Override
        public void run() {
            if (x < 0 || x + ballW > viewW){
                dx *= -1;
            }
            if (y < 0 || y + ballH > viewH){
                dy *= -1;
            }
            x += dx;
            y += dy;
        }
    }
}
