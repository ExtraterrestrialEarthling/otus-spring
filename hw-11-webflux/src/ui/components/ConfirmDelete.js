import { deleteStudent, getStudent } from '../services/studentService'; // Ensure these functions are correctly defined in your service
import React, { useEffect, useState } from 'react';
import { useParams, Link, useNavigate } from 'react-router-dom';
import styles from "./ConfirmDelete.module.css";
import { Helmet } from "react-helmet";

const ConfirmDelete = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const [student, setStudent] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchStudent = async () => {
            try {
                const data = await getStudent(id);
                setStudent(data);
            } catch (error) {
                setError('Failed to fetch teacher');
            } finally {
                setLoading(false);
            }
        };
        fetchStudent();
    }, [id]);

    const handleDelete = async () => {
        try {
            await deleteStudent(id);
            navigate('/students');
        } catch (error) {
            setError('Failed to delete teacher');
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
    if (!student) return <p>No student found</p>;

    return (
        <div className={styles.body}>
            <Helmet>
                <title>Confirm Deletion</title>
            </Helmet>
            <div className={styles.confirmDeleteContainer}>
                <h3>Are you sure you want to delete student "{student.name}"?</h3>
                <div className={styles.buttonContainer}>
                    <button className={styles.confirmButton} onClick={handleDelete}>Yes</button>
                    <Link className={styles.cancelButton} to={`/students/${student.id}`}>No</Link>
                </div>
            </div>
        </div>
    );
};

export default ConfirmDelete;
