import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

export const reservationApi = {
  // Get all franchises
  getFranchises: async () => {
    try {
      const response = await axios.get(`${API_BASE_URL}/franchises`);
      // Transform backend data to match frontend expectations
      return response.data.map(franchise => ({
        id: franchise.id,
        name: franchise.location, // Assuming location is used as name
        location: franchise.location,
        layout: {
          width: 640,
          height: 480,
          tables: franchise.tables.map(table => ({
            id: table.tableId,
            seats: table.seats,
            isReserved: table.isReserved,
            position: {
              // You might need to adjust these calculations based on your needs
              x: (table.tableId % 3) * 160 + 80,
              y: Math.floor(table.tableId / 3) * 120 + 80
            },
            type: table.seats > 4 ? 'rectangle' : 'square'
          }))
        }
      }));
    } catch (error) {
      console.error('Error fetching franchises:', error);
      throw error;
    }
  },

  // Get tables for a specific franchise
  getTables: async (franchiseId) => {
    try {
      const response = await axios.get(`${API_BASE_URL}/franchises/${franchiseId}/tables`);
      // Transform backend data to match frontend expectations
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
      console.error('Error fetching tables:', error);
      throw error;
    }
  },

  // Create a new reservation
  createReservation: async (userId, tableId, startTime, endTime) => {
    try {
      const response = await axios.post(`${API_BASE_URL}/reservations`, {
        userId,
        tableId,
        startTime,
        endTime
      });
      return response.data;
    } catch (error) {
      console.error('Error creating reservation:', error);
      throw error;
    }
  },

  // Cancel a reservation
  cancelReservation: async (reservationId) => {
    try {
      await axios.delete(`${API_BASE_URL}/reservations/${reservationId}`);
      return { success: true };
    } catch (error) {
      console.error('Error canceling reservation:', error);
      throw error;
    }
  }
};