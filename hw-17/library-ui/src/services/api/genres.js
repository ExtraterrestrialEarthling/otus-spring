const GENRES_API_BASE_URL =
    "/genres"

export const GENRES_API_ENDPOINTS = {
    getGenres: `${GENRES_API_BASE_URL}`,
    getGenreById: (id) => `${GENRES_API_BASE_URL}/${id}`,
    createGenre: `${GENRES_API_BASE_URL}`,
    updateGenre: (id) => `${GENRES_API_BASE_URL}/${id}`,
    deleteGenre: (id) => `${GENRES_API_BASE_URL}/${id}`
}