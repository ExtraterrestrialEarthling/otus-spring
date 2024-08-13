const HOMEWORKS_API_BASE_URL = "/homeworks";

export const HOMEWORKS_API_ENDPOINTS = {
    getHomeworks: `${HOMEWORKS_API_BASE_URL}`,
    getHomeworkById: (id) => `${HOMEWORKS_API_BASE_URL}/${id}`,
    getHomeworksByStudentId: `${HOMEWORKS_API_BASE_URL}`,
    createHomework: `${HOMEWORKS_API_BASE_URL}`,
    updateHomework: (id) => `${HOMEWORKS_API_BASE_URL}/${id}`,
    deleteHomework: (id) => `${HOMEWORKS_API_BASE_URL}/${id}`
};
