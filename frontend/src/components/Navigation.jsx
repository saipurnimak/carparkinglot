import React from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../AuthContext';

function Navigation() {
  const { user, logout } = useAuth();

  return (
    <nav className="nav">
      <h1>🚗 Parking Garage</h1>
      <div className="nav-links">
        <Link to="/dashboard">Dashboard</Link>
        <Link to="/cars">My Cars</Link>
        <Link to="/park">Park Car</Link>
        <Link to="/active">Active Sessions</Link>
        <span>Welcome, {user?.firstName}!</span>
        <button className="btn btn-danger" onClick={logout}>Logout</button>
      </div>
    </nav>
  );
}

export default Navigation;
