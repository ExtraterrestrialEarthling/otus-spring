import React from 'react'
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import StudentsTable from "./StudentsTable";
import StudentDetails from "./StudentDetails"
import Header from "./Header"
import CreateStudent from "./CreateStudent";
import CreateTeacher from "./CreateTeacher";
import CreateSkill from "./CreateSkill";
import ConfirmDelete from "./ConfirmDelete";

const App = () => (
    <Router>
        <Header title="Students" />
        <Routes>
            <Route path="/" element={<StudentsTable />} />
            <Route path="/students/:id" element={<StudentDetails />} />
            <Route path="/students/new" element={<CreateStudent />} />
            <Route path="/teachers/new" element={<CreateTeacher />} />
            <Route path="/skills/new" element={<CreateSkill />} />
            <Route path="/students/delete/:id/confirm" element={<ConfirmDelete />} />
            <Route path="*" element={<h2>404 Not Found</h2>} />
        </Routes>
    </Router>
);

export default App;