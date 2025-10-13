import { Sprout, TrendingUp, Target, Sparkles } from "lucide-react";

const WelcomeScreen = ({ currentScreen, onNavigate }) => {
  const handleScreenTransition = (screen) => {
    onNavigate(screen);
  };

  return (
    <div
      className={`absolute inset-0 bg-gradient-to-b from-green-50 via-blue-50 to-purple-50 transition-transform duration-500 ${
        currentScreen === 'welcome' ? 'translate-y-0' : '-translate-y-full'
      }`}
    >
      <div className="flex flex-col items-center justify-center min-h-screen px-6 py-8">
        {/* Subtle Floating Icons */}
        <div className="absolute top-20 left-8 animate-bounce opacity-40">
          <div className="w-10 h-10 bg-green-200 rounded-full flex items-center justify-center">
            <Sprout className="w-5 h-5 text-green-600" />
          </div>
        </div>
        <div className="absolute top-32 right-12 animate-pulse opacity-40">
          <div className="w-8 h-8 bg-purple-200 rounded-full flex items-center justify-center">
            <Target className="w-4 h-4 text-purple-600" />
          </div>
        </div>

        {/* Main Logo */}
        <div className="mb-6">
          <div className="relative w-24 h-24 mx-auto">
            <div className="absolute inset-0 bg-gradient-to-r from-green-400 via-blue-500 to-purple-600 rounded-full animate-pulse shadow-2xl" />
            <div className="absolute inset-2 bg-white rounded-full flex items-center justify-center">
              <Sprout className="w-10 h-10 text-green-500" />
            </div>
            <div className="absolute -top-1 -right-1">
              <Sparkles className="w-5 h-5 text-yellow-500 animate-pulse" />
            </div>
          </div>
        </div>

        {/* Brand Name */}
        <div className="text-center mb-8">
          <h1 className="text-4xl font-bold text-gray-900 mb-2 tracking-tight">
            Daily<span className="bg-gradient-to-r from-green-500 via-blue-500 to-purple-600 bg-clip-text text-transparent">Growth</span>
          </h1>
          <p className="text-sm text-gray-500 font-medium">매일 성장하는 나</p>
        </div>

        {/* Main Hook Message - 압축 */}
        <div className="text-center mb-8 max-w-xs">
          <p className="text-xl font-semibold text-gray-800 mb-2 leading-snug">
            작은 습관이<br />
            <span className="bg-gradient-to-r from-green-500 to-blue-500 bg-clip-text text-transparent">
              큰 변화
            </span>를 만듭니다
          </p>
          <p className="text-sm text-gray-600">
            매일 1%씩, 1년 후 <strong className="text-green-600">37배</strong> 성장
          </p>
        </div>

        {/* Growth Visualization - 작게 */}
        <div className="flex items-end space-x-2 mb-8">
          {[25, 35, 50, 65, 80].map((height, index) => (
            <div key={index} className="relative">
              <div
                className="bg-gradient-to-t from-green-500 via-blue-400 to-purple-400 rounded-md shadow-sm"
                style={{
                  width: '14px',
                  height: `${height}px`,
                }}
              />
              {index === 4 && (
                <TrendingUp className="absolute -top-6 left-1/2 -translate-x-1/2 w-4 h-4 text-green-500 animate-bounce" />
              )}
            </div>
          ))}
        </div>

        {/* Feature Pills - 간소화 */}
        <div className="flex gap-2 mb-8">
          <div className="px-3 py-1.5 bg-white/70 backdrop-blur-sm rounded-full text-xs text-gray-700 shadow-sm">
            📊 목표 달성
          </div>
          <div className="px-3 py-1.5 bg-white/70 backdrop-blur-sm rounded-full text-xs text-gray-700 shadow-sm">
            ✅ 습관 형성
          </div>
          <div className="px-3 py-1.5 bg-white/70 backdrop-blur-sm rounded-full text-xs text-gray-700 shadow-sm">
            📈 성장 기록
          </div>
        </div>

        {/* CTA Button */}
        <button
          onClick={() => handleScreenTransition('auth')}
          className="w-full max-w-xs py-3.5 bg-gradient-to-r from-green-500 via-blue-500 to-purple-600 text-white text-base font-semibold rounded-2xl shadow-lg hover:shadow-xl transform hover:scale-105 transition-all duration-300 mb-3"
        >
          지금 바로 시작하기
        </button>

        {/* Trust Elements */}
        <p className="text-xs text-gray-500">
          무료 • 30초 가입 • 언제든 탈퇴 가능
        </p>
      </div>
    </div>
  );
};

export default WelcomeScreen;