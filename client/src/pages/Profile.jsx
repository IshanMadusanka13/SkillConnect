import React, { useState, useEffect } from 'react';
import { Navigate } from 'react-router-dom';
import AppShell from '../components/layout/AppShell';
import { dummyPosts } from '../data/dummyPosts';
import PostCard from '../components/ui/PostCard';
import { useAuth } from '../hooks/useAuth';
import { api } from '../utils/api';
import { XIcon, PencilIcon, TrashIcon } from '@heroicons/react/outline';

const Profile = () => {
  const { currentUser, loading } = useAuth();
  const [isEditing, setIsEditing] = useState(false);
  const [formData, setFormData] = useState({});
  const [updateLoading, setUpdateLoading] = useState(false);
  const [updateError, setUpdateError] = useState(null);
  const [updateSuccess, setUpdateSuccess] = useState(false);
  
  // Post-related states
  const [userPosts, setUserPosts] = useState([]);
  const [postsLoading, setPostsLoading] = useState(true);
  const [postsError, setPostsError] = useState(null);
  const [editingPost, setEditingPost] = useState(null);
  const [editPostData, setEditPostData] = useState({ title: '', content: '' });
  const [showEditPostModal, setShowEditPostModal] = useState(false);
  
  // Initialize form data when currentUser changes
  useEffect(() => {
    if (currentUser) {
      setFormData({
        firstName: currentUser.firstName || '',
        lastName: currentUser.lastName || '',
        bio: currentUser.bio || '',
        profileImage: currentUser.profileImage || '',
      });
      
      // Fetch user posts when user data is available
      fetchUserPosts();
    }
  }, [currentUser]);
  
  // Fetch user posts
  const fetchUserPosts = async () => {
    if (!currentUser) return;
    
    setPostsLoading(true);
    setPostsError(null);
    
    try {
      // Use the specific endpoint for user posts
      const posts = await api.getUserPosts(currentUser.userId);
      setUserPosts(posts);
    } catch (error) {
      setPostsError(error.message || 'Failed to fetch posts');
      console.error('Error fetching posts:', error);
      // Fallback to dummy posts if API fails
      setUserPosts(dummyPosts.slice(0, 3));
    } finally {
      setPostsLoading(false);
    }
  };
  
  // Handle post edit
  const handleEditPost = (post) => {
    setEditingPost(post);
    setEditPostData({
      title: post.title,
      content: post.description
    });
    setShowEditPostModal(true);
  };
  
  // Handle post delete
  const handleDeletePost = async (postId) => {
    if (!confirm('Are you sure you want to delete this post?')) return;
    
    try {
      await api.deletePost(postId);
      // Remove the deleted post from the state
      setUserPosts(prevPosts => prevPosts.filter(post => post.postId !== postId));
      alert('Post deleted successfully!');
    } catch (error) {
      alert('Failed to delete post: ' + (error.message || 'Unknown error'));
    }
  };
  
  // Handle post update
  const handleUpdatePost = async (e) => {
    e.preventDefault();
    
    try {
      const updatedPost = await api.updatePost(editingPost.postId, {
        ...editingPost,
        description: editPostData.content
      });
      
      // Update the post in the state
      setUserPosts(prevPosts => 
        prevPosts.map(post => 
          post.postId === editingPost.postId ? updatedPost : post
        )
      );
      
      // Close the modal
      setShowEditPostModal(false);
      alert('Post updated successfully!');
    } catch (error) {
      alert('Failed to update post: ' + (error.message || 'Unknown error'));
    }
  };

  // Show loading state while authentication is being checked
  if (loading) {
    return (
      <AppShell>
        <div className="flex justify-center items-center h-64">
          <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-blue-500"></div>
        </div>
      </AppShell>
    );
  }
  
  // Redirect to login if not authenticated
  if (!currentUser) {
    return <Navigate to="/login" />;
  }

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setUpdateLoading(true);
    setUpdateError(null);
    setUpdateSuccess(false);
    
    try {
      // We need to send the userId to match the backend endpoint
      const updatedUser = await api.updateUserProfile({
        ...formData,
        userId: currentUser.userId
      });
      
      setUpdateSuccess(true);
      setIsEditing(false);
      
      // Refresh the page after successful update to get the updated user data
      setTimeout(() => {
        window.location.reload();
      }, 1500);
    } catch (error) {
      setUpdateError(error.message || 'Failed to update profile');
    } finally {
      setUpdateLoading(false);
    }
  };

  // Default values for missing user properties
  const defaultProfileImage = "/assets/images/default-avatar.png";
  
  return (
    <AppShell>
      <div className="space-y-8">
        <div className="relative">
          <div className="h-48 w-full mb-19 bg-gradient-to-r from-blue-400 to-indigo-500 rounded-2xl"></div>
          <img 
            src={currentUser.profileImage || defaultProfileImage} 
            alt={`${currentUser.firstName || 'User'} ${currentUser.lastName || ''}`}
            className="absolute bottom-0 left-6 transform translate-y-1/2 w-24 h-24 rounded-full border-4 border-white dark:border-slate-800 object-cover"
            onError={(e) => {
              e.target.onerror = null;
              e.target.src = defaultProfileImage;
            }}
          />
        </div>

        <div className="mt-16 px-6">
          {!isEditing ? (
            <>
              <h1 className="text-2xl mt-12 font-bold text-gray-900 dark:text-white">
                {currentUser.firstName} {currentUser.lastName}
              </h1>
              <p className="text-sm text-gray-500 dark:text-gray-400 mt-1">@{currentUser.username}</p>
              <p className="text-gray-600 dark:text-gray-300 mt-2">
                {currentUser.bio || 'No bio available'}
              </p>
              
              <div className="mt-4 flex space-x-4">
                <button 
                  onClick={() => setIsEditing(true)}
                  className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors duration-200"
                >
                  Edit Profile
                </button>
              </div>
            </>
          ) : (
            <div className="mt-12 bg-white dark:bg-slate-800 rounded-xl shadow-md p-6">
              <h2 className="text-xl font-semibold text-gray-900 dark:text-white mb-4">Edit Profile</h2>
              
              {updateError && (
                <div className="mb-4 p-3 bg-red-100 border border-red-400 text-red-700 rounded-lg">
                  {updateError}
                </div>
              )}
              
              {updateSuccess && (
                <div className="mb-4 p-3 bg-green-100 border border-green-400 text-green-700 rounded-lg">
                  Profile updated successfully!
                </div>
              )}
              
              <form onSubmit={handleSubmit} className="space-y-4">
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <div>
                    <label htmlFor="firstName" className="block text-sm font-medium text-gray-700 dark:text-gray-300">
                      First Name
                    </label>
                    <input
                      type="text"
                      id="firstName"
                      name="firstName"
                      value={formData.firstName}
                      onChange={handleChange}
                      className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500 dark:bg-slate-700 dark:border-slate-600 dark:text-white"
                      required
                    />
                  </div>
                  
                  <div>
                    <label htmlFor="lastName" className="block text-sm font-medium text-gray-700 dark:text-gray-300">
                      Last Name
                    </label>
                    <input
                      type="text"
                      id="lastName"
                      name="lastName"
                      value={formData.lastName}
                      onChange={handleChange}
                      className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500 dark:bg-slate-700 dark:border-slate-600 dark:text-white"
                      required
                    />
                  </div>
                </div>
                
                <div>
                  <label htmlFor="profileImage" className="block text-sm font-medium text-gray-700 dark:text-gray-300">
                    Profile Image URL
                  </label>
                  <input
                    type="url"
                    id="profileImage"
                    name="profileImage"
                    value={formData.profileImage}
                    onChange={handleChange}
                    className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500 dark:bg-slate-700 dark:border-slate-600 dark:text-white"
                    placeholder="https://example.com/image.jpg"
                  />
                </div>
                
                <div>
                  <label htmlFor="bio" className="block text-sm font-medium text-gray-700 dark:text-gray-300">
                    Bio
                  </label>
                  <textarea
                    id="bio"
                    name="bio"
                    rows={3}
                    value={formData.bio}
                    onChange={handleChange}
                    className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500 dark:bg-slate-700 dark:border-slate-600 dark:text-white"
                    placeholder="Tell us about yourself"
                  />
                </div>
                
                <div className="flex justify-end space-x-3">
                  <button
                    type="button"
                    onClick={() => setIsEditing(false)}
                    className="px-4 py-2 bg-gray-200 text-gray-800 rounded-lg hover:bg-gray-300 transition-colors duration-200 dark:bg-slate-700 dark:text-white dark:hover:bg-slate-600"
                  >
                    Cancel
                  </button>
                  <button
                    type="submit"
                    disabled={updateLoading}
                    className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors duration-200 disabled:opacity-50 disabled:cursor-not-allowed"
                  >
                    {updateLoading ? 'Saving...' : 'Save Changes'}
                  </button>
                </div>
              </form>
            </div>
          )}
          
          <div className="mt-6 flex space-x-4">
            <div className="text-center">
              <span className="block text-2xl font-bold text-gray-900 dark:text-white">
                {currentUser.followers || 0}
              </span>
              <span className="text-sm text-gray-500 dark:text-gray-400">Followers</span>
            </div>
            <div className="text-center">
              <span className="block text-2xl font-bold text-gray-900 dark:text-white">
                {currentUser.following || 0}
              </span>
              <span className="text-sm text-gray-500 dark:text-gray-400">Following</span>
            </div>
            <div className="text-center">
              <span className="block text-2xl font-bold text-gray-900 dark:text-white">
                {userPosts.length || 0}
              </span>
              <span className="text-sm text-gray-500 dark:text-gray-400">Posts</span>
            </div>
          </div>
        </div>
        
        <div className="mt-8">
          <h2 className="text-xl font-semibold text-gray-900 dark:text-white mb-4">Recent Posts</h2>
          
          {postsLoading ? (
            <div className="flex justify-center items-center h-32">
              <div className="animate-spin rounded-full h-8 w-8 border-t-2 border-b-2 border-blue-500"></div>
            </div>
          ) : postsError ? (
            <div className="p-4 bg-red-50 border border-red-200 rounded-lg text-red-700">
              Failed to load posts: {postsError}
            </div>
          ) : userPosts.length === 0 ? (
            <div className="p-6 bg-gray-50 dark:bg-slate-700 rounded-lg text-center">
              <p className="text-gray-500 dark:text-gray-400">You haven't created any posts yet.</p>
            </div>
          ) : (
            <div className="space-y-6">
              {userPosts.map(post => (
                <div key={post.postId} className="relative">
                  <PostCard post={post} />
                  
                  {/* Post actions */}
                  <div className="absolute top-4 right-4 flex space-x-2">
                    <button 
                      onClick={() => handleEditPost(post)}
                      className="p-1.5 bg-white dark:bg-slate-700 rounded-full shadow-sm hover:bg-gray-100 dark:hover:bg-slate-600"
                    >
                      <PencilIcon className="h-4 w-4 text-gray-600 dark:text-gray-300" />
                    </button>
                    <button 
                      onClick={() => handleDeletePost(post.postId)}
                      className="p-1.5 bg-white dark:bg-slate-700 rounded-full shadow-sm hover:bg-gray-100 dark:hover:bg-slate-600"
                      >
                        <TrashIcon className="h-4 w-4 text-red-600 dark:text-red-400" />
                      </button>
                    </div>
                  </div>
                ))}
  
              </div>
            )}
          </div>
        </div>
  
        {/* Edit Post Modal */}
        {showEditPostModal && (
          <div className="fixed inset-0 z-50 overflow-y-auto">
            <div className="flex items-center justify-center min-h-screen pt-4 px-4 pb-20 text-center sm:block sm:p-0">
              <div className="fixed inset-0 transition-opacity" aria-hidden="true">
                <div className="absolute inset-0 bg-gray-500 dark:bg-gray-900 opacity-75"></div>
              </div>
  
              <span className="hidden sm:inline-block sm:align-middle sm:h-screen" aria-hidden="true">&#8203;</span>
  
              <div className="inline-block align-bottom bg-white dark:bg-slate-800 rounded-lg text-left overflow-hidden shadow-xl transform transition-all sm:my-8 sm:align-middle sm:max-w-lg sm:w-full">
                <div className="absolute top-0 right-0 pt-4 pr-4">
                  <button
                    onClick={() => setShowEditPostModal(false)}
                    className="text-gray-400 hover:text-gray-500 dark:text-gray-300 dark:hover:text-gray-200 focus:outline-none"
                  >
                    <XIcon className="h-6 w-6" />
                  </button>
                </div>
                
                <div className="px-4 pt-5 pb-4 sm:p-6 sm:pb-4">
                  <h3 className="text-lg leading-6 font-medium text-gray-900 dark:text-white mb-4">Edit Post</h3>
                  
                  <form onSubmit={handleUpdatePost}>
                    <div className="mb-4">
                      <label htmlFor="editPostTitle" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
                        Title
                      </label>
                      <input
                        type="text"
                        id="editPostTitle"
                        value={editPostData.title}
                        onChange={(e) => setEditPostData({...editPostData, title: e.target.value})}
                        className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500 dark:bg-slate-700 dark:border-slate-600 dark:text-white"
                        placeholder="Enter a title for your post"
                      />
                    </div>
                    
                    <div className="mb-4">
                      <label htmlFor="editPostContent" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
                        Content
                      </label>
                      <textarea
                        id="editPostContent"
                        value={editPostData.content}
                        onChange={(e) => setEditPostData({...editPostData, content: e.target.value})}
                        rows={5}
                        className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500 dark:bg-slate-700 dark:border-slate-600 dark:text-white"
                        placeholder="What's on your mind?"
                        required
                      />
                    </div>
                    
                    <div className="mt-5 sm:mt-6 flex justify-end space-x-3">
                      <button
                        type="button"
                        onClick={() => setShowEditPostModal(false)}
                        className="px-4 py-2 bg-gray-200 text-gray-800 rounded-lg hover:bg-gray-300 transition-colors duration-200 dark:bg-slate-700 dark:text-white dark:hover:bg-slate-600"
                      >
                        Cancel
                      </button>
                      <button
                        type="submit"
                        className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors duration-200"
                      >
                        Save Changes
                      </button>
                    </div>
                  </form>
                </div>
              </div>
            </div>
          </div>
        )}
      </AppShell>
    );
  };
  
  export default Profile;
  
