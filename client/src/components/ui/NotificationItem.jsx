import React from 'react';

const NotificationItem = ({ notification }) => {
  const getBackgroundColor = () => {
    if (!notification.read) {
      return 'bg-blue-50 dark:bg-blue-900/30';
    }
    return '';
  };
  
  const getIcon = () => {
    switch (notification.type) {
      case 'like':
        return (
          <div className="flex-shrink-0 h-8 w-8 rounded-full bg-red-100 dark:bg-red-900/30 flex items-center justify-center">
            <svg className="h-4 w-4 text-red-500" fill="currentColor" viewBox="0 0 20 20">
              <path fillRule="evenodd" d="M3.172 5.172a4 4 0 015.656 0L10 6.343l1.172-1.171a4 4 0 115.656 5.656L10 17.657l-6.828-6.829a4 4 0 010-5.656z" clipRule="evenodd" />
            </svg>
          </div>
        );
      case 'comment':
        return (
          <div className="flex-shrink-0 h-8 w-8 rounded-full bg-blue-100 dark:bg-blue-900/30 flex items-center justify-center">
            <svg className="h-4 w-4 text-blue-500" fill="currentColor" viewBox="0 0 20 20">
              <path fillRule="evenodd" d="M18 10c0 3.866-3.582 7-8 7a8.841 8.841 0 01-4.083-.98L2 17l1.338-3.123C2.493 12.767 2 11.434 2 10c0-3.866 3.582-7 8-7s8 3.134 8 7zM7 9H5v2h2V9zm8 0h-2v2h2V9zM9 9h2v2H9V9z" clipRule="evenodd" />
            </svg>
          </div>
        );
      case 'follow':
        return (
          <div className="flex-shrink-0 h-8 w-8 rounded-full bg-green-100 dark:bg-green-900/30 flex items-center justify-center">
            <svg className="h-4 w-4 text-green-500" fill="currentColor" viewBox="0 0 20 20">
              <path d="M8 9a3 3 0 100-6 3 3 0 000 6zM8 11a6 6 0 016 6H2a6 6 0 016-6zM16 7a1 1 0 10-2 0v1h-1a1 1 0 100 2h1v1a1 1 0 102 0v-1h1a1 1 0 100-2h-1V7z" />
            </svg>
          </div>
        );
      case 'system':
        return (
          <div className="flex-shrink-0 h-8 w-8 rounded-full bg-purple-100 dark:bg-purple-900/30 flex items-center justify-center">
            <svg className="h-4 w-4 text-purple-500" fill="currentColor" viewBox="0 0 20 20">
              <path fillRule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7-4a1 1 0 11-2 0 1 1 0 012 0zM9 9a1 1 0 000 2v3a1 1 0 001 1h1a1 1 0 100-2v-3a1 1 0 00-1-1H9z" clipRule="evenodd" />
            </svg>
          </div>
        );
      default:
        return null;
    }
  };
  
  return (
    <div className={`p-3 rounded-lg hover:bg-gray-50 dark:hover:bg-slate-700/50 transition-colors duration-150 ${getBackgroundColor()}`}>
      <div className="flex items-start">
        {notification.user && (
          <img 
            src={notification.user.avatar} 
            alt={notification.user.name} 
            className="h-10 w-10 rounded-full mr-3"
          />
        )}
        
        {!notification.user && getIcon()}
        
        <div className="flex-1">
          <p className="text-sm text-gray-800 dark:text-gray-200">
            {notification.user && (
              <span className="font-medium">{notification.user.name} </span>
            )}
            {notification.message}
          </p>
          <p className="text-xs text-gray-500 dark:text-gray-400 mt-1">{notification.time}</p>
        </div>
        
        {!notification.read && (
          <div className="h-2 w-2 bg-blue-500 rounded-full"></div>
        )}
      </div>
    </div>
  );
};

export default NotificationItem;
