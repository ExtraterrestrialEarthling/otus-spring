import React, { useEffect, useState } from "react";
import { useParams, Link } from 'react-router-dom';
import { getBook } from "../services/bookService";
import { getCommentsByBookId, createComment } from "../services/commentService";
import styles from "./BookDetails.module.css";
import { Helmet } from 'react-helmet';
import EditBook from "./EditBook";

const BookDetails = () => {
    const { id } = useParams();
    const [book, setBook] = useState(null);
    const [comments, setComments] = useState([]);
    const [newComment, setNewComment] = useState('');
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [editing, setEditing] = useState(false);

    const fetchBook = async () => {
        setLoading(true);
        try {
            const data = await getBook(id);
            setBook(data);
            const commentData = await getCommentsByBookId(id);
            setComments(commentData);
        } catch (error) {
            setError('Failed to fetch book details');
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchBook();
    }, [id]);

    const handleCommentSubmit = async (event) => {
        event.preventDefault();
        try {
            await createComment({ text: newComment, bookId: book.id });
            setComments([...comments, { text: newComment, bookId: book.id }]);
            setNewComment('');
        } catch (error) {
            setError('Failed to add comment');
        }
    };

    const toggleEditing = () => setEditing(!editing);

    if (loading) {
        return (
            <div className={styles.loaderContainer}>
                <div className={styles.spinner}></div>
                <p>Loading...</p>
            </div>
        );
    }

    if (error) return <p>{error}</p>;

    return (
        <div className={styles.bookDetails}>
            <Helmet>
                <title>Book Details</title>
            </Helmet>

            <div className={styles.headerContainer}>
                <Link to="/" className={styles.backButton}>Back</Link>
                <h3 className={styles.title}>{editing ? 'Edit Book' : book?.title}</h3>
                {!editing && (
                    <div className={styles.buttonContainer}>
                        <button onClick={toggleEditing} className={styles.editButton}>Edit</button>
                        <Link to={`/books/delete/${book.id}/confirm`} className={styles.deleteButton}>Delete book</Link>
                    </div>
                )}
            </div>

            {editing ? (
                <EditBook
                    book={book}
                    onEditComplete={() => {
                        setEditing(false);
                        fetchBook();
                    }}
                    onCancel={() => setEditing(false)}
                />
            ) : (
                <>
                    <p className={styles.detail}><strong>Author:</strong> {book?.author?.fullName}</p>
                    <p className={styles.detail}><strong>Genres:</strong> {book?.genres?.map(genre => genre.name).join(', ')}</p>

                    <div className={styles.commentsSection}>
                        <h4 className={styles.commentsHeader}>Comments</h4>
                        <ul className={styles.commentsList}>
                            {comments.map(comment => (
                                <li key={comment.id} className={styles.comment}>
                                    {comment.text}
                                </li>
                            ))}
                        </ul>
                        <form onSubmit={handleCommentSubmit} className={styles.commentForm}>
                            <textarea
                                className={styles.commentInput}
                                value={newComment}
                                onChange={(e) => setNewComment(e.target.value)}
                                placeholder="Add a comment..."
                                required
                            />
                            <button type="submit" className={styles.commentButton}>Submit</button>
                        </form>
                    </div>
                </>
            )}
        </div>
    );
};

export default BookDetails;
