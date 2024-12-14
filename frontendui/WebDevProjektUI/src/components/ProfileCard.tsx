import Card from "@mui/material/Card";
import Typography from "@mui/material/Typography";
import { Grid } from "@mui/material";
import Avatar from "@mui/material/Avatar";
import PhotoCameraIcon from "@mui/icons-material/PhotoCamera";
import Badge from "@mui/material/Badge";
import Button from "@mui/material/Button";
import {Link} from "react-router-dom";
import {Home} from "@mui/icons-material";

const styles = {
    details: {
        padding: "1rem",
        borderTop: "1px solid #e1e1e1"
    },
    value: {
        padding: "1rem 2rem",
        borderTop: "1px solid #e1e1e1",
        color: "#899499"
    }
};

export default function ProfileCard(props: any) {
    return (
        <Card variant="outlined">
            <Grid
                container
                direction="column"
                justifyContent="center"
                alignItems="center"
            >
                <Grid item sx={{ p: "1.5rem 0rem", textAlign: "center" }}>
                    <Badge
                        overlap="circular"
                        anchorOrigin={{ vertical: "bottom", horizontal: "right" }}
                        badgeContent={
                            <PhotoCameraIcon
                                sx={{
                                    border: "5px solid white",
                                    backgroundColor: "#ff558f",
                                    borderRadius: "50%",
                                    padding: ".2rem",
                                    width: 35,
                                    height: 35
                                }}
                            ></PhotoCameraIcon>
                        }
                    >
                        <Avatar
                            sx={{ width: 100, height: 100, mb: 1.5 }}
                            src="../public/profile.png"
                        ></Avatar>
                    </Badge>

                    <Typography variant="h6">{props.name}</Typography>
                    <Typography color="text.secondary">{props.role}</Typography>
                </Grid>

                <Grid container>
                    <Grid item xs={6}>
                        <Typography style={styles.details}>Password</Typography>
                        <Typography style={styles.details}>Email</Typography>
                        <Typography style={styles.details}>Phone</Typography>
                    </Grid>

                    <Grid item xs={6} sx={{ textAlign: "end" }}>
                        <Typography style={styles.value}>{props.pass}</Typography>
                        <Typography style={styles.value}>{props.email}</Typography>
                        <Typography style={styles.value}>{props.phone}</Typography>
                    </Grid>
                </Grid>

                <Grid item style={styles.details} sx={{ width: "100%" }}>
                    <Link to="/home">
                        <Button
                            variant="contained"
                            color="secondary"
                            sx={{ width: "99%", p: 1, my: 2 }}
                        >
                            View Home page
                        </Button>
                    </Link>
                </Grid>
            </Grid>
        </Card>
    );
}
