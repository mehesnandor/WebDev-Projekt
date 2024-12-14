import {Profiler, useState} from 'react'
import Register from "./pages/Register";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Login from "./pages/Login";
import NotFoundPage from "./pages/NotFoundPage";
import HomePage from "./pages/HomePage";
import Profile from "./pages/Profile";
import "./index.css";


function App() {

  return (
    <BrowserRouter>
        <Routes>
            <Route path="/" element={<Login />} />
            <Route path="/login" element={<Login />} />
            <Route path="/registration" element={<Register />}/>
            <Route path="/home" element={<HomePage />}/>
            <Route path="/profile" element={<Profile/>}/>
            <Route path="*" element={<NotFoundPage />}/>
        </Routes>
    </BrowserRouter>
  )

}
{/*Valahogy meg kell oldani, hogy csak ha authentikálva van akkor lehessen az oldalt megnyitni
                    és az authoritásának megfelelő dolgokat dobja fel */}
export default App
