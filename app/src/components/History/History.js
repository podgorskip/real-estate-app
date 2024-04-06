import { useAuth } from '../AuthContext';
import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import "./History.css";
import { Carousel } from 'react-responsive-carousel';
import "react-responsive-carousel/lib/styles/carousel.min.css"; 
import PhotosFetcher from '../../PhotosFetcher';
import bath from '../OfferCard/bath.png';
import room from '../OfferCard/room.png';
import balcony from '../OfferCard/balcony.png';
import garage from '../OfferCard/garage.png';
import size from '../OfferCard/size.png';
import stairs from '../OfferCard/stairs.png';
import income from './income.png';
import { Link } from 'react-router-dom';

function History() {
    const { authenticatedUser } = useAuth();
    const { role } = useParams();
    const [archived, setArchived] = useState([]);
    const [totalIncome, setTotalIncome] = useState(0);

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

            console.log(data)

            const fetchPhotosForEstate = async (estate) => {
                const photos = await PhotosFetcher(estate.estateID);
                    return {
                        id: estate.id,
                        info: estate,
                        photos: photos
                    };
            }

            const estates = await Promise.all(data.map(fetchPhotosForEstate));

            setArchived(estates);
        };

        fetchArchivedOffers();

    }, [authenticatedUser]);

    useEffect(() => {
        const totalIncome = archived.reduce((total, offer) => total + offer.info.price, 0);
        setTotalIncome(totalIncome);
    
    }, [archived]);

    function numberWithCommas(x) {
        return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    }

    return (
        <div className="history">
            <h4>Transactions history</h4>
            <hr></hr>
            {archived.length > 0 ? (
                <div>
                    {role === 'OWNER' && (
                        <h6 className='income'><img src={income} width='35'/>Your total income  ${numberWithCommas(totalIncome)}</h6>
                    )}
                    
                    {archived.map((estate) => (
                        <div className='estate'>
                        <div id="carouselExampleSlidesOnly" class="carousel slide" data-ride="carousel">
                            <Carousel>
                                {estate.photos.map((photo) => (
                                    <div>
                                        <img src={photo}></img>
                                    </div>
                                ))}
                            </Carousel>
                        </div>
                        <div className='info'>
                            <h4>{estate.info.location}</h4>
                            <div className='header'>
                                <p>{estate.info.type.toLowerCase().replace("_", " ")} &bull;</p>
                                <p>{estate.info.availability.toLowerCase().replace("_", " ")} &bull;</p>
                                <p>{estate.info.condition.toLowerCase().replace("_", " ")}</p> 
                            </div>
                            <h3>${numberWithCommas(estate.info.price)}</h3>
                            <hr></hr>
                            <div className='parameters'>
                                <div className='col'>
                                    <img src={size}/><p><span>{estate.info.size}m²</span> surface</p>
                                    <img src={bath}/><p><span>{estate.info.bathrooms}</span> {estate.info.bathrooms === 1 ? "bathroom" : "bathrooms"}</p>
                                    <img src={room}/><p><span>{estate.info.rooms}</span> rooms</p>
                                </div>
                                <div className='col'>
                                    <img src={stairs}/><p><span>{estate.info.storey}</span> storey</p>
                                    <img src={garage}/><p>Garage {estate.info.garage == false ? "not included" : "included"}</p>
                                    <img src={balcony}/><p>Balcony {estate.info.balcony == false ? "not included" : "included"}</p>
                                </div>
                            </div>
                            {!estate.info.isReviewed && (
                                <button className='btn btn-dark'><Link to={`/review/${estate.info.agentID}/${role.toLowerCase()}/${estate.id}`}>Review</Link></button>
                            )}
                        </div>
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