import { useAuth } from "../AuthContext";
import "./ReportEstate.css";
import { useEffect, useState } from 'react';

function ReportEstate() {
    const { authenticatedUser } = useAuth();
    const [agents, setAgents] = useState([]);
    const [selectedAgent, setSelectedAgent] = useState("");
    const [type, setType] = useState("");
    const [bathrooms, setBathrooms] = useState("");
    const [rooms, setRooms] = useState("");
    const [garage, setGarage] = useState(false);
    const [storey, setStorey] = useState("");
    const [balcony, setBalcony] = useState(false);
    const [description, setDescription] = useState("");
    const [size, setSize] = useState("");
    const [availability, setAvailability] = useState("");
    const [condition, setCondition] = useState("");
    const [offeredPrice, setOfferedPrice] = useState("");
    const [location, setLocation] = useState("");
    const [document, setDocument] = useState(null);
    const [photos, setPhotos] = useState([]);
    const [isPhotoAdded, setIsPhotoAdded] = useState(false);
    const [isDocumentAdded, setIsDocumentAdded] = useState(false);

    useEffect(() => {
        const fetchAgents = async () => {
            const response = await fetch('/api/auth/agents', {
                method: "GET"
            });

            if (!response.ok) {
                console.log("Failed to fetch agents");
                return;
            }

            const data = await response.json();

            setAgents(data);
        };

        fetchAgents();
    }, []);

    const isFormFilled = () => {
        return (
            selectedAgent !== "" &&
            type !== "" &&
            bathrooms !== "" &&
            rooms !== "" &&
            storey !== "" &&
            description !== "" &&
            size !== "" &&
            availability !== "" &&
            condition !== "" &&
            offeredPrice !== "" &&
            location !== "" &&
            photos !== null &&
            document !== null
        );
    };

    const handleSubmit = (e) => {
        e.preventDefault();

        console.log(authenticatedUser.token)

        const postEstate = async () => {
            const body = {
                agent: selectedAgent,
                type: type,
                bathrooms: bathrooms,
                rooms: rooms,
                garage: garage, 
                storey: storey,
                location: location,
                balcony: balcony,
                description: description,
                availability: availability,
                size: size,
                condition: condition,
                offeredPrice: offeredPrice
            };
    
            const response = await fetch('/api/owner/report-offer', {
                method: "POST",
                headers: {
                    "Authorization": "Bearer " + authenticatedUser.token,
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(body)
            });

            if (!response.ok) {
                console.log("Failed to post the offer");
                return;
            }

            const data = await response.json();

            console.log(data);

            photos.forEach((photo) => {
                const postPhoto = async () => {
                    const formData = new FormData();
                    formData.append('file', photo);
                    formData.append('id', data);

                    const response = await fetch(`/api/owner/upload-photo`, {
                        method: "POST",
                        body: formData,
                        headers: {
                            "Accept": "application/json",
                            "Authorization": "Bearer " + authenticatedUser.token
                        }
                    });


                    if (!response.ok) {
                        console.log("Failed to post photos");
                        return;
                    }
                }

                postPhoto();
            });

            const postDocument = async () => {
                const formData = new FormData();
                formData.append('file', document);
                formData.append('id', data);

                const response = await fetch(`/api/owner/upload-document`, {
                    method: "POST",
                    body: formData,
                    headers: {
                        "Accept": "application/json",
                        "Authorization": "Bearer " + authenticatedUser.token
                    }
                });

                if (!response.ok) {
                    console.log("Failed to post the document");
                    return;
                }
            }

            postDocument();
        };

        postEstate();
    };

    const handleDelete = (indexToDelete) => {
        setPhotos(prevPhotos => prevPhotos.filter((_, index) => index !== indexToDelete));
    };

    return (
        <div className="report-estate">
            <h2 className="lead">Report your estate</h2>
            <h6>Fill out the form and our top agents will review your proposal</h6>
            <hr></hr>
            <form>
                <p>Agent</p>
                <div>
                    {agents.map((agent) => (
                        <div key={agent.id}>
                            <input className="form-check-input" type='radio' name="agent" onChange={() => setSelectedAgent(agent.id)}></input>
                            <label>{agent.fullName}</label>
                        </div>
                    ))}
                </div>

                <label>Type</label>
                <div className="type">
                    <div>
                        <input className="form-check-input" type='radio' name='type' value="APARTMENT" onChange={(e) => setType(e.target.value)}></input>
                        <label>Apartment</label> 
                    </div>
                    <div>
                        <input className="form-check-input" type='radio' name='type' value="BUNGALOW" onChange={(e) => setType(e.target.value)}></input>
                        <label>Bungalow</label> 
                    </div>
                    <div>
                        <input className="form-check-input" type='radio' name='type' value="COTTAGE" onChange={(e) => setType(e.target.value)}></input>
                        <label>Cottage</label> 
                    </div>
                    <div>
                        <input className="form-check-input" type='radio' name='type' value="DUPLEX" onChange={(e) => setType(e.target.value)}></input>
                        <label>Duplex</label> 
                    </div>
                    <div>
                        <input className="form-check-input" type='radio' name='type' value="SINGLE_FAMILY_HOUSE" onChange={(e) => setType(e.target.value)}></input>
                        <label>Single family house</label> 
                    </div>
                    <div>
                        <input className="form-check-input" type='radio' name='type' value="MANSION" onChange={(e) => setType(e.target.value)}></input>
                        <label>Mansion</label> 
                    </div>
                </div>

                <label>Availability</label>
                <div className="availability">
                    <div>
                        <input className="form-check-input" type='radio' name='availability' value="FOR_SALE" onChange={(e) => setAvailability(e.target.value)}></input>
                        <label>For sale</label> 
                    </div>
                    <div>
                        <input className="form-check-input" type='radio' name='availability' value="FOR_RENT" onChange={(e) => setAvailability(e.target.value)}></input>
                        <label>For rent</label> 
                    </div>
                </div>

                <label>Condition</label>
                <div className="condition">
                    <div>
                        <input className="form-check-input" type='radio' name='condition' value="NEEDS_RENOVATION" onChange={(e) => setCondition(e.target.value)}></input>
                        <label>Needs renovation</label> 
                    </div>
                    <div>
                        <input className="form-check-input" type='radio' name='condition' value="DEVELOPER_CONDITION" onChange={(e) => setCondition(e.target.value)}></input>
                        <label>Developer condition</label> 
                    </div>
                    <div>
                        <input className="form-check-input" type='radio' name='condition' value="AFTER_RENOVATION" onChange={(e) => setCondition(e.target.value)}></input>
                        <label>After renovation</label> 
                    </div>
                    <div>
                        <input className="form-check-input" type='radio' name='condition' value="NORMAL_USE_SIGNS" onChange={(e) => setCondition(e.target.value)}></input>
                        <label>Normal use signs</label> 
                    </div>
                </div>
            </form>
            <form>
            <label>Garage</label>
                <input type='checkbox' onChange={(e) => setGarage(e.target.checked ? true : false)}></input>

                <label>Balcony</label>
                <input type='checkbox' onChange={(e) => setBalcony(e.target.checked ? true : false)}></input>

                <label>Location</label>
                <input type='text' onChange={(e) => setLocation(e.target.value)}></input>

                <label>Bathrooms</label>
                <input type='number' min='1' onChange={(e) => setBathrooms(e.target.value)}></input>

                <label>Rooms</label>
                <input type='number' min='1' onChange={(e) => setRooms(e.target.value)}></input>

                <label>Storey</label>
                <input type='number' min='1' onChange={(e) => setStorey(e.target.value)}></input>

                <label>Size</label>
                <input type='number' min='1' step='10' onChange={(e) => setSize(e.target.value)}></input>

                <label>Description</label>
                <input type='text' minlength='20' onChange={(e) => setDescription(e.target.value)}></input>

                <label>Offered price</label>
                <input type="number" min='1' step='1000' onChange={(e) => setOfferedPrice(e.target.value)}></input>
            </form>
            <div className="files">
                
                    <>
                        <p>Attach necessary documents</p>
                        <input type="file" className="file" id="document" name="document" accept=".pdf" onChange={(e) => {
                            setDocument(e.target.files[0]);
                            setIsDocumentAdded(true);
                        }}/>
                        <label for="document">Select document</label>
                    </>
                    {isDocumentAdded && (
                        <div className='added-file'>
                            {document.name}
                            <button className="btn btn-dark" onClick={() => setDocument(null)}>x</button>
                        </div>    
                    )}
                    <>
                        <p>Attach the estate photos</p>
                        <input type="file" className="file" id="photo" name="photo" accept="image/png, image/jpg" onChange={(e) => {
                            setPhotos((prev) => [...prev, e.target.files[0]]);
                            setIsPhotoAdded(true);
                        }}/>
                        <label for="photo">Select photo</label>
                    </>
                    {isPhotoAdded && (
                        photos.map((photo, index) => (
                        <div key={index} className='added-file'>
                            {photo.name}
                            <button className="btn btn-dark" onClick={() => handleDelete(index)}>x</button>
                        </div>
                        ))
                    )}
             
                  
            </div>
            <button className="btn btn-dark submit-btn" disabled={!isFormFilled()} onClick={handleSubmit}>Submit</button>
            
        </div>
    );
}

export default ReportEstate;