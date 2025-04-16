import React, { useState, useEffect } from 'react';
import { XIcon } from '@heroicons/react/outline';
import { api } from '../../utils/api';

const AdminMessageBanner = () => {
  const [messages, setMessages] = useState([]);
  const [currentMessageIndex, setCurrentMessageIndex] = useState(0);
  const [isLoading, setIsLoading] = useState(true);
  const [dismissed, setDismissed] = useState([]);

  useEffect(() => {
    fetchAdminMessages();
  }, []);

  const fetchAdminMessages = async () => {
    setIsLoading(true);
    try {
      const fetchedMessages = await api.getAdminMessages();
      setMessages(fetchedMessages);
    } catch (err) {
      console.error('Error fetching admin messages:', err);
    } finally {
      setIsLoading(false);
    }
  };

  const handleDismiss = (messageId) => {
    setDismissed([...dismissed, messageId]);
    
    // Move to next message if available
    if (currentMessageIndex < messages.length - 1) {
      setCurrentMessageIndex(currentMessageIndex + 1);
    }
  };

  // Filter out dismissed messages
  const activeMessages = messages.filter(msg => !dismissed.includes(msg.messageId));
  
  // Don't render anything if loading or no messages
  if (isLoading || activeMessages.length === 0) {
    return null;
  }

  const currentMessage = activeMessages[currentMessageIndex];

  return (
    <div className="bg-blue-50 dark:bg-blue-900/20 border-l-4 border-blue-500 p-4 mb-6 rounded-lg">
      <div className="flex justify-between">
        <div className="flex">
          <div className="flex-shrink-0">
            <svg className="h-5 w-5 text-blue-600 dark:text-blue-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
          </div>
          <div className="ml-3">
            <h3 className="text-sm font-medium text-blue-800 dark:text-blue-300">{currentMessage.title}</h3>
            <div className="mt-2 text-sm text-blue-700 dark:text-blue-200">
              <p>{currentMessage.content}</p>
            </div>
            <div className="mt-1 text-xs text-blue-500 dark:text-blue-400">
              {new Date(currentMessage.createdAt).toLocaleString()} • From Admin
            </div>
          </div>
        </div>
        <button
          onClick={() => handleDismiss(currentMessage.messageId)}
          className="flex-shrink-0 ml-4 text-blue-500 hover:text-blue-700 dark:text-blue-400 dark:hover:text-blue-300 focus:outline-none"
        >
          <XIcon className="h-5 w-5" />
        </button>
      </div>
      
      {activeMessages.length > 1 && (
        <div className="mt-3 flex justify-center">
          <div className="flex space-x-1">
            {activeMessages.map((_, index) => (
              <button
                key={index}
                onClick={() => setCurrentMessageIndex(index)}
                className={`h-2 w-2 rounded-full ${
                    index === currentMessageIndex 
                      ? 'bg-blue-600 dark:bg-blue-400' 
                      : 'bg-blue-300 dark:bg-blue-700'
                  }`}
                  aria-label={`Go to message ${index + 1}`}
                />
              ))}
            </div>
          </div>
        )}
      </div>
    );
  };
  
  export default AdminMessageBanner;
  