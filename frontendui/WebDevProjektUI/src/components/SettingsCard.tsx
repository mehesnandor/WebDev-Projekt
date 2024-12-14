import React, { useState } from "react";
import Card from "@mui/material/Card";
import Divider from "@mui/material/Divider";
import InputAdornment from "@mui/material/InputAdornment";
import MenuItem from "@mui/material/MenuItem";
import IconButton from "@mui/material/IconButton";
import VisibilityOff from "@mui/icons-material/VisibilityOff";
import Visibility from "@mui/icons-material/Visibility";
import CardContent from "@mui/material/CardContent";
import Box from "@mui/material/Box";
import { Grid } from "@mui/material";
import FormControl from "@mui/material/FormControl";
import Button from "@mui/material/Button";
import Tabs from "@mui/material/Tabs";
import Tab from "@mui/material/Tab";
import CustomInput from "./CustomInput";
import axios from "axios";
import {useNavigate} from "react-router-dom";

export default function SettingsCard(props: any) {

    const [value, setValue] = React.useState("one");
    const REGISTER_URL_UPDATE = "http://localhost:8080/user_data/update";
    const REGISTER_URL_DELETE = "http://localhost:8080/user_data/delete/"; //needs a PathVariable - userId
    const navigate = useNavigate();
    const token = btoa("Student:Aa12345!");



    const handleChange = (event: React.SyntheticEvent, newValue: string) => {
        setValue(newValue);
    };

    function handleParentDataChange(data: any) {
        props.getData(data);
        console.log(data)
    }


    const [user, setUser] = useState({
        userId: props.userId,
        username: props.username,
        pass: props.pass,
        role: props.role,
        email: props.email,
        phone: props.phone,
        showPassword: false
    });

    const changeField = (event: React.ChangeEvent<HTMLInputElement>) => {
        setUser({ ...user, [event.target.name]: event.target.value });
    };

    const [edit, update] = useState({
        required: true,
        disabled: true,
        isEdit: true
    });

    const handleDelete = async (event: React.SyntheticEvent) => {
        event.preventDefault();

        await axios.delete(REGISTER_URL_DELETE + user.userId, {
            headers: {
                'Authorization': `Basic ${token}`
            }
        })
        .then(response => {
            console.log('Törlés sikeres:', response.data);
            navigate("/login");
        })
        .catch(error => {
            console.error(error);
        });


    }

    const changeButton = async (event: any) => {
        event.preventDefault();

        user.showPassword = false;
        edit.disabled = !edit.disabled;
        edit.isEdit = !edit.isEdit;
        update({...edit});
        console.log("user: ", user);


        await axios.put(REGISTER_URL_UPDATE, user,
            {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Basic ${token}`
                },
                withCredentials: true
            }
        ).then(
            response => {
                console.log(response?.data);
                if (response?.status === 200) {
                    console.log("Successfully updated");
                }
            }
        ).catch(
            err => {
                if (!err?.response) {
                    console.log('No Server Response');
                } else if (err?.response.status === 400) {
                    console.log('Bad_Request');
                } else if (err?.response.status === 500) {
                    console.log(err?.response.message);
                } else {
                    console.log('Update Failed')
                }
            }
        );
        handleParentDataChange(user);
    };

    const handlePassword = () => {
        user.showPassword = !user.showPassword;
        setUser({ ...user });
    };


    return (
        <Card variant="outlined" sx={{ height: "100%", width: "100%" }}>
            <br></br>
            <Tabs
                value={value}
                onChange={handleChange}
                textColor="secondary"
                indicatorColor="secondary"
            >
                <Tab value="one" label="Update Profile" />
            </Tabs>
            <Divider></Divider>

            <form>
                <CardContent
                    sx={{
                        p: 3,
                        maxHeight: { md: "40vh" },
                        textAlign: { xs: "center", md: "start" }
                    }}
                >
                    <FormControl fullWidth>
                        <Grid
                            container
                            direction={{ xs: "column", md: "row" }}
                            columnSpacing={5}
                            rowSpacing={3}
                        >

                            <Grid component="form" item xs={5}>
                                <CustomInput
                                    id="username"
                                    name="username"
                                    value={user.username.value}
                                    onChange={changeField}
                                    title="Username"
                                    dis={edit.disabled}
                                    req={edit.required}
                                ></CustomInput>
                            </Grid>


                            <Grid item xs={5}>
                                <CustomInput
                                    id="phone"
                                    name="phone"
                                    value={user.phone.value}
                                    onChange={changeField}
                                    title="Phone Number"
                                    dis={edit.disabled}
                                    req={edit.required}
                                ></CustomInput>
                            </Grid>


                            <Grid item xs={5}>
                                <CustomInput
                                    type="email"
                                    id="email"
                                    name="email"
                                    value={user.email.value}
                                    onChange={changeField}
                                    title="Email Address"
                                    dis={edit.disabled}
                                    req={edit.required}
                                ></CustomInput>
                            </Grid>


                            <Grid item xs={5}>
                                <CustomInput
                                    id="pass"
                                    name="pass"
                                    value={user.pass.value}
                                    onChange={changeField}
                                    title="Password"
                                    dis={edit.disabled}
                                    req={edit.required}
                                    type={user.showPassword ? "text" : "password"}
                                    InputProps={{
                                        endAdornment: (
                                            <InputAdornment position="end">
                                                <IconButton
                                                    onClick={handlePassword}
                                                    edge="end"
                                                    disabled={edit.disabled}
                                                >
                                                    {user.showPassword ? (
                                                        <VisibilityOff />
                                                    ) : (
                                                        <Visibility />
                                                    )}
                                                </IconButton>
                                            </InputAdornment>
                                        )
                                    }}
                                ></CustomInput>
                            </Grid>

                            <Grid
                                container
                                justifyContent={{ xs: "center", md: "flex-end" }}
                                item
                                xs={5}
                            >
                                <Button
                                    sx={{ p: "1rem 2rem", my: 2, height: "3rem" }}
                                    component="button"
                                    size="large"
                                    variant="contained"
                                    color="secondary"
                                    onClick={(e) => changeButton(e)}
                                >
                                    {!edit.isEdit ? "UPDATE" : "EDIT"}
                                </Button>

                            </Grid>

                            <Grid
                                container
                                justifyContent={{ xs: "center", md: "flex-end" }}
                                item
                                xs={5}
                            >
                                <Button
                                    sx={{ p: "1rem 2rem", my: 2, height: "3rem" }}
                                    component="button"
                                    size="large"
                                    variant="contained"
                                    color="secondary"
                                    onClick={(e) => handleDelete(e)}
                                >
                                    DELETE USER ACCOUNT
                                </Button>

                            </Grid>

                        </Grid>

                    </FormControl>

                </CardContent>
            </form>
        </Card>
    );
}
