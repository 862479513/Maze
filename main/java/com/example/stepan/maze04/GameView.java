package com.example.stepan.maze04;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.BLUE;
import static android.graphics.Color.RED;
import static android.graphics.Color.YELLOW;

public class GameView extends View {

    public enum Direction {UP, DOWN, LEFT, RIGHT, STAND}
    private Cell[][] cells;
    private Cell player, fPlayer, exit, enemy1, fEnemy1, enemy2, fEnemy2, enemy3, fEnemy3, enemy4, fEnemy4;
    private int COLS = SettingsActivity.COLS, ROWS = SettingsActivity.ROWS;
    private static final float WALL_THICKNESS = 4;
    private Paint wallPaint, playerPaint, exitPaint, collectiblePaint, teleportPaint, enemyPaint;
    private Random random;
    private int xSpeed = 0, ySpeed = 0, collected = 0, allCollected = 0;
    private float cellSize, drawMoveCol = 0, drawMoveRow = 0, colMovement = 0, rowMovement = 0;
    private float drawColfEnemy1 = 0, drawRowfEnemy1 = 0, drawColfEnemy2 = 0, drawRowfEnemy2 = 0;
    private float drawColfEnemy3 = 0, drawRowfEnemy3 = 0, drawColfEnemy4 = 0, drawRowfEnemy4 = 0;
    private float scl = MainActivity.scl, countdown = 0;
    private ArrayList<Cell> teleports = new ArrayList<>();

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        wallPaint = new Paint();
        wallPaint.setColor(Color.WHITE);
        wallPaint.setStrokeWidth(WALL_THICKNESS);

        playerPaint = new Paint();
        playerPaint.setColor(Color.WHITE);

        exitPaint = new Paint();
        exitPaint.setColor(Color.WHITE);

        collectiblePaint = new Paint();
        collectiblePaint.setColor(YELLOW);

        teleportPaint = new Paint();
        teleportPaint.setColor(BLUE);

        enemyPaint = new Paint();
        enemyPaint.setColor(RED);

        random = new Random();

