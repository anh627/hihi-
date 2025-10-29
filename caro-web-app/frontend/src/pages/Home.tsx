import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Client } from '@stomp/stompjs';

const Home: React.FC = () => {
  const [playerName, setPlayerName] = useState('');
  const navigate = useNavigate();
  
  const createRoom = async () => {
    if (!playerName.trim()) return;
    
    const client = new Client({
      brokerURL: 'ws://localhost:8080/ws-caro',
      onConnect: () => {
        client.publish({
          destination: '/app/create',
          body: JSON.stringify({ name: playerName })
        });
        
        client.subscribe('/topic/rooms', message => {
          const room = JSON.parse(message.body);
          navigate(`/game/${room.id}`);
        });
      }
    });
    
    client.activate();
  };
  
  const joinRoom = async (roomId: string) => {
    if (!playerName.trim() || !roomId.trim()) return;
    navigate(`/game/${roomId}`);
  };
  
  return (
    <div className="home">
      <h1>Caro Game</h1>
      <input
        type="text"
        placeholder="Enter your name"
        value={playerName}
        onChange={(e) => setPlayerName(e.target.value)}
      />
      <button onClick={createRoom}>Create Room</button>
      <div>
        <input
          type="text"
          placeholder="Room ID"
          onChange={(e) => joinRoom(e.target.value)}
        />
      </div>
    </div>
  );
};

export default Home;