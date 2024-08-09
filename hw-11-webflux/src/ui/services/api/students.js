const STUDENTS_API_BASE_URL = "/students";

export const STUDENTS_API_ENDPOINTS = {
    getStudents: `${STUDENTS_API_BASE_URL}`,
    getStudentById: (id) => `${STUDENTS_API_BASE_URL}/${id}`,
    createStudent: `${STUDENTS_API_BASE_URL}`,
    updateStudent: `${STUDENTS_API_BASE_URL}`,
    deleteStudent: (id) => `${STUDENTS_API_BASE_URL}/${id}`
};