        createMaze();
    }

    private void removeWall(Cell current, Cell next) {
        if(current.col == next.col && current.row == next.row + 1){
            current.topWall = false;
            next.bottomWall = false;
        }
        if(current.col == next.col && current.row == next.row - 1){
            current.bottomWall = false;
            next.topWall = false;
        }
        if(current.col == next.col + 1 && current.row == next.row){
            current.leftWall = false;
            next.rightWall = false;
        }
        if(current.col == next.col - 1 && current.row == next.row){
            current.rightWall = false;
            next.leftWall = false;
        }
    }

    private Cell getNeighbour(Cell cell){
        ArrayList<Cell> neighbours = new ArrayList<>();

        if(cell.col > 0)
            if(!cells[cell.col - 1][cell.row].visited)
                neighbours.add(cells[cell.col - 1][cell.row]);

        if(cell.row > 0)
            if(!cells[cell.col][cell.row - 1].visited)
                neighbours.add(cells[cell.col][cell.row - 1]);

        if(cell.col < COLS - 1)
            if(!cells[cell.col + 1][cell.row].visited)
                neighbours.add(cells[cell.col + 1][cell.row]);

        if(cell.row < ROWS - 1)
            if(!cells[cell.col][cell.row + 1].visited)
                neighbours.add(cells[cell.col][cell.row + 1]);

        if (neighbours.size() > 0) {
            int index = random.nextInt(neighbours.size());
            return neighbours.get(index);
        } else {
            return null;
        }
    }

    private void getTeleports(){
        teleports.clear();

        for (int x = 0; x < COLS; x++) {
            for (int y = 0; y < ROWS; y++) {
                if(x + y != 0 && x + y != COLS + ROWS - 2) {
                    if ((cells[x][y].topWall && cells[x][y].rightWall && cells[x][y].bottomWall) ||
                            (cells[x][y].rightWall && cells[x][y].bottomWall && cells[x][y].leftWall) ||
                            (cells[x][y].bottomWall && cells[x][y].leftWall && cells[x][y].topWall) ||
                            (cells[x][y].leftWall && cells[x][y].topWall && cells[x][y].rightWall)) {
                        teleports.add(cells[x][y]);
                    }
                }
            }
        }
    }

    private void newCollection(){
        Cell collectibles;
        collected = 0;
        exitPaint.setColor(Color.GRAY);

        for (int x=0; x<COLS; x++) {
            for (int y=0; y<ROWS; y++) {
                cells[x][y].collectible = false;
            }
        }

        for (int i = 0; i < COLS*ROWS/20; i++) {
            collectibles = cells[random.nextInt(COLS - 1)][random.nextInt(ROWS - 1)];
            if (!collectibles.collectible) {
                collectibles.collectible = true;
            } else i--;
        }
        allCollected = COLS*ROWS/20;
    }

    private void createMaze() {
        Stack<Cell> stack = new Stack<>();
        Cell current, next, teleport;
        collected = 0;

        cells = new Cell[COLS][ROWS];

        for (int x=0; x<COLS; x++) {
            for (int y=0; y<ROWS; y++) {
                cells[x][y] = new Cell(x, y);
            }
        }

        current = cells[COLS - 1][ROWS - 1];
        current.visited = true;

        do {
            next = getNeighbour(current);
            if (next != null) {
                removeWall(current, next);
                stack.push(current);
                current = next;
                current.visited = true;
            } else {
                current = stack.pop();
            }
        } while(!stack.empty());

        if(SettingsActivity.collection) {
            newCollection();
        }

        if(SettingsActivity.teleportation){
            getTeleports();

            if(teleports.size() > 0) {
                for (int i = 0; i < teleports.size() / 2; i++) {
                    int index = random.nextInt(teleports.size());
                    teleport = teleports.get(index);

                    if (!teleport.teleportable) {
                        teleport.teleportable = true;
                    } else i--;
                }
            }
        }

        if(SettingsActivity.enemies){
            enemy1 = cells[COLS/2][ROWS/2];
            fEnemy1 = enemy1;

            if(COLS*ROWS >= 100) {
                enemy2 = cells[COLS/2][ROWS/2 - 1];
                fEnemy2 = enemy2;

                if (COLS * ROWS >= 200) {
                    enemy3 = cells[COLS/2 - 1][ROWS/2];
                    fEnemy3 = enemy3;

                    if (COLS * ROWS >= 300) {
                        enemy4 = cells[COLS/2 - 1][ROWS/2 - 1];
                        fEnemy4 = enemy4;
                    }
                }
            }
        }

        exit = cells[COLS - 1][ROWS - 1];
        player = cells[0][0];
        fPlayer = player;
    }

    protected void onDraw(Canvas canvas) {
        canvas.drawColor(BLACK);

        float height = getHeight();
        float width = getWidth();
        float mapSize = (float)COLS / (float)ROWS;
        float deviceSize = width / height;

        if (deviceSize < mapSize) {
            cellSize = width / (COLS + 1);
        } else {
            cellSize = height / (ROWS + 1);
        }

        float hGap = (width - COLS*cellSize)/2;
        float vGap = (height - ROWS*cellSize)/2;

        canvas.translate(hGap, vGap);
        
        float space = cellSize/10;

        if(SettingsActivity.light) {
            for (int x = 0; x < COLS; x++) {
                for (int y = 0; y < ROWS; y++) {
                    if (cells[x][y].topWall)
                        canvas.drawLine(x * cellSize, y * cellSize, (x + 1) * cellSize, y * cellSize, wallPaint);
                    if (cells[x][y].leftWall)
                        canvas.drawLine(x * cellSize, y * cellSize, x * cellSize, (y + 1) * cellSize, wallPaint);
                    if (cells[x][y].bottomWall)
                        canvas.drawLine(x * cellSize, (y + 1) * cellSize, (x + 1) * cellSize, (y + 1) * cellSize, wallPaint);
                    if (cells[x][y].rightWall)
                        canvas.drawLine((x + 1) * cellSize, y * cellSize, (x + 1) * cellSize, (y + 1) * cellSize, wallPaint);
                    if (cells[x][y].teleportable) {
                        canvas.drawRect(
                                x * cellSize + space,
                                y * cellSize + space,
                                (x + 1) * cellSize - space,
                                (y + 1) * cellSize - space,
                                teleportPaint
                        );
                    }
                    if (cells[x][y].collectible){
                        canvas.drawRect(
                                x*cellSize + 3*space,
                                y*cellSize + 3*space,
                                (x+1)*cellSize - 3*space,
                                (y+1)*cellSize - 3*space,
                                collectiblePaint
                        );
                    }
                }
            }
        } else {
            canvas.drawColor(BLACK);
            int checkX = 0, checkY = 0;
            if(player.col - 2 < 0) checkX = 2 - player.col;
            if(player.row - 2 < 0) checkY = 2 - player.row;

            for(int x = player.col - 2 + checkX; x < player.col + 3 && x < COLS; x++) {
                for(int y = player.row - 2 + checkY; y < player.row + 3 && y < ROWS; y++) {
                    if (cells[x][y].topWall)
                        canvas.drawLine(x * cellSize, y * cellSize, (x + 1) * cellSize, y * cellSize, wallPaint);
                    if (cells[x][y].leftWall)
                        canvas.drawLine(x * cellSize, y * cellSize, x * cellSize, (y + 1) * cellSize, wallPaint);
                    if (cells[x][y].bottomWall)
                        canvas.drawLine(x * cellSize, (y + 1) * cellSize, (x + 1) * cellSize, (y + 1) * cellSize, wallPaint);
                    if (cells[x][y].rightWall)
                        canvas.drawLine((x + 1) * cellSize, y * cellSize, (x + 1) * cellSize, (y + 1) * cellSize, wallPaint);
                    if (cells[x][y].teleportable) {
                        canvas.drawRect(
                                x * cellSize + space,
                                y * cellSize + space,
                                (x + 1) * cellSize - space,
                                (y + 1) * cellSize - space,
                                teleportPaint
                        );
                    }
                    if (cells[x][y].collectible){
                        canvas.drawRect(
                                x*cellSize + 3*space,
                                y*cellSize + 3*space,
                                (x+1)*cellSize - 3*space,
                                (y+1)*cellSize - 3*space,
                                collectiblePaint
                        );
                    }

                }
            }
        }

        canvas.drawRect(
                exit.col*cellSize + space,
                exit.row*cellSize + space,
                (exit.col + 1)*cellSize - space,
                (exit.row + 1)*cellSize - space,
                exitPaint
        );

        if(SettingsActivity.enemies) {
            if (countdown != 1) {
                canvas.drawRect(
                        fEnemy1.col * cellSize + space + drawColfEnemy1,
                        fEnemy1.row * cellSize + space + drawRowfEnemy1,
                        (fEnemy1.col + 1) * cellSize - space + drawColfEnemy1,
                        (fEnemy1.row + 1) * cellSize - space + drawRowfEnemy1,
                        enemyPaint
                );
                if(COLS*ROWS >= 100) {
                    canvas.drawRect(
                            fEnemy2.col * cellSize + space + drawColfEnemy2,
                            fEnemy2.row * cellSize + space + drawRowfEnemy2,
                            (fEnemy2.col + 1) * cellSize - space + drawColfEnemy2,
                            (fEnemy2.row + 1) * cellSize - space + drawRowfEnemy2,
                            enemyPaint
                    );
                    if(COLS*ROWS >= 200) {
                        canvas.drawRect(
                                fEnemy3.col * cellSize + space + drawColfEnemy3,
                                fEnemy3.row * cellSize + space + drawRowfEnemy3,
                                (fEnemy3.col + 1) * cellSize - space + drawColfEnemy3,
                                (fEnemy3.row + 1) * cellSize - space + drawRowfEnemy3,
                                enemyPaint
                        );
                        if (COLS * ROWS >= 300) {
                            canvas.drawRect(
                                    fEnemy4.col * cellSize + space + drawColfEnemy4,
                                    fEnemy4.row * cellSize + space + drawRowfEnemy4,
                                    (fEnemy4.col + 1) * cellSize - space + drawColfEnemy4,
                                    (fEnemy4.row + 1) * cellSize - space + drawRowfEnemy4,
                                    enemyPaint
                            );
                        }
                    }
                }
                countdown = Math.round((countdown + 1 / scl) * scl) / scl;
                moveEnemies();
            } else {
                canvas.drawRect(
                        enemy1.col * cellSize + space,
                        enemy1.row * cellSize + space,
                        (enemy1.col + 1) * cellSize - space,
                        (enemy1.row + 1) * cellSize - space,
                        playerPaint
                );
                if(COLS*ROWS >= 100) {
                    canvas.drawRect(
                            enemy2.col * cellSize + space,
                            enemy2.row * cellSize + space,
                            (enemy2.col + 1) * cellSize - space,
                            (enemy2.row + 1) * cellSize - space,
                            playerPaint
                    );
                    if (COLS * ROWS >= 200) {
                        canvas.drawRect(
                                enemy3.col * cellSize + space,
                                enemy3.row * cellSize + space,
                                (enemy3.col + 1) * cellSize - space,
                                (enemy3.row + 1) * cellSize - space,
                                playerPaint
                        );
                        if (COLS * ROWS >= 300) {
                            canvas.drawRect(
                                    enemy4.col * cellSize + space,
                                    enemy4.row * cellSize + space,
                                    (enemy4.col + 1) * cellSize - space,
                                    (enemy4.row + 1) * cellSize - space,
                                    playerPaint
                            );
                        }
                    }
                }
                countdown = 0;
                drawRowfEnemy1 = 0; drawColfEnemy1 = 0;
                drawRowfEnemy2 = 0; drawColfEnemy2 = 0;
                drawRowfEnemy3 = 0; drawColfEnemy3 = 0;
                drawRowfEnemy4 = 0; drawColfEnemy4 = 0;
                updateEnemies();
            }
        }

        if ((fPlayer.row + rowMovement != player.row) || (fPlayer.col + colMovement != player.col)) {
            canvas.drawRect(
                    fPlayer.col * cellSize + space + drawMoveCol,
                    fPlayer.row * cellSize + space + drawMoveRow,
                    (fPlayer.col + 1) * cellSize - space + drawMoveCol,
                    (fPlayer.row + 1) * cellSize - space + drawMoveRow,
                    playerPaint
            );

            movePlayer();
        } else {
            canvas.drawRect(
                    player.col * cellSize + space,
                    player.row * cellSize + space,
                    (player.col + 1) * cellSize - space,
                    (player.row + 1) * cellSize - space,
                    playerPaint
            );

            drawMoveRow = 0;
            drawMoveCol = 0;
            rowMovement = 0;
            colMovement = 0;
            updatePlayer();
        }
    }

    private void moveEnemies(){

        float[] enemyMovement1 = moveEnemy(enemy1, fEnemy1, drawColfEnemy1, drawRowfEnemy1);
        drawColfEnemy1 = enemyMovement1[0];
        drawRowfEnemy1 = enemyMovement1[1];

        if(COLS*ROWS >= 100) {
            float[] enemyMovement2 = moveEnemy(enemy2, fEnemy2, drawColfEnemy2, drawRowfEnemy2);
            drawColfEnemy2 = enemyMovement2[0];
            drawRowfEnemy2 = enemyMovement2[1];

            if (COLS * ROWS >= 200) {
                float[] enemyMovement3 = moveEnemy(enemy3, fEnemy3, drawColfEnemy3, drawRowfEnemy3);
                drawColfEnemy3 = enemyMovement3[0];
                drawRowfEnemy3 = enemyMovement3[1];

                if (COLS * ROWS >= 300) {
                    float[] enemyMovement4 = moveEnemy(enemy4, fEnemy4, drawColfEnemy4, drawRowfEnemy4);
                    drawColfEnemy4 = enemyMovement4[0];
                    drawRowfEnemy4 = enemyMovement4[1];
                }
            }
        }
    }

    private float[] moveEnemy(Cell cell, Cell fCell, Float x, Float y){
        float[] ans = new float[2];

        if(cell.row != fCell.row) {
            if (fCell.row - cell.row < 0) {
                y += cellSize / scl;
            } else {
                y -= cellSize / scl;
            }
        }
        if(cell.col != fCell.col){
            if (fCell.col - cell.col < 0) {
                x += cellSize / scl;
            } else {
                x -= cellSize / scl;
            }
        }
        ans[0] = x;
        ans[1] = y;
        return ans;
    }

    private void updateEnemies(){
        Cell save;

        save = enemy1;
        enemy1 = setEnemyDirection(enemy1, fEnemy1);
        fEnemy1 = save;

        if(COLS*ROWS >= 100) {
            save = enemy2;
            enemy2 = setEnemyDirection(enemy2, fEnemy2);
            fEnemy2 = save;

            if(COLS*ROWS >= 200) {
                save = enemy3;
                enemy3 = setEnemyDirection(enemy3, fEnemy3);
                fEnemy3 = save;

                if(COLS*ROWS >= 300) {
                    save = enemy4;
                    enemy4 = setEnemyDirection(enemy4, fEnemy4);
                    fEnemy4 = save;
                }
            }
        }
    }

    private Cell setEnemyDirection(Cell cell, Cell fCell){
        ArrayList<Cell> enemyNeighbours = new ArrayList<>();

        if(!cell.topWall && cell.row - 1 != fCell.row)
            enemyNeighbours.add(cells[cell.col][cell.row - 1]);
        if(!cell.leftWall && cell.col - 1 != fCell.col)
            enemyNeighbours.add(cells[cell.col - 1][cell.row]);
        if(!cell.bottomWall && cell.row + 1 != fCell.row)
            enemyNeighbours.add(cells[cell.col][cell.row + 1]);
        if(!cell.rightWall && cell.col + 1 != fCell.col)
            enemyNeighbours.add(cells[cell.col + 1][cell.row]);

        if(enemyNeighbours.size() != 0) {
            int index = random.nextInt(enemyNeighbours.size());
            return enemyNeighbours.get(index);
        } else {
            return cell;
        }
    }

    private void movePlayer(){

        if(player.row != fPlayer.row) {
            if (fPlayer.row - player.row < 0) {
                drawMoveRow = drawMoveRow + cellSize / scl;
                rowMovement = Math.round((rowMovement + 1/scl)*scl)/scl;
            } else {
                drawMoveRow = drawMoveRow - cellSize / scl;
                rowMovement = Math.round((rowMovement - 1/scl)*scl)/scl;
            }
        }
        if(player.col != fPlayer.col){
            if (fPlayer.col - player.col < 0) {
                drawMoveCol = drawMoveCol + cellSize / scl;
                colMovement = Math.round((colMovement + 1/scl)*scl)/scl;
            } else {
                drawMoveCol = drawMoveCol - cellSize / scl;
                colMovement = Math.round((colMovement - 1/scl)*scl)/scl;
            }
        }
        invalidate();
    }

    private void updatePlayer(){

        if(player.topWall && ySpeed == -1) {
            ySpeed = 0;
        }
        if(player.bottomWall && ySpeed == +1) {
            ySpeed = 0;
        }
        if(player.leftWall && xSpeed == -1) {
            xSpeed = 0;
        }
        if(player.rightWall && xSpeed == +1) {
            xSpeed = 0;
        }

        fPlayer = player;
        player = cells[player.col + xSpeed][player.row + ySpeed];

        checkDeath();
        checkCollection();
        checkExit();

        if (cells[fPlayer.col][fPlayer.row].teleportable){
            teleporting();
        }

        invalidate();
    }

    public void directionPlayer(Direction direction){

        switch (direction) {
            case UP:
                xSpeed = 0;
                ySpeed = -1;
                break;
            case DOWN:
                xSpeed = 0;
                ySpeed = +1;
                break;
            case LEFT:
                xSpeed = -1;
                ySpeed = 0;
                break;
            case RIGHT:
                xSpeed = +1;
                ySpeed = 0;
                break;
            case STAND:
                xSpeed = 0;
                ySpeed = 0;
                break;
        }

        invalidate();
    }

    private void checkDeath(){
        if(
                player == enemy1 || player == enemy2 || player == enemy3 || player == enemy4 ||
                fPlayer == enemy1 && player == fEnemy1 || fPlayer == enemy2 && player == fEnemy2 ||
                fPlayer == enemy3 && player == fEnemy3 || fPlayer == enemy4 && player == fEnemy4){

            Toast.makeText(getContext(), "You got caught", Toast.LENGTH_SHORT).show();
            player = cells[0][0];
            fPlayer = player;
            directionPlayer(Direction.STAND);

            if(enemy1 == player) {
                enemy1 = cells[COLS/2][ROWS/2];
                fEnemy1 = enemy1;
            }
            if (enemy2 == player) {
                enemy2 = cells[COLS/2 - 1][ROWS/2];
                fEnemy2 = enemy2;
            }
            if (enemy3 == player) {
                enemy3 = cells[COLS/2][ROWS/2 - 1];
                fEnemy3 = enemy3;
            }
            if (enemy4 == player) {
                enemy4 = cells[COLS/2 - 1][ROWS/2 - 1];
                fEnemy4 = enemy4;
            }

            if(SettingsActivity.collection){
                newCollection();
            }
        }
    }

    private void teleporting(){
        Cell teleportTo;

        int index = random.nextInt(teleports.size());
        teleportTo = teleports.get(index);

        if(!teleportTo.teleportable){
            player = teleportTo;
            fPlayer = teleportTo;
        } else teleporting();
    }

    private void checkCollection() {
        if (cells[fPlayer.col][fPlayer.row].collectible) {
            collected++;
            cells[fPlayer.col][fPlayer.row].collectible = false;
            Toast.makeText(getContext(), collected + "/" + allCollected, Toast.LENGTH_SHORT).show();
        }
        if(collected == allCollected)
            exitPaint.setColor(Color.WHITE);
    }

    private void checkExit() {
        if(player == exit) {
            if (collected == allCollected) {
                String congrats = congratsChoose();

                Toast toast = Toast.makeText(getContext(), congrats, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();

                directionPlayer(Direction.STAND);
                createMaze();
            } else if(fPlayer != exit) {
                Toast.makeText(getContext(), collected + "/" + allCollected, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String congratsChoose(){
        String getCongrat[] = {"Good Job!","Great Job!","Well Done!","Perfect!", "Mazed it!", "I'm amazed!","Keep it up!"};

        int index = random.nextInt(getCongrat.length);
        return getCongrat[index];
    }

    private class Cell{
        boolean
                topWall = true,
                bottomWall = true,
                leftWall = true,
                rightWall = true,
                collectible = false,
                teleportable = false,
                visited = false;

        int row, col;

        Cell(int col, int row) {
            this.col = col;
            this.row = row;
        }
    }
}