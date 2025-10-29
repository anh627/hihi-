import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { Client } from '@stomp/stompjs';

interface GameState {
  board: number[][];
  currentTurn: boolean;
  winner: number | null;
}

const Game: React.FC = () => {
  const { roomId } = useParams<{ roomId: string }>();
  const [gameState, setGameState] = useState<GameState>({
    board: Array(20).fill(null).map(() => Array(20).fill(0)),
    currentTurn: true,
    winner: null
  });
  
  const [stompClient, setStompClient] = useState<Client | null>(null);
  
  useEffect(() => {
    const client = new Client({
      brokerURL: 'ws://localhost:8080/ws-caro',
      onConnect: () => {
        client.subscribe(`/topic/room/${roomId}`, message => {
          const updatedRoom = JSON.parse(message.body);
          setGameState(prev => ({
            ...prev,
            board: updatedRoom.board,
            currentTurn: updatedRoom.player1Turn
          }));
        });
      }
    });
    
    client.activate();
    setStompClient(client);
    
    return () => {
      if (client.active) {
        client.deactivate();
      }
    };
  }, [roomId]);
  
  const makeMove = (row: number, col: number) => {
    if (!stompClient || gameState.board[row][col] !== 0) return;
    
    stompClient.publish({
      destination: '/app/move',
      body: JSON.stringify({
        roomId,
        playerId: localStorage.getItem('playerId'),
        row,
        col
      })
    });
  };
  
  return (
    <div className="game">
      <h2>Room: {roomId}</h2>
      <div className="board">
        {gameState.board.map((row, i) => (
          <div key={i} className="row">
            {row.map((cell, j) => (
              <button
                key={`${i}-${j}`}
                className={`cell ${cell === 1 ? 'x' : cell === 2 ? 'o' : ''}`}
                onClick={() => makeMove(i, j)}
              >
                {cell === 1 ? 'X' : cell === 2 ? 'O' : ''}
              </button>
            ))}
          </div>
        ))}
      </div>
    </div>
  );
};

export default Game;