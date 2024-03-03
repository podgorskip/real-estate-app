import { useAuth } from "../AuthContext";
import "./Login.css";
import { useState } from "react";

function Login() {
    const [isNotFilled, setIsNotFilled] = useState(true);
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const { setAuthenticatedUser } = useAuth();
    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const [loginFailure, setLoginFailure] = useState(false);

    const handleUsernameChange = (e) => {
        setUsername(e.target.value);
        setIsNotFilled(false);
    }

    const handlePasswordChange = (e) => {
        setPassword(e.target.value);
        setIsNotFilled(false);
    }

    const handleSubmit = (e) => {
        e.preventDefault();

        const body = {
            username: username,
            password: password
        };

        const authenticate = async () => {
            const response = await fetch('/api/auth/authenticate', {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(body)
            });

            if (!response.ok) {
                console.log("Failed to authenticate: " + response.json);
                setLoginFailure(true);
                return;
            } 

            setIsAuthenticated(true);

            setAuthenticatedUser({
                username: username,
                token: response.token
            });
        }

        authenticate();
    }

    const clear = (e) => {
        e.preventDefault();

        setIsAuthenticated(false);
        setIsNotFilled(true);
        setLoginFailure(false);

        if (password !== "" || username !== "") {
            setPassword("");
            setUsername("");
        }
    }

    return (
        <div className="login">
            <h3 className="lead">Please login to gain full access to web</h3>
            <hr></hr>
            <form className="form">
                <label>Username</label>
                <input type="text" value={username} onChange={handleUsernameChange}></input>
                <label>Password</label>
                <input type="password" value={password} onChange={handlePasswordChange}></input>
                <div>
                    <button className="btn btn-dark" onClick={handleSubmit} disabled={isNotFilled}>Submit</button>
                    <button className="btn btn-dark" onClick={clear}>Clear</button>
                </div>
            </form>
            {isAuthenticated && (
                <div class="alert alert-success" role="alert">
                <h4 class="alert-heading">Well done!</h4>
                <hr></hr>
                <p>You successfully signed in.</p>
                </div>
            )}
            {loginFailure && (
                <div class="alert alert-danger" role="alert">
                    Authentication failed. Try again.
                </div>
            )}
        </div>
    )
}

export default Login;