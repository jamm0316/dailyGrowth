import React, { useState } from 'react';
import { Plus, Bell, Calendar, Home, Search } from 'lucide-react';

const BottomNav = ({ activeTab = 'home', onTabChange, onAddTask }) => {
  const [tooltip, setTooltip] = useState({ visible: false, id: null });

  const navItems = [
    { id: 'home', icon: Home, disabled: false },
    { id: 'calendar', icon: Calendar, disabled: true },
    { id: 'add', icon: Plus, active: false, isCenter: true, disabled: false },
    { id: 'notifications', icon: Bell, hasNotification: true, disabled: true },
    { id: 'search', icon: Search, disabled: true }
  ];

  const handleClick = (item) => {
    if (item.disabled) {
      // 툴팁 표시
      setTooltip({ visible: true, id: item.id });

      // 5초 후 툴팁 자동 숨김
      setTimeout(() => {
        setTooltip({ visible: false, id: null });
      }, 7000);

      return;
    }

    // 정상 동작
    if (item.isCenter) {
      onAddTask?.();
    } else {
      onTabChange(item.id);
    }
  };

  return (
    <div className="fixed bottom-0 left-0 right-0 bg-white border-t border-gray-200 px-6 py-4 backdrop-blur-sm bg-white/90">
      <div className="flex justify-around items-center max-w-md mx-auto relative">
        {navItems.map((item, index) => {
          const Icon = item.icon;

          // 중앙 + 버튼 특별 스타일링
          if (item.isCenter) {
            return (
              <button
                key={item.id}
                onClick={() => handleClick(item)}
                className="relative -top-4 w-14 h-14 bg-gradient-to-r from-blue-600 to-purple-600 rounded-full flex items-center justify-center shadow-lg hover:shadow-xl transition-all hover:scale-105 active:scale-95"
              >
                <Icon className="w-7 h-7 text-white" />
                {/* 중앙 버튼 백그라운드 원 */}
                <div className="absolute inset-0 rounded-full bg-gradient-to-r from-blue-600 to-purple-600 opacity-20 scale-125 animate-pulse"></div>
              </button>
            );
          }

          const isActive = activeTab === item.id;
          const isDisabled = item.disabled;

          // 일반 네비게이션 버튼들
          return (
            <div key={item.id} className="relative">
              <button
                onClick={() => handleClick(item)}
                //disabled={isDisabled}
                className={`relative p-3 rounded-xl transition-colors ${
                  isDisabled
                    ? 'cursor-not-allowed'
                    : 'hover:bg-gray-100'
                }`}
              >
                <Icon
                  className={`w-6 h-6 ${
                    isDisabled
                      ? 'text-gray-300'
                      : isActive
                        ? 'text-blue-600'
                        : 'text-gray-400'
                  }`}
                />
                {item.hasNotification && !isDisabled && (
                  <div className="absolute top-2 right-2 w-3 h-3 bg-blue-600 rounded-full border-2 border-white"></div>
                )}
              </button>

              {/* 툴팁 */}
              {tooltip.visible && tooltip.id === item.id && (
                <div className="absolute bottom-full left-1/2 -translate-x-1/2 mb-2 px-3 py-2 bg-gray-800 text-white text-xs rounded-lg whitespace-nowrap shadow-lg animate-fade-in">
                  기능을 준비중이에요
                  <div className="absolute top-full left-1/2 -translate-x-1/2 -mt-1 w-0 h-0 border-l-4 border-r-4 border-t-4 border-transparent border-t-gray-800"></div>
                </div>
              )}
            </div>
          );
        })}
      </div>
    </div>
  );
};

export default BottomNav;