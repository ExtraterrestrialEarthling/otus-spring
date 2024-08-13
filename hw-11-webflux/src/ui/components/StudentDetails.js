import React, { useEffect, useState } from "react";
import { useParams, Link } from 'react-router-dom';
import { getStudent } from "../services/studentService";
import { getHomeworksByStudentId, createHomework } from "../services/homeworkService";
import styles from "./StudentDetails.module.css";
import { Helmet } from 'react-helmet';
import EditStudent from "./EditStudent";

const StudentDetails = () => {
    const { id } = useParams();
    const [student, setStudent] = useState(null);
    const [homeworks, setHomeworks] = useState([]);
    const [newHomework, setNewHomework] = useState('');
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [editing, setEditing] = useState(false);

    const fetchStudent = async () => {
        setLoading(true);
        try {
            const data = await getStudent(id);
            setStudent(data);
            const homeworkData = await getHomeworksByStudentId(id);
            setHomeworks(homeworkData);
        } catch (error) {
            setError('Failed to fetch student details');
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchStudent();
    }, [id]);

    const handleHomeworkSubmit = async (event) => {
        event.preventDefault();
        try {
            await createHomework({ text: newHomework, studentId: student.id });
            setHomeworks([...homeworks, { text: newHomework, studentId: student.id }]);
            setNewHomework('');
        } catch (error) {
            setError('Failed to add homework');
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
        <div className={styles.studentDetails}>
            <Helmet>
                <title>Student Details</title>
            </Helmet>

            <div className={styles.headerContainer}>
                <Link to="/" className={styles.backButton}>Back</Link>
                <h3 className={styles.title}>{editing ? 'Edit Student' : student?.name}</h3>
                {!editing && (
                    <div className={styles.buttonContainer}>
                        <button onClick={toggleEditing} className={styles.editButton}>Edit</button>
                        <Link to={`/students/delete/${student.id}/confirm`} className={styles.deleteButton}>Delete</Link>
                    </div>
                )}
            </div>

            {editing ? (
                <EditStudent
                    student={student}
                    onEditComplete={() => {
                        setEditing(false);
                        fetchStudent();
                    }}
                    onCancel={() => setEditing(false)}
                />
            ) : (
                <>
                    <p className={styles.detail}><strong>Teacher:</strong> {student?.teacher?.fullName}</p>
                    <p className={styles.detail}><strong>Skills:</strong> {student?.skills?.map(skill => skill.name).join(', ')}</p>

                    <div className={styles.homeworksSection}>
                        <h4 className={styles.homeworksHeader}>Homework</h4>
                        <ul className={styles.homeworksList}>
                            {homeworks.map(homework => (
                                <li key={homework.id} className={styles.homework}>
                                    {homework.text}
                                </li>
                            ))}
                        </ul>
                        <form onSubmit={handleHomeworkSubmit} className={styles.homeworkForm}>
                            <textarea
                                className={styles.homeworkInput}
                                value={newHomework}
                                onChange={(e) => setNewHomework(e.target.value)}
                                placeholder="Add homework..."
                                required
                            />
                            <button type="submit" className={styles.homeworkButton}>Submit</button>
                        </form>
                    </div>
                </>
            )}
        </div>
    );
};

export default StudentDetails;
