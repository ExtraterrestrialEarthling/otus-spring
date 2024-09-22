const AUTHORS_API_BASE_URL =
    "/authors"

export const AUTHORS_API_ENDPOINTS = {
    getAuthors: `${AUTHORS_API_BASE_URL}`,
    getAuthorById: (id) => `${AUTHORS_API_BASE_URL}/${id}`,
    createAuthor: `${AUTHORS_API_BASE_URL}`,
    updateAuthor: (id) => `${AUTHORS_API_BASE_URL}/${id}`,
    deleteAuthor: (id) => `${AUTHORS_API_BASE_URL}/${id}`
}