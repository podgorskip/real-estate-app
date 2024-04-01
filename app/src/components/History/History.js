import { useAuth } from '../AuthContext';
import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import "./History.css";

function History() {
    const { authenticatedUser } = useAuth();
    const { role } = useParams();
    const [archived, setArchived] = useState([]);

    useEffect(() => {
        const fetchArchivedOffers = async () => {
            const response = await fetch(`/api/${new String(role).toLowerCase()}/archived-offers`, {
                method: "GET",
                headers: {
                    "Authorization": "Bearer " + authenticatedUser.token
                }
            });

            if (!response.ok) {
                console.log("Failed to fetch archived offers");
                return;
            };

            const data = await response.json();
            setArchived(data);
        };

        fetchArchivedOffers();

    }, [authenticatedUser]);

    return (
        <div className="history">
            <h4>Transactions history</h4>
            <hr></hr>
            {archived.length > 0 ? (
                <div>
                    {archived.map((offer) => (
                        <div className='offer'>
                            {offer.id}
                        </div>
                    ))}
                </div>
            ) : (
                <div>
                    <p>You haven't {role === 'CUSTOMER' ? 'got' : 'sold'} any estates yet</p>
                </div>
            )}

        </div>
    );
}

export default History;