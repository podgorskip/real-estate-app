import { useAuth } from "../AuthContext"; 
import { useState, useEffect } from "react";
import "./MyEstates.css";
import JSZip from 'jszip';
import { Carousel } from 'react-responsive-carousel';
import "react-responsive-carousel/lib/styles/carousel.min.css"; 

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
                            <h3 className="lead">{estate.data.location}</h3>
                            <hr/>
                            {!estate.data.isSubmitted ? (<p>Status: waiting</p>) : (<p>Status: submitted</p>)}
                            <p>Agent: {estate.data.agent}</p>
                            <p>Type: {estate.data.type.toLowerCase()}</p>
                            <p>Availability: {estate.data.availability.toLowerCase().replace("_", " ")}</p>
                            <p>Condition: {estate.data.condition.toLowerCase().replace("_", " ")}</p>
                            <p>Bathrooms: {estate.data.bathrooms}</p>
                            <p>Rooms: {estate.data.rooms}</p>
                            <p>Garage: {estate.data.garage == true ? "included" : "not included"}</p>
                            <p>Balcony: {estate.data.balcony == true ? "included" : "not included"}</p>
                            <p>Storey: {estate.data.storey}</p>
                            <p>Size: {estate.data.size} m²</p>
                            <p className="description">Description: {estate.data.description}</p>
                            <p className="price">Offered price: <strong>{estate.data.offeredPrice}$</strong></p>

                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
}

export default MyEstates;