// reservationApi.js
import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

export const reservationApi = {
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
          tables: franchise.tables.map((table) => ({
            id: table.tableId,
            seats: table.seats,
            isReserved: table.isReserved,
            position: {
              x: (table.tableId % 3) * 160 + 80,
              y: Math.floor(table.tableId / 3) * 120 + 80,
            },
            type: table.seats > 4 ? 'rectangle' : 'square',
          })),
        },
      }));
    } catch (error) {
      console.error('Error fetching franchises:', error);
      throw new Error(error.response?.data || 'Failed to fetch franchises');
    }
  },

  getTables: async (franchiseId) => {
    try {
      const response = await axios.get(`${API_BASE_URL}/franchises/${franchiseId}/tables`);
      return response.data.map((table) => ({
        id: table.tableId,
        seats: table.seats,
        isReserved: table.isReserved,
        position: {
          x: (table.tableId % 3) * 160 + 80,
          y: Math.floor(table.tableId / 3) * 120 + 80,
        },
        type: table.seats > 4 ? 'rectangle' : 'square',
      }));
    } catch (error) {
      console.error('Error fetching tables:', error);
      throw new Error(error.response?.data || 'Failed to fetch tables');
    }
  },

  createReservation: async (name, email, tableId, startTime) => {
    try {
      console.log('Creating reservation with data:', {
        name,
        email,
        tableId,
        startTime,
      });

      // Calculate endTime by adding 2 hours to startTime
      let [datePart, timePart] = startTime.split('T');
      let [hours, minutes, seconds] = timePart.split(':').map(Number);

      hours += 2;

      // Handle rollover past midnight
      if (hours >= 24) {
        hours -= 24;
        // Increment date by one day
        const date = new Date(datePart);
        date.setDate(date.getDate() + 1);
        datePart = date.toISOString().split('T')[0];
      }

      // Ensure hours, minutes, seconds are two digits
      const endTimeStr = `${String(hours).padStart(2, '0')}:${String(minutes)
        .toString()
        .padStart(2, '0')}:${String(seconds || 0).toString().padStart(2, '0')}`;
      const endTime = `${datePart}T${endTimeStr}`;

      const response = await axios.post(`${API_BASE_URL}/reservations`, {
        name,
        email,
        tableId,
        startTime,
        endTime,
      });

      console.log('Reservation response:', response.data);
      return response.data;
    } catch (error) {
      console.error('Server error response:', error.response?.data);
      throw new Error(error.response?.data || 'Failed to create reservation');
    }
  },
};
