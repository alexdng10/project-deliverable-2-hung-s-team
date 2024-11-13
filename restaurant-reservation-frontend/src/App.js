import React, { useState, useEffect } from 'react';
import { MapPin, Calendar } from 'lucide-react';
import { reservationApi } from './api/reservationApi';
import RestaurantLayout from './components/RestaurantLayout';

const ErrorMessage = ({ message }) => {
  if (!message) return null;
  return (
    <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative mb-4">
      <strong className="font-bold">Error: </strong>
      <span className="block sm:inline">{message}</span>
    </div>
  );
};

function App() {
  const [franchises, setFranchises] = useState([]);
  const [selectedFranchise, setSelectedFranchise] = useState(null);
  const [tables, setTables] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // Fetch franchises on component mount
  useEffect(() => {
    const fetchFranchises = async () => {
      try {
        setLoading(true);
        setError(null);
        const data = await reservationApi.getFranchises();
        console.log('Fetched franchises:', data);
        setFranchises(data);
      } catch (err) {
        console.error('Error fetching franchises:', err);
        setError('Failed to fetch franchises. Please try again.');
      } finally {
        setLoading(false);
      }
    };

    fetchFranchises();
  }, []);

  // Fetch tables when a franchise is selected
  useEffect(() => {
    const fetchTables = async () => {
      if (!selectedFranchise) return;

      try {
        setError(null);
        console.log('Fetching tables for franchise:', selectedFranchise.id);
        const data = await reservationApi.getTables(selectedFranchise.id);
        console.log('Fetched tables:', data);
        setTables(data);
      } catch (err) {
        console.error('Error fetching tables:', err);
        setError('Failed to fetch tables. Please try again.');
      }
    };

    fetchTables();
  }, [selectedFranchise]);

  const handleTableSelect = async (table) => {
    try {
      setError(null);
      
      // For testing - you'll need to replace this with actual user auth
      const userEmail = "test@example.com";
      
      // Create timestamp strings
      const startTime = new Date().toISOString();
      const endTime = new Date(Date.now() + 2 * 60 * 60 * 1000).toISOString(); // 2 hours later

      console.log('Creating reservation:', {
        userEmail,
        tableId: table.id,
        startTime,
        endTime
      });

      await reservationApi.createReservation(
        userEmail,
        table.id,
        startTime,
        endTime
      );

      // Refresh tables after successful reservation
      if (selectedFranchise) {
        const updatedTables = await reservationApi.getTables(selectedFranchise.id);
        setTables(updatedTables);
      }

      // Show success message
      alert('Reservation created successfully!');
      
    } catch (err) {
      console.error('Reservation error:', err);
      setError(err.message || 'Failed to create reservation. Please try again.');
    }
  };

  // Loading state with spinner
  if (loading) {
    return (
      <div className="flex justify-center items-center h-screen">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-gray-900"></div>
      </div>
    );
  }

  return (
    <div className="max-w-6xl mx-auto p-6">
      {error && <ErrorMessage message={error} />}

      <div className="bg-white rounded-lg shadow-lg mb-8">
        <div className="p-6 border-b">
          <h1 className="text-2xl font-bold flex items-center gap-2">
            <Calendar className="w-6 h-6" />
            Restaurant Reservation System
          </h1>
        </div>
        
        <div className="p-6">
          {!selectedFranchise ? (
            // Franchise selection grid
            <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
              {franchises.map(franchise => (
                <div 
                  key={franchise.id}
                  className="bg-white rounded-lg shadow border p-4 cursor-pointer hover:bg-gray-50 transition-colors"
                  onClick={() => setSelectedFranchise(franchise)}
                >
                  <h2 className="text-lg font-semibold mb-2">{franchise.name}</h2>
                  <p className="flex items-center text-gray-600">
                    <MapPin className="w-4 h-4 mr-1" />
                    {franchise.location}
                  </p>
                  <p className="text-sm text-gray-500 mt-2">
                    {franchise.layout.tables.filter(t => !t.isReserved).length} tables available
                  </p>
                </div>
              ))}
            </div>
          ) : (
            // Restaurant layout view
            <div>
              <button 
                className="mb-4 px-4 py-2 border rounded hover:bg-gray-50 flex items-center gap-2"
                onClick={() => setSelectedFranchise(null)}
              >
                <span>←</span>
                Back to Franchises
              </button>
              
              <div className="mb-4">
                <h2 className="text-xl font-semibold">{selectedFranchise.name}</h2>
                <p className="text-gray-600 flex items-center gap-1">
                  <MapPin className="w-4 h-4" />
                  {selectedFranchise.location}
                </p>
              </div>

              <RestaurantLayout 
                franchise={selectedFranchise}
                tables={tables}
                onTableSelect={handleTableSelect}
              />
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

export default App;