import React, { useState } from 'react';
import { HeartIcon, ChatIcon, ShareIcon, BookmarkIcon } from '@heroicons/react/outline';
import { HeartIcon as HeartIconSolid } from '@heroicons/react/solid';

const PostCard = ({ post }) => {
  const [liked, setLiked] = useState(false);
  const [likes, setLikes] = useState(post.likes || 0);
  const [showComments, setShowComments] = useState(false);
  
  const handleLike = () => {
    if (liked) {
      setLikes(likes - 1);
    } else {
      setLikes(likes + 1);
    }
    setLiked(!liked);
  };

  // Default values
  const defaultAvatar = "/assets/images/default-avatar.png";
  const defaultDate = new Date().toLocaleDateString();
  
  // Check if we're dealing with the backend API structure or the frontend dummy data structure
  const isBackendData = post.user && post.description;
  
  // Extract data based on the structure
  const authorName = isBackendData 
    ? `${post.user.firstName || ''} ${post.user.lastName || ''}`.trim() 
    : (post.author ? post.author.name : 'Unknown User');
  
  const authorAvatar = isBackendData
    ? (post.user.profileImage || defaultAvatar)
    : (post.author ? post.author.avatar : defaultAvatar);
  
  const postContent = isBackendData ? post.description : post.content;
  const postTitle = isBackendData ? (post.title || '') : post.title;
  const postDate = isBackendData 
    ? (post.createdAt ? new Date(post.createdAt).toLocaleDateString() : defaultDate)
    : (post.createdAt || defaultDate);
  
  const comments = post.comments || [];
  
  return (
    <div className="bg-white dark:bg-slate-800 rounded-xl shadow-md overflow-hidden transition-all duration-300 hover:shadow-lg">
      <div className="p-4">
        <div className="flex items-center mb-4">
          <img 
            src={authorAvatar} 
            alt={authorName} 
            className="h-10 w-10 rounded-full mr-3 object-cover"
            onError={(e) => {
              e.target.onerror = null;
              e.target.src = defaultAvatar;
            }}
          />
          <div>
            <h3 className="font-medium text-gray-900 dark:text-white">{authorName}</h3>
            <p className="text-xs text-gray-500 dark:text-gray-400">{postDate}</p>
          </div>
        </div>
        
        {postTitle && (
          <h2 className="text-lg font-semibold text-gray-900 dark:text-white mb-2">{postTitle}</h2>
        )}
        <p className="text-gray-700 dark:text-gray-300 mb-4">{postContent}</p>
        
        {post.image && (
          <div className="mb-4 rounded-lg overflow-hidden">
            <img 
              src={post.image} 
              alt={postTitle || "Post image"} 
              className="w-full h-auto object-cover"
            />
          </div>
        )}
        
        <div className="flex items-center justify-between text-gray-500 dark:text-gray-400">
          <div className="flex space-x-4">
            <button 
              onClick={handleLike}
              className={`flex items-center space-x-1 ${liked ? 'text-red-500 dark:text-red-400' : ''}`}
            >
              {liked ? (
                <HeartIconSolid className="h-5 w-5" />
              ) : (
                <HeartIcon className="h-5 w-5" />
              )}
              <span>{likes}</span>
            </button>
            
            <button 
              onClick={() => setShowComments(!showComments)}
              className="flex items-center space-x-1"
            >
              <ChatIcon className="h-5 w-5" />
              <span>{comments.length}</span>
            </button>
            
            <button className="flex items-center space-x-1">
              <ShareIcon className="h-5 w-5" />
            </button>
          </div>
          
          <button className="flex items-center space-x-1">
            <BookmarkIcon className="h-5 w-5" />
          </button>
        </div>
      </div>
      
      {showComments && comments.length > 0 && (
        <div className="border-t border-gray-200 dark:border-slate-700 p-4">
          <h3 className="text-sm font-medium text-gray-900 dark:text-white mb-3">Comments</h3>
          <div className="space-y-3">
            {comments.map((comment, index) => (
              <div key={index} className="flex items-start">
                <img 
                  src={comment.author?.avatar || defaultAvatar} 
                  alt={comment.author?.name || "Commenter"} 
                  className="h-8 w-8 rounded-full mr-2 object-cover"
                  onError={(e) => {
                    e.target.onerror = null;
                    e.target.src = defaultAvatar;
                  }}
                />
                <div className="flex-1 bg-gray-50 dark:bg-slate-700/50 rounded-lg p-2">
                  <div className="flex justify-between items-center mb-1">
                    <h4 className="text-sm font-medium text-gray-900 dark:text-white">
                      {comment.author?.name || "Anonymous"}
                    </h4>
                    <span className="text-xs text-gray-500 dark:text-gray-400">
                      {comment.createdAt || "Recently"}
                    </span>
                  </div>
                  <p className="text-sm text-gray-700 dark:text-gray-300">{comment.content}</p>
                </div>
              </div>
            ))}
            
            <div className="flex items-start mt-4">
              <img 
                src={defaultAvatar} 
                alt="Your avatar" 
                className="h-8 w-8 rounded-full mr-2 object-cover"
              />
              <div className="flex-1">
                <input
                  type="text"
                  placeholder="Add a comment..."
                  className="w-full border border-gray-300 dark:border-slate-600 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 dark:bg-slate-700 dark:text-white"
                />
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default PostCard;
