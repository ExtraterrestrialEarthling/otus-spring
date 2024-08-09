const TEACHERS_API_BASE_URL =
    "/teachers"

export const TEACHERS_API_ENDPOINTS = {
    getTeachers: `${TEACHERS_API_BASE_URL}`,
    getTeacherById: (id) => `${TEACHERS_API_BASE_URL}/${id}`,
    createTeacher: `${TEACHERS_API_BASE_URL}`,
    updateTeacher: (id) => `${TEACHERS_API_BASE_URL}/${id}`,
    deleteTeacher: (id) => `${TEACHERS_API_BASE_URL}/${id}`
}