import React, { useState } from 'react';
import { createTeacher } from '../services/teacherService';
import styles from './CreateTeacher.module.css';
import { Link } from 'react-router-dom';
import { Helmet } from "react-helmet";

const CreateTeacher = () => {
    const [fullName, setFullName] = useState('');
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(null);

    const handleSubmit = async (event) => {
        event.preventDefault();
        const teacher = { fullName };

        try {
            await createTeacher(teacher);
            setSuccess('Teacher created successfully!');
            setError(null);
            setFullName('');
        } catch (error) {
            console.error('Error creating teacher:', error);
            setError('Failed to create teacher');
            setSuccess(null);
        }
    };

    return (
        <div className={styles.body}>
            <Helmet>
                <title>Create Teacher</title>
            </Helmet>
            <div className={styles.formContainer}>
                <div className={styles.headerContainer}>
                    <Link to="/" className={styles.backButton}>Back</Link>
                    <h3 className={styles.header}>Create Teacher</h3>
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
                        <label className={styles.label} htmlFor="fullName-input">Full Name:</label>
                        <input
                            id="fullName-input"
                            className={styles.inputText}
                            type="text"
                            value={fullName}
                            onChange={(e) => setFullName(e.target.value)}
                            required
                        />
                    </div>
                    <button type="submit" className={styles.submitButton}>Save</button>
                </form>
            </div>
        </div>
    );
};

export default CreateTeacher;
