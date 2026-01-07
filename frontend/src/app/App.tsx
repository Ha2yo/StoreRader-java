import { useNavigate } from "react-router-dom";

function App() {
    const navigate = useNavigate();

    navigate("/home");

    return (
        <div className='container'>
        </div>
    );
}

export default App;