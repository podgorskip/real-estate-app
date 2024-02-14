import './App.css';
import { Route, BrowserRouter as Router, Routes, Link } from 'react-router-dom'
import { AuthProvider } from './components/AuthContext';
import Login from './components/Login';
import Document from './components/Document';


function App() {
  return (
    <div className="App">
      <Router>
        <AuthProvider>
          <Link to='/login-page'>Log in</Link>
          <Routes>
            <Route path='/login-page' element={<Login/>}></Route>
            <Route path='/upload' element={<Document/>}></Route>
          </Routes>
        </AuthProvider>

      </Router>
      
    </div>
  );
}

export default App;
