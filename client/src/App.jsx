import React from 'react';
import { Routes, Route } from 'react-router-dom';
import { AuthProvider } from '../src/contexts/AuthContext';
import { ThemeProvider } from '../src/contexts/ThemeContext';

// Pages
import Login from '../src/pages/auth/Login';
import Register from '../src/pages/auth/Register';
import Home from '../src/pages/Home';
import Discover from '../src/pages/Discover';
import Profile from '../src/pages/Profile';
import LearningPlan from '../src/pages/LearningPlan';
import Progress from '../src/pages/Progress';
import Notifications from '../src/pages/Notifications';
import Admin from '../src/pages/Admin';

const App = () => {
  return (
    <ThemeProvider>
      <AuthProvider>
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/" element={<Home />} />
          <Route path="/discover" element={<Discover />} />
          <Route path="/profile" element={<Profile />} />
          <Route path="/plan" element={<LearningPlan />} />
          <Route path="/progress" element={<Progress />} />
          <Route path="/notifications" element={<Notifications />} />
          <Route path="/admin" element={<Admin />} />
        </Routes>
      </AuthProvider>
    </ThemeProvider>
  );
};

export default App;
