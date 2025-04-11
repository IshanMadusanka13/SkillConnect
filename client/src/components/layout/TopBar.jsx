import React, { useState } from 'react';
import { SearchIcon, MenuIcon, BellIcon, PlusIcon } from '@heroicons/react/outline';
import { useMediaQuery } from '../../hooks/useMediaQuery';
import { Link } from 'react-router-dom';

const TopBar = () => {
  const isMobile = !useMediaQuery('(min-width: 768px)');
  const [showMobileMenu, setShowMobileMenu] = useState(false);
  
  return (
    <header className="bg-white dark:bg-slate-800 shadow-sm z-10">
      <div className="px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between h-16">
          {isMobile && (
            <div className="flex items-center">
              <button
                onClick={() => setShowMobileMenu(!showMobileMenu)}
                className="p-2 rounded-md text-gray-500 hover:text-gray-700 hover:bg-gray-100 dark:text-gray-400 dark:hover:text-gray-200 dark:hover:bg-slate-700"
              >
                <MenuIcon className="h-6 w-6" />
              </button>
            </div>
          )}
          
          <div className="flex-1 flex items-center justify-center sm:justify-start">
            {isMobile && (
              <div className="flex-shrink-0 flex items-center">
                <img className="h-8 w-auto" src="/assets/images/logo.svg" alt="SkillSync" />
              </div>
            )}
            
            <div className="hidden sm:ml-6 sm:flex sm:items-center">
              <div className="relative">
                <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                  <SearchIcon className="h-5 w-5 text-gray-400" />
                </div>
                <input
                  className="block w-full pl-10 pr-3 py-2 border border-gray-200 rounded-xl text-sm placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 dark:bg-slate-700 dark:border-slate-600 dark:placeholder-gray-400 dark:text-white"
                  placeholder="Search for skills, topics, users..."
                  type="search"
                />
              </div>
            </div>
          </div>
          
          <div className="flex items-center space-x-4">
            <button className="p-1.5 rounded-lg text-gray-500 hover:text-gray-700 hover:bg-gray-100 dark:text-gray-400 dark:hover:text-gray-200 dark:hover:bg-slate-700">
              <BellIcon className="h-6 w-6" />
              <span className="absolute top-3 right-3 block h-2.5 w-2.5 rounded-full bg-red-500 ring-2 ring-white dark:ring-slate-800"></span>
            </button>
            
            {!isMobile && (
              <button className="flex items-center space-x-1 px-3 py-1.5 bg-blue-600 hover:bg-blue-700 text-white rounded-lg transition-all duration-200 shadow-sm hover:shadow-md">
                <PlusIcon className="h-5 w-5" />
                <span>Create</span>
              </button>
            )}
            
            <div className="relative">
            <Link to="/profile">
              <button className="flex rounded-full bg-gray-100 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 dark:bg-slate-700">
                <img
                  className="h-8 w-8 rounded-full border-2 border-white dark:border-slate-700"
                  src="/assets/images/default-avatar.png"
                  alt="User avatar"
                />
              </button>
            </Link>
            </div>
          </div>
        </div>
      </div>
    </header>
  );
};

export default TopBar;
