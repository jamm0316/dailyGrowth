import React, { useState } from 'react';
import { Plus, Home, Trophy, FolderKanban, User } from 'lucide-react';

const BottomNav = ({ activeTab = 'home', onTabChange, onAddTask }) => {
  const [tooltip, setTooltip] = useState({ visible: false, id: null });

  const navItems = [
    {
      id: 'home',
      icon: Home,
      label: '홈',
      disabled: false
    },
    {
      id: 'challenge',
      icon: Trophy,
      label: '챌린지',
      badge: 'NEW',
      disabled: false
    },
    {
      id: 'add',
      icon: Plus,
      isCenter: true,
      disabled: false
    },
    {
      id: 'mystuff',
      icon: FolderKanban,
      label: '내 프로젝트',
      disabled: false
    },
    {
      id: 'profile',
      icon: User,
      label: '프로필',
      disabled: true // 추후 개발 예정
    }
  ];

  const handleClick = (item) => {
    if (item.disabled) {
      setTooltip({ visible: true, id: item.id });
      setTimeout(() => {
        setTooltip({ visible: false, id: null });
      }, 5000);
      return;
    }

    if (item.isCenter) {
      onAddTask?.();
    } else {
      onTabChange?.(item.id);
    }
  };

  return (
    <nav className="fixed bottom-0 left-0 right-0 bg-white border-t border-gray-200 px-4 py-2 backdrop-blur-sm bg-white/95 z-50">
      <div className="flex justify-around items-center max-w-md mx-auto relative">
        {navItems.map((item) => {
          const Icon = item.icon;

          // 중앙 추가 버튼
          if (item.isCenter) {
            return (
              <button
                key={item.id}
                onClick={() => handleClick(item)}
                className="relative -top-4 w-14 h-14 bg-gradient-to-r from-blue-600 to-purple-600 rounded-full flex items-center justify-center shadow-lg hover:shadow-xl transition-all hover:scale-105 active:scale-95"
                aria-label="할 일 추가"
              >
                <Icon className="w-7 h-7 text-white" />
                <div className="absolute inset-0 rounded-full bg-gradient-to-r from-blue-600 to-purple-600 opacity-20 scale-125 animate-pulse" />
              </button>
            );
          }

          const isActive = activeTab === item.id;
          const isDisabled = item.disabled;

          // 일반 네비게이션 버튼
          return (
            <div key={item.id} className="relative flex-1 flex flex-col items-center">
              <button
                onClick={() => handleClick(item)}
                className={`relative p-2 rounded-xl transition-all ${
                  isDisabled ? 'cursor-not-allowed' : 'hover:bg-gray-100 active:scale-95'
                }`}
                aria-label={item.label}
              >
                <Icon
                  className={`w-6 h-6 transition-colors ${
                    isDisabled
                      ? 'text-gray-300'
                      : isActive
                        ? 'text-blue-600'
                        : 'text-gray-400'
                  }`}
                />

                {/* NEW 뱃지 */}
                {item.badge && !isDisabled && (
                  <span className="absolute -top-0.5 -right-0.5 bg-gradient-to-r from-orange-500 to-red-500 text-white text-[10px] font-bold px-1.5 py-0.5 rounded-full shadow-md">
                    {item.badge}
                  </span>
                )}
              </button>

              {/* 레이블 */}
              <span
                className={`text-[11px] font-medium mt-0.5 transition-colors ${
                  isDisabled
                    ? 'text-gray-300'
                    : isActive
                      ? 'text-blue-600'
                      : 'text-gray-500'
                }`}
              >
                {item.label}
              </span>

              {/* 툴팁 */}
              {tooltip.visible && tooltip.id === item.id && (
                <div className="absolute bottom-full left-1/2 -translate-x-1/2 mb-2 px-3 py-2 bg-gray-800 text-white text-xs rounded-lg whitespace-nowrap shadow-lg animate-fade-in z-10">
                  기능을 준비중이에요
                  <div className="absolute top-full left-1/2 -translate-x-1/2 -mt-1 w-0 h-0 border-l-4 border-r-4 border-t-4 border-transparent border-t-gray-800" />
                </div>
              )}
            </div>
          );
        })}
      </div>
    </nav>
  );
};

export default BottomNav;