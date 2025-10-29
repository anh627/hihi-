package com.caro.controller;

import com.caro.model.GameRoom;
import com.caro.model.Player;
import com.caro.service.GameService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class GameController {
    
    @Autowired
    private GameService gameService;
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    @MessageMapping("/create")
    @SendTo("/topic/rooms")
    public GameRoom createRoom(Player player) {
        GameRoom room = gameService.createRoom();
        gameService.joinRoom(room.getId(), player);
        return room;
    }
    
    @MessageMapping("/join")
    public void joinRoom(JoinRequest request) {
        GameRoom room = gameService.joinRoom(request.getRoomId(), request.getPlayer());
        if (room != null) {
            messagingTemplate.convertAndSend("/topic/room/" + room.getId(), room);
        }
    }
    
    @MessageMapping("/move")
    public void makeMove(MoveRequest request) {
        if (gameService.makeMove(request.getRoomId(), request.getPlayerId(), 
                request.getRow(), request.getCol())) {
            GameRoom room = gameService.getRoom(request.getRoomId());
            messagingTemplate.convertAndSend("/topic/room/" + room.getId(), room);
        }
    }
}

@Data
class JoinRequest {
    private String roomId;
    private Player player;
}

@Data
class MoveRequest {
    private String roomId;
    private String playerId;
    private int row;
    private int col;
}