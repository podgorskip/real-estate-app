import JSZip from "jszip";

const PhotosFetcher = async (id) => {
    const response = await fetch(`/api/auth/photos?id=${id}`, {
        method: "GET"
    });

    if (!response.ok) {
        console.log("Failed to fetch photos");
        throw new Error("Failed to fetch photos");
    }

    const zipFileData = await response.arrayBuffer();
    const zip = await JSZip.loadAsync(zipFileData);

    const photos = [];

    zip.forEach((_, entry) => {
        if (entry.dir) return;
        
        const photoData = entry.async("blob").then(blob => URL.createObjectURL(blob));
        photos.push(photoData);
    });

    return Promise.all(photos);
};

export default PhotosFetcher;