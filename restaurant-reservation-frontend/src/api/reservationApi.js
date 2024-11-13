const mockFranchises = [
    {
      id: 1,
      name: "Downtown Restaurant",
      location: "123 Main Street",
      layout: {
        width: 640,
        height: 480,
        tables: [
          { id: 1, seats: 4, isReserved: false, position: { x: 80, y: 80 }, type: 'square' },
          { id: 2, seats: 2, isReserved: true, position: { x: 160, y: 80 }, type: 'square' },
          { id: 3, seats: 6, isReserved: false, position: { x: 320, y: 120 }, type: 'rectangle' },
          { id: 4, seats: 4, isReserved: false, position: { x: 480, y: 80 }, type: 'square' },
          { id: 5, seats: 8, isReserved: false, position: { x: 80, y: 240 }, type: 'rectangle' },
        ]
      }
    },
    {
      id: 2,
      name: "Uptown Restaurant",
      location: "456 Park Avenue",
      layout: {
        width: 340,
        height: 280,
        tables: [
          { id: 6, seats: 4, isReserved: false, position: { x: 80, y: 80 }, type: 'square' },
          { id: 7, seats: 2, isReserved: false, position: { x: 200, y: 80 }, type: 'square' },
          { id: 8, seats: 6, isReserved: false, position: { x: 320, y: 120 }, type: 'rectangle' },
        ]
      }
    },
    {
      id: 3,
      name: "Midtown Restaurant",
      location: "789 Center Street",
      layout: {
        width: 640,
        height: 480,
        tables: [
          { id: 9, seats: 4, isReserved: false, position: { x: 80, y: 80 }, type: 'square' },
          { id: 10, seats: 2, isReserved: true, position: { x: 200, y: 80 }, type: 'square' },
          { id: 11, seats: 6, isReserved: false, position: { x: 320, y: 120 }, type: 'rectangle' },
        ]
      }
    }
  ];
  
  export const reservationApi = {
    // Get all franchises
    getFranchises: async () => {
      // Simulate API call
      return new Promise((resolve) => {
        setTimeout(() => resolve(mockFranchises), 500);
      });
    },
  
    // Get tables for a specific franchise
    getTables: async (franchiseId) => {
      // Simulate API call
      return new Promise((resolve) => {
        const franchise = mockFranchises.find(f => f.id === franchiseId);
        setTimeout(() => resolve(franchise.layout.tables), 500);
      });
    },
  
    // Create a new reservation
    createReservation: async (userId, tableId, startTime, endTime) => {
      // Simulate API call
      return new Promise((resolve) => {
        console.log('Reservation created:', { userId, tableId, startTime, endTime });
        setTimeout(() => resolve({ success: true }), 500);
      });
    },
  
    // Cancel a reservation
    cancelReservation: async (reservationId) => {
      // Simulate API call
      return new Promise((resolve) => {
        console.log('Reservation cancelled:', reservationId);
        setTimeout(() => resolve({ success: true }), 500);
      });
    }
  };