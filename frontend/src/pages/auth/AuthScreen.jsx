import { TrendingUp } from "lucide-react";
import {useOAuthLogin} from "/src/hooks/oauth/useOAuthLogin.jsx";

const PROVIDER = {KAKAO: "kakao", NAVER: "naver", GOOGLE: "google"}
const AuthScreen = ({currentScreen, onNavigateBack, onLogin}) => {

  const {loginWithProvider, loading, error} = useOAuthLogin();

  return (
    <div
      className={`absolute inset-0 bg-gradient-to-b from-blue-50 via-purple-50 to-green-50 transition-transform duration-500 ${
        currentScreen === "auth"
          ? "translate-y-0"
          : currentScreen === "welcome"
            ? "translate-y-full"
            : "-translate-y-full"
      }`}
    >
      <div className="flex flex-col items-center justify-center min-h-screen px-6">
        {/* Growth Animation */}
        <div className="mb-8">
          <div className="relative w-32 h-32">
            <div
              className="absolute inset-0 bg-gradient-to-r from-green-400 to-blue-500 rounded-full animate-spin opacity-20"/>
            <div className="absolute inset-4 bg-gradient-to-r from-blue-500 to-purple-500 rounded-full animate-pulse"/>
            <div className="absolute inset-8 bg-white rounded-full flex items-center justify-center">
              <TrendingUp className="w-8 h-8 text-green-500"/>
            </div>
          </div>
        </div>

        <div className="text-center mb-12">
          <h2 className="text-3xl font-bold text-gray-800 mb-2">성장할 준비 되셨나요??</h2>
          <p className="text-gray-600">Choose how you'd like to continue</p>
        </div>

        {/* Social Login Buttons (Brand-styled) */}
        <div className="w-full max-w-sm space-y-3 mb-8">
          {/* Kakao */}
          <button
            aria-label="카카오로 로그인"
            className="w-full h-12 rounded-full bg-[#FEE500] flex items-center gap-3 px-4 shadow-sm hover:shadow-md transition-all"
            onClick={() => {
              loginWithProvider(PROVIDER.KAKAO)
            }}
          >
            <svg width="22" height="22" viewBox="0 0 24 24" fill="#3C1E1E" aria-hidden="true">
              <path
                d="M12 3C6.48 3 2 6.76 2 11.14c0 2.7 1.73 5.07 4.36 6.53l-.86 3.2a.6.6 0 0 0 .9.67l3.56-2.23c.67.1 1.35.15 2.04.15 5.52 0 10-3.76 10-8.14S17.52 3 12 3z"/>
            </svg>
            <span className="text-[15px] font-semibold text-[#3C1E1E]">카카오 로그인</span>
          </button>

          {/* Naver */}
          <button
            aria-label="네이버로 로그인"
            className="w-full h-12 rounded-full bg-[#03C75A] flex items-center gap-3 px-4 shadow-sm hover:shadow-md transition-all"
            onClick={() => {
              loginWithProvider(PROVIDER.NAVER)
            }}
          >
            <svg width="22" height="22" viewBox="0 0 24 24" fill="#FFFFFF" aria-hidden="true">
              <path d="M5 4h5.6l2.9 4.2V4H19v16h-5.6L10.5 15.8V20H5V4z"/>
            </svg>
            <span className="text-[15px] font-semibold text-white">네이버 로그인</span>
          </button>

          {/* Google */}
          <button
            aria-label="구글로 로그인"
            className="w-full h-12 rounded-full bg-[#F2F2F2] flex items-center gap-3 px-4
               border border-[#E6E6E6] shadow-sm hover:shadow-md transition-all"
            onClick={() => {
              loginWithProvider(PROVIDER.GOOGLE)
            }}
          >
            {/* Google G */}
            <svg width="22" height="22" viewBox="0 0 48 48" aria-hidden="true">
              <path fill="#FFC107"
                    d="M43.6 20.5H42V20H24v8h11.3C33.8 32.6 29.3 36 24 36c-6.6 0-12-5.4-12-12s5.4-12 12-12c3 0 5.7 1.1 7.7 3l5.7-5.7C34.5 6.1 29.5 4 24 4 12.9 4 4 12.9 4 24s8.9 20 20 20c10 0 19-7.3 19-20 0-1.2-.1-2.3-.4-3.5z"/>
              <path fill="#FF3D00"
                    d="M6.3 14.7l6.6 4.8C14.8 16 19 13.9 24 13.9c3 0 5.7 1.1 7.7 3l5.7-5.7C34.5 6.1 29.5 4 24 4 16 4 9.2 8.5 6.3 14.7z"/>
              <path fill="#4CAF50"
                    d="M24 44c5.2 0 10-1.9 13.6-5.2l-6.3-5.2C29.4 35.2 26.9 36 24 36c-5.3 0-9.8-3.4-11.4-8.1l-6.6 5.1C9 39.4 16 44 24 44z"/>
              <path fill="#1976D2"
                    d="M43.6 20.5H42V20H24v8h11.3c-1 3.2-3.5 5.9-6.7 7.1l6.3 5.2C37.7 38.3 40 32.7 40 28c0-2.6-.5-4.8-1.4-7.5z"/>
            </svg>
            <span className="text-[15px] font-semibold text-[#222]">구글 로그인</span>
          </button>
        </div>

        {/* 에러 메시지 */}
        {error && (
          <p className="text-sm text-red-600 mb-4">
            로그인에 실패했어요. 잠시 후 다시 시도해 주세요.
          </p>
        )}

        {/* Traditional Login */}
        <div className="text-center space-y-4">
          <div className="flex items-center space-x-4">
            <div className="flex-1 h-px bg-gray-300"/>
            <span className="text-gray-500 text-sm">or</span>
            <div className="flex-1 h-px bg-gray-300"/>
          </div>

          <button
            onClick={onLogin}
            className="w-full max-w-sm py-3 bg-gradient-to-r from-green-500 to-blue-500 text-white font-semibold rounded-xl hover:shadow-lg transition-all duration-300 hover:scale-105"
          >
            Sign In with Email
          </button>

          <p className="text-gray-500 text-sm">
            Don't have an account?{" "}
            <button className="text-green-500 hover:text-green-600 font-medium" onClick={onNavigateBack}>
              Sign up and start growing
            </button>
          </p>
        </div>
      </div>
    </div>
  );
};

export default AuthScreen;
