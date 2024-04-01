import "./Agents.css";
import { useState, useEffect } from 'react';
import star from './star.png';
import { Link } from 'react-router-dom';
import calendar from './calendar.png';
import question from './question.png';
import { useAuth } from '../AuthContext';
import hide from './hide.png';

function Agents() {
    const [agents, setAgents] = useState([]);
    const [calendarSlots, setCalendarSlots] = useState([]);
    const [isCalendarEmpty, setIsCalendarEmpty] = useState(false);
    const { authenticatedUser } = useAuth();

    useEffect(() => {
        const fetchAgents = async () => {
            const response = await fetch('/api/auth/agents', {
                method: "GET"
            });

            if (!response.ok) {
                console.log("Failed to fetch agents");
                return;
            }

            const data = await response.json();

            const agentsWithReviews = await Promise.all(data.map(async (agent) => {
                const reviewsResponse = await fetch(`/api/auth/reviews?id=${agent.id}`, {
                    method: "GET"
                });

                if (reviewsResponse.ok) {
                    const reviews = await reviewsResponse.json();
                    return {
                        agentInfo: agent,
                        reviews: reviews
                    }
                }
            }));

            
            setAgents(agentsWithReviews);
            
        }

        fetchAgents();

    }, [calendarSlots]);

    const reviewArr = (n) => {
        return new Array(n).fill(null);
    }

    const checkCalendar = (id) => {
        const fetchCalendar = async () => {
            const response = await fetch(`/api/auth/calendar?id=${id}`, {
                method: "GET"
            });

            if (!response.ok) {
                setIsCalendarEmpty(true);
                return;
            }

            const data = await response.json();
            setCalendarSlots(data);
        };

        fetchCalendar();
    }

    const scheduleMeeting = (slot) => {
        if (authenticatedUser.token === '') {
            alert("You have to log in first");
            return;
        };

        const schedule = async () => {
            const response = await fetch(`/api/schedule-meeting?id=${slot}`, {
                method: "POST",
                headers: {
                    "Authorization": "Bearer " + authenticatedUser.token
                }
            });

            if (!response.ok) {
                console.log("Failed to schedule meeting: " + response);
                return;
            };

            setCalendarSlots((prev) => prev.filter((s) => s.id != slot));
        };

        schedule();
    }

    return (
        <div className="agents">
            <h2 className="lead">Our excellent team of experts</h2>
            <p>Choose from a selection of qualified and experienced real estate agents. 
                Our team brings a wealth of expertise to every transaction, ensuring you 
                find the perfect property with confidence.
                Trust in our dedication and expertise to make your real estate dreams a reality.
            </p>
            <hr></hr>
            {agents.length > 0 && agents.map((agent) => (
                <div className="card">
                    <h2 className="lead">{agent.agentInfo.fullName}</h2>
                    <hr></hr>
                    <h6><em>{agent.agentInfo.email}</em></h6>
                    <h6><em>{agent.agentInfo.phoneNumber}</em></h6> 
                    <hr></hr>
                    <div className="actions">
                        <div>
                            <img src={calendar} width='40' height='40'/>
                            <Link onClick={() => checkCalendar(agent.agentInfo.id)}>Check calendar</Link>
                        </div>    
                        <div>
                            <img src={question} width='40' height='40'/>
                            <Link to={`/ask-question/${agent.agentInfo.id}`}>Ask a question</Link>
                        </div>
                    </div>
                    {calendarSlots.length > 0 && (
                        <div className="available-slots">
                            {calendarSlots.map((slot) => (
                                <div className="card slot">
                                    {new Date(slot.date).toLocaleString().slice(0, -3)}
                                    <button className="btn btn-grey" onClick={() => scheduleMeeting(slot.id)}>Book</button>
                                </div>
                            ))}
                        </div>
                    )}
                    {isCalendarEmpty && (
                        <div className="empty-slots">
                            No calendar is present
                        </div>
                    )}
                    <hr></hr>
                    {agent.reviews.length > 0 ? (agent.reviews.map((review) => (
                        <div className="review">
                            <div>
                                {reviewArr(review.rating).map((_, idx) => (
                                    <img src={star} width='35' height='35'/>
                                ))}
                            </div>
                            <div>
                                <h5>{review.comment}</h5>
                                <h6>{review.reviewer}</h6>
                            </div>
                        </div>
                    ))) : (
                        <div>
                        </div>
                    )}
                    
                </div>
            ))}
        </div>
    );
}

export default Agents;