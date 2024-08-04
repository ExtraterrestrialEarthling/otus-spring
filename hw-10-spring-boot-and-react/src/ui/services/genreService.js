import apiClient from "./apiClient";
import {GENRES_API_ENDPOINTS} from "./api/genres";
import ApiError from "../errors/ApiError";

export const getGenres = async () => {
    const response = await apiClient.get(GENRES_API_ENDPOINTS.getGenres);
    return response.data;
};

export const getGenre = async (id) => {
    const response = await apiClient.get(GENRES_API_ENDPOINTS.getGenreById(id));
    return response.data;
};

export const createGenre = async (genre) => {
    try {
        const response = await apiClient.post(GENRES_API_ENDPOINTS.createGenre, genre);
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