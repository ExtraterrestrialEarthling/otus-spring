import React, { useEffect, useState } from 'react';
import { getStudents } from "../services/studentService";
import { Link } from 'react-router-dom';
import styles from './StudentsTable.module.css';
import { Helmet } from "react-helmet";

const StudentsTable = () => {
    const [students, setStudents] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchStudents = async () => {
            try {
                const data = await getStudents();
                console.log('Fetched Students:', data); // Проверяем данные
                setStudents(data);
            } catch (error) {
                setError('Failed to fetch students');
            } finally {
                setLoading(false);
            }
        };

        fetchStudents();
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
        <div className={styles.studentList}>
            <Helmet>
                <title>Students Table</title>
            </Helmet>
            <h3>Students:</h3>
            <table className={styles.students}>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Teacher</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                {students.map(student => (
                    <tr key={student.id}>
                        <td>{student.id}</td>
                        <td>{student.name}</td>
                        <td>{student.teacher?.fullName || 'No Teacher'}</td>
                        <td>
                            <Link to={`/students/${student.id}`} className={styles.viewDetails}>View Details</Link>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
            <div className={styles.createButtonContainer}>
                <Link to="/teachers/new" className={styles.createButton}>Create teacher</Link>
                <Link to="/students/new" className={styles.createButton}>Create student</Link>
                <Link to="/skills/new" className={styles.createButton}>Create skill</Link>
            </div>
        </div>
    );
};

export default StudentsTable;
