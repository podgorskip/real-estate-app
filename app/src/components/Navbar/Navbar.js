import { Link } from 'react-router-dom';
import './Navbar.css';
import logo from '../logo.png'

function Navbar() {
    return (
        <div className='navbar'>
            <ul>
                <li><Link to="/homepage">Home</Link></li>
                <li><Link to="#">Agents</Link></li>
                <li><Link to="/estates">Estates</Link></li>
            </ul>
            <div>
                <img src={logo} alt='logo' width={50} height={50}></img>
                <h3>Noble Nest</h3>
            </div>
            <ul>
                <li><Link to="/register">Register</Link></li>
                <li><Link to="/login">Login</Link></li>
                <li><Link to="/account" id='account' className='disabled-link'>Account</Link></li>
            </ul>
        </div>
    )
}

export default Navbar;