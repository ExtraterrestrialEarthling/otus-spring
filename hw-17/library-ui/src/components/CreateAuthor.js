import React, { useState } from 'react';
import { createAuthor } from "../services/authorService";
import styles from "./CreateAuthor.module.css";
import { Link } from 'react-router-dom';
import {Helmet} from "react-helmet";

const CreateAuthor = () => {
    const [fullName, setFullName] = useState('');
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(null);

    const handleSubmit = async (event) => {
        event.preventDefault();
        const author = { fullName };

        try {
            await createAuthor(author);
            setSuccess('Author created successfully!');
            setError(null);
            setFullName('');
        } catch (error) {
            console.error('Error creating author:', error);
            setError('Failed to create author');
            setSuccess(null);
        }
    };

    return (
        <div className={styles.body}>
            <Helmet>
                <title>Create Author</title>
            </Helmet>
            <div className={styles.formContainer}>
                <div className={styles.headerContainer}>
                    <Link to="/" className={styles.backButton}>Back</Link>
                    <h3 className={styles.header}>Create Author</h3>
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
                        <label className={styles.label} htmlFor="fullName-input">Full name:</label>
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

export default CreateAuthor;
