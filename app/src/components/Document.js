import { useAuth } from './AuthContext';
import { useState } from 'react';

function Document() {
    const { authenticatedUser } = useAuth();
    const [file, setFile] = useState(null);

    const handleFileChosen = (e) => {
        setFile(e.target.files[0]);
    }

    const handleSubmit = async (e) => {
        e.preventDefault();
    
        const formData = new FormData();
        formData.append('file', file)
        formData.append('id', 2);
    
        const url = `/api/owner/add-document`;
    
        const response = await fetch(url, {
            method: "POST",
            headers: {
                "Authorization": "Bearer " + authenticatedUser.token
            },
            body: formData
        });
    
        if (!response.ok) {
            console.log("Failed to upload file");
        }

        console.log("Successfully posted file")
    }

    return (
        <div>
            <form onSubmit={handleSubmit}>
                <input type="file" onChange={handleFileChosen}></input>
            
                <button type="submit">Upload</button>
            </form>
        </div>        
    )
}

export default Document;
