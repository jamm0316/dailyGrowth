import {Sprout, ArrowUp, Mail, Lock, EyeOff, Eye, ArrowRight} from "lucide-react";
import React from "react";

const LoginFormScreen = ({
                           currentScreen,
                           onNavigateBack,
                           formData,
                           onChange,
                           showPassword,
                           onTogglePassword,
                           onSubmit,
                           isLoading,
                         }) => {
  return (
    <div
      className={`absolute inset-0 bg-gradient-to-b from-white via-green-50 to-blue-50 transition-transform duration-500 ${
        currentScreen === "login" ? "translate-y-0" : "translate-y-full"
      }`}
    >
      <div className="flex flex-col min-h-screen px-6 py-8">
        {/* Header */}
        <div className="flex items-center justify-between mb-8">
          <button
            onClick={onNavigateBack}
            className="w-10 h-10 bg-white rounded-full flex items-center justify-center shadow-sm hover:shadow-lg transition-all"
          >
            <ArrowRight className="w-5 h-5 text-gray-600 transform rotate-180" />
          </button>
          <div className="w-8 h-8 bg-gradient-to-r from-green-500 to-blue-500 rounded-full flex items-center justify-center">
            <Sprout className="w-4 h-4 text-white" />
          </div>
        </div>

        <div className="flex-1 flex flex-col justify-center">
          <div className="text-center mb-8">
            <h2 className="text-3xl font-bold text-gray-800 mb-2">Welcome Back!</h2>
            <p className="text-gray-600">Continue your growth journey</p>
          </div>

          <div className="space-y-6 mb-8">
            {/* Email */}
            <div className="relative">
              <input
                type="email"
                name="email"
                value={formData.email}
                onChange={onChange}
                placeholder="Your email address"
                className="w-full pl-12 pr-4 py-4 bg-white rounded-2xl border border-gray-200 outline-none text-gray-700 placeholder-gray-400 focus:border-green-400 focus:shadow-lg transition-all"
              />
              <Mail className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-400 w-5 h-5" />
            </div>

            {/* Password */}
            <div className="relative">
              <input
                type={showPassword ? "text" : "password"}
                name="password"
                value={formData.password}
                onChange={onChange}
                placeholder="Your password"
                className="w-full pl-12 pr-12 py-4 bg-white rounded-2xl border border-gray-200 outline-none text-gray-700 placeholder-gray-400 focus:border-green-400 focus:shadow-lg transition-all"
              />
              <Lock className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-400 w-5 h-5" />
              <button
                type="button"
                onClick={onTogglePassword}
                className="absolute right-4 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600 transition-colors"
              >
                {showPassword ? <EyeOff className="w-5 h-5" /> : <Eye className="w-5 h-5" />}
              </button>
            </div>

            {/* Submit */}
            <button
              onClick={onSubmit}
              disabled={isLoading}
              className="w-full py-4 bg-gradient-to-r from-green-500 via-blue-500 to-purple-600 text-white font-semibold rounded-2xl hover:shadow-xl transition-all duration-300 hover:scale-105 active:scale-95 disabled:opacity-70 disabled:cursor-not-allowed disabled:hover:scale-100"
            >
              {isLoading ? (
                <div className="flex items-center justify-center space-x-2">
                  <div className="w-5 h-5 border-2 border-white border-t-transparent rounded-full animate-spin"></div>
                  <span>Growing into your space...</span>
                </div>
              ) : (
                "Continue Growing"
              )}
            </button>
          </div>

          {/* Forgot */}
          <div className="text-center mb-8">
            <button className="text-green-500 hover:text-green-600 font-medium transition-colors">
              Forgot your password?
            </button>
          </div>

          {/* Social */}
          <div className="space-y-4">
            <div className="flex items-center space-x-4">
              <div className="flex-1 h-px bg-gray-300" />
              <span className="text-gray-500 text-sm">Quick access</span>
              <div className="flex-1 h-px bg-gray-300" />
            </div>

            <div className="flex justify-center space-x-4">
              <button className="w-12 h-12 bg-white rounded-2xl border border-gray-200 flex items-center justify-center hover:shadow-lg transition-all hover:scale-110">
                <svg width="22" height="22" viewBox="0 0 24 24" fill="#3C1E1E" aria-hidden="true">
                  <path d="M12 3C6.48 3 2 6.76 2 11.14c0 2.7 1.73 5.07 4.36 6.53l-.86 3.2a.6.6 0 0 0 .9.67l3.56-2.23c.67.1 1.35.15 2.04.15 5.52 0 10-3.76 10-8.14S17.52 3 12 3z"/>
                </svg>
              </button>
              <button className="w-12 h-12 bg-white rounded-2xl border border-gray-200 flex items-center justify-center hover:shadow-lg transition-all hover:scale-110">
                <svg width="22" height="22" viewBox="0 0 24 24" fill="#03C75A" aria-hidden="true">
                  <path d="M5 4h5.6l2.9 4.2V4H19v16h-5.6L10.5 15.8V20H5V4z"/>
                </svg>
              </button>
              <button className="w-12 h-12 bg-white rounded-2xl border border-gray-200 flex items-center justify-center hover:shadow-lg transition-all hover:scale-110">
                <svg width="22" height="22" viewBox="0 0 48 48" aria-hidden="true">
                  <path fill="#FFC107" d="M43.6 20.5H42V20H24v8h11.3C33.8 32.6 29.3 36 24 36c-6.6 0-12-5.4-12-12s5.4-12 12-12c3 0 5.7 1.1 7.7 3l5.7-5.7C34.5 6.1 29.5 4 24 4 12.9 4 4 12.9 4 24s8.9 20 20 20c10 0 19-7.3 19-20 0-1.2-.1-2.3-.4-3.5z"/>
                  <path fill="#FF3D00" d="M6.3 14.7l6.6 4.8C14.8 16 19 13.9 24 13.9c3 0 5.7 1.1 7.7 3l5.7-5.7C34.5 6.1 29.5 4 24 4 16 4 9.2 8.5 6.3 14.7z"/>
                  <path fill="#4CAF50" d="M24 44c5.2 0 10-1.9 13.6-5.2l-6.3-5.2C29.4 35.2 26.9 36 24 36c-5.3 0-9.8-3.4-11.4-8.1l-6.6 5.1C9 39.4 16 44 24 44z"/>
                  <path fill="#1976D2" d="M43.6 20.5H42V20H24v8h11.3c-1 3.2-3.5 5.9-6.7 7.1l6.3 5.2C37.7 38.3 40 32.7 40 28c0-2.6-.5-4.8-1.4-7.5z"/>
                </svg>
              </button>
            </div>
          </div>
        </div>

        {/* Bottom */}
        <div className="text-center pt-4">
          <p className="text-gray-500">
            Ready to start your journey?{" "}
            <button className="text-green-500 hover:text-green-600 font-medium">
              Create your growth account
            </button>
          </p>
        </div>
      </div>
    </div>
  );
};


export default LoginFormScreen