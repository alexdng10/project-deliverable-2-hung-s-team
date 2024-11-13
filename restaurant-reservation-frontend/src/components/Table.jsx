import React from 'react';
import { Users } from 'lucide-react';

const Table = ({ table, onSelect, layoutScale = { width: 1, height: 1 } }) => {
  if (!table) return null;

  // Base dimensions
  const baseSquareSize = 48;
  const baseRectWidth = 80;
  const baseRectHeight = 48;

  // Scale dimensions based on layout size
  const dimensions = (table.type === 'rectangle')
    ? { 
        width: `${baseRectWidth * layoutScale.width}px`, 
        height: `${baseRectHeight * layoutScale.height}px` 
      }
    : { 
        width: `${baseSquareSize * layoutScale.width}px`, 
        height: `${baseSquareSize * layoutScale.height}px` 
      };

  const position = table.position || { x: 80, y: 80 };

  // Scale position
  const scaledPosition = {
    x: position.x * layoutScale.width,
    y: position.y * layoutScale.height
  };

  return (
    <div 
      className={`absolute rounded-lg border-2 cursor-pointer transition-colors flex flex-col items-center justify-center
        ${table.isReserved 
          ? 'bg-red-100 border-red-500 cursor-not-allowed' 
          : 'bg-green-100 border-green-500 hover:bg-green-200'}`}
      style={{ 
        left: scaledPosition.x,
        top: scaledPosition.y,
        ...dimensions
      }}
      onClick={() => !table.isReserved && onSelect(table)}
    >
      <Users className="w-3 h-3" />
      <span className="text-xs font-medium">{table.seats || '?'}</span>
    </div>
  );
};

export default Table;