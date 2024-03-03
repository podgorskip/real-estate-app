import { useContext, createContext, useState } from 'react';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [authenticatedUser, setAuthenticatedUser] = useState({
        "username": "",
        "token": ""
    });

    return (
        <AuthContext.Provider value={{ authenticatedUser, setAuthenticatedUser }}>
            { children }
        </AuthContext.Provider>
    )
}

export const useAuth = () => {
    return useContext(AuthContext);
}