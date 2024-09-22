import { deleteBook, getBook } from '../services/bookService';
import React, { useEffect, useState } from 'react';
import { useParams, Link, useNavigate } from 'react-router-dom';
import styles from "./ConfirmDelete.module.css";
import {Helmet} from "react-helmet";

const ConfirmDelete = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const [book, setBook] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchBook = async () => {
            try {
                const data = await getBook(id);
                setBook(data);
            } catch (error) {
                setError('Failed to fetch book');
            } finally {
                setLoading(false);
            }
        };
        fetchBook();
    }, [id]);

    const handleDelete = async () => {
        try {
            await deleteBook(id);
            navigate('/'); // Redirect to the books list after deletion
        } catch (error) {
            setError('Failed to delete book');
        }
    };

    if (loading) {
        return (
            <div className={styles.loaderContainer}>
                <div className={styles.spinner}></div>
                <p>Loading...</p>
            </div>
        );
    }

    if (error) return <p>{error}</p>;
    if (!book) return <p>No book found</p>;

    return (
        <div className={styles.body}>
            <Helmet>
                <title>Confirm Deletion</title>
            </Helmet>
            <div className={styles.confirmDeleteContainer}>
                <h3>Are you sure you want to delete book "{book.title}"?</h3>
                <div className={styles.buttonContainer}>
                    <button className={styles.confirmButton} onClick={handleDelete}>Yes</button>
                    <Link className={styles.cancelButton} to={`/books/${book.id}`}>No</Link>
                </div>
            </div>
        </div>
    );
};

export default ConfirmDelete;
