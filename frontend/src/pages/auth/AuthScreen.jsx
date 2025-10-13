import { TrendingUp, Users, Target, Zap } from "lucide-react";
import { useOAuthLogin } from "/src/hooks/oauth/useOAuthLogin.jsx";

const PROVIDER = { KAKAO: "kakao", NAVER: "naver", GOOGLE: "google" };

const AuthScreen = ({ currentScreen, onNavigateBack, onLogin }) => {
  const { loginWithProvider, loading, error } = useOAuthLogin();

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
        <div className="mb-6">
          <div className="relative w-24 h-24">
            <div className="absolute inset-0 bg-gradient-to-r from-green-400 to-blue-500 rounded-full animate-spin opacity-20" />
            <div className="absolute inset-3 bg-gradient-to-r from-blue-500 to-purple-500 rounded-full animate-pulse" />
            <div className="absolute inset-6 bg-white rounded-full flex items-center justify-center">
              <TrendingUp className="w-6 h-6 text-green-500" />
            </div>
          </div>
        </div>

        {/* Main Headline */}
        <div className="text-center mb-8">
          <h2 className="text-4xl font-bold text-gray-900 mb-3 leading-tight">
            당신의 성장,{" "}
            <span className="bg-gradient-to-r from-green-500 to-blue-500 bg-clip-text text-transparent">
              오늘부터
            </span>
          </h2>
          <p className="text-gray-600 text-lg mb-4">
            1분 만에 시작하는 나만의 성장 여정
          </p>

          {/* Social Proof */}
          <div className="flex items-center justify-center gap-2 text-sm text-gray-500">
            <Users className="w-4 h-4 text-green-500" />
            <span>
              이미 <strong className="text-green-600">1,000명</strong>이 성장 중
            </span>
          </div>
        </div>

        {/* Benefit Cards */}
        <div className="grid grid-cols-3 gap-3 mb-8 w-full max-w-sm">
          <div className="text-center p-3 bg-white/60 rounded-xl backdrop-blur-sm">
            <div className="w-10 h-10 bg-green-100 rounded-full mx-auto mb-2 flex items-center justify-center">
              <Target className="w-5 h-5 text-green-600" />
            </div>
            <p className="text-xs font-medium text-gray-700">목표 설정</p>
          </div>
          <div className="text-center p-3 bg-white/60 rounded-xl backdrop-blur-sm">
            <div className="w-10 h-10 bg-blue-100 rounded-full mx-auto mb-2 flex items-center justify-center">
              <TrendingUp className="w-5 h-5 text-blue-600" />
            </div>
            <p className="text-xs font-medium text-gray-700">성장 추적</p>
          </div>
          <div className="text-center p-3 bg-white/60 rounded-xl backdrop-blur-sm">
            <div className="w-10 h-10 bg-purple-100 rounded-full mx-auto mb-2 flex items-center justify-center">
              <Zap className="w-5 h-5 text-purple-600" />
            </div>
            <p className="text-xs font-medium text-gray-700">습관 형성</p>
          </div>
        </div>

        {/* Social Login Buttons */}
        <div className="w-full max-w-sm space-y-3 mb-6">
          {/* Kakao */}
          <button
            aria-label="카카오로 로그인"
            className="w-full h-14 rounded-2xl bg-[#FEE500] flex items-center justify-center gap-3 px-6 shadow-md hover:shadow-lg transition-all hover:scale-[1.02] disabled:opacity-50 disabled:cursor-not-allowed"
            onClick={() => {
              loginWithProvider(PROVIDER.KAKAO);
            }}
            disabled={loading}
          >
            <svg width="24" height="24" viewBox="0 0 24 24" fill="#3C1E1E" aria-hidden="true">
              <path d="M12 3C6.48 3 2 6.76 2 11.14c0 2.7 1.73 5.07 4.36 6.53l-.86 3.2a.6.6 0 0 0 .9.67l3.56-2.23c.67.1 1.35.15 2.04.15 5.52 0 10-3.76 10-8.14S17.52 3 12 3z" />
            </svg>
            <span className="text-base font-semibold text-[#3C1E1E]">
              {loading ? "로그인 중..." : "3초만에 카카오로 시작"}
            </span>
          </button>

          {/* Naver - 주석 처리된 코드 유지
          <button
            aria-label="네이버로 로그인"
            className="w-full h-14 rounded-2xl bg-[#03C75A] flex items-center justify-center gap-3 px-6 shadow-md hover:shadow-lg transition-all hover:scale-[1.02]"
            onClick={() => {
              loginWithProvider(PROVIDER.NAVER);
            }}
          >
            <svg width="24" height="24" viewBox="0 0 24 24" fill="#FFFFFF" aria-hidden="true">
              <path d="M5 4h5.6l2.9 4.2V4H19v16h-5.6L10.5 15.8V20H5V4z" />
            </svg>
            <span className="text-base font-semibold text-white">네이버로 빠르게 시작</span>
          </button>
          */}

          {/* Google - 주석 처리된 코드 유지
          <button
            aria-label="구글로 로그인"
            className="w-full h-14 rounded-2xl bg-white flex items-center justify-center gap-3 px-6 border-2 border-gray-200 shadow-md hover:shadow-lg transition-all hover:scale-[1.02]"
            onClick={() => {
              loginWithProvider(PROVIDER.GOOGLE);
            }}
          >
            <svg width="24" height="24" viewBox="0 0 48 48" aria-hidden="true">
              <path fill="#FFC107" d="M43.6 20.5H42V20H24v8h11.3C33.8 32.6 29.3 36 24 36c-6.6 0-12-5.4-12-12s5.4-12 12-12c3 0 5.7 1.1 7.7 3l5.7-5.7C34.5 6.1 29.5 4 24 4 12.9 4 4 12.9 4 24s8.9 20 20 20c10 0 19-7.3 19-20 0-1.2-.1-2.3-.4-3.5z" />
              <path fill="#FF3D00" d="M6.3 14.7l6.6 4.8C14.8 16 19 13.9 24 13.9c3 0 5.7 1.1 7.7 3l5.7-5.7C34.5 6.1 29.5 4 24 4 16 4 9.2 8.5 6.3 14.7z" />
              <path fill="#4CAF50" d="M24 44c5.2 0 10-1.9 13.6-5.2l-6.3-5.2C29.4 35.2 26.9 36 24 36c-5.3 0-9.8-3.4-11.4-8.1l-6.6 5.1C9 39.4 16 44 24 44z" />
              <path fill="#1976D2" d="M43.6 20.5H42V20H24v8h11.3c-1 3.2-3.5 5.9-6.7 7.1l6.3 5.2C37.7 38.3 40 32.7 40 28c0-2.6-.5-4.8-1.4-7.5z" />
            </svg>
            <span className="text-base font-semibold text-gray-800">구글로 빠르게 시작</span>
          </button>
          */}
        </div>

        {/* Error Message */}
        {error && (
          <div className="mb-4 p-3 bg-red-50 border border-red-200 rounded-xl w-full max-w-sm">
            <p className="text-sm text-red-600 text-center">
              로그인에 실패했어요. 잠시 후 다시 시도해 주세요.
            </p>
          </div>
        )}

        {/* Trust Elements */}
        <div className="text-center space-y-2">
          <p className="text-xs text-gray-400">
            회원가입 없이 바로 시작 • 언제든 탈퇴 가능
          </p>
          <p className="text-xs text-gray-400">
            안전한 로그인 • 개인정보 보호
          </p>
        </div>

        {/* Traditional Login - 주석 처리된 코드 유지
        <div className="text-center space-y-4 mt-8">
          <div className="flex items-center space-x-4">
            <div className="flex-1 h-px bg-gray-300" />
            <span className="text-gray-500 text-sm">or</span>
            <div className="flex-1 h-px bg-gray-300" />
          </div>

          <button
            onClick={onLogin}
            className="w-full max-w-sm py-3 bg-gradient-to-r from-green-500 to-blue-500 text-white font-semibold rounded-xl hover:shadow-lg transition-all duration-300 hover:scale-105"
          >
            Sign In with Email
          </button>

          <p className="text-gray-500 text-sm">
            Don't have an account?{" "}
            <button
              className="text-green-500 hover:text-green-600 font-medium"
              onClick={onNavigateBack}
            >
              Sign up and start growing
            </button>
          </p>
        </div>
        */}
      </div>
    </div>
  );
};

export default AuthScreen;