import apiClient from "./apiClient";
import { HOMEWORKS_API_ENDPOINTS } from "./api/homeworks";

export const getHomeworksByStudentId = async (studentId) => {
    const response = await apiClient.get(HOMEWORKS_API_ENDPOINTS.getHomeworksByStudentId, {
        params: { studentId }
    });
    return response.data;
};

export const createHomework = async (homework) => {
    const response = await apiClient.post(HOMEWORKS_API_ENDPOINTS.createHomework, homework);
    return response.data;
};
