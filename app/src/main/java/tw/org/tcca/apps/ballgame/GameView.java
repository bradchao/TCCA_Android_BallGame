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

import java.util.Timer;
import java.util.TimerTask;

public class GameView extends View {
    private Bitmap ball;
    private float viewW, viewH, ballW, ballH, ballX, ballY, dx, dy;

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(Color.BLACK);

        ball = BitmapFactory.decodeResource(context.getResources(), R.drawable.ball);

        timer.schedule(new RefreshTask(), 0, 17);
        timer.schedule(new BallTask(), 1000, 30);
    }

    private boolean isInit; // false
    private void init(){
        isInit = true;
        viewW = getWidth(); viewH = getHeight();
        ballW = viewW / 12; ballH = ballW;
        dx = dy = 16;
        Matrix matrix = new Matrix();
        matrix.postScale(ballW / ball.getWidth(), ballH / ball.getHeight());
        ball = Bitmap.createBitmap(ball, 0, 0, ball.getWidth(), ball.getHeight(), matrix, false);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isInit) init();

        canvas.drawBitmap(ball, ballX, ballY, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        ballX = event.getX() - ballW/2;
        ballY = event.getY() - ballH/2;
        invalidate();
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
        @Override
        public void run() {
            if (ballX < 0 || ballX + ballW > viewW){
                dx *= -1;
            }
            if (ballY < 0 || ballY + ballH > viewH){
                dy *= -1;
            }
            ballX += dx;
            ballY += dy;
        }
    }
}
