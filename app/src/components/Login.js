import { useState, useEffect } from 'react';
import { useAuth } from './AuthContext';
import { Link } from 'react-router-dom';

function Login() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const { setAuthenticatedUser } = useAuth();

    const handleUsernameInputChange = (e) => {
        setUsername(e.target.value);
    }

    const handlePasswordInputChange = (e) => {
        setPassword(e.target.value);
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
                setIsAuthenticated(false);
                console.log("Authentication failure");
            }

            const authResponse = await response.json();

            setIsAuthenticated(true);
            setAuthenticatedUser({
                "username": username, 
                "token": authResponse.token
            });
            console.log("Successfully authenticated");
        }

        authenticate();
    }

    return (
        <div>
            <form>
                <label htmlFor="username">Username</label>
                <input type="text" onChange={handleUsernameInputChange} name="username"></input>

                <label htmlFor="password">Password</label>
                <input type="password" onChange={handlePasswordInputChange} name="password"></input>

                <button onClick={handleSubmit}>Submit</button>
            </form>
            {isAuthenticated && (
                <div>
                    <Link to="/upload">Upload document</Link>
                </div>
            )}
        </div>
    )
}

export default Login;