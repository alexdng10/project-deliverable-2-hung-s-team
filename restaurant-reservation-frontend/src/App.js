// App.js
import React, { useState, useEffect } from 'react';
import { MapPin, Calendar, X } from 'lucide-react';
import { reservationApi } from './api/reservationApi';
import RestaurantLayout from './components/RestaurantLayout';
import ReservationForm from './components/ReservationForm';

const ErrorMessage = ({ message, onClose }) => (
  <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative mb-4">
    <strong className="font-bold">Error: </strong>
    <span className="block sm:inline">{message}</span>
    {onClose && (
      <button onClick={onClose} className="absolute top-0 right-0 px-4 py-3">
        <X className="h-4 w-4" />
      </button>
    )}
  </div>
);

function App() {
  const [franchises, setFranchises] = useState([]);
  const [selectedFranchise, setSelectedFranchise] = useState(null);
  const [tables, setTables] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [showReservationForm, setShowReservationForm] = useState(false);
  const [selectedTable, setSelectedTable] = useState(null);

  useEffect(() => {
    fetchFranchises();
  }, []);

  useEffect(() => {
    if (selectedFranchise) {
      fetchTables();
    }
  }, [selectedFranchise]);

  const fetchFranchises = async () => {
    try {
      setLoading(true);
      setError(null);
      const data = await reservationApi.getFranchises();
      setFranchises(data);
    } catch (err) {
      console.error('Error fetching franchises:', err);
      setError('Failed to fetch franchises. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const fetchTables = async () => {
    if (!selectedFranchise) return;

    try {
      setError(null);
      const data = await reservationApi.getTables(selectedFranchise.id);
      setTables(data);
    } catch (err) {
      console.error('Error fetching tables:', err);
      setError('Failed to fetch tables. Please try again.');
    }
  };

  const handleTableSelect = (table) => {
    if (table.isReserved) {
      setError('This table is already reserved.');
      return;
    }
    setSelectedTable(table);
    setShowReservationForm(true);
  };

  const handleReservationSubmit = async (formData) => {
    try {
      setError(null);
      console.log('Creating reservation with data:', formData);

      await reservationApi.createReservation(
        formData.name,
        formData.email,
        formData.tableId,
        formData.startTime
      );

      await fetchTables();
      setShowReservationForm(false);
      setSelectedTable(null);
      alert('Reservation created successfully!');
    } catch (err) {
      console.error('Reservation error:', err);
      setError(err.message || 'Failed to create reservation');
    }
  };

  if (loading) {
    return (
      <div className="flex justify-center items-center h-screen">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-gray-900" />
      </div>
    );
  }

  return (
    <div className="max-w-6xl mx-auto p-6">
      {error && <ErrorMessage message={error} onClose={() => setError(null)} />}

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
              {franchises.map((franchise) => (
                <div
                  key={franchise.id}
                  className="bg-white rounded-lg shadow border p-4 cursor-pointer hover:bg-gray-50"
                  onClick={() => setSelectedFranchise(franchise)}
                >
                  <h2 className="text-lg font-semibold mb-2">{franchise.name}</h2>
                  <p className="flex items-center text-gray-600">
                    <MapPin className="w-4 h-4 mr-1" />
                    {franchise.location}
                  </p>
                  <p className="text-sm text-gray-500 mt-2">
                    {franchise.layout.tables.filter((t) => !t.isReserved).length} tables available
                  </p>
                </div>
              ))}
            </div>
          ) : (
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
                <p className="text-sm text-gray-500 mt-1">
                  Open daily from 5:00 PM to 11:00 PM
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

      {showReservationForm && selectedTable && (
        <ReservationForm
          table={selectedTable}
          onSubmit={handleReservationSubmit}
          onCancel={() => {
            setShowReservationForm(false);
            setSelectedTable(null);
          }}
        />
      )}
    </div>
  );
}

export default App;
