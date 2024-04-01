import { useAuth } from '../AuthContext';
import './MeetingSlotsAdder.css';
import { useState } from 'react';
import remove from './remove.png';
import calendar from './calendar.png';

function MeetingSlotsAdder() {
    const { authenticatedUser } = useAuth();
    const [slots, setSlots] = useState([]);
    const [date, setDate] = useState(new Date().toISOString().slice(0, 16));
    const [isSuccess, setIsSuccess] = useState(false);
    const [isFailed, setIsFailed] = useState(false);

    const handleRemoveSlot = (index) => {
        setSlots(prev => prev.filter((_, idx) => idx != index));
    }

    const handleAddToCalendar = (e) => {
        e.preventDefault();

        const body = {
            slots: slots
        };

        const add = async () => {
            const response = await fetch('/api/agent/add-meeting-slots', {
                method: "POST",
                headers: {
                    "Authorization": "Bearer " + authenticatedUser.token,
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(body)
            });

            if (!response.ok) {
                console.log("Failed to add dates to calendar");
                setIsSuccess(false);
                setIsFailed(true);
                return;
            }

            setIsFailed(false);
            setIsSuccess(true);
        };

        add();
    }

    return (
        <div className='meeting-slots'>
            <h1 className='lead'>Welcome, Agent!</h1>
            <p>Here, you can conveniently set up your available meeting times for clients. 
                Simply specify the date, time, and any additional details to ensure smooth scheduling.
            </p>
            <hr></hr>
            <div className='info'>
                <img src={calendar} width='100' height='100'/>
                <div className='adder'>
                    <input type='datetime-local' value={date} onChange={(e) => setDate(e.target.value)}></input>
                    <button className='btn btn-dark' onClick={() => setSlots((prevSlots) => [...prevSlots, date])}>Add</button>
                </div>
            </div>

            {slots.length > 0 ? (
                <div className='slots'>
                    {slots.map((slot, idx) => (
                        <div className='date'>
                            {new Date(slot).toLocaleString().slice(0, -3)}
                            <img src={remove} width='35' height='35' onClick={() => handleRemoveSlot(idx)}/>
                        </div>
                    ))}
                    <hr></hr>
                    <h6>You can now add the selected visits to your calendar</h6>
                    <button className='btn btn-dark' onClick={handleAddToCalendar}>Add to calendar</button>
                    {isSuccess && (
                        <div className='alert alert-success'>
                            Successfully added available slots to calendar
                        </div>
                    )}
                    {isFailed && (
                        <div className='alert alert-danger'>
                            Failed to add available slots to calendar
                        </div>
                    )}
                </div>
            ) : (
                <div></div>
            )}
        </div>
    )
}

export default MeetingSlotsAdder;