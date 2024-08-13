import React, { useState } from 'react';
import { createSkill } from "../services/skillService";
import { Link } from 'react-router-dom'; // Import Link for routing
import styles from "./CreateSkill.module.css";
import { Helmet } from "react-helmet";

const CreateSkill = () => {
    const [name, setName] = useState('');
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(null);

    const handleSubmit = async (event) => {
        event.preventDefault();
        const skill = { name };

        try {
            await createSkill(skill);
            setSuccess('Skill created successfully!');
            setError(null);
        } catch (error) {
            if (error.message === 'ALREADY_EXISTS') {
                setError('The skill already exists. Please try adding a different skill.');
            } else {
                setError('Failed to create skill. Please try again later.');
                console.error('Caught error:', error); // Log the error for debugging
            }
            setSuccess(null);
        }
    };

    return (
        <div className={styles.body}>
            <Helmet>
                <title>Create Skill</title>
            </Helmet>
            <div className={styles.formContainer}>
                <div className={styles.headerContainer}>
                    <Link to="/" className={styles.backButton}>Back</Link>
                    <h3 className={styles.header}>Create Skill</h3>
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

export default CreateSkill;
