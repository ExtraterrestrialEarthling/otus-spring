import apiClient from "./apiClient";
import {AUTHORS_API_ENDPOINTS} from "./api/authors";

export const getAuthors = async () => {
    const response = await apiClient.get(AUTHORS_API_ENDPOINTS.getAuthors);
    return response.data;
};

export const getAuthor = async (id) => {
    const response = await apiClient.get(AUTHORS_API_ENDPOINTS.getAuthorById(id));
    return response.data;
};

export const createAuthor = async (author) => {
    const response= await apiClient.post(AUTHORS_API_ENDPOINTS.createAuthor, author);
    return response.data;
}
