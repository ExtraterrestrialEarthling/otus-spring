import React, { useState, useEffect } from 'react';
import carService from '../services/carService';
import rentalService from '../services/rentalService';

const CarList = ({ onRentCar }) => {
    const [cars, setCars] = useState([]);
    const [rentalDurations, setRentalDurations] = useState({});
    const [isOpen, setIsOpen] = useState(false);

    useEffect(() => {
        carService.getCars()
            .then(response => {
                setCars(response.data._embedded.cars);
            })
            .catch(error => {
                alert('Error fetching cars: ' + error.message);
            });
    }, []);

    const handleRent = (carId) => {
        const duration = rentalDurations[carId] || 60;
        rentalService.rentCar(carId, duration)
            .then(() => {
                alert('Rental has been processed, check your rentals to see the details');
                onRentCar();
            })
            .catch(error => {
                alert('Error during rental: ' + error.message);
            });
    };

    const handleDurationChange = (carId, event) => {
        const { value } = event.target;
        setRentalDurations(prevDurations => ({
            ...prevDurations,
            [carId]: value,
        }));
    };

    return (
        <div className="accordion">
            <div className="accordion-header" onClick={() => setIsOpen(!isOpen)}>
                <h2>Available Cars</h2>
                <span>{isOpen ? '-' : '+'}</span>
            </div>
            <div className={`accordion-content ${isOpen ? 'active' : ''}`}>
                <ul>
                    {cars.map(car => (
                        <li key={car.id}>
                            <div className="car-info">
                                <strong>{car.brand} {car.model} ({car.yearOfManufacture})</strong> - ${car.pricePerHour}/hr
                            </div>
                            <div className="car-actions">
                                <input
                                    type="number"
                                    placeholder="Duration"
                                    value={rentalDurations[car.id] || ''}
                                    onChange={(event) => handleDurationChange(car.id, event)}
                                />
                                <div> min. </div>
                                <button onClick={() => handleRent(car.id)}>Rent</button>
                            </div>
                        </li>
                    ))}
                </ul>
            </div>
        </div>
    );
};

export default CarList;
