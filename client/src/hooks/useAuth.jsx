import { useState, useEffect, useContext, createContext } from 'react';
import { api } from '../utils/api';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [currentUser, setCurrentUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    // Check if user is logged in on mount
    const token = localStorage.getItem('token');
    if (token) {
      api.getCurrentUser()
        .then(user => {
          setCurrentUser(user);
        })
        .catch(err => {
          console.error('Failed to fetch user:', err);
          localStorage.removeItem('token');
        })
        .finally(() => {
          setLoading(false);
        });
    } else {
      setLoading(false);
    }
  }, []);

  const register = async (userData) => {
    try {
      setError(null);
      const response = await api.register(userData);

      // If registration is successful, you might want to automatically log in the user
      // or redirect them to login
      return { success: true, data: response };
    } catch (err) {
      setError(err.message || 'Registration failed');
      return { success: false, error: err.message };
    }
  };

  const login = async (credentials) => {
    try {
      setError(null);

      // Simulate login API call
      const response = await api.login(credentials);
      console.log('Login response:', response);
      // Store token in localStorage
      console.log('Token stored:');
      console.log(response.token);
      localStorage.setItem('token', response.token);

      // Use email to fetch user
      const user = await api.getCurrentUser(credentials.email);
      setCurrentUser(user);

      return { success: true };
    } catch (err) {
      setError(err.message || 'Login failed');
      return { success: false, error: err.message };
    }
  };


  const logout = () => {
    localStorage.removeItem('token');
    setCurrentUser(null);
  };

  const value = {
    currentUser,
    loading,
    error,
    register,
    login,
    logout
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === null) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};
