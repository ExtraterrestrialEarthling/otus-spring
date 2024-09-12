import React, { useState } from 'react';
import { createGenre } from "../services/genreService";
import { Link } from 'react-router-dom'; // Import Link for routing
import styles from "./CreateGenre.module.css";
import {Helmet} from "react-helmet";

const CreateGenre = () => {
    const [name, setName] = useState('');
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(null);

    const handleSubmit = async (event) => {
        event.preventDefault();
        const genre = { name };

        try {
            await createGenre({ name });
            setSuccess('Genre created successfully!');
            setError(null);
        } catch (error) {
            if (error.message === 'ALREADY_EXISTS') {
                setError('The genre already exists. Please try adding a different genre.');
            } else {
                setError('Failed to create genre. Please try again later.');
                console.error('Caught error:', error); // Log the error for debugging
            }
            setSuccess(null);
        }
    };

    return (
        <div className={styles.body}>
            <Helmet>
                <title>Create Genre</title>
            </Helmet>
            <div className={styles.formContainer}>
                <div className={styles.headerContainer}>
                    <Link to="/" className={styles.backButton}>Back</Link>
                    <h3 className={styles.header}>Create Genre</h3>
                </div>
                {success && (
                    <div className={styles.successMessage}>
                        {success}
                    </div>
                )}
                {error && (
                    <div className={styles.errorMessage}>
                            {error}
                    </div>
                )}
                <form onSubmit={handleSubmit}>
                    <div className={styles.row}>
                        <label className={styles.label} htmlFor="name-input">Name:</label>
                        <input
                            id="name-input"
                            className={styles.inputText}
                            type="text"
                            value={name}
                            onChange={(e) => setName(e.target.value)}
                            required
                        />
                    </div>
                    <button type="submit" className={styles.submitButton}>Save</button>
                </form>
            </div>
        </div>
    );
};

export default CreateGenre;
