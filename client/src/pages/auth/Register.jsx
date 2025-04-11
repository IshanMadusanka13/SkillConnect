import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../hooks/useAuth';

const Register = () => {
const navigate = useNavigate();
const { register } = useAuth();

const [formData, setFormData] = useState({
name: '',
username: '',
email: '',
password: '',
confirmPassword: '',
acceptTerms: false
});

const [errors, setErrors] = useState({});

const handleChange = (e) => {
const { name, value, type, checked } = e.target;
setFormData(prev => ({
...prev,
[name]: type === 'checkbox' ? checked : value
}));

text
// Clear error when field is edited
if (errors[name]) {
  setErrors(prev => ({
    ...prev,
    [name]: null
  }));
}
};

const validateForm = () => {
const newErrors = {};

text
if (!formData.name.trim()) {
  newErrors.name = 'Name is required';
}

if (!formData.username.trim()) {
  newErrors.username = 'Username is required';
} else if (formData.username.includes(' ')) {
  newErrors.username = 'Username cannot contain spaces';
}

if (!formData.email.trim()) {
  newErrors.email = 'Email is required';
} else if (!/\S+@\S+\.\S+/.test(formData.email)) {
  newErrors.email = 'Email is invalid';
}

if (!formData.password) {
  newErrors.password = 'Password is required';
} else if (formData.password.length < 6) {
  newErrors.password = 'Password must be at least 6 characters';
}

if (formData.password !== formData.confirmPassword) {
  newErrors.confirmPassword = 'Passwords do not match';
}

if (!formData.acceptTerms) {
  newErrors.acceptTerms = 'You must accept the terms and conditions';
}

return newErrors;
};

const handleSubmit = (e) => {
e.preventDefault();

text
const validationErrors = validateForm();
if (Object.keys(validationErrors).length > 0) {
  setErrors(validationErrors);
  return;
}

// Register the user
const success = register({
  name: formData.name,
  username: formData.username,
  email: formData.email,
  password: formData.password,
  bio: '',
  skills: []
});

if (success) {
  navigate('/');
}
};

return (
<div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-blue-50 to-indigo-100 dark:from-slate-900 dark:to-blue-900 p-4">
<div className="w-full max-w-md">
<div className="text-center mb-10">
<img src="/assets/images/logo.svg" alt="SkillSync Logo" className="h-16 w-16 mx-auto" />
<h1 className="mt-4 text-3xl font-bold text-gray-900 dark:text-white">Join SkillSync</h1>
<p className="mt-2 text-gray-600 dark:text-gray-300">Create an account to start your learning journey</p>
</div>

text
    <div className="bg-white dark:bg-slate-800 rounded-2xl shadow-xl backdrop-blur-sm backdrop-filter bg-opacity-80 dark:bg-opacity-80 p-8 border border-gray-200 dark:border-slate-700">
      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label htmlFor="name" className="block text-sm font-medium text-gray-700 dark:text-gray-300">
            Full Name
          </label>
          <div className="mt-1">
            <input
              id="name"
              name="name"
              type="text"
              autoComplete="name"
              required
              value={formData.name}
              onChange={handleChange}
              className={`appearance-none block w-full px-4 py-3 border ${errors.name ? 'border-red-300 dark:border-red-700' : 'border-gray-300 dark:border-slate-600'} rounded-xl shadow-sm placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition-all duration-200 dark:bg-slate-700 dark:text-white`}
              placeholder="John Doe"
            />
            {errors.name && (
              <p className="mt-1 text-sm text-red-600 dark:text-red-400">{errors.name}</p>
            )}
          </div>
        </div>
        
        <div>
          <label htmlFor="username" className="block text-sm font-medium text-gray-700 dark:text-gray-300">
            Username
          </label>
          <div className="mt-1">
            <input
              id="username"
              name="username"
              type="text"
              autoComplete="username"
              required
              value={formData.username}
              onChange={handleChange}
              className={`appearance-none block w-full px-4 py-3 border ${errors.username ? 'border-red-300 dark:border-red-700' : 'border-gray-300 dark:border-slate-600'} rounded-xl shadow-sm placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition-all duration-200 dark:bg-slate-700 dark:text-white`}
              placeholder="johndoe"
            />
            {errors.username && (
              <p className="mt-1 text-sm text-red-600 dark:text-red-400">{errors.username}</p>
            )}
          </div>
        </div>
        
        <div>
          <label htmlFor="email" className="block text-sm font-medium text-gray-700 dark:text-gray-300">
            Email address
          </label>
          <div className="mt-1">
            <input
              id="email"
              name="email"
              type="email"
              autoComplete="email"
              required
              value={formData.email}
              onChange={handleChange}
              className={`appearance-none block w-full px-4 py-3 border ${errors.email ? 'border-red-300 dark:border-red-700' : 'border-gray-300 dark:border-slate-600'} rounded-xl shadow-sm placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition-all duration-200 dark:bg-slate-700 dark:text-white`}
              placeholder="you@example.com"
            />
            {errors.email && (
              <p className="mt-1 text-sm text-red-600 dark:text-red-400">{errors.email}</p>
            )}
          </div>
        </div>
        
        <div>
          <label htmlFor="password" className="block text-sm font-medium text-gray-700 dark:text-gray-300">
            Password
          </label>
          <div className="mt-1">
            <input
              id="password"
              name="password"
              type="password"
              autoComplete="new-password"
              required
              value={formData.password}
              onChange={handleChange}
              className={`appearance-none block w-full px-4 py-3 border ${errors.password ? 'border-red-300 dark:border-red-700' : 'border-gray-300 dark:border-slate-600'} rounded-xl shadow-sm placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition-all duration-200 dark:bg-slate-700 dark:text-white`}
              placeholder="- - - - - - - - "
            />
            {errors.password && (
              <p className="mt-1 text-sm text-red-600 dark:text-red-400">{errors.password}</p>
            )}
          </div>
        </div>
        
        <div>
          <label htmlFor="confirmPassword" className="block text-sm font-medium text-gray-700 dark:text-gray-300">
            Confirm Password
          </label>
          <div className="mt-1">
            <input
              id="confirmPassword"
              name="confirmPassword"
              type="password"
              autoComplete="new-password"
              required
              value={formData.confirmPassword}
              onChange={handleChange}
              className={`appearance-none block w-full px-4 py-3 border ${errors.confirmPassword ? 'border-red-300 dark:border-red-700' : 'border-gray-300 dark:border-slate-600'} rounded-xl shadow-sm placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition-all duration-200 dark:bg-slate-700 dark:text-white`}
              placeholder="- - - - - - - - "
            />
            {errors.confirmPassword && (
              <p className="mt-1 text-sm text-red-600 dark:text-red-400">{errors.confirmPassword}</p>
            )}
          </div>
        </div>
        
        <div className="flex items-center">
          <input
            id="acceptTerms"
            name="acceptTerms"
            type="checkbox"
            checked={formData.acceptTerms}
            onChange={handleChange}
            className={`h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded ${errors.acceptTerms ? 'border-red-300' : ''}`}
          />
          <label htmlFor="acceptTerms" className="ml-2 block text-sm text-gray-700 dark:text-gray-300">
            I accept the <a href="#" className="text-blue-600 hover:text-blue-500 dark:text-blue-400">Terms and Conditions</a>
          </label>
        </div>
        {errors.acceptTerms && (
          <p className="mt-1 text-sm text-red-600 dark:text-red-400">{errors.acceptTerms}</p>
        )}
        
        <div>
          <button
            type="submit"
            className="w-full flex justify-center py-3 px-4 border border-transparent rounded-xl shadow-sm text-sm font-medium text-white bg-gradient-to-r from-blue-600 to-indigo-600 hover:from-blue-700 hover:to-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 transition-all duration-200 transform hover:scale-[1.02]"
          >
            Create Account
          </button>
        </div>
      </form>
    </div>
    
    <p className="mt-8 text-center text-sm text-gray-600 dark:text-gray-400">
      Already have an account?{' '}
      <Link to="/login" className="font-medium text-blue-600 hover:text-blue-500 dark:text-blue-400">
        Sign in
      </Link>
    </p>
  </div>
</div>
);
};

export default Register;