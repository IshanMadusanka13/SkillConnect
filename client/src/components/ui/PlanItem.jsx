import React, { useState } from 'react';
import { ChevronDownIcon, ChevronUpIcon } from '@heroicons/react/outline';
import ProgressBar from './ProgressBar';

const PlanItem = ({ plan }) => {
  const [expanded, setExpanded] = useState(false);
  
  return (
    <div className="bg-white dark:bg-slate-800 border border-gray-200 dark:border-slate-700 rounded-xl overflow-hidden transition-all duration-300">
      <div 
        className="p-4 flex items-center justify-between cursor-pointer"
        onClick={() => setExpanded(!expanded)}
      >
        <div className="flex-1">
          <h3 className="font-medium text-gray-900 dark:text-white">{plan.title}</h3>
          <div className="mt-1 flex items-center">
            <ProgressBar progress={plan.progress} />
            <span className="ml-2 text-sm text-gray-500 dark:text-gray-400">{plan.progress}% complete</span>
          </div>
        </div>
        
        <div className="flex items-center space-x-2">
          <span className="text-sm font-medium text-blue-600 dark:text-blue-400">{plan.totalItems} items</span>
          {expanded ? (
            <ChevronUpIcon className="h-5 w-5 text-gray-500 dark:text-gray-400" />
          ) : (
            <ChevronDownIcon className="h-5 w-5 text-gray-500 dark:text-gray-400" />
          )}
        </div>
      </div>
      
      {expanded && (
        <div className="border-t border-gray-200 dark:border-slate-700 p-4">
          <p className="text-sm text-gray-700 dark:text-gray-300 mb-4">{plan.description}</p>
          
          <h4 className="text-sm font-medium text-gray-900 dark:text-white mb-2">Resources</h4>
          <ul className="space-y-2">
            {plan.resources.map((resource, index) => (
              <li key={index} className="flex items-center">
                <input
                  type="checkbox"
                  checked={resource.completed}
                  onChange={() => {}}
                  className="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
                />
                <a 
                  href={resource.url} 
                  target="_blank" 
                  rel="noopener noreferrer"
                  className={`ml-2 text-sm ${
                    resource.completed 
                      ? 'line-through text-gray-500 dark:text-gray-400' 
                      : 'text-blue-600 dark:text-blue-400 hover:underline'
                  }`}
                >
                  {resource.title}
                </a>
              </li>
            ))}
          </ul>
          
          <div className="mt-4 flex justify-end space-x-2">
            <button className="px-3 py-1 text-sm bg-gray-200 text-gray-800 rounded-lg hover:bg-gray-300 transition-colors duration-200 dark:bg-slate-700 dark:text-white dark:hover:bg-slate-600">
              Edit
            </button>
            <button className="px-3 py-1 text-sm bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors duration-200">
              Add Resource
            </button>
          </div>
        </div>
      )}
    </div>
  );
};

export default PlanItem;
