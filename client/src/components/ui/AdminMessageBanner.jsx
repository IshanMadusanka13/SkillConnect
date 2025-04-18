import React, { useState, useEffect } from 'react';
import { XIcon } from '@heroicons/react/outline';
import { api } from '../../utils/api';

const AdminMessageBanner = () => {
  const [latestMessage, setLatestMessage] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const [isBannerClosed, setIsBannerClosed] = useState(false);

  useEffect(() => {
    // Fetch the latest admin message
    const fetchLatestMessage = async () => {
      setIsLoading(true);
      try {
        const messages = await api.getAdminMessages();
        if (messages && messages.length > 0) {
          // Sort by date and get the latest
          const sorted = [...messages].sort((a, b) => 
            new Date(b.createdAt) - new Date(a.createdAt)
          );
          
          const newestMessage = sorted[0];
          setLatestMessage(newestMessage);
          
          // Check if this message has been dismissed before
          const lastClosedMessageId = localStorage.getItem('lastClosedAdminMessageId');
          
          if (lastClosedMessageId && parseInt(lastClosedMessageId) === newestMessage.messageId) {
            setIsBannerClosed(true);
          } else {
            setIsBannerClosed(false);
          }
        }
      } catch (error) {
        console.error('Error fetching admin messages:', error);
      } finally {
        setIsLoading(false);
      }
    };

    fetchLatestMessage();
    
    
    const intervalId = setInterval(fetchLatestMessage, 1 * 60 * 1000); 
    
    return () => {
      clearInterval(intervalId);
    };
  }, []);

  const handleCloseBanner = () => {
    setIsBannerClosed(true);
    
    // Store the ID of the message being closed
    if (latestMessage) {
      localStorage.setItem('lastClosedAdminMessageId', latestMessage.messageId.toString());
    }
  };

  // Don't render anything if loading, no message, or banner is closed
  if (isLoading || !latestMessage || isBannerClosed) {
    return null;
  }

  return (
    <div className="bg-blue-50 dark:bg-blue-900/20 border-l-4 border-blue-500 p-4 mb-6 rounded-lg relative">
      <button 
        onClick={handleCloseBanner}
        className="absolute top-2 right-2 text-gray-500 hover:text-gray-700 dark:text-gray-400 dark:hover:text-gray-300"
        aria-label="Close banner"
      >
        <XIcon className="h-5 w-5" />
      </button>
      <div className="flex">
        <div className="flex-shrink-0">
          <svg className="h-5 w-5 text-blue-600 dark:text-blue-400" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true">
            <path fillRule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7-4a1 1 0 11-2 0 1 1 0 012 0zM9 9a1 1 0 000 2v3a1 1 0 001 1h1a1 1 0 100-2v-3a1 1 0 00-1-1H9z" clipRule="evenodd" />
          </svg>
        </div>
        <div className="ml-3">
          <h3 className="text-sm font-medium text-blue-800 dark:text-blue-300">{latestMessage.title}</h3>
          <div className="mt-2 text-sm text-blue-700 dark:text-blue-200">
            <p>{latestMessage.content}</p>
          </div>
          <div className="mt-2">
            <a href="/announcements" className="text-sm font-medium text-blue-600 hover:text-blue-500 dark:text-blue-400 dark:hover:text-blue-300">
              View all announcements →
            </a>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AdminMessageBanner;
