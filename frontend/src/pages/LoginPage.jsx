import {Sprout, ArrowUp, Mail, Lock, EyeOff, Eye} from "lucide-react";
import React, {useState} from 'react';
import WelcomeScreen from "/src/pages/WelcomeScreen.jsx";
import AuthScreen from "/src/pages/AuthScreen.jsx";
import LoginFormScreen from "/src/pages/LoginFormScreen.jsx";

const SCREEN = { WELCOME: "welcome", AUTH: "auth", LOGIN: "login"}

const LoginPage = () => {
  const [currentScreen, setCurrentScreen] = useState('welcome'); // 'welcome', 'auth', 'login'
  const [showPassword, setShowPassword] = useState(false);
  const [formData, setFormData] = useState({email: "", password: ""});
  const [isLoading, setIsLoading] = useState(false);

  const goWelcome = () => setCurrentScreen(SCREEN.WELCOME);
  const goAuth = () => setCurrentScreen(SCREEN.AUTH);
  const goLogin = () => setCurrentScreen(SCREEN.LOGIN);

  const handleInputChange = (e) => {
    const {name, value} = e.target;
    setFormData((prev) => ({...prev, [name]: value}));
  };
  const handleTogglePassword = () => setShowPassword((v) => !v);

  const handleLogin = async () => {
    setIsLoading(true);
    try {
      // TODO: 실제 로그인 API 호출
      console.log("Login attempted with:", formData);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="relative min-h-screen overflow-hidden">
      {currentScreen == SCREEN.WELCOME && (
        <WelcomeScreen
          currentScreen={currentScreen}
          onNavigate={(next) => setCurrentScreen(next)}/>
      )}
      {currentScreen == SCREEN.AUTH && (
        <AuthScreen
          currentScreen={currentScreen}
          onNavigateBack={() => setCurrentScreen("welcome")}
          onLogin={() => setCurrentScreen("login")}
        />
      )}
      {currentScreen == SCREEN.LOGIN && (
        <LoginFormScreen
          currentScreen={currentScreen}
          onNavigateBack={() => setCurrentScreen("auth")}
          formData={formData}
          onChange={handleInputChange}
          showPassword={showPassword}
          onTogglePassword={handleTogglePassword}
          onSubmit={handleLogin}
          isLoading={isLoading}
        />
      )}
    </div>
  );
};

export default LoginPage;