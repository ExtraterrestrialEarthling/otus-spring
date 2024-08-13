import React, { useState, useEffect } from 'react';
import { updateStudent } from '../services/studentService';
import { getSkills } from '../services/skillService';
import { getTeachers } from "../services/teacherService";
import styles from './EditStudent.module.css';

const EditStudent = ({ student, onEditComplete, onCancel }) => {
    const [name, setName] = useState(student.name);
    const [availableTeachers, setAvailableTeachers] = useState([]);
    const [selectedTeacher, setSelectedTeacher] = useState(student.teacher.id);
    const [availableSkills, setAvailableSkills] = useState([]);
    const [selectedSkills, setSelectedSkills] = useState(student.skills.map(skill => skill.id));
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(null);

    useEffect(() => {
        const fetchTeachers = async () => {
            try {
                const teachers = await getTeachers();
                setAvailableTeachers(teachers);
            } catch (error) {
                console.error('Error fetching teachers:', error);
                setError('Failed to fetch teachers');
            } finally {
                setLoading(false);
            }
        };

        fetchTeachers();
    }, []);

    useEffect(() => {
        const fetchSkills = async () => {
            try {
                const skills = await getSkills();
                setAvailableSkills(skills);
            } catch (error) {
                console.error('Error fetching skills:', error);
                setError('Failed to fetch skills');
            } finally {
                setLoading(false);
            }
        };

        fetchSkills();
    }, []);

    const handleSkillChange = (skillId) => {
        if (selectedSkills.includes(skillId)) {
            setSelectedSkills(selectedSkills.filter(s => s !== skillId));
        } else {
            setSelectedSkills([...selectedSkills, skillId]);
        }
    };

    const handleSubmit = async (event) => {
        event.preventDefault();

        if (selectedSkills.length === 0) {
            setError('At least one skill must be selected');
            return;
        }

        const updatedStudent = {
            id: student.id,
            name,
            teacherId: selectedTeacher,
            skillIds: selectedSkills
        };

        try {
            await updateStudent(updatedStudent);
            setSuccess('Student updated successfully!');
            onEditComplete();
        } catch (error) {
            console.error('Error updating student:', error);
            setError('Failed to update student');
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
                <div className={styles.row}>
                    <label className={styles.label} htmlFor="teacher-select">Teacher:</label>
                    <select
                        id="teacher-select"
                        className={styles.inputSelect}
                        value={selectedTeacher}
                        onChange={(e) => setSelectedTeacher(e.target.value)}
                        required
                    >
                        <option value="">Select a teacher</option>
                        {availableTeachers.map(teacher => (
                            <option key={teacher.id} value={teacher.id}>
                                {teacher.fullName}
                            </option>
                        ))}
                    </select>
                </div>
                <div className={styles.row}>
                    <label className={styles.label}>Skills:</label>
                    <div className={styles.skillList}>
                        {availableSkills.map(skill => (
                            <label key={skill.id}>
                                <input
                                    type="checkbox"
                                    id={`skill-${skill.id}`}
                                    value={skill.id}
                                    checked={selectedSkills.includes(skill.id)}
                                    onChange={() => handleSkillChange(skill.id)}
                                />
                                {skill.name}
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

export default EditStudent;
