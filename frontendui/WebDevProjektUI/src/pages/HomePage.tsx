
/*Valahogy meg kell oldani, hogy csak ha authentikálva van akkor lehessen az oldalt megnyitni
                    és az authoritásának megfelelő dolgokat dobja fel */
import {useState} from "react";
import {useLocation} from "react-router-dom";
import NotFoundPage from "./NotFoundPage";
import StudentView from "./Views/StudentView";
import TeacherView from "./Views/TeacherView";
import AdminView from "./Views/AdminView";

export default function HomePage() {

    //HomePage-et csak a beloginoltak nyithatják meg
    //JWT használata:
        //Ha van használd, ha nincs vagy lejárt dobd a Loginra

    const location = useLocation();
    const { jwt } = location.state || {};

    console.log(jwt);

    //JWT-ből az adatok kinyerése valahogy

    const [role, setRole] = useState("ROLE_ADMIN");
    setRole("ROLE_STUDENT");

    if (role === "ROLE_STUDENT") {
        return <StudentView />
    }else if (role === "ROLE_TEACHER"){
        return <TeacherView />
    }else if (role === "ROLE_ADMIN"){
        return <AdminView />
    }else {
        return <NotFoundPage />
    }

}