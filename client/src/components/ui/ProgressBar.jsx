import React from 'react';

const ProgressBar = ({ progress }) => {
  return (
    <div className="w-32 h-2 bg-gray-200 dark:bg-slate-700 rounded-full overflow-hidden">
      <div 
        className="h-full bg-gradient-to-r from-blue-500 to-indigo-500 transition-all duration-300"
        style={{ width: `${progress}%` }}
      ></div>
    </div>
  );
};

export default ProgressBar;
