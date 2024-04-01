import { useAuth } from '../AuthContext.js';
import { useParams } from 'react-router-dom';
import { useState, useEffect } from 'react';
import PhotosFetcher from '../../PhotosFetcher';

function Offer() {
    const { authenticatedUser } = useAuth();
    const { id } = useParams();
    const [offer, setOffer] = useState();

    useEffect(() => {
        const fetchOffer = async () => {
            const response = await fetch(`/api/customer/offer?id=${id}`, {
                method: "GET",
                headers: {
                    "Authorization": "Bearer " + authenticatedUser.token
                }
            });

            if (!response.ok) {
                console.log("Failed to fetch the offer");
                return;
            };

            const data = await response.json();

            const photos = await PhotosFetcher(data.id);

            setOffer({
                info: data,
                photos: photos
            });
        }
    }, [id, authenticatedUser]);

    return (
        <div className="offer">
            dgdfds
        </div>
    );
}

export default Offer;