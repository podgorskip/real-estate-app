import { useAuth } from "../AuthContext"; 
import { useState, useEffect } from "react";
import "./MyEstates.css";
import JSZip from 'jszip';
import { Carousel } from 'react-responsive-carousel';
import "react-responsive-carousel/lib/styles/carousel.min.css"; 
import size from './size.png';
import bathroom from './bathroom.png';
import garage from './garage.png';
import room from './room.png';
import balcony from './balcony.png';
import storey from './storey.png';
import status from './status.png';

function MyEstates() {
    const { authenticatedUser } = useAuth();
    const [estates, setEstates] = useState([]);


    const fetchPhotosFromZip = async (id) => {
        try {
            const response = await fetch(`/api/auth/photos?id=${id}`, {
                method: "GET"
            });

            if (!response.ok) {
                throw new Error('Failed to fetch photos');
            }

            const zipFileData = await response.arrayBuffer(); 
            const zip = await JSZip.loadAsync(zipFileData); 

            const photoUrls = [];

            zip.forEach((relativePath, zipEntry) => {
                if (zipEntry.dir) {
                    return; 
                }
                const photoData = zipEntry.async("blob").then(blob => URL.createObjectURL(blob));
                photoUrls.push(photoData);
            });

            return Promise.all(photoUrls);
        } catch (error) {
            console.error('Error fetching and displaying photos:', error);
            return [];
        }
    };

    useEffect(() => {
        const fetchEstates = async () => {
            const response = await fetch('/api/owner/my-estates', {
                method: "GET",
                headers: {
                    "Authorization": "Bearer " + authenticatedUser.token 
                }
            });

            if (!response.ok) {
                console.log("Failed to fetch estates");
                return;
            }

            const data = await response.json();

            const estatesWithPhotos = await Promise.all(data.map(async (estate) => {
                const photos = await fetchPhotosFromZip(estate.id);
                return {
                    id: estate.id,
                    data: estate,
                    photos: photos,
                    // document: await fetchDocument(estate.id)
                };
            }));
    
            setEstates(estatesWithPhotos);
        }

        fetchEstates();

        estates.forEach(async (estate) => {
            const photos = await fetchPhotosFromZip(estate.id);
            estate.photos = photos;
        });

    }, [authenticatedUser]);

    function numberWithCommas(x) {
        return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    }


     return (
        <div className="my-estates">
            <h2 className="lead">Here are the offers you have trusted us to manage</h2>
            <hr></hr>
            <div className="reported">
                {estates.map((estate) => (
                    <div className="card">
                        <div id="carouselExampleSlidesOnly" class="carousel slide" data-ride="carousel">
                            <Carousel>
                                {estate.photos.map((photo) => (
                                    <div>
                                        <img src={photo}></img>
                                    </div>
                                ))}
                            </Carousel>
                        <div/>
                        </div>
                        <div className="info">
                            <div>
                                <div className="header">
                                    <h2>{estate.data.location}</h2>
                                    <h6><img src={status} width='20' height='20'/>{!estate.data.isSubmitted ? (<p>Status: waiting</p>) : (<p>Status: submitted</p>)}</h6>
                                </div>
                                <p>{estate.data.type.toLowerCase().replace("_", " ")} &bull;</p>
                                <p>{estate.data.availability.toLowerCase().replace("_", " ")} &bull;</p>
                                <p>{estate.data.condition.toLowerCase().replace("_", " ")}</p> 
                                <hr></hr>
                            </div>
                            <p><img src={bathroom} width='40' height='40'/><span>{estate.data.bathrooms}</span> bathrooms</p>
                            <p><img src={room} width='40' height='40'/><span>{estate.data.rooms}</span> rooms</p>
                            <p><img src={garage} width='40' height='40'/>Garage {estate.data.garage == true ? "included" : "not included"}</p>
                            <p><img src={storey} width='40' height='40'/><span>{estate.data.storey}</span> {estate.data.storey === 1 ? "storey" : "storeys"}</p>
                            <p><img src={size} width='40' height='40'/><span>{estate.data.size} m²</span> surface</p>
                            <p><img src={balcony} width='40' height='40'/>Balcony {estate.data.balcony == true ? "included" : "not included"}</p>
                            <p className="description"><em>{estate.data.description}</em></p>
                            <p className="price">${numberWithCommas(estate.data.offeredPrice)}</p>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
}

export default MyEstates;