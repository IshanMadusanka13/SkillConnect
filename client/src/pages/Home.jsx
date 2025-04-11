import React from 'react';
import AppShell from '../components/layout/AppShell';
import PostCard from '../components/ui/PostCard';
import { dummyPosts } from '../data/dummyPosts';

const Home = () => {
return (
<AppShell>
<div className="space-y-8">
<div className="bg-white dark:bg-slate-800 rounded-2xl shadow-md p-6">
<h1 className="text-2xl font-bold text-gray-900 dark:text-white mb-2">Welcome back, Alex!</h1>
<p className="text-gray-600 dark:text-gray-300">Ready to continue your learning journey?</p>
<div className="mt-4 flex space-x-4">
<button className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors duration-200">
Continue Learning
</button>
<button className="px-4 py-2 bg-gray-200 text-gray-800 rounded-lg hover:bg-gray-300 transition-colors duration-200 dark:bg-slate-700 dark:text-white dark:hover:bg-slate-600">
Explore New Skills
</button>
</div>
</div>


    <div className="bg-white dark:bg-slate-800 rounded-2xl shadow-md p-6">
      <h2 className="text-xl font-semibold text-gray-900 dark:text-white mb-4">Your Feed</h2>
      <div className="space-y-6">
        {dummyPosts.map(post => (
          <PostCard key={post.id} post={post} />
        ))}
      </div>
    </div>
  </div>
</AppShell>
);
};

export default Home;