import React from 'react';
import AppShell from '../components/layout/AppShell';
import { dummyUsers } from '../data/dummyUsers';
import { dummyPosts } from '../data/dummyPosts';
import PostCard from '../components/ui/PostCard';

const Profile = () => {
  const user = dummyUsers.find(user => user.id === 1);
  // Assume this is the logged-in user

return (
<AppShell>
<div className="space-y-8">
<div className="relative">
<div className="h-48 w-full mb-19 bg-gradient-to-r from-blue-400 to-indigo-500 rounded-2xl"></div>
<img src={user.avatar} alt={user.name} className="absolute bottom-0 left-6 transform translate-y-1/2 w-24 h-24 rounded-full border-4 border-white dark:border-slate-800" />
</div>


    <div className="mt-16 px-6">
      <h1 className="text-2xl mt-12 font-bold text-gray-900 dark:text-white">{user.name}</h1>
      <p className="text-gray-600 dark:text-gray-300 mt-1">{user.bio}</p>
      
      <div className="mt-4 flex space-x-4">
        <button className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors duration-200">
          Edit Profile
        </button>
        <button className="px-4 py-2 bg-gray-200 text-gray-800 rounded-lg hover:bg-gray-300 transition-colors duration-200 dark:bg-slate-700 dark:text-white dark:hover:bg-slate-600">
          Share Profile
        </button>
      </div>
      
      <div className="mt-6 flex space-x-4">
        <div className="text-center">
          <span className="block text-2xl font-bold text-gray-900 dark:text-white">{user.followers}</span>
          <span className="text-sm text-gray-500 dark:text-gray-400">Followers</span>
        </div>
        <div className="text-center">
          <span className="block text-2xl font-bold text-gray-900 dark:text-white">{user.following}</span>
          <span className="text-sm text-gray-500 dark:text-gray-400">Following</span>
        </div>
        <div className="text-center">
          <span className="block text-2xl font-bold text-gray-900 dark:text-white">{dummyPosts.length}</span>
          <span className="text-sm text-gray-500 dark:text-gray-400">Posts</span>
        </div>
      </div>
    </div>
    
    <div className="mt-8">
      <h2 className="text-xl font-semibold text-gray-900 dark:text-white mb-4">Recent Posts</h2>
      <div className="space-y-6">
        {dummyPosts.slice(0, 3).map(post => (
          <PostCard key={post.id} post={post} />
        ))}
      </div>
    </div>
  </div>
</AppShell>
);
};

export default Profile;