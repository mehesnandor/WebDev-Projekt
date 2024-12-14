import React, {useEffect, useState} from "react";
import Grid from "@mui/material/Grid";
import CssBaseline from "@mui/material/CssBaseline";
import { createTheme, ThemeProvider } from "@mui/material/styles";
import ProfileCard from "../components/ProfileCard";
import SettingsCard from "../components/SettingsCard";

import "@fontsource/roboto/300.css";
import "@fontsource/roboto/400.css";
import "@fontsource/roboto/500.css";
import "@fontsource/roboto/700.css";
import axios from "axios";

const theme = createTheme();

export default function Profile(props: any) {

    const REGISTER_URL_GET_USERINFO = "http://localhost:8080/user_data/"; //username as PathVariable
    const USERNAME = "Student"; //props.username; - majd a menüből
    const token = btoa("Student:Aa12345!");

    const [mainUser, setUserDataFromUpdate] = useState({
        userId: 0,
        username: "DEFAULT_USERNAME",
        pass: "DEFAULT_PASSWORD",
        role: "ROLE_DEFAULT",
        email: "default@mail.com",
        phone: "+12 1234567890"
    });

    useEffect(() => {
        console.log("mainUser:", mainUser);
    }, [mainUser]);

    async function getUserInfo() {
        await axios.get(REGISTER_URL_GET_USERINFO + USERNAME, {
            headers: {
                'Authorization': `Basic ${token}`
            }
        })
            .then(response => {
                console.log(response.data);
                setUserDataFromUpdate(response.data);
                console.log(mainUser);
            })
            .catch(error => {
                console.error(error);
            });
    }

    function handleDataChange(newUserData: any) {
        setUserDataFromUpdate(newUserData);
        console.log(newUserData);
    }

    return (
        <ThemeProvider theme={theme} >
            <CssBaseline>
                <Grid container direction="column" sx={{ overflowX: "hidden" }}>
                    <Grid item xs={12} md={6}>
                        <img
                            alt="avatar"
                            style={{
                                width: "100vw",
                                height: "35vh",
                                objectFit: "cover",
                                objectPosition: "50% 50%",
                                position: "relative"
                            }}
                            src="https://iris2.gettimely.com/images/default-cover-image.jpg"
                        />

                    </Grid>

                    <Grid
                        container
                        direction={{ xs: "column", md: "row" }}
                        spacing={3}
                        sx={{
                            position: "absolute",
                            top: "20vh",
                            px: { xs: 0, md: 7 }
                        }}
                    >
                        <Grid item md={4} onLoad={getUserInfo}>
                            <ProfileCard
                                name={mainUser.username}
                                role={mainUser.role}
                                pass={mainUser.pass}
                                email={mainUser.email}
                                phone={mainUser.phone}
                            ></ProfileCard>
                        </Grid>

                        <Grid item md={8}>
                            <SettingsCard
                                userId={mainUser.userId}
                                username={mainUser.username}
                                pass={mainUser.pass}
                                role={mainUser.role}
                                phone={mainUser.phone}
                                email={mainUser.email}
                                getData={handleDataChange}
                            ></SettingsCard>
                        </Grid>
                    </Grid>
                </Grid>
            </CssBaseline>
        </ThemeProvider>
    );
}
