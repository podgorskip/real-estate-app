import { useEffect, useState } from 'react';
import { useAuth } from '../AuthContext.js';
import OfferCard from '../OfferCard/OfferCard.js';
import './Favorites.css';

function Favorites() {
    const { authenticatedUser } = useAuth();
    const [favorites, setFavorites] = useState([]);

    useEffect(() => {
        const fetchFavorites = async () => {
            const response = await fetch('/api/customer/favorites', {
                method: "GET",
                headers: {
                    "Authorization": "Bearer " + authenticatedUser.token
                }
            });

            if (!response.ok) {
                console.log("Failed to fetch favorites");
                return;
            }

            const data = await response.json();

            console.log(data)
            setFavorites(data);

            console.log(favorites)
        }

        fetchFavorites();
    }, [])

    return (
        <div className='favorites'>
            <h4>Favorites</h4>
            <hr></hr>
            <div className='offers'>
                {favorites.length > 0 ? (
                    favorites.map((offer) => (
                        <OfferCard id={offer.id}></OfferCard>
                    ))
                ) : (
                    <p>No offers have been added to favorites yet</p>
                )}
            </div>
        </div>
    );
}

export default Favorites;