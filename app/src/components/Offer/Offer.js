import { useAuth } from '../AuthContext.js';
import { useParams } from 'react-router-dom';
import { useState, useEffect } from 'react';
import PhotosFetcher from '../../PhotosFetcher';
import './Offer.css';
import OfferCard from '../OfferCard/OfferCard.js';



function Offer() {
    const { authenticatedUser } = useAuth();
    const { id } = useParams();
    const [offer, setOffer] = useState();
    const [isBlocked, setIsBlocked] = useState(false);
    

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

            console.log(data)

            if (data.blockedBy === authenticatedUser.username) {
                setIsBlocked(true);
            }

            const photos = await PhotosFetcher(data.estateID);

            setOffer({
                info: data,
                photos: photos
            });
        }

        fetchOffer();

    }, [id, authenticatedUser]);

    const onReserveClick = (e) => {
        e.preventDefault();
        
        const blockOffer = async () => {
            const response = await fetch(`/api/customer/block-offer?id=${id}`, {
                method: "PATCH",
                headers: {
                    "Authorization": "Bearer " + authenticatedUser.token
                }
            });

            if (!response.ok) {
                console.log("Failed to block the offer");
                return;
            }

            setIsBlocked(true);
        }

        blockOffer();
    };

    const onCancelClick = (e) => {
        e.preventDefault();

        const unblockOffer = async () => {
            const response = await fetch(`/api/unblock-offer?id=${id}`, {
                method: "PATCH",
                headers: {
                    "Authorization": "Bearer " + authenticatedUser.token
                }
            });

            if (!response.ok) {
                console.log("Failed to unblock the offer");
                return;
            }

            setIsBlocked(false);
        }
  
        unblockOffer();
    }

    return (
        <div className="offer">
            {offer != null && (
                <>
                <h4>Offer details</h4>
                <hr></hr>
                <OfferCard id={id}></OfferCard>
                </>
            )}
        </div>
    );
}

export default Offer;