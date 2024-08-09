import apiClient from "./apiClient";
import { TEACHERS_API_ENDPOINTS } from "./api/teachers";

export const getTeachers = async () => {
    const response = await apiClient.get(TEACHERS_API_ENDPOINTS.getTeachers);
    return response.data;
};

export const getTeacher = async (id) => {
    const response = await apiClient.get(TEACHERS_API_ENDPOINTS.getTeacherById(id));
    return response.data;
};

export const createTeacher = async (teacher) => {
    const response = await apiClient.post(TEACHERS_API_ENDPOINTS.createTeacher, teacher);
    return response.data;
};
