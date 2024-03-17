import './Estates.css';
import { useState, useEffect } from 'react';

function Estates() {
    const [offers, setOffers] = useState([{}]);
    const [criteria, setCriteria] = useState(null);

    useEffect(() => {
        const fetchOffers = async () => {
            try {
                const response = await fetch('/api/auth/offers', {
                    method: "GET"
                });
    
                if (response.ok) {
                    const offersData = await response.json(); 
                    const offersWithPhotos = await Promise.all(offersData.map(async (offer) => {
                        const photoResponse = await fetch(`/api/auth/photos?id=${offer.id}`, {
                            method: "GET"
                        });
                        if (photoResponse.ok) {
                            const photosData = await photoResponse.json();
                            return {
                                offer: offer,
                                photos: photosData
                            };
                        }
                    }));

                    setOffers(offersWithPhotos.filter(Boolean)); 
                } else {
                    console.log("Failed to fetch offers");
                }
            } catch (error) {
                console.error("Error fetching offers:", error);
            }
        }
        fetchOffers();
    }, []);

    return (
        <div className="estates">
            <h2 className="lead">Explore Exclusive Offers on Estates</h2>
            <p>
                Welcome to our Estates section, where you'll find more than just properties – 
                you'll discover exclusive offers that add exceptional value to your real estate 
                journey. Dive into our curated selection of deals, promotions, and special incentives 
                designed to enhance your experience and help you find your dream property.</p>
            <hr></hr>
            <ul>
                {offers.map((offer) => (
                    <li>
                        <div className='card'>
                            {offer.id}
                        </div>
                    </li>
                ))}
            </ul>
        </div>
    );
}

export default Estates;

