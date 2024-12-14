import {Link} from "react-router-dom";
import Login from "./Login";
import {Button} from "@mui/material";

export default function NotFoundPage(){
    return (
        <section>
            <h1>404 - Not Found</h1>
            <Link to="/login"><Button variant="contained">Login</Button></Link>
            <Link to="/registration"><Button variant="contained">Register</Button></Link>
        </section>
    );
}