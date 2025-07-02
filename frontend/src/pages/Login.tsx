import { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

export const Login = () => {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const navigate = useNavigate();

    const handleLogin = async () => {
        try {
        const res = await axios.post("/api/auth/login", { email, password });
        localStorage.setItem("token", res.data.token);
        navigate("/dashboard");
        } catch (err) {
            console.error("ログインエラー:", err);
            alert("ログイン失敗");
        }
    };

    return (
        <div style={{ padding: "2rem" }}>
            <h2>ログイン</h2>
            <input value={email} onChange={(e) => setEmail(e.target.value)} placeholder="email" />
            <input value={password} onChange={(e) => setPassword(e.target.value)} type="password" placeholder="password" />
            <button onClick={handleLogin}>ログイン</button>
        </div>
    );
};
