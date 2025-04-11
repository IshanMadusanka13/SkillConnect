import React from 'react';
import AppShell from '../components/layout/AppShell';
import ProgressBar from '../components/ui/ProgressBar';
import { dummyPlans } from '../data/dummyPlans';

const Progress = () => {
  // Calculate overall progress
  const totalProgress = dummyPlans.reduce((acc, plan) => acc + plan.progress, 0) / dummyPlans.length;
  
  const achievements = [
    { id: 1, title: "React Fundamentals", date: "March 15, 2025", icon: "🏆" },
    { id: 2, title: "JavaScript ES6+", date: "February 20, 2025", icon: "🌟" },
    { id: 3, title: "CSS Grid Master", date: "January 10, 2025", icon: "🎯" }
  ];
  
  return (
    <AppShell>
      <div className="space-y-8">
        <div className="bg-white dark:bg-slate-800 rounded-2xl shadow-md p-6">
          <h1 className="text-2xl font-bold text-gray-900 dark:text-white mb-2">Your Learning Progress</h1>
          <p className="text-gray-600 dark:text-gray-300 mb-6">Track your journey and celebrate your achievements</p>
          
          <div className="bg-gray-50 dark:bg-slate-700/50 rounded-xl p-6">
            <div className="flex flex-col md:flex-row md:items-center md:justify-between mb-4">
              <div>
                <h2 className="text-lg font-semibold text-gray-900 dark:text-white">Overall Progress</h2>
                <p className="text-sm text-gray-500 dark:text-gray-400">Across all learning plans</p>
              </div>
              <div className="mt-2 md:mt-0">
                <span className="text-2xl font-bold text-blue-600 dark:text-blue-400">{Math.round(totalProgress)}%</span>
              </div>
            </div>
            
            <div className="h-4 bg-gray-200 dark:bg-slate-600 rounded-full overflow-hidden">
              <div 
                className="h-full bg-gradient-to-r from-blue-500 to-indigo-500 transition-all duration-300"
                style={{ width: `${totalProgress}%` }}
              ></div>
            </div>
          </div>
        </div>
        
        <div className="bg-white dark:bg-slate-800 rounded-2xl shadow-md p-6">
          <h2 className="text-xl font-semibold text-gray-900 dark:text-white mb-4">Learning Plans Progress</h2>
          
          <div className="space-y-6">
            {dummyPlans.map(plan => (
              <div key={plan.id} className="bg-gray-50 dark:bg-slate-700/50 rounded-xl p-4">
                <div className="flex flex-col md:flex-row md:items-center md:justify-between mb-2">
                  <h3 className="text-lg font-medium text-gray-900 dark:text-white">{plan.title}</h3>
                  <span className="text-sm font-medium text-blue-600 dark:text-blue-400">{plan.progress}%</span>
                </div>
                <ProgressBar progress={plan.progress} />
                <div className="mt-3 flex justify-between text-sm">
                  <span className="text-gray-500 dark:text-gray-400">{Math.round(plan.totalItems * (plan.progress / 100))}/{plan.totalItems} items completed</span>
                  <button className="text-blue-600 hover:text-blue-800 dark:text-blue-400 dark:hover:text-blue-300">
                    View Details
                  </button>
                </div>
              </div>
            ))}
          </div>
        </div>
        
        <div className="bg-white dark:bg-slate-800 rounded-2xl shadow-md p-6">
          <h2 className="text-xl font-semibold text-gray-900 dark:text-white mb-4">Recent Achievements</h2>
          
          <div className="relative">
            <div className="absolute inset-0 flex items-center" aria-hidden="true">
              <div className="w-0.5 h-full mx-auto bg-gray-200 dark:bg-slate-700"></div>
            </div>
            
            <div className="relative space-y-8">
              {achievements.map((achievement, index) => (
                <div key={achievement.id} className="relative">
                  <div className="flex items-center">
                    <div className={`h-9 w-9 rounded-full flex items-center justify-center ring-8 ring-white dark:ring-slate-800 ${index === 0 ? 'bg-blue-500' : 'bg-gray-100 dark:bg-slate-700'}`}>
                      <span className="text-lg">{achievement.icon}</span>
                    </div>
                    <div className="ml-4 min-w-0 flex-1">
                      <h3 className="text-base font-medium text-gray-900 dark:text-white">
                        {achievement.title}
                      </h3>
                      <p className="text-sm text-gray-500 dark:text-gray-400">
                        {achievement.date}
                      </p>
                    </div>
                    <div>
                      <button className="inline-flex items-center px-3 py-1 border border-transparent text-sm leading-4 font-medium rounded-full shadow-sm text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 dark:focus:ring-offset-slate-800">
                        Share
                      </button>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          </div>
        </div>
      </div>
    </AppShell>
  );
};

export default Progress;
