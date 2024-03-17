import './App.css';
import { Route, BrowserRouter as Router, Routes, Link } from 'react-router-dom'
import { AuthProvider } from './components/AuthContext';
import Navbar from './components/Navbar/Navbar';
import Login from './components/Login/Login';
import Footer from './components/Footer/Footer';
import Register from './components/Register/Register';
import Account from './components/Account/Account';
import HomePage from './components/HomePage/HomePage';
import Estates from './components/Estates/Estates';
import ReportEstate from './components/ReportEstate/ReportEstate';
import MyEstates from './components/MyEstates/MyEstates';

function App() {
  return (
    <div className="App">
      <Router>
        <AuthProvider>
          <Navbar/>
          <Routes>
            <Route path='register' element={<Register/>}></Route>
            <Route path='login' element={<Login/>}></Route>
            <Route path='account' element={<Account/>}></Route>
            <Route path='homepage' element={<HomePage/>}></Route>
            <Route path='estates' element={<Estates/>}></Route>
            <Route path='report-estate' element={<ReportEstate/>}></Route>
            <Route path='my-estates' element={<MyEstates/>}></Route>
          </Routes>
          <Footer/>
        </AuthProvider>

      </Router>
      
    </div>
  );
}

export default App;
