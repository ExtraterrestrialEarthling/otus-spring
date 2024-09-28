import React, { useState, useEffect } from 'react';
import rentalService from '../services/rentalService';
import CarList from './CarList';
import RentalList from './RentalList';

const RentalManager = () => {
    const [rentals, setRentals] = useState([]);

    const fetchRentals = () => {
        rentalService.getRentals()
            .then(response => {
                setRentals(response.data);
            })
            .catch(error => {
                console.log('Error fetching rentals: ' + error.message);
            });
    };

    useEffect(() => {
        fetchRentals();

        const intervalId = setInterval(fetchRentals, 60000);

        return () => clearInterval(intervalId);
    }, []);

    return (
        <div className="container">
            <CarList onRentCar={fetchRentals} />
            <RentalList rentals={rentals} />
        </div>
    );
};

export default RentalManager;
