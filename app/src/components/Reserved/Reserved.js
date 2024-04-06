import './Reserved.css';
import { useState, useEffect } from 'react';
import { useAuth } from '../AuthContext.js';
import OfferCard from '../OfferCard/OfferCard';

function Reserved() {
    const { authenticatedUser } = useAuth();
    const [reserved, setReserved] = useState([]);

    useState(() => {
        const fetchReserved = async () => {
            const response = await fetch(`/api/customer/blocked-offers`, {
                method: "GET",
                headers: {
                    "Authorization": "Bearer " + authenticatedUser.token
                }
            });

            if (!response.ok) {
                console.log("Failed to fetch");
                return;
            }

            const data = await response.json();
            setReserved(data);
        }

        fetchReserved();

    }, [])
    return (
        <div className='reserved'>
            <h4>Reserved</h4>
            <hr></hr>
            {reserved.length > 0 ? (
                <div>
                    {reserved.map((offer) => (
                        <OfferCard id={offer.id}></OfferCard>
                    ))}
                </div>
            ) : (
                <p>You don't have any offers reserved yet.</p>
            )}

        </div>
    );
}

export default Reserved;