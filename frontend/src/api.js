import axios from 'axios';

const api = axios.create({
  baseURL: '/api',
});

// Add token to requests
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Auth API
export const authAPI = {
  register: (data) => api.post('/auth/register', data),
  login: (data) => api.post('/auth/login', data),
  getCurrentUser: () => api.get('/auth/me'),
};

// Cars API
export const carsAPI = {
  getCars: () => api.get('/cars'),
  addCar: (data) => api.post('/cars', data),
  deleteCar: (id) => api.delete(`/cars/${id}`),
};

// Parking API
export const parkingAPI = {
  getAvailableSpots: (floor) => {
    const params = floor ? { floor } : {};
    return api.get('/spots/available', { params });
  },
  parkCar: (data) => api.post('/parking/park', data),
  getActiveSessions: () => api.get('/parking/active'),
  leaveParkingSpot: (sessionId) => api.post(`/parking/${sessionId}/leave`),
};

export default api;
