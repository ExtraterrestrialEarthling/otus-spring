const BOOKS_API_BASE_URL =
    "/books"

export const BOOKS_API_ENDPOINTS = {
    getBooks: `${BOOKS_API_BASE_URL}`,
    getBookById: (id) => `${BOOKS_API_BASE_URL}/${id}`,
    createBook: `${BOOKS_API_BASE_URL}`,
    updateBook: `${BOOKS_API_BASE_URL}`,
    deleteBook: (id) => `${BOOKS_API_BASE_URL}/${id}`
}