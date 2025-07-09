import { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { Box, Button, TextField, Typography, Paper } from "@mui/material";

export const Login = ({ onLoginSuccess }: { onLoginSuccess: () => void }) => {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const navigate = useNavigate();

    const handleLogin = async () => {
        try {
            const res = await axios.post("/api/auth/login", { email, password });
            localStorage.setItem("token", res.data.token);
            onLoginSuccess();
            navigate("/dashboard");
        } catch (err) {
            console.error("ログインエラー:", err);
            alert("ログイン失敗");
        }
    };

    return (
        <Box
            sx={{
                display: "flex",
                justifyContent: "center",
                alignItems: "center",
                height: "100vh",
                backgroundColor: "#f0f2f5",
            }}
        >
            <Paper
                sx={{
                    p: 4,
                    display: "flex",
                    flexDirection: "column",
                    gap: 3,
                    borderRadius: 4,
                    boxShadow: "0 8px 32px 0 rgba(31, 38, 135, 0.1)",
                    width: "100%",
                    maxWidth: "400px",
                }}
            >
                <Typography variant="h5" component="h1" sx={{ fontWeight: "bold", textAlign: "center" }}>
                    ログイン
                </Typography>
                <TextField
                    label="Email"
                    variant="outlined"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                />
                <TextField
                    label="Password"
                    type="password"
                    variant="outlined"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                />
                <Button
                    variant="contained"
                    onClick={handleLogin}
                    sx={{ borderRadius: 2, boxShadow: "none", textTransform: "none", py: 1.5 }}
                >
                    ログイン
                </Button>
            </Paper>
        </Box>
    );
};
