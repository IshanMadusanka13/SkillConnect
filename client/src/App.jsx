import { Routes, Route } from "react-router-dom";
import Login from "./pages/Login";
import Home from "./pages/Home";
import AdminDashboard from "./pages/Admin";
import Navbar from "./components/Navbar";
import ProtectedRoute from "./components/ProtectedRoute";
import { ToastProvider } from "./components/ToastProvider";


const App = () => {
    return (
        <div className="pt-20">
            <ToastProvider>
                <Navbar />
                <Routes>
                    {/* Public Routes */}
                    <Route path="/" element={<Login />} />

                    {/* Protected Routes */}
                    <Route element={<ProtectedRoute allowedRoles={["Admin", "User"]} />}>
                        <Route path="/home" element={<Home />} />
                        
                    </Route>

                    {/* Role-Specific Routes */}
                    <Route element={<ProtectedRoute allowedRoles={["Admin"]} />}>
                        <Route path="/admin" element={<AdminDashboard />} />
                    </Route>

                    

                    

                    
                </Routes>
            </ToastProvider>
        </div>
    );
};

export default App;
