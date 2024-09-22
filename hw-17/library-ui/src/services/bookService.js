import apiClient from "./apiClient";
import {BOOKS_API_ENDPOINTS} from "./api/books";

export const getBooks = async () => {
        const response = await apiClient.get(BOOKS_API_ENDPOINTS.getBooks);
        return response.data;
};

export const getBook = async (id) => {
        const response = await apiClient.get(BOOKS_API_ENDPOINTS.getBookById(id));
        return response.data;
};

export const createBook = async (book) => {
        const response = await apiClient.post(BOOKS_API_ENDPOINTS.createBook, book);
        return response.data;
}

export const updateBook = async (book) => {
        const response = await apiClient.put(BOOKS_API_ENDPOINTS.updateBook, book);
        return response.data;
}

export const deleteBook = async (id) => {
        const response = await apiClient.delete(BOOKS_API_ENDPOINTS.deleteBook(id));
        return response.data;
}