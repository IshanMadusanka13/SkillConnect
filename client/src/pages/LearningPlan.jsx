import React from 'react';
import AppShell from '../components/layout/AppShell';
import { dummyPlans } from '../data/dummyPlans';
import PlanItem from '../components/ui/PlanItem';

const LearningPlan = () => {
return (
<AppShell>
<div className="space-y-8">
<div className="bg-white dark:bg-slate-800 rounded-2xl shadow-md p-6">
<h1 className="text-2xl font-bold text-gray-900 dark:text-white mb-2">Your Learning Plan</h1>
<p className="text-gray-600 dark:text-gray-300">Track your progress and set new goals</p>
<button className="mt-4 px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors duration-200">
Add New Goal
</button>
</div>


    <div className="bg-white dark:bg-slate-800 rounded-2xl shadow-md p-6">
      <h2 className="text-xl font-semibold text-gray-900 dark:text-white mb-4">Current Goals</h2>
      <div className="space-y-4">
        {dummyPlans.map(plan => (
          <PlanItem key={plan.id} plan={plan} />
        ))}
      </div>
    </div>
    
    <div className="bg-white dark:bg-slate-800 rounded-2xl shadow-md p-6">
      <h2 className="text-xl font-semibold text-gray-900 dark:text-white mb-4">Suggested Skills</h2>
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
        {['React', 'Node.js', 'Python', 'Machine Learning', 'UI/UX Design', 'Data Analysis'].map(skill => (
          <div key={skill} className="bg-gray-100 dark:bg-slate-700 rounded-lg p-4 flex items-center justify-between">
            <span className="text-gray-800 dark:text-white">{skill}</span>
            <button className="text-blue-600 hover:text-blue-800 dark:text-blue-400 dark:hover:text-blue-300">
              Add
            </button>
          </div>
        ))}
      </div>
    </div>
  </div>
</AppShell>
);
};

export default LearningPlan;