import React, { useEffect, useState } from 'react';
import { carsAPI } from '../api';

function MyCars() {
  const [cars, setCars] = useState([]);
  const [showForm, setShowForm] = useState(false);
  const [formData, setFormData] = useState({
    make: '',
    model: '',
    licensePlate: '',
    color: '',
  });
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  useEffect(() => {
    loadCars();
  }, []);

  const loadCars = async () => {
    try {
      const response = await carsAPI.getCars();
      setCars(response.data);
    } catch (error) {
      console.error('Error loading cars:', error);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');

    try {
      await carsAPI.addCar(formData);
      setSuccess('Car added successfully!');
      setFormData({ make: '', model: '', licensePlate: '', color: '' });
      setShowForm(false);
      loadCars();
    } catch (err) {
      setError(err.response?.data?.message || 'Error adding car');
    }
  };

  const handleDelete = async (carId) => {
    if (window.confirm('Are you sure you want to delete this car?')) {
      try {
        await carsAPI.deleteCar(carId);
        setSuccess('Car deleted successfully!');
        loadCars();
      } catch (err) {
        setError('Error deleting car');
      }
    }
  };

  return (
    <div className="container">
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '2rem' }}>
        <h1>My Cars</h1>
        <button className="btn btn-primary" onClick={() => setShowForm(!showForm)}>
          {showForm ? 'Cancel' : 'Add New Car'}
        </button>
      </div>

      {error && <div className="error">{error}</div>}
      {success && <div className="success">{success}</div>}

      {showForm && (
        <div className="card">
          <h2>Add New Car</h2>
          <form onSubmit={handleSubmit}>
            <div className="form-group">
              <label>Make</label>
              <input
                type="text"
                value={formData.make}
                onChange={(e) => setFormData({ ...formData, make: e.target.value })}
                required
              />
            </div>
            <div className="form-group">
              <label>Model</label>
              <input
                type="text"
                value={formData.model}
                onChange={(e) => setFormData({ ...formData, model: e.target.value })}
                required
              />
            </div>
            <div className="form-group">
              <label>License Plate (7 characters)</label>
              <input
                type="text"
                value={formData.licensePlate}
                onChange={(e) => setFormData({ ...formData, licensePlate: e.target.value.toUpperCase() })}
                maxLength="7"
                pattern="[A-Z0-9]{7}"
                required
              />
            </div>
            <div className="form-group">
              <label>Color</label>
              <input
                type="text"
                value={formData.color}
                onChange={(e) => setFormData({ ...formData, color: e.target.value })}
              />
            </div>
            <button type="submit" className="btn btn-success">Add Car</button>
          </form>
        </div>
      )}

      <div className="grid grid-2">
        {cars.map((car) => (
          <div key={car.id} className="card">
            <h3>{car.make} {car.model}</h3>
            <p><strong>License Plate:</strong> {car.licensePlate}</p>
            {car.color && <p><strong>Color:</strong> {car.color}</p>}
            <button className="btn btn-danger" onClick={() => handleDelete(car.id)}>
              Delete
            </button>
          </div>
        ))}
      </div>

      {cars.length === 0 && !showForm && (
        <div className="card">
          <p style={{ textAlign: 'center' }}>No cars registered yet. Add your first car!</p>
        </div>
      )}
    </div>
  );
}

export default MyCars;
