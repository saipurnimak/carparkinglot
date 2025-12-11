import React, { useEffect, useState } from 'react';
import { parkingAPI } from '../api';

function ActiveSessions() {
  const [sessions, setSessions] = useState([]);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  useEffect(() => {
    loadSessions();
  }, []);

  const loadSessions = async () => {
    try {
      const response = await parkingAPI.getActiveSessions();
      setSessions(response.data);
    } catch (error) {
      console.error('Error loading sessions:', error);
    }
  };

  const handleLeave = async (sessionId) => {
    if (window.confirm('Are you sure you want to leave this parking spot?')) {
      setError('');
      setSuccess('');

      try {
        await parkingAPI.leaveParkingSpot(sessionId);
        setSuccess('Parking spot freed successfully!');
        loadSessions();
      } catch (err) {
        setError(err.response?.data?.error || 'Error leaving parking spot');
      }
    }
  };

  const calculateDuration = (startTime) => {
    const start = new Date(startTime);
    const now = new Date();
    const diff = now - start;
    const hours = Math.floor(diff / (1000 * 60 * 60));
    const minutes = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60));
    return `${hours}h ${minutes}m`;
  };

  return (
    <div className="container">
      <h1 style={{ marginBottom: '2rem' }}>Active Parking Sessions</h1>

      {error && <div className="error">{error}</div>}
      {success && <div className="success">{success}</div>}

      {sessions.length === 0 ? (
        <div className="card">
          <p style={{ textAlign: 'center' }}>You don't have any active parking sessions.</p>
        </div>
      ) : (
        <div className="grid grid-2">
          {sessions.map((session) => (
            <div key={session.parkingSessionId} className="card">
              <h3>{session.car.make} {session.car.model}</h3>
              <p><strong>License Plate:</strong> {session.car.licensePlate}</p>
              {session.car.color && <p><strong>Color:</strong> {session.car.color}</p>}
              <hr style={{ margin: '1rem 0', border: '1px solid #ddd' }} />
              <p><strong>Floor:</strong> {session.spot.floor}</p>
              <p><strong>Spot Number:</strong> {session.spot.spotNumber}</p>
              <p><strong>Started:</strong> {new Date(session.startTime).toLocaleString()}</p>
              <p><strong>Duration:</strong> {calculateDuration(session.startTime)}</p>
              <button
                className="btn btn-danger"
                style={{ width: '100%', marginTop: '1rem' }}
                onClick={() => handleLeave(session.parkingSessionId)}
              >
                Leave Spot
              </button>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

export default ActiveSessions;
