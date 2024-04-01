import { useAuth } from '../AuthContext';
import { useState, useEffect } from 'react';
import './ReportedOffers.css';
import PhotosFetcher from '../../PhotosFetcher';
import { Carousel } from 'react-responsive-carousel';
import "react-responsive-carousel/lib/styles/carousel.min.css"; 

function ReportedOffers() {
    const { authenticatedUser } = useAuth();
    const [reportedEstates, setReportedEstates] = useState([]);
    const [isOfferPosted, setIsOfferPosted] = useState(false);
    const [isOfferFailed, setIsOfferFailed] = useState(false);
    const [description, setDescription] = useState();
    const [price, setPrice] = useState();

    useEffect(() => {
        const fetchReportedEstates = async () => {
            const response = await fetch('/api/agent/reported-estates', {
                method: "GET",
                headers: {
                    "Authorization": "Bearer " + authenticatedUser.token
                }
            });

            if (!response.ok) {
                console.log("Failed to retrieve reported estates");
                return;
            }

            const data = await response.json();

            const fetchPhotosForEstate = async (estate) => {
                setDescription(estate.description);
                setPrice(estate.offeredPrice);

                const photos = await PhotosFetcher(estate.id);
                return {
                    id: estate.id,
                    info: estate,
                    photos: photos
                };
            };
    
            const estates = await Promise.all(data.map(fetchPhotosForEstate));

            setReportedEstates(estates);
        };

        fetchReportedEstates();

    }, [authenticatedUser]);

    function numberWithCommas(x) {
        return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    }

    const onConfirmClick = (e, id) => {
        e.preventDefault();

        const confirm = async () => {
            const body = {
                price: price,
                description: description
            };

            const response = await fetch(`/api/agent/post-offer?id=${id}`, {
                method: "POST",
                headers: {
                    "Authorization": "Bearer " + authenticatedUser.token,
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(body)
            });

            if (!response.ok) {
                console.log("Failed to confirm the offer");
                setIsOfferFailed(true);
                return;
            }

            setIsOfferPosted(true);
        }

        confirm();
    }

    return (
        <div className='reported-offers'>
            <h4>Manage reported estates</h4>
            <hr></hr>
            {reportedEstates.length > 0 ? (
                <div className='offers'>
                    {reportedEstates.map((estate) => (
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
                                <h4>{estate.info.owner}</h4>
                                <div className='header'>
                                    <p>{estate.info.type.toLowerCase().replace("_", " ")} &bull;</p>
                                    <p>{estate.info.availability.toLowerCase().replace("_", " ")} &bull;</p>
                                    <p>{estate.info.condition.toLowerCase().replace("_", " ")}</p> 
                                </div>
                                <h3>${numberWithCommas(estate.info.offeredPrice)}</h3>
                                <hr></hr>
                                <h5>Estate summary</h5>
                                <div className='parameters'>
                                    <p><span>{estate.info.bathrooms}</span> {estate.info.bathrooms === 1 ? "bathroom" : "bathrooms"}</p>
                                    <p><span>{estate.info.rooms}</span> rooms</p>
                                    <p><span>{estate.info.size}m²</span> surface</p>
                                    <p>Garage {estate.info.garage == false ? "not included" : "included"}</p>
                                    <p>Balcony {estate.info.balcony == false ? "not included" : "included"}</p>
                                </div>
                                <div className='desc'>
                                    <p>Edit description</p>
                                    <input type='text' value={description} onChange={(e) => setDescription(e.target.value)}></input>
                                </div> 
                                <div className='price'>
                                    <p>Edit price</p>
                                    <input type='number' value={price} onChange={(e) => setPrice(e.target.value)}></input>
                                </div>
                            </div>
                            <div className='manage'>
                                <button className='btn confirm' onClick={(e) => onConfirmClick(e, estate.id)}>Confirm</button>
                                <button className='btn reject'>Reject</button>
                            </div>
                            <hr></hr>
                        </div>
                    ))}
                </div>
            ) : (
                <div>
                    <p>No estates are waiting to be posted</p>
                </div>
            )}
        </div>
    );
}

export default ReportedOffers;