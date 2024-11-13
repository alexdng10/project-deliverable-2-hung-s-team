import React from 'react';
import Table from './Table';
import { DoorClosed } from 'lucide-react';

const RestaurantLayout = ({ franchise, tables = [], onTableSelect }) => {
  // Use franchise layout dimensions or fallback to defaults
  const layoutWidth = franchise?.layout?.width || 640;
  const layoutHeight = franchise?.layout?.height || 480;

  // Scale the features based on layout size
  const barWidth = Math.floor(layoutWidth * 0.3); // 30% of layout width
  const barHeight = Math.floor(layoutHeight * 0.15); // 15% of layout height
  
  const kitchenWidth = Math.floor(layoutWidth * 0.3);
  const kitchenHeight = Math.floor(layoutHeight * 0.3);

  return (
    <div className="w-full overflow-x-auto">
      <div 
        className="relative bg-white border rounded-lg"
        style={{ 
          width: layoutWidth,
          height: layoutHeight,
          margin: '0 auto'
        }}
      >
        {/* Features - scaled according to layout size */}
        <div 
          className="absolute top-0 left-0 bg-gray-200 rounded-lg flex items-center justify-center"
          style={{
            width: `${barWidth}px`,
            height: `${barHeight}px`
          }}
        >
          <span className="text-sm font-medium">Bar</span>
        </div>
        
        <div 
          className="absolute bottom-0 right-0 bg-gray-100 rounded-lg flex items-center justify-center"
          style={{
            width: `${kitchenWidth}px`,
            height: `${kitchenHeight}px`
          }}
        >
          <span className="text-sm font-medium">Kitchen</span>
        </div>

        <div className="absolute top-0 left-1/2 transform -translate-x-1/2 flex flex-col items-center">
          <DoorClosed className="w-6 h-6 text-gray-600" />
          <span className="text-xs">Entrance</span>
        </div>

        {/* Tables */}
        {Array.isArray(tables) && tables.map(table => (
          <Table 
            key={table.id || Math.random()} 
            table={table}
            onSelect={onTableSelect}
            layoutScale={{
              width: layoutWidth / 640, // Calculate scale factor
              height: layoutHeight / 480
            }}
          />
        ))}
      </div>
      
      <div className="mt-4 text-sm text-gray-500">
        <div className="flex items-center gap-2">
          <div className="w-4 h-4 bg-green-100 border-2 border-green-500 rounded"></div>
          Available
        </div>
        <div className="flex items-center gap-2 mt-1">
          <div className="w-4 h-4 bg-red-100 border-2 border-red-500 rounded"></div>
          Reserved
        </div>
      </div>
    </div>
  );
};

export default RestaurantLayout;