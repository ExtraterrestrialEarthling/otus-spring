import React, { useEffect, useState } from 'react';
import { getBooks } from "../services/bookService";
import { Link } from 'react-router-dom';
import styles from './BooksTable.module.css';
import {Helmet} from "react-helmet";

const BooksTable = () => {
    const [books, setBooks] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchBooks = async () => {
            try {
                const data = await getBooks();
                setBooks(data);
            } catch (error) {
                setError('Failed to fetch books');
            } finally {
                setLoading(false);
            }
        };

        fetchBooks();
    }, []);

    if (loading) {
        return (
            <div className={styles.loaderContainer}>
                <div className={styles.spinner}></div>
                <p>Loading...</p>
            </div>
        );
    }

    if (error) {
        return (
            <div className={styles.errorContainer}>
                <p className={styles.errorMessage}>{error}</p>
            </div>
        );
    }

    return (
        <div className={styles.bookList}>
            <Helmet>
                <title>Books Table</title>
            </Helmet>
            <h3>Books:</h3>
            <table className={styles.books}>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Title</th>
                    <th>Author</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                {books.map(book => (
                    <tr key={book.id}>
                        <td>{book.id}</td>
                        <td>{book.title}</td>
                        <td>{book.author.fullName}</td>
                        <td>
                            <Link to={`/books/${book.id}`} className={styles.viewDetails}>View Details</Link>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
            <div className={styles.createButtonContainer}>
                <Link to="/authors/new" className={styles.createButton}>Create author</Link>
                <Link to="/books/new" className={styles.createButton}>Create book</Link>
                <Link to="/genres/new" className={styles.createButton}>Create genre</Link>
            </div>
        </div>
    );
};

export default BooksTable;
