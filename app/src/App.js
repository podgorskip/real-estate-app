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
import ScheduleMeeting from './components/ScheduleMeeting/ScheduleMeeting';
import Agents from './components/Agents/Agents';
import MeetingSlotsAdder from './components/MeetingSlotsAdder/MeetingSlotsAdder';
import AskQuestion from './components/AskQuestion/AskQuestion';
import Settings from './components/Settings/Settings';
import History from './components/History/History';
import ReportedOffers from './components/ReportedOffers/ReportedOffers';
import Offer from './components/Offer/Offer';

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
            <Route path='schedule-meeting' element={<ScheduleMeeting/>}></Route>
            <Route path='agents' element={<Agents/>}></Route>
            <Route path='add-slots' element={<MeetingSlotsAdder/>}></Route>
            <Route path='ask-question/:id' element={<AskQuestion/>}></Route>
            <Route path='settings' element={<Settings/>}></Route>
            <Route path='history/:role' element={<History/>}></Route>
            <Route path='reported-offers' element={<ReportedOffers/>}></Route>
            <Route path='check-details/:id' element={<Offer/>}></Route>
          </Routes>
          <Footer/>
        </AuthProvider>

      </Router>
      
    </div>
  );
}

export default App;
