export const API_BASE_URL = import.meta.env.VITE_API_URL;

export const adminService = {
  addLocation: async (location) => {
    const response = await fetch(`${API_BASE_URL}/admin/locations`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ location }),
    });
    return response.json();
  },

};

