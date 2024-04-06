import './Manage.css';
import { useAuth } from '../AuthContext.js';
import { useState, useEffect } from 'react';
import bullet from './checked.png';


function Manage() {
    const { authenticatedUser } = useAuth();
    const [reserved, setReserved] = useState([]);
    const [isCancelled, setIsCancelled] = useState(false);
    const [isFinalized, setIsFinalized] = useState(false);

    useEffect(() => {
        const fetchReserved = async () => {
            const response = await fetch('/api/agent/to-finalize', {
                method: "GET",
                headers: {
                    "Authorization": "Bearer " + authenticatedUser.token
                }
            });

            if (!response.ok) {
                console.log("Failed to fetch reserved offers");
                return;
            }

            const data = await response.json();

            setReserved(data);
        }

        fetchReserved();
    }, [authenticatedUser, isCancelled, isFinalized]);

    const onFinalizeClick = (id) => {
        const finalizeOffer = async () => {
            const response = await fetch(`/api/agent/finalize-offer?id=${id}`, {
                method: "POST",
                headers: {
                    "Authorization": "Bearer " + authenticatedUser.token
                }
            });

            if (!response.ok) {
                console.log("Failed to finalize the offer");
                return;
            }

            alert("Successfully finalized the offer.");
            setIsFinalized(true);
        }

        finalizeOffer();
    }

    const onCancelClick = (id) => {
        const cancelReservation = async () => {
            const response = await fetch(`/api/unblock-offer?id=${id}`, {
                method: "PATCH",
                headers: {
                    "Authorization": "Bearer " + authenticatedUser.token
                }
            });

            if (!response.ok) {
                console.log("Failed to cancel reservation");
                return;
            }

            setIsCancelled(true);
        }

        cancelReservation();
    } 

    return (
        <div className="manage-reserved">
            <h4>Manage reserved offers</h4>
            <hr></hr>
            {reserved.length > 0 ? (
                <div className='offer'>
                    {reserved.map((offer) => (
                        <div className='card'>
                            <h4>#{offer.id}</h4>
                            <hr></hr>
                            <div className='offer-info'>
                                <p><img src={bullet} width={30}/><span>{offer.location}</span></p>
                                <p><img src={bullet} width={30}/><span>{offer.rooms}</span> rooms</p>
                                <p><img src={bullet} width={30}/><span>{offer.bathrooms}</span> bathrooms</p>
                                <p><img src={bullet} width={30}/><span>{offer.storey}</span> storey</p>
                                <p className='capitalize'><img src={bullet} width={30}/><span>{offer.availability.toLowerCase().replace("_", " ")}</span></p>
                                <p className='capitalize'><img src={bullet} width={30}/><span>{offer.condition.toLowerCase().replace("_", " ")}</span></p>
                                <p className='capitalize'><img src={bullet} width={30}/><span>{offer.type.toLowerCase().replace("_", " ")}</span></p>
                                <p><img src={bullet} width={30}/><span>{offer.size}m²</span> surface</p>
                                <p><img src={bullet} width={30}/>Garage{offer.garage ? " included" : " not included"}</p>
                                <p><img src={bullet} width={30}/>Balcony{offer.garage ? " included" : " not included"}</p>
                            </div>
                            <p>Reserved by: {offer.blockedBy}</p>
                            <div className='buttons'>
                                <button className='btn finalize' onClick={() => onFinalizeClick(offer.id)}>Finalize</button>
                                <button className='btn cancel' onClick={() => onCancelClick(offer.id)}>Cancel reservation</button>
                            </div>
                        </div>
                    ))}
                </div>
            ) : (
                <p>No reservations have been made yet.</p>
            )}
        </div>
    )
}

export default Manage;