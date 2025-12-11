import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { carsAPI, parkingAPI } from '../api';

function Dashboard() {
  const [cars, setCars] = useState([]);
  const [activeSessions, setActiveSessions] = useState([]);
  const [availableSpots, setAvailableSpots] = useState(0);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      const [carsRes, sessionsRes, spotsRes] = await Promise.all([
        carsAPI.getCars(),
        parkingAPI.getActiveSessions(),
        parkingAPI.getAvailableSpots(),
      ]);
      setCars(carsRes.data);
      setActiveSessions(sessionsRes.data);
      setAvailableSpots(spotsRes.data.length);
    } catch (error) {
      console.error('Error loading dashboard data:', error);
    }
  };

  return (
    <div className="container">
      <h1 style={{ marginBottom: '2rem' }}>Dashboard</h1>
      
      <div className="grid grid-3">
        <div className="card">
          <h3>My Cars</h3>
          <p style={{ fontSize: '2rem', margin: '1rem 0' }}>{cars.length}</p>
          <Link to="/cars" className="btn btn-primary">Manage Cars</Link>
        </div>
        
        <div className="card">
          <h3>Active Parking Sessions</h3>
          <p style={{ fontSize: '2rem', margin: '1rem 0' }}>{activeSessions.length}</p>
          <Link to="/active" className="btn btn-primary">View Sessions</Link>
        </div>
        
        <div className="card">
          <h3>Available Spots</h3>
          <p style={{ fontSize: '2rem', margin: '1rem 0' }}>{availableSpots}</p>
          <Link to="/park" className="btn btn-success">Park a Car</Link>
        </div>
      </div>

      {activeSessions.length > 0 && (
        <div style={{ marginTop: '2rem' }}>
          <h2>Current Parking Sessions</h2>
          <div className="grid grid-2">
            {activeSessions.map((session) => (
              <div key={session.parkingSessionId} className="card">
                <h3>{session.car.make} {session.car.model}</h3>
                <p><strong>License Plate:</strong> {session.car.licensePlate}</p>
                <p><strong>Location:</strong> Floor {session.spot.floor}, Spot {session.spot.spotNumber}</p>
                <p><strong>Started:</strong> {new Date(session.startTime).toLocaleString()}</p>
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  );
}

export default Dashboard;
