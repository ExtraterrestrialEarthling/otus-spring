import axios from 'axios';

const API_BASE_URL = 'http://localhost:8888/api';

const getCars = () => {
    return axios.get(`${API_BASE_URL}/cars`);
};

const carService = {
    getCars,
};

export default carService;
