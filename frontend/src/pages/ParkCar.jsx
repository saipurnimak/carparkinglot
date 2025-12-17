import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { carsAPI, parkingAPI } from '../api';

function ParkCar() {
  const [cars, setCars] = useState([]);
  const [spots, setSpots] = useState([]);
  const [selectedCar, setSelectedCar] = useState('');
  const [selectedFloor, setSelectedFloor] = useState('');
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    loadCars();
    loadSpots();
  }, [selectedFloor]);

  const loadCars = async () => {
    try {
      const response = await carsAPI.getCars();
      setCars(response.data);
    } catch (error) {
      console.error('Error loading cars:', error);
    }
  };

  const loadSpots = async () => {
    try {
      const response = await parkingAPI.getAvailableSpots(selectedFloor || null);
      setSpots(response.data);
    } catch (error) {
      console.error('Error loading spots:', error);
    }
  };

  const handlePark = async (spot) => {
    if (!selectedCar) {
      setError('Please select a car first');
      return;
    }

    setError('');
    setSuccess('');

    try {
      await parkingAPI.parkCar({
        carId: parseInt(selectedCar),
        floor: spot.floor,
        spotNumber: spot.spotNumber,
      });
      setSuccess('Car parked successfully!');
      setTimeout(() => navigate('/active'), 2000);
    } catch (err) {
      setError(err.response?.data?.error || 'Error parking car');
    }
  };

  const handleAutoPark = async () => {
    if (!selectedCar) {
      setError('Please select a car first');
      return;
    }

    setError('');
    setSuccess('');

    try {
      await parkingAPI.parkCar({
        carId: parseInt(selectedCar),
      });
      setSuccess('Car parked automatically in the best available spot!');
      setTimeout(() => navigate('/active'), 2000);
    } catch (err) {
      setError(err.response?.data?.error || 'Error parking car');
    }
  };

  const floors = [1, 2, 3];

  return (
    <div className="container">
      <h1 style={{ marginBottom: '2rem' }}>Park a Car</h1>

      {error && <div className="error">{error}</div>}
      {success && <div className="success">{success}</div>}

      <div className="card">
        <div className="form-group">
          <label>Select Car</label>
          <select value={selectedCar} onChange={(e) => setSelectedCar(e.target.value)}>
            <option value="">-- Select a car --</option>
            {cars.map((car) => (
              <option key={car.id} value={car.id}>
                {car.make} {car.model} ({car.licensePlate})
              </option>
            ))}
          </select>
        </div>

        {selectedCar && (
          <button 
            className="btn btn-primary" 
            onClick={handleAutoPark}
            style={{ marginTop: '1rem' }}
          >
            Auto-Park (Let System Choose Best Spot)
          </button>
        )}
      </div>

      <h2 style={{ marginTop: '2rem' }}>Or Choose Your Spot Manually</h2>

      <div className="card">
        <div className="form-group">
          <label>Filter by Floor</label>
          <select value={selectedFloor} onChange={(e) => setSelectedFloor(e.target.value)}>
            <option value="">All Floors</option>
            {floors.map((floor) => (
              <option key={floor} value={floor}>Floor {floor}</option>
            ))}
          </select>
        </div>
      </div>

      <h2 style={{ marginTop: '2rem' }}>Available Parking Spots ({spots.length})</h2>
      
      {spots.length === 0 ? (
        <div className="card">
          <p style={{ textAlign: 'center' }}>No available spots on the selected floor.</p>
        </div>
      ) : (
        <div className="grid grid-3">
          {spots.map((spot) => (
            <div
              key={spot.id}
              className="parking-spot available"
              onClick={() => handlePark(spot)}
            >
              <h3>Spot {spot.spotNumber}</h3>
              <p>Floor {spot.floor}</p>
              <button className="btn btn-success" style={{ marginTop: '0.5rem' }}>
                Park Here
              </button>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

export default ParkCar;
