import apiClient from "./apiClient";
import { SKILLS_API_ENDPOINTS } from "./api/skills";
import ApiError from "../errors/ApiError";

export const getSkills = async () => {
    const response = await apiClient.get(SKILLS_API_ENDPOINTS.getSkills);
    return response.data;
};

export const getSkill = async (id) => {
    const response = await apiClient.get(SKILLS_API_ENDPOINTS.getSkillById(id));
    return response.data;
};

export const createSkill = async (skill) => {
    try {
        const response = await apiClient.post(SKILLS_API_ENDPOINTS.createSkill, skill);
        return response.data;
    } catch (error) {
        if (error instanceof ApiError && error.statusCode === 400) {
            if (error.code === 'ALREADY_EXISTS') {
                throw new Error('ALREADY_EXISTS');
            }
        }
        console.error('Unexpected error:', error);
        throw error;
    }
};
