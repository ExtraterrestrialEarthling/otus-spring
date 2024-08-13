import apiClient from "../services/apiClient";
import { STUDENTS_API_ENDPOINTS } from "./api/students";

export const getStudents = async () => {
        const response = await apiClient.get(STUDENTS_API_ENDPOINTS.getStudents);
        return response.data;
};

export const getStudent = async (id) => {
        const response = await apiClient.get(STUDENTS_API_ENDPOINTS.getStudentById(id));
        return response.data;
};

export const createStudent = async (student) => {
        const response = await apiClient.post(STUDENTS_API_ENDPOINTS.createStudent, student);
        return response.data;
};

export const updateStudent = async (student) => {
        const response = await apiClient.put(STUDENTS_API_ENDPOINTS.updateStudent, student);
        return response.data;
};

export const deleteStudent = async (id) => {
        const response = await apiClient.delete(STUDENTS_API_ENDPOINTS.deleteStudent(id));
        return response.data;
};
