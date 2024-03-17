import './HomePage.css';
import logo from '../logo.png';

function HomePage() {
    return (
        <div className="homepage">
            <h1 className="lead">Welcome to Noble Nest!</h1>
            <hr></hr>
            <img src={logo} width={400} height={400}></img>
            <div>
                <h4 className='lead'>About us</h4>
                <hr></hr>
                <p>
                    At Noble Nest Real Estate Agency, we believe in the transformative power of 
                    finding the perfect home. Founded on principles of integrity, professionalism, 
                    and personalized service, we are committed to helping our clients navigate the 
                    intricate world of real estate with confidence and ease.
                </p>
                <h4 className='lead'>Our mission</h4>
                <hr></hr>
                <p>Our mission at Noble Nest is simple yet profound: to provide unparalleled 
                    service and guidance throughout every step of your real estate journey. 
                    Whether you're buying, selling, or renting a property, our dedicated team 
                    of experts is here to ensure that your experience is not only successful 
                    but also enjoyable.
                </p>
            </div>
            <hr></hr>
            <h2 className='lead'>Why choose Noble Nest?</h2>
            <ul>
                <li>
                    <div className='card card-body'>
                        <h5>Expertise & Experiences</h5>
                        <hr/>
                        <p>
                            Backed by years of experience and a deep understanding of the local market, 
                            our team of seasoned professionals possesses the knowledge, skills, and 
                            insights needed to guide you towards informed decisions. 
                            Whether you're searching for your dream home, negotiating the best deal, 
                            or marketing your property effectively, you can trust Noble Nest to deliver 
                            exceptional results.
                        </p>
                    </div>
                </li>
                <li>
                    <div className='card card-body'>
                        <h5>Client-Centric Approach</h5>
                        <hr/>
                        <p>
                            At Noble Nest, our clients always come first. We prioritize building meaningful 
                            relationships based on trust, respect, and open communication. From the initial 
                            consultation to the final closing, we remain committed to providing personalized 
                            attention, expert guidance, and unwavering support every step of the way.
                        </p>
                    </div>
                </li>
                <li>
                    <div className='card card-body'>
                        <h5>Community Involvement</h5>
                        <hr/>
                        <p>
                            As members of the community ourselves, we take great pride in giving back 
                            and making a positive impact wherever we can. Whether through charitable 
                            initiatives, volunteer work, or supporting local businesses, Noble Nest 
                            is dedicated to fostering a sense of belonging and prosperity within our 
                            neighborhoods.
                        </p>
                    </div>
                </li>
            </ul>
        </div>        
    );
}

export default HomePage;