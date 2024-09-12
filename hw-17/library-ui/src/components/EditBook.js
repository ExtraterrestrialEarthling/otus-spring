import React, { useState, useEffect } from 'react';
import { updateBook } from '../services/bookService';
import { getGenres } from '../services/genreService';
import { getAuthors } from "../services/authorService";
import styles from './EditBook.module.css';

const EditBook = ({ book, onEditComplete, onCancel }) => {
    const [title, setTitle] = useState(book.title);
    const [availableAuthors, setAvailableAuthors] = useState([]);
    const [selectedAuthor, setSelectedAuthor] = useState(book.author.id);
    const [availableGenres, setAvailableGenres] = useState([]);
    const [selectedGenres, setSelectedGenres] = useState(book.genres.map(genre => genre.id));
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(null);

    useEffect(() => {
        const fetchAuthors = async () => {
            try {
                const authors = await getAuthors();
                setAvailableAuthors(authors);
            } catch (error) {
                console.error('Error fetching authors:', error);
                setError('Failed to fetch authors');
            } finally {
                setLoading(false);
            }
        };

        fetchAuthors();
    }, []);

    useEffect(() => {
        const fetchGenres = async () => {
            try {
                const genres = await getGenres();
                setAvailableGenres(genres);
            } catch (error) {
                console.error('Error fetching genres:', error);
                setError('Failed to fetch genres');
            } finally {
                setLoading(false);
            }
        };

        fetchGenres();
    }, []);

    const handleGenreChange = (genreId) => {
        if (selectedGenres.includes(genreId)) {
            setSelectedGenres(selectedGenres.filter(g => g !== genreId));
        } else {
            setSelectedGenres([...selectedGenres, genreId]);
        }
    };

    const handleSubmit = async (event) => {
        event.preventDefault();

        if (selectedGenres.length === 0) {
            setError('At least one genre must be selected');
            return;
        }

        const updatedBook = {
            bookId: book.id,
            title,
            authorId: selectedAuthor,
            genreIds: selectedGenres
        };

        try {
            await updateBook(updatedBook);
            setSuccess('Book updated successfully!');
            onEditComplete();
        } catch (error) {
            console.error('Error updating book:', error);
            setError('Failed to update book');
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

    return (
        <div>
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
                    <label className={styles.label} htmlFor="title-input">Title:</label>
                    <input
                        id="title-input"
                        className={styles.inputText}
                        type="text"
                        value={title}
                        onChange={(e) => setTitle(e.target.value)}
                        required
                    />
                </div>
                <div className={styles.row}>
                    <label className={styles.label} htmlFor="author-select">Author:</label>
                    <select
                        id="author-select"
                        className={styles.inputSelect}
                        value={selectedAuthor}
                        onChange={(e) => setSelectedAuthor(e.target.value)}
                        required
                    >
                        <option value="">Select an author</option>
                        {availableAuthors.map(author => (
                            <option key={author.id} value={author.id}>
                                {author.fullName}
                            </option>
                        ))}
                    </select>
                </div>
                <div className={styles.row}>
                    <label className={styles.label}>Genres:</label>
                    <div className={styles.genreList}>
                        {availableGenres.map(genre => (
                            <label key={genre.id}>
                                <input
                                    type="checkbox"
                                    id={`genre-${genre.id}`}
                                    value={genre.id}
                                    checked={selectedGenres.includes(genre.id)}
                                    onChange={() => handleGenreChange(genre.id)}
                                />
                                {genre.name}
                            </label>
                        ))}
                    </div>
                </div>
                <div className={styles.buttonGroup}>
                    <button type="submit" className={styles.submitButton}>Save</button>
                    <button type="button" className={styles.cancelButton} onClick={onCancel}>Cancel</button>
                </div>
            </form>
        </div>
    );
};

export default EditBook;
