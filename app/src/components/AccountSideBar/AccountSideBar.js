import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { useAuth } from "../AuthContext";
import "./AccountSideBar.css";

function AccountSideBar() {
    const [role, setRole] = useState("");
    const { authenticatedUser } = useAuth();

    useEffect(() => {
        const fetchDetails = async () => {
            const response = await fetch('/api/user-details', {
            method: "GET",
            headers: {
                "Authorization": "Bearer " + authenticatedUser.token
            }
        });
            if (response.ok) {
                const data = await response.json();
                setRole(data.role);
            } else {
                console.error('Failed to fetch user details:', response.statusText);
            }
        }

        fetchDetails();

    }, [authenticatedUser]);

    return (
        <div className="sidebar"> 
            <ul>
                {role === "CUSTOMER" && (
                    <>
                        <li><Link path='/liked-offers'>Favorites</Link></li>
                        <li><Link path='/scheduled-meetings'>Meetings</Link></li>
                        <li><Link path='/review-offer'>Review</Link></li>
                    </>
                )}
                {role === "OWNER" && (
                    <>
                        <li><Link to='/my-estates'>My estates</Link></li>
                        <li><Link to='/report-estate'>Report</Link></li>
                    </>
                )}
                {role === "AGENT" && (
                    <>
                        <li><Link to='/add-slots'>Add</Link></li>
                        <li><Link to='/reported-offers'>Reported</Link></li>
                        <li><Link to='/manage-offers'>Manage</Link></li>
                    </>
                )}
                {role === "ADMIN" && (
                    <div>
                    </div>
                )}
                {role === "CUSTOMER" || role === "OWNER" && (
                    <>
                    <li><Link path='/schedule-meeting'>Meetings</Link></li>
                    </>
                )}
                <li><Link path='settings'>Settings</Link></li>
            </ul>
            

        </div>
    );
}

export default AccountSideBar;
