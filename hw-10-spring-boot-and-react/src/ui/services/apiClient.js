import axios from 'axios';
import ApiError from '../errors/ApiError';

const apiClient = axios.create({
    baseURL: 'http://localhost:8080/api',
    headers: {
        'Content-Type': 'application/json',
    },
    timeout: 10000
});


apiClient.interceptors.response.use(
    response => response,
    error => {
        if (error.response) {
            const { status, data } = error.response;
            const message = data.message || 'An error occurred';
            const code = data.code || 'UNKNOWN_ERROR';
            const apiError = new ApiError(message, status, code);
            console.error('API error:', apiError);
            throw apiError;
        }
        console.error('Error without response:', error);
        throw new ApiError('Network error', 0, 'NETWORK_ERROR');
    }
);

export default apiClient;
