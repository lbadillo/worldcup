import api from './api';

export const getGroups = async () => {
  try {
    const response = await api.get('/groups');
    return response.data;
  } catch (error) {
    console.error('Error getting groups:', error);
    throw error;
  }
};
