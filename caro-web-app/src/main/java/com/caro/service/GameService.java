package com.caro.service;

import com.caro.model.GameRoom;
import com.caro.model.Player;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameService {
    private final Map<String, GameRoom> rooms = new ConcurrentHashMap<>();
    
    public GameRoom createRoom() {
        String roomId = generateRoomId();
        GameRoom room = new GameRoom(roomId);
        rooms.put(roomId, room);
        return room;
    }
    
    public GameRoom joinRoom(String roomId, Player player) {
        GameRoom room = rooms.get(roomId);
        if (room == null) {
            return null;
        }
        
        if (room.getPlayer1() == null) {
            room.setPlayer1(player);
        } else if (room.getPlayer2() == null) {
            room.setPlayer2(player);
        } else {
            return null; // Room is full
        }
        
        return room;
    }
    
    public boolean makeMove(String roomId, String playerId, int row, int col) {
        GameRoom room = rooms.get(roomId);
        if (room == null) return false;
        
        // Validate move
        if (row < 0 || row >= 20 || col < 0 || col >= 20) return false;
        if (room.getBoard()[row][col] != 0) return false;
        
        // Check if it's player's turn
        boolean isPlayer1 = room.getPlayer1().getId().equals(playerId);
        if ((isPlayer1 && !room.isPlayer1Turn()) || (!isPlayer1 && room.isPlayer1Turn())) {
            return false;
        }
        
        // Make move
        room.getBoard()[row][col] = isPlayer1 ? 1 : 2;
        room.setPlayer1Turn(!room.isPlayer1Turn());
        
        return true;
    }
    
    private String generateRoomId() {
        return String.format("%04d", rooms.size() + 1);
    }
    
    public GameRoom getRoom(String roomId) {
        return rooms.get(roomId);
    }
}