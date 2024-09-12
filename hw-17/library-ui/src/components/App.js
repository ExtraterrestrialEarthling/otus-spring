import React from 'react'
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import BooksTable from "./BooksTable";
import BookDetails from "./BookDetails"
import Header from "./Header"
import CreateBook from "./CreateBook";
import CreateAuthor from "./CreateAuthor";
import CreateGenre from "./CreateGenre";
import ConfirmDelete from "./ConfirmDelete";

const App = () => (
    <Router>
        <Header title="Library" />
        <Routes>
            <Route path="/" element={<BooksTable />} />
            <Route path="/books/:id" element={<BookDetails />} />
            <Route path="/books/new" element={<CreateBook />} />
            <Route path="/authors/new" element={<CreateAuthor />} />
            <Route path="/genres/new" element={<CreateGenre />} />
            <Route path="/books/delete/:id/confirm" element={<ConfirmDelete />} />
            <Route path="*" element={<h2>404 Not Found</h2>} />
        </Routes>
    </Router>
);

export default App;