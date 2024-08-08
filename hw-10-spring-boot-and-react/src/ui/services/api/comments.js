const COMMENTS_API_BASE_URL =
    "/comments"

export const COMMENTS_API_ENDPOINTS = {
    getComments: `${COMMENTS_API_BASE_URL}`,
    getCommentById: (id) => `${COMMENTS_API_BASE_URL}/${id}`,
    getCommentsByBookId: `${COMMENTS_API_BASE_URL}`,
    createComment: `${COMMENTS_API_BASE_URL}`,
    updateComment: (id) => `${COMMENTS_API_BASE_URL}/${id}`,
    deleteComment: (id) => `${COMMENTS_API_BASE_URL}/${id}`
}