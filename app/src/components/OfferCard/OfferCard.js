import { useAuth } from '../AuthContext.js';
import { useState, useEffect } from 'react';
import PhotosFetcher from '../../PhotosFetcher';
import './OfferCard.css';
import { Carousel } from 'react-responsive-carousel';
import "react-responsive-carousel/lib/styles/carousel.min.css"; 
import balcony from './balcony.png';
import bath from './bath.png';
import garage from './garage.png';
import room from './balcony.png';
import size from './size.png';
import stairs from './stairs.png';
import redHeart from './heart-red.png';
import heart from './heart.png';

const OfferCard = ({ id }) => {
    const { authenticatedUser } = useAuth();
    const [offer, setOffer] = useState();
    const [isBlocked, setIsBlocked] = useState(false);
    const [isFavorite, setIsFavorite] = useState(false);    

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

    }, [authenticatedUser, isFavorite]);

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

            if (data != null && offer != null && data.some(o => o.id === offer.info.id)) {
                setIsFavorite(true);
            }
        }

        fetchFavorites();

    }, [offer]);

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

    const onFavoriteClick = (e) => {
        e.preventDefault();

        const addFavorite = async () => {
            const response = await fetch(`/api/customer/add-to-favorites?id=${offer.info.id}`, {
                method: "POST",
                headers: {
                    "Authorization": "Bearer " + authenticatedUser.token
                }
            });

            if (!response.ok) {
                console.log("Failed to add offer to favorites");
                return;
            }

            setIsFavorite(true);
        }

        const removeFavorite =  async () => {
            const response = await fetch(`/api/customer/remove-from-favorites?id=${offer.info.id}`, {
                method: "POST", 
                headers: {
                    "Authorization": "Bearer " + authenticatedUser.token
                }
            });

            if (!response.ok) {
                console.log("Failed to add offer to favorites");
                return;
            }

            setIsFavorite(false);
        }

        if (isFavorite) {
            removeFavorite();
        } else {
            addFavorite();
        }
    }

    return (
        <div className="offer">
            {offer != null && (
                <>
                <div id="carouselExampleSlidesOnly" class="carousel slide" data-ride="carousel">
                    <Carousel>
                        {offer != null && offer.photos.map((photo) => (
                            <div>
                                <img src={photo}></img>
                            </div>
                         ))}
                    </Carousel>
                </div>
                <div className='info'>
                    <div className='top-bar'>
                        <div>
                            <h3>{offer.info.location}</h3>
                            <div className='header'>
                                <p>{offer.info.type.toLowerCase().replace("_", " ")} &bull;</p>
                                <p>{offer.info.availability.toLowerCase().replace("_", " ")} &bull;</p>
                                <p>{offer.info.condition.toLowerCase().replace("_", " ")}</p> 
                            </div>
                        </div>
                        <div className='favorite'>
                            <img className='heart' src={isFavorite ? redHeart : heart} width='25' onClick={onFavoriteClick}/>
                            <h5>{isFavorite ? "REMOVE" : "ADD"}</h5>
                        </div>
                        
                    </div>
                   
                    <div className='desc'>
                            <img src={size}/><p><span>{offer.info.size}m²</span> surface</p>
                            <img src={stairs}/><p><span>{offer.info.storey}</span> storeys</p>
                            <img src={bath}/><p><span>{offer.info.bathrooms}</span> bathrooms</p>
                            <img src={room}/><p><span>{offer.info.rooms}</span> rooms</p>
                            <img src={garage}/><p>Garage {offer.info.garage ? " included" : " not included"}</p>
                            <img src={balcony}/><p>Balcony {offer.info.balcony ? " included" : " not included"}</p>
                    </div>
                    {offer.info.blocked && offer.info.blockedBy != authenticatedUser.username ? (
                        <h6>The offer is currently reserved</h6>
                    ) : (
                        <>
                        {isBlocked ? (
                            <button className='btn' onClick={onCancelClick}>Cancel reservation</button>
                        ) : (
                            <button className='btn' onClick={onReserveClick}>Reserve</button>
                        )}
                        </>
                    )}
                    
                    
                </div>
                </>
             )}   
        </div>
    );
}

export default OfferCard;