import apiClient from "./apiClient";
import {COMMENTS_API_ENDPOINTS} from "./api/comments";

export const getCommentsByBookId = async (bookId) => {
    const response = await apiClient.get(COMMENTS_API_ENDPOINTS.getCommentsByBookId,  {
        params: { bookId }
    });
    return response.data;
};

export const createComment = async (comment) => {
    const response = await apiClient.post(COMMENTS_API_ENDPOINTS.createComment, comment);
    return response.data;
}
