package com.example.stepan.maze04;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class GameActivity extends AppCompatActivity {

    GameView gameView;
    private GestureDetectorCompat gestureObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        gameView = findViewById(R.id.game_view);
        gestureObject = new GestureDetectorCompat(this, new LearnGesture());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureObject.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    class LearnGesture extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onFling(MotionEvent downEvent, MotionEvent moveEvent, float velocityX, float velocityY) {
            boolean result = false;
            float yDif = moveEvent.getY() - downEvent.getY();
            float xDif = moveEvent.getX() - downEvent.getX();

            if(Math.abs(xDif) > 10 || Math.abs(yDif) > 10) {
                if (Math.abs(xDif) > Math.abs(yDif)) {
                    if (xDif > 0) {
                        gameView.directionPlayer(GameView.Direction.RIGHT);
                    } else {
                        gameView.directionPlayer(GameView.Direction.LEFT);
                    }
                } else {
                    if (yDif > 0) {
                        gameView.directionPlayer(GameView.Direction.DOWN);
                    } else {
                        gameView.directionPlayer(GameView.Direction.UP);
                    }
                }
                result = true;
            }
            return result;
        }
    }
}
