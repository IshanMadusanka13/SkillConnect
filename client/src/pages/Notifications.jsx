import React, { useState } from 'react';
import AppShell from '../components/layout/AppShell';
import NotificationItem from '../components/ui/NotificationItem';

const Notifications = () => {
  const [activeTab, setActiveTab] = useState('all');
  
  const tabs = [
    
{ id: 'all', label: 'All' },
{ id: 'mentions', label: 'Mentions' },
{ id: 'system', label: 'System' }
];

const notifications = [
{
id: 1,
type: 'like',
user: {
id: 2,
name: 'Sarah Johnson',
avatar: '/assets/images/default-avatar.png'
},
message: 'liked your post about React Hooks',
time: '10 minutes ago',
read: false
},
{
id: 2,
type: 'comment',
user: {
id: 3,
name: 'Mike Peters',
avatar: '/assets/images/default-avatar.png'
},
message: 'commented on your post: "This is really helpful, thanks for sharing!"',
time: '2 hours ago',
read: false
},
{
id: 3,
type: 'follow',
user: {
id: 4,
name: 'David Kim',
avatar: '/assets/images/default-avatar.png'
},
message: 'started following you',
time: 'Yesterday',
read: true
},
{
id: 4,
type: 'mention',
user: {
id: 5,
name: 'Emily Zhang',
avatar: '/assets/images/default-avatar.png'
},
message: 'mentioned you in a comment: "@alexchen what resources did you use to learn Redux?"',
time: '2 days ago',
read: true
},
{
id: 5,
type: 'system',
message: 'Your learning streak is now 7 days! Keep it up!',
time: '3 days ago',
read: true
}
];

const filteredNotifications = notifications.filter(notification => {
if (activeTab === 'all') return true;
if (activeTab === 'mentions') return notification.type === 'mention';
if (activeTab === 'system') return notification.type === 'system';
return false;
});

return (
<AppShell>
<div className="space-y-6">
<div className="bg-white dark:bg-slate-800 rounded-2xl shadow-md p-6">
<h1 className="text-2xl font-bold text-gray-900 dark:text-white mb-6">Notifications</h1>

text
      <div className="border-b border-gray-200 dark:border-slate-700">
        <nav className="-mb-px flex space-x-8">
          {tabs.map(tab => (
            <button
              key={tab.id}
              onClick={() => setActiveTab(tab.id)}
              className={`
                whitespace-nowrap pb-4 px-1 border-b-2 font-medium text-sm
                ${activeTab === tab.id
                  ? 'border-blue-500 text-blue-600 dark:text-blue-400'
                  : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300 dark:text-gray-400 dark:hover:text-gray-300 dark:hover:border-gray-600'}
              `}
            >
              {tab.label}
              {tab.id === 'all' && (
                <span className="ml-2 bg-blue-100 text-blue-600 dark:bg-blue-900/30 dark:text-blue-400 px-2 py-0.5 rounded-full text-xs">
                  {notifications.filter(n => !n.read).length}
                </span>
              )}
            </button>
          ))}
        </nav>
      </div>
    </div>
    
    <div className="bg-white dark:bg-slate-800 rounded-2xl shadow-md overflow-hidden">
      {filteredNotifications.length > 0 ? (
        <div className="divide-y divide-gray-200 dark:divide-slate-700">
          {filteredNotifications.map(notification => (
            <NotificationItem key={notification.id} notification={notification} />
          ))}
        </div>
      ) : (
        <div className="p-6 text-center">
          <svg className="mx-auto h-12 w-12 text-gray-400 dark:text-gray-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1} d="M15 17h5l-1.405-1.405A2.032 2.032 0 0118 14.158V11a6.002 6.002 0 00-4-5.659V5a2 2 0 10-4 0v.341C7.67 6.165 6 8.388 6 11v3.159c0 .538-.214 1.055-.595 1.436L4 17h5m6 0v1a3 3 0 11-6 0v-1m6 0H9" />
          </svg>
          <h3 className="mt-2 text-sm font-medium text-gray-900 dark:text-white">No notifications</h3>
          <p className="mt-1 text-sm text-gray-500 dark:text-gray-400">You're all caught up!</p>
        </div>
      )}
    </div>
  </div>
</AppShell>
);
};

export default Notifications;
