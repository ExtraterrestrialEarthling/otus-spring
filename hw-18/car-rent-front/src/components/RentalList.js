import React, { useState } from 'react';

const RentalList = ({ rentals }) => {
    const [isOpen, setIsOpen] = useState(false);

    return (
        <div className="accordion">
            <div className="accordion-header" onClick={() => setIsOpen(!isOpen)}>
                <h2>Your Rentals</h2>
                <span>{isOpen ? '-' : '+'}</span>
            </div>
            <div className={`accordion-content ${isOpen ? 'active' : ''}`}>
                {rentals.length > 0 ? (
                    <ul>
                        {rentals.map(rental => (
                            <li key={rental.id}>
                                Car ID: {rental.carId}, Status: {rental.status}, Total price: ${rental.priceTotal}
                            </li>
                        ))}
                    </ul>
                ) : (
                    <p>No rentals found.</p>
                )}
            </div>
        </div>
    );
};

export default RentalList;
