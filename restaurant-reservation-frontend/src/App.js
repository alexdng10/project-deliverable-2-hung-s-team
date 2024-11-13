import React, { useState, useEffect } from 'react';
import { MapPin, Calendar } from 'lucide-react';
import { reservationApi } from './api/reservationApi';
import RestaurantLayout from './components/RestaurantLayout';

function App() {
  const [franchises, setFranchises] = useState([]);
  const [selectedFranchise, setSelectedFranchise] = useState(null);
  const [tables, setTables] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchFranchises = async () => {
      try {
        const data = await reservationApi.getFranchises();
        setFranchises(data);
      } catch (err) {
        setError('Failed to fetch franchises');
      } finally {
        setLoading(false);
      }
    };

    fetchFranchises();
  }, []);

  useEffect(() => {
    if (selectedFranchise) {
      const fetchTables = async () => {
        try {
          const data = await reservationApi.getTables(selectedFranchise.id);
          setTables(data);
        } catch (err) {
          setError('Failed to fetch tables');
        }
      };

      fetchTables();
    }
  }, [selectedFranchise]);

  const handleTableSelect = async (table) => {
    try {
      // For testing - you'll need to integrate with actual user authentication
      const userId = "test-user";
      const startTime = new Date().toISOString();
      const endTime = new Date(Date.now() + 2 * 60 * 60 * 1000).toISOString(); // 2 hours later

      await reservationApi.createReservation(userId, table.id, startTime, endTime);
      // Refresh tables after reservation
      if (selectedFranchise) {
        const updatedTables = await reservationApi.getTables(selectedFranchise.id);
        setTables(updatedTables);
      }
    } catch (err) {
      setError('Failed to create reservation');
    }
  };

  if (loading) return <div className="flex justify-center items-center h-screen">Loading...</div>;
  if (error) return <div className="flex justify-center items-center h-screen text-red-500">Error: {error}</div>;

  return (
    <div className="max-w-6xl mx-auto p-6">
      <div className="bg-white rounded-lg shadow-lg mb-8">
        <div className="p-6 border-b">
          <h1 className="text-2xl font-bold flex items-center gap-2">
            <Calendar className="w-6 h-6" />
            Restaurant Reservation System
          </h1>
        </div>
        <div className="p-6">
          {!selectedFranchise ? (
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
                </div>
              ))}
            </div>
          ) : (
            <div>
              <button 
                className="mb-4 px-4 py-2 border rounded hover:bg-gray-50"
                onClick={() => setSelectedFranchise(null)}
              >
                ← Back to Franchises
              </button>
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