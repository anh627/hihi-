package com.caro.model;

import lombok.Data;

@Data
public class Player {
    private String id;
    private String name;
    private boolean ready;
    
    public Player(String id, String name) {
        this.id = id;
        this.name = name;
        this.ready = false;
    }
}