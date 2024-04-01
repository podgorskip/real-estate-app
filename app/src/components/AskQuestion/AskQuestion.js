import { useState } from 'react';
import { useAuth } from '../AuthContext';
import './AskQuestion.css';
import { useParams } from 'react-router-dom';

function AskQuestion() {
    const { authenticatedUser } = useAuth();
    const [question, setQuestion] = useState("");
    const { id } = useParams();

    const submitQuestion = () => {
        if (authenticatedUser.token === '') {
            alert("You have to sign in first");
            return;
        };

        // const submit = async () => {
        //     // TO-DO
        //     const response = await fetch(`/api/`, {
        //         method: "POST",
        //     });

        // }

    };

    return (
        <div className='ask-question'>
            <h2>Have a Question?</h2>
            <p>Feel free to ask us anything about our services, properties, or anything else related to real estate!</p>
            <hr></hr>
            <input type='text' placeholder='Ask a question...' onChange={(e) => setQuestion(e.target.value)}></input>
            <button className='btn btn-dark' disabled={question.length === 0}>Submit</button>
        </div>
    );
}

export default AskQuestion;