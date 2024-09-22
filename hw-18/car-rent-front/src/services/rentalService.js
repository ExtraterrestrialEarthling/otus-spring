import axios from 'axios';

const API_BASE_URL = 'http://localhost:8888/api';

const getRentals = () => {
    return axios.get(`${API_BASE_URL}/rentals`);
};

const rentCar = (carId, duration) => {
    return axios.post(`${API_BASE_URL}/rentals`, {
        carId: carId,
        durationInMinutes: duration,
    });
};

const rentalService = {
    getRentals,
    rentCar
};

export default rentalService;
