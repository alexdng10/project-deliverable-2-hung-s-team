import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

export const reservationApi = {
  // Get all franchises
  getFranchises: async () => {
    try {
      const response = await axios.get(`${API_BASE_URL}/franchises`);
      return response.data.map((franchise, index) => ({
        id: index,
        name: franchise.location,
        location: franchise.location,
        layout: {
          width: 640,
          height: 480,
          tables: franchise.tables.map(table => ({
            id: table.tableId,
            seats: table.seats,
            isReserved: table.isReserved,
            position: {
              x: (table.tableId % 3) * 160 + 80,
              y: Math.floor(table.tableId / 3) * 120 + 80
            },
            type: table.seats > 4 ? 'rectangle' : 'square'
          }))
        }
      }));
    } catch (error) {
      console.error('Error fetching franchises:', error.response?.data || error);
      throw error;
    }
  },

  // Get tables for a specific franchise
  getTables: async (franchiseId) => {
    try {
      console.log('Fetching tables for franchise:', franchiseId);
      const response = await axios.get(`${API_BASE_URL}/franchises/${franchiseId}/tables`);
      return response.data.map(table => ({
        id: table.tableId,
        seats: table.seats,
        isReserved: table.isReserved,
        position: {
          x: (table.tableId % 3) * 160 + 80,
          y: Math.floor(table.tableId / 3) * 120 + 80
        },
        type: table.seats > 4 ? 'rectangle' : 'square'
      }));
    } catch (error) {
      console.error('Error fetching tables:', error.response?.data || error);
      throw error;
    }
  },

  // Create a new reservation
  createReservation: async (userEmail, tableId, startTime, endTime) => {
    try {
      const response = await axios.post(`${API_BASE_URL}/reservations`, {
        userEmail,
        tableId,
        startTime,
        endTime
      });
      return response.data;
    } catch (error) {
      console.error('Error creating reservation:', error.response?.data || error);
      throw new Error(error.response?.data?.message || 'Failed to create reservation');
    }
  },

  // Cancel a reservation
  cancelReservation: async (reservationId) => {
    try {
      await axios.delete(`${API_BASE_URL}/reservations/${reservationId}`);
      return { success: true };
    } catch (error) {
      console.error('Error canceling reservation:', error.response?.data || error);
      throw new Error(error.response?.data?.message || 'Failed to cancel reservation');
    }
  },

  // Get all reservations for a user
  getUserReservations: async (userEmail) => {
    try {
      const response = await axios.get(`${API_BASE_URL}/reservations/user/${userEmail}`);
      return response.data;
    } catch (error) {
      console.error('Error fetching user reservations:', error.response?.data || error);
      throw new Error(error.response?.data?.message || 'Failed to fetch reservations');
    }
  }
};