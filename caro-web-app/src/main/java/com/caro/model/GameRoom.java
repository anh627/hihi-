package com.caro.model;

import lombok.Data;

@Data
public class GameRoom {
    private String id;
    private Player player1;
    private Player player2;
    private int[][] board;
    private boolean player1Turn;
    private GameStatus status;
    
    public GameRoom(String id) {
        this.id = id;
        this.board = new int[20][20]; // 20x20 board
        this.player1Turn = true;
        this.status = GameStatus.WAITING;
    }
    
    public boolean isFull() {
        return player1 != null && player2 != null;
    }
}

enum GameStatus {
    WAITING,    // Waiting for second player
    PLAYING,    // Game in progress
    FINISHED    // Game ended
}