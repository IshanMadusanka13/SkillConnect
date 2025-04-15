import React, { useState, useContext, useRef, useEffect } from 'react';
import { SearchIcon, MenuIcon, BellIcon, PlusIcon, SunIcon, MoonIcon, XIcon } from '@heroicons/react/outline';
import { useMediaQuery } from '../../hooks/useMediaQuery';
import { Link, useNavigate } from 'react-router-dom';
import { ThemeContext } from '../../contexts/ThemeContext';
import { useAuth } from '../../hooks/useAuth';
import { api } from '../../utils/api';

const TopBar = () => {
  const isMobile = !useMediaQuery('(min-width: 768px)');
  const [showMobileMenu, setShowMobileMenu] = useState(false);
  const { darkMode, toggleTheme } = useContext(ThemeContext);
  const { currentUser } = useAuth();
  const [showCreatePostModal, setShowCreatePostModal] = useState(false);
  const [postContent, setPostContent] = useState('');
  const [postTitle, setPostTitle] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState(null);
  const [searchQuery, setSearchQuery] = useState('');
  const [searchResults, setSearchResults] = useState([]);
  const [showSearchResults, setShowSearchResults] = useState(false);
  const [isSearching, setIsSearching] = useState(false);
  const searchRef = useRef(null);
  const navigate = useNavigate();

  // Default avatar if no user or no profile image
  const defaultAvatar = "/assets/images/default-avatar.png";
  // Use user's profile image if available
  const profileImage = currentUser?.profileImage || defaultAvatar;

  const handleCreatePost = async (e) => {
    e.preventDefault();
    setIsSubmitting(true);
    setError(null);

    try {
      const newPost = {
        description: postContent,
        user: {
          userId: currentUser.userId
        }
      };

      await api.createPost(newPost);
      
      // Reset form and close modal
      setPostTitle('');
      setPostContent('');
      setShowCreatePostModal(false);
      
      // Show success notification (you could add a toast notification here)
      alert('Post created successfully!');
    } catch (err) {
      setError(err.message || 'Failed to create post');
    } finally {
      setIsSubmitting(false);
    }
  };

  // Close search results when clicking outside
  useEffect(() => {
    const handleClickOutside = (event) => {
      if (searchRef.current && !searchRef.current.contains(event.target)) {
        setShowSearchResults(false);
      }
    };

    document.addEventListener('mousedown', handleClickOutside);
    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, []);

  // Handle search input change
  const handleSearchChange = async (e) => {
    const query = e.target.value;
    setSearchQuery(query);
    
    if (query.trim().length >= 2) {
      setIsSearching(true);
      setShowSearchResults(true);
      
      try {
        const results = await api.searchUsers(query);
        setSearchResults(results);
      } catch (error) {
        console.error('Error searching users:', error);
      } finally {
        setIsSearching(false);
      }
    } else {
      setSearchResults([]);
      setShowSearchResults(false);
    }
  };

  // Navigate to user profile
  const navigateToProfile = (userId) => {
    setShowSearchResults(false);
    setSearchQuery('');
    navigate(`/user/${userId}`);
  };

  return (
    <>
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
              
              <div className="hidden sm:ml-6 sm:flex sm:items-center relative" ref={searchRef}>
                <div className="relative">
                  <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                    <SearchIcon className="h-5 w-5 text-gray-400" />
                  </div>
                  <input
                    className="block w-full pl-10 pr-3 py-2 border border-gray-200 rounded-xl text-sm placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 dark:bg-slate-700 dark:border-slate-600 dark:placeholder-gray-400 dark:text-white"
                    placeholder="Search for users..."
                    type="search"
                    value={searchQuery}
                    onChange={handleSearchChange}
                  />
                </div>
                
                {/* Search Results Dropdown */}
                {showSearchResults && (
                  <div className="absolute top-full left-0 mt-2 w-full bg-white dark:bg-slate-800 rounded-lg shadow-lg z-50 max-h-96 overflow-y-auto">
                    {isSearching ? (
                      <div className="p-4 text-center text-gray-500 dark:text-gray-400">
                        <div className="animate-spin rounded-full h-6 w-6 border-t-2 border-b-2 border-blue-500 mx-auto"></div>
                        <p className="mt-2">Searching...</p>
                      </div>
                    ) : searchResults.length > 0 ? (
                      <ul>
                        {searchResults.map(user => (
                          <li 
                            key={user.userId} 
                            className="px-4 py-3 hover:bg-gray-100 dark:hover:bg-slate-700 cursor-pointer"
                            onClick={() => navigateToProfile(user.userId)}
                          >
                            <div className="flex items-center">
                              <img 
                                src={user.profileImage || "/assets/images/default-avatar.png"} 
                                alt={`${user.firstName} ${user.lastName}`}
                                className="h-10 w-10 rounded-full mr-3 object-cover"
                                onError={(e) => {
                                  e.target.onerror = null;
                                  e.target.src = "/assets/images/default-avatar.png";
                                }}
                              />
                              <div>
                                <p className="font-medium text-gray-900 dark:text-white">
                                  {user.firstName} {user.lastName}
                                </p>
                                <p className="text-sm text-gray-500 dark:text-gray-400">
                                  @{user.username}
                                </p>
                              </div>
                            </div>
                          </li>
                        ))}
                      </ul>
                    ) : searchQuery.trim().length >= 2 ? (
                      <div className="p-4 text-center text-gray-500 dark:text-gray-400">
                        No users found matching "{searchQuery}"
                      </div>
                    ) : null}
                  </div>
                )}
              </div>
            </div>
            
            <div className="flex items-center space-x-4">
              {/* Theme Toggle Button */}
              <button 
                onClick={toggleTheme}
                className="p-1.5 rounded-lg text-gray-500 hover:text-gray-700 hover:bg-gray-100 dark:text-gray-400 dark:hover:text-gray-200 dark:hover:bg-slate-700"
                aria-label="Toggle dark mode"
              >
                {darkMode ? (
                  <SunIcon className="h-6 w-6" />
                ) : (
                  <MoonIcon className="h-6 w-6" />
                )}
              </button>
              <button className="p-1.5 rounded-lg text-gray-500 hover:text-gray-700 hover:bg-gray-100 dark:text-gray-400 dark:hover:text-gray-200 dark:hover:bg-slate-700">
                <BellIcon className="h-6 w-6" />
                <span className="absolute top-3 right-3 block h-2.5 w-2.5 rounded-full bg-red-500 ring-2 ring-white dark:ring-slate-800"></span>
              </button>
              
              {!isMobile && (
                <button 
                  onClick={() => setShowCreatePostModal(true)}
                  className="flex items-center space-x-1 px-3 py-1.5 bg-blue-600 hover:bg-blue-700 text-white rounded-lg transition-all duration-200 shadow-sm hover:shadow-md"
                >
                  <PlusIcon className="h-5 w-5" />
                  <span>Create</span>
                </button>
              )}
              
              <div className="relative">
                <Link to="/profile">
                  <button className="flex rounded-full bg-gray-100 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 dark:bg-slate-700">
                    <img
                      className="h-8 w-8 rounded-full border-2 border-white dark:border-slate-700 object-cover"
                      src={profileImage}
                      alt={currentUser ? `${currentUser.firstName} ${currentUser.lastName}` : "User avatar"}
                      onError={(e) => {
                        e.target.onerror = null;
                        e.target.src = defaultAvatar;
                      }}
                    />
                  </button>
                </Link>
                {currentUser && (
                  <div className="hidden group-hover:block absolute right-0 mt-2 w-48 bg-white dark:bg-slate-800 rounded-md shadow-lg py-1 z-10">
                    <div className="px-4 py-2 text-sm text-gray-700 dark:text-gray-200">
                      <p className="font-medium">{currentUser.firstName} {currentUser.lastName}</p>
                      <p className="text-xs text-gray-500 dark:text-gray-400">@{currentUser.username}</p>
                    </div>
                  </div>
                )}
              </div>
            </div>
          </div>
        </div>
      </header>

      {/* Create Post Modal */}
      {showCreatePostModal && (
        <div className="fixed inset-0 z-50 overflow-y-auto">
          <div className="flex items-center justify-center min-h-screen pt-4 px-4 pb-20 text-center sm:block sm:p-0">
            <div className="fixed inset-0 transition-opacity" aria-hidden="true">
              <div className="absolute inset-0 bg-gray-500 dark:bg-gray-900 opacity-75"></div>
            </div>

            <span className="hidden sm:inline-block sm:align-middle sm:h-screen" aria-hidden="true">​</span>

            <div className="inline-block align-bottom bg-white dark:bg-slate-800 rounded-lg text-left overflow-hidden shadow-xl transform transition-all sm:my-8 sm:align-middle sm:max-w-lg sm:w-full">
              <div className="absolute top-0 right-0 pt-4 pr-4">
                <button
                  onClick={() => setShowCreatePostModal(false)}
                  className="text-gray-400 hover:text-gray-500 dark:text-gray-300 dark:hover:text-gray-200 focus:outline-none"
                >
                  <XIcon className="h-6 w-6" />
                </button>
              </div>
              
              <div className="px-4 pt-5 pb-4 sm:p-6 sm:pb-4">
                <h3 className="text-lg leading-6 font-medium text-gray-900 dark:text-white mb-4">Create a New Post</h3>
                
                {error && (
                  <div className="mb-4 p-3 bg-red-100 border border-red-400 text-red-700 rounded-lg">
                    {error}
                  </div>
                )}
                
                <form onSubmit={handleCreatePost}>
                  <div className="mb-4">
                    <label htmlFor="postTitle" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
                      Title
                    </label>
                    <input
                      type="text"
                      id="postTitle"
                      value={postTitle}
                      onChange={(e) => setPostTitle(e.target.value)}
                      className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500 dark:bg-slate-700 dark:border-slate-600 dark:text-white"
                      placeholder="Enter a title for your post"
                    />
                  </div>
                  
                  <div className="mb-4">
                    <label htmlFor="postContent" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
                      Content
                    </label>
                    <textarea
                      id="postContent"
                      value={postContent}
                      onChange={(e) => setPostContent(e.target.value)}
                      rows={5}
                      className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500 dark:bg-slate-700 dark:border-slate-600 dark:text-white"
                      placeholder="What's on your mind?"
                      required
                    />
                  </div>
                  
                  <div className="mt-5 sm:mt-6 flex justify-end space-x-3">
                    <button
                      type="button"
                      onClick={() => setShowCreatePostModal(false)}
                      className="px-4 py-2 bg-gray-200 text-gray-800 rounded-lg hover:bg-gray-300 transition-colors duration-200 dark:bg-slate-700 dark:text-white dark:hover:bg-slate-600"
                    >
                      Cancel
                    </button>
                    <button
                      type="submit"
                      disabled={isSubmitting}
                      className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors duration-200 disabled:opacity-50 disabled:cursor-not-allowed"
                    >
                      {isSubmitting ? 'Posting...' : 'Post'}
                    </button>
                  </div>
                </form>
              </div>
            </div>
          </div>
        </div>
      )}
    </>
  );
};

export default TopBar;
