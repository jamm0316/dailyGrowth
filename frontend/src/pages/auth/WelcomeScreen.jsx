import { Sprout, TrendingUp, Target } from "lucide-react";

const WelcomeScreen = ({currentScreen, onNavigate}) => {

  const handleScreenTransition = (screen) => {
    onNavigate(screen);
  };

  return (
    <div
      className={`absolute inset-0 bg-gradient-to-b from-green-50 via-blue-50 to-purple-50 transition-transform duration-500 ${
        currentScreen === 'welcome' ? 'translate-y-0' : '-translate-y-full'
      }`}>
      <div className="flex flex-col items-center justify-center min-h-screen px-6">
        {/* Floating Growth Icons */}
        <div className="absolute top-20 left-10 animate-bounce">
          <div className="w-8 h-8 bg-green-200 rounded-full flex items-center justify-center">
            <Sprout className="w-4 h-4 text-green-600"/>
          </div>
        </div>
        <div className="absolute top-32 right-16 animate-pulse">
          <div className="w-6 h-6 bg-blue-200 rounded-full flex items-center justify-center">
            <TrendingUp className="w-3 h-3 text-blue-600"/>
          </div>
        </div>
        <div className="absolute top-48 right-8 animate-bounce delay-500">
          <div className="w-10 h-10 bg-purple-200 rounded-full flex items-center justify-center">
            <Target className="w-5 h-5 text-purple-600"/>
          </div>
        </div>

        {/* Main Content */}
        <div className="text-center mb-12">
          <div
            className="w-24 h-24 bg-gradient-to-r from-green-400 via-blue-500 to-purple-600 rounded-full flex items-center justify-center mb-8 mx-auto shadow-2xl animate-pulse">
            <Sprout className="w-12 h-12 text-white"/>
          </div>
          <h1 className="text-4xl font-bold text-gray-800 mb-4">
            Daily<span className="text-green-500">Growth</span>
          </h1>
          <p className="text-gray-600 text-lg leading-relaxed">
            Every day is a new opportunity<br/>
            to grow and achieve your goals
          </p>
        </div>

        {/* Animated Growth Visualization */}
        <div className="flex items-end space-x-2 mb-16">
          {[1, 2, 3, 4, 5].map((item) => (
            <div
              key={item}
              className={`bg-gradient-to-t from-green-400 to-green-300 rounded-t-lg animate-pulse`}
              style={{
                width: '20px',
                height: `${item * 20}px`,
                animationDelay: `${item * 200}ms`
              }}
            />
          ))}
        </div>

        <button
          onClick={() => handleScreenTransition('auth')}
          className="w-64 py-4 bg-gradient-to-r from-green-500 via-blue-500 to-purple-600 text-white font-semibold rounded-full shadow-lg hover:shadow-xl transform hover:scale-105 transition-all duration-300"
        >
          함께 성장 시작하기 🚀
        </button>
      </div>
    </div>
  );
};

export default WelcomeScreen;