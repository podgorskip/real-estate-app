import './App.css';
import { Route, BrowserRouter as Router, Routes, Link } from 'react-router-dom'
import { AuthProvider } from './components/AuthContext';
import Navbar from './components/Navbar/Navbar';
import Login from './components/Login/Login';
import Footer from './components/Footer/Footer';
import Register from './components/Register/Register';

function App() {
  return (
    <div className="App">
      <Router>
        <AuthProvider>
          <Navbar/>
          <Routes>
            <Route path='register' element={<Register/>}></Route>
            <Route path='login' element={<Login/>}></Route>
          </Routes>
          <Footer/>
        </AuthProvider>

      </Router>
      
    </div>
  );
}

export default App;
