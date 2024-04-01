import { useState } from 'react';
import './Register.css';

function Register() {
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [repeatPassword, setRepeatPassword] = useState("");
    const [email, setEmail] = useState("");
    const [phoneNumber, setPhoneNumber] = useState("");
    const [passwordMatch, setPasswordMatch] = useState(true);
    const [role, setRole] = useState("");
    const [registrationFailure, setRegistrationFailure] = useState(false);
    const [registrationSuccess, setRegistrationSuccess] = useState(false);

    const isAnyFieldEmpty = () => {
        return !(
            firstName &&
            lastName &&
            username &&
            password &&
            repeatPassword &&
            email &&
            phoneNumber &&
            role
        );
    };

    const handleClear = (e) => {
        e.preventDefault();

        setFirstName("");
        setLastName("");
        setUsername("");
        setPassword("");
        setRepeatPassword("");
        setEmail("");
        setPhoneNumber("");
        setRole("");
        setPasswordMatch(true); 
    }

    const handleSubmit = (e) => {
        e.preventDefault();

        if (password !== repeatPassword) {
            setPasswordMatch(false);
            return;
        }

        const body = {
            firstName: firstName,
            lastName: lastName,
            username: username,
            password: password,
            email: email,
            phoneNumber: phoneNumber
        };

        const register = async () => {
            const response = await fetch(`/api/auth/${role}/register`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                }, 
                body: JSON.stringify(body)
            });

            if (!response.ok) {
                console.log("Failed to register");
                setRegistrationFailure(true);
                return;
            }

            setRegistrationSuccess(true);
        }

        register();
    }

    return (
        <div className='registration'>
            <h2 className='lead'>Register to unlock all the functionalities</h2>
            <hr></hr>
            <form>
                <label htmlFor='fName'>First name</label>
                <input type='text' id='fName' name='fName' value={firstName} onChange={(e) => setFirstName(e.target.value)}></input>
                <label htmlFor='lName'>Last name</label>
                <input type='text' id='lName' name='lName' value={lastName} onChange={(e) => setLastName(e.target.value)}></input>
                <label htmlFor='username'>Username</label>
                <input type='text' id='username' name='username' value={username} onChange={(e) => setUsername(e.target.value)}></input>
                <label htmlFor='fPassword'>Password</label>
                <input type='password' id='fPassword' name='fPassword' value={password} onChange={(e) => setPassword(e.target.value)}></input>
                <label htmlFor='rPassword'>Repeat password</label>
                <input type='password' id='rPassword' name='rPassword' value={repeatPassword} onChange={(e) => setRepeatPassword(e.target.value)}></input>
                {!passwordMatch && (
                    <div class="alert alert-danger" role="alert">
                        <p>You successfully signed in.</p>
                    </div>
                )}
                <label htmlFor='email'>Email</label>
                <input type='email' id='email' name='email' value={email} onChange={(e) => setEmail(e.target.value)}></input>
                <label htmlFor='phoneNumber'>Phone number</label>
                <input type='tel' id='phoneNumber' name='phoneNumber' value={phoneNumber} onChange={(e) => setPhoneNumber(e.target.value)} pattern='[0-9]{3} [0-9]{3} [0-9]{3}'></input>
                <label>Account type</label>
                <div className='checkboxes'>
                    <div>
                        <input className="form-check-input" type="radio" name="role" id="customer" value="customer" onChange={(e) => setRole(e.target.value)}/>
                        <label className="form-check-label" for="customer">Customer</label>
                    </div>
                    <div>
                        <input className="form-check-input" type="radio" name="role" id="owner" value="owner" onChange={(e) => setRole(e.target.value)}/>
                        <label className="form-check-label" for="owner">Owner</label>
                    </div>
                </div>
                <div className='buttons'>
                    <button className='btn btn-dark' onClick={handleSubmit} disabled={isAnyFieldEmpty()}>Submit</button>
                    <button className='btn btn-dark' onClick={handleClear}>Clear</button>
                </div>
                {registrationSuccess && (
                    <div class="alert alert-success" role="alert">
                        <p>You successfully created account.</p>
                    </div>
                )}
                {registrationFailure && (
                    <div class="alert alert-danger" role="alert">
                        <p>Failed to create account.</p>
                    </div>
                )}
            </form>

        </div>
    );
}

export default Register;