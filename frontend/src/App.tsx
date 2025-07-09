import { jwtDecode } from "jwt-decode";
import { BrowserRouter as Router, Route, Routes, Navigate } from "react-router-dom";
import { Dashboard } from "./pages/Dashboard";
import Header from "./components/Header";
import { Login } from "./pages/Login";
import { useState, useEffect } from "react";
import axios from "axios";
import { ThemeProvider } from "@mui/material/styles";
import { theme } from "./theme";
import { CssBaseline } from "@mui/material";

interface User {
  sub: string;
  iat: number;
  exp: number;
}

function App() {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [user, setUser] = useState<User | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const decodedToken = jwtDecode<User>(token);
        const currentTime = Date.now() / 1000;
        if (decodedToken.exp > currentTime) {
          setIsAuthenticated(true);
          setUser(decodedToken);
        } else {
          localStorage.removeItem("token");
        }
      } catch (error) {
        console.error("Invalid token:", error);
        localStorage.removeItem("token");
      }
    }
    setLoading(false);
  }, []);

  const handleLoginSuccess = () => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const decodedToken = jwtDecode<User>(token);
        setIsAuthenticated(true);
        setUser(decodedToken);
      } catch (error) {
        console.error("Invalid token:", error);
      }
    }
  };

  if (loading) {
    return <div>Loading...</div>;
  }

  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <Router>
        <Routes>
          <Route path="/login" element={!isAuthenticated ? <Login onLoginSuccess={handleLoginSuccess} /> : <Navigate to="/dashboard" />} />
          <Route
            path="/dashboard"
            element={
              isAuthenticated && user ? (
                <>
                  <Header user={user} />
                  <Dashboard />
                </>
              ) : (
                <Navigate to="/login" />
              )
            }
          />
          <Route path="*" element={<Navigate to="/login" />} />
        </Routes>
      </Router>
    </ThemeProvider>
  );
}

export default App;
