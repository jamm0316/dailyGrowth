import React, { useCallback, useMemo, useState } from 'react';
import { Trophy, TrendingUp, Users, Sparkles } from 'lucide-react';
import SearchBar from "/src/components/ui/SearchBar.jsx";
import HorizontalProjectScroll from "/src/components/ui/HorizontalProjectScroll.jsx";
import HorizontalTaskScroll from "/src/components/ui/HorizontalTaskScroll.jsx";
import Header from "/src/components/layout/Header.jsx";
import BottomNav from "/src/components/layout/BottomNav.jsx";
import useSummaryProject from "/src/hooks/project/useSummaryProject.jsx";
import useSummaryTask from "/src/hooks/task/useSummaryTask.jsx";
import useTodayTask from "/src/hooks/task/useTodayTask.jsx";
import useSearchProjects from "/src/hooks/project/useSearchProjects.jsx";
import { ROUTES } from "/src/router/routes.js";
import { useNavigate } from "react-router-dom";

const colorMap = {
  1: 'bg-gradient-to-br from-blue-600 to-purple-700',
  2: 'bg-gradient-to-br from-blue-500 to-cyan-500',
  3: 'bg-gradient-to-br from-purple-600 to-pink-600',
  4: 'bg-gradient-to-br from-green-500 to-teal-600',
  5: 'bg-gradient-to-br from-orange-500 to-amber-600',
  6: 'bg-gradient-to-br from-pink-500 to-rose-600',
};

const Dashboard = () => {
  // bottomNav 탭 (home, challenge, calendar, mystuff)
  const [activeBottomTab, setActiveBottomTab] = useState('home');

  // "내 것" 탭 내부의 서브탭 (today, projects, tasks)
  const [myStuffTab, setMyStuffTab] = useState('today');

  // 챌린지 필터
  const [challengeFilter, setChallengeFilter] = useState('all');

  const {
    data: projectData,
    loading: projectLoading,
    error: projectError,
    refetch: refetchProjects
  } = useSummaryProject();

  const {
    data: taskData,
    loading: taskLoading,
    error: taskError,
    refetch: refetchTasks
  } = useSummaryTask();

  const {
    data: todayData,
    loading: todayLoading,
    error: todayError,
    refetch: refetchTodayTasks
  } = useTodayTask();

  const {
    keyword: searchKeyword,
    setKeyword: setSearchKeyword,
    data: searchedProjects,
    loading: searchLoading,
    error: searchError,
    clear: clearSearch,
  } = useSearchProjects("");

  const refetchAllTasks = useCallback(() => {
    refetchTasks();
    refetchTodayTasks();
  }, [refetchTasks, refetchTodayTasks]);

  const handleSearch = (query) => {
    setSearchKeyword(query);
    if (query && query.trim().length > 0) {
      setActiveBottomTab('mystuff');
      setMyStuffTab('projects');
    }
  };

  // 데이터 매핑
  const projectsForCard = useMemo(() => {
    const list = Array.isArray(projectData) ? projectData : [];
    return list
      .filter(p => p.projectType !== 'CHALLENGE')
      .map((p) => ({
        id: p.id,
        title: p.name,
        type: p.visibility,
        date: `${p.startDate} ~ ${p.endDate}`,
        color: colorMap[p.colorId] ?? 'bg-gradient-to-br from-slate-500 to-slate-700',
        progress: typeof p.progress === 'number'
          ? (p.progress <= 1 ? Math.round(p.progress * 100) : Math.round(p.progress))
          : 0,
      }));
  }, [projectData]);

  const tasksForCard = useMemo(() => {
    const list = Array.isArray(taskData) ? taskData : [];
    return list.map((t) => ({
      id: t.id,
      title: t.title,
      status: t.status,
      priority: t.priority,
      dueDate: t.dueDate,
      dayLabel: t.dayLabel,
      color: colorMap[t.colorId] ?? 'bg-gradient-to-br from-slate-500 to-slate-700',
    }));
  }, [taskData]);

  const todayForCard = useMemo(() => {
    const list = Array.isArray(todayData) ? todayData : [];
    return list.map((t) => ({
      id: t.id,
      title: t.title,
      status: t.status,
      priority: t.priority,
      dueDate: t.dueDate,
      dayLabel: t.dayLabel,
      color: colorMap[t.colorId] ?? 'bg-gradient-to-br from-slate-500 to-slate-700',
    }));
  }, [todayData]);

  const joinedChallenges = useMemo(() => {
    const list = Array.isArray(projectData) ? projectData : [];
    return list
      .filter(p => p.originalChallengeId)
      .map((p) => ({
        id: p.id,
        title: p.name,
        category: p.category,
        participants: p.participantCount || 0,
        color: colorMap[p.colorId] ?? 'bg-gradient-to-br from-slate-500 to-slate-700',
        progress: typeof p.progress === 'number'
          ? (p.progress <= 1 ? Math.round(p.progress * 100) : Math.round(p.progress))
          : 0,
      }));
  }, [projectData]);

  const searchedProjectsForCard = useMemo(() => {
    const list = Array.isArray(searchedProjects) ? searchedProjects : [];
    return list.map((p) => ({
      id: p.id,
      title: p.name ?? p.title ?? "(이름 없음)",
      type: p.visibility ?? "",
      date: p.startDate && p.endDate ? `${p.startDate} ~ ${p.endDate}` : "",
      color: colorMap[p.colorId] ?? 'bg-gradient-to-br from-slate-500 to-slate-700',
      progress: typeof p.progress === 'number'
        ? (p.progress <= 1 ? Math.round(p.progress * 100) : Math.round(p.progress))
        : 0,
    }));
  }, [searchedProjects]);

  // 챌린지 카드 컴포넌트
  const ChallengeCard = ({ challenge, joined = false }) => (
    <div
      className={`relative rounded-2xl p-5 ${challenge.color} text-white min-w-[280px] shadow-lg hover:shadow-xl transition-all cursor-pointer`}>
      <div className="flex items-start justify-between mb-3">
        <div className="flex items-center gap-2">
          <Trophy className="w-5 h-5" />
          {challenge.category && (
            <span className="text-xs bg-white/20 px-2 py-1 rounded-full">
              #{challenge.category}
            </span>
          )}
        </div>
        {joined && (
          <span className="text-xs bg-green-400/30 px-2 py-1 rounded-full border border-green-300/50">
            참여중
          </span>
        )}
      </div>

      <h3 className="text-lg font-bold mb-2 line-clamp-2">{challenge.title}</h3>

      <div className="flex items-center justify-between mt-4">
        <div className="flex items-center gap-1.5 text-sm">
          <Users className="w-4 h-4" />
          <span>{challenge.participants || 0}명 참여</span>
        </div>
        {!joined && (
          <button className="bg-white/20 hover:bg-white/30 px-4 py-1.5 rounded-lg text-sm font-medium transition-all">
            참여하기
          </button>
        )}
      </div>

      {joined && (
        <div className="mt-3">
          <div className="flex justify-between text-xs mb-1">
            <span>진행률</span>
            <span>{challenge.progress}%</span>
          </div>
          <div className="w-full bg-white/20 rounded-full h-2">
            <div
              className="bg-white rounded-full h-2 transition-all"
              style={{ width: `${challenge.progress}%` }}
            />
          </div>
        </div>
      )}
    </div>
  );

  // 홈 화면
  const renderHomeTab = () => (
    <div className="space-y-6">
      {/* 오늘 할 일 요약 */}
      <section>
        <div className="flex items-center justify-between mb-3">
          <h2 className="text-xl font-bold text-gray-800 flex items-center gap-2">
            <Sparkles className="w-5 h-5 text-yellow-500" />
            오늘 할 일
          </h2>
          <span className="text-sm text-gray-500">{todayForCard.length}개</span>
        </div>
        {todayLoading ? (
          <div className="py-6 text-gray-500">불러오는 중…</div>
        ) : todayError ? (
          <div className="py-6 text-red-500">😅오늘 할 일을 불러오지 못했어요...</div>
        ) : todayForCard.length === 0 ? (
          <div className="bg-gray-50 rounded-xl p-6 text-center text-gray-500">
            오늘 할 일이 없어요. 여유롭게 보내세요! ☺️
          </div>
        ) : (
          <HorizontalTaskScroll
            tasks={todayForCard.slice(0, 5)}
            title=""
            onUpdate={refetchAllTasks}
          />
        )}
      </section>

      {/* 참여 중인 챌린지 */}
      {joinedChallenges.length > 0 && (
        <section>
          <div className="flex items-center justify-between mb-3">
            <h2 className="text-xl font-bold text-gray-800 flex items-center gap-2">
              <Trophy className="w-5 h-5 text-purple-600" />
              참여 중인 챌린지
            </h2>
            <button
              onClick={() => setActiveBottomTab('challenge')}
              className="text-blue-600 text-sm font-medium hover:text-blue-700"
            >
              전체보기
            </button>
          </div>
          <div className="flex gap-4 overflow-x-auto pb-2 scrollbar-hide">
            {joinedChallenges.slice(0, 3).map((challenge) => (
              <ChallengeCard key={challenge.id} challenge={challenge} joined />
            ))}
          </div>
        </section>
      )}

      {/* 추천 챌린지 */}
      <section>
        <div className="flex items-center justify-between mb-3">
          <h2 className="text-xl font-bold text-gray-800 flex items-center gap-2">
            <TrendingUp className="w-5 h-5 text-orange-500" />
            인기 챌린지
          </h2>
          <button
            onClick={() => setActiveBottomTab('challenge')}
            className="text-blue-600 text-sm font-medium hover:text-blue-700"
          >
            더보기
          </button>
        </div>
        <div className="flex gap-4 overflow-x-auto pb-2 scrollbar-hide">
          {[
            { id: 1, title: '매일 1시간 운동하기', category: '운동', participants: 234, color: colorMap[4] },
            { id: 2, title: '알고리즘 100문제 풀기', category: '코딩', participants: 189, color: colorMap[1] },
          ].map((challenge) => (
            <ChallengeCard key={challenge.id} challenge={challenge} />
          ))}
        </div>
      </section>
    </div>
  );

  // 챌린지 탐색 화면
  const renderChallengeTab = () => (
    <div className="space-y-4">
      {/* 필터 버튼 */}
      <div className="flex gap-2">
        {[
          { id: 'all', label: '전체', icon: Trophy },
          { id: 'trending', label: '인기', icon: TrendingUp },
          { id: 'joined', label: '참여중', icon: Users },
        ].map((filter) => {
          const Icon = filter.icon;
          return (
            <button
              key={filter.id}
              onClick={() => setChallengeFilter(filter.id)}
              className={`flex items-center gap-1.5 px-4 py-2 rounded-xl font-medium transition-all ${
                challengeFilter === filter.id
                  ? 'bg-gradient-to-r from-blue-600 to-purple-600 text-white shadow-lg'
                  : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
              }`}
            >
              <Icon className="w-4 h-4" />
              {filter.label}
            </button>
          );
        })}
      </div>

      {/* 챌린지 카테고리 */}
      <div className="flex gap-2 overflow-x-auto pb-2 scrollbar-hide">
        {['전체', '운동', '공부', '독서', '코딩', '습관'].map((cat) => (
          <button
            key={cat}
            className="px-4 py-1.5 rounded-full bg-white border border-gray-200 text-sm text-gray-700 hover:border-blue-500 hover:text-blue-600 whitespace-nowrap transition-all"
          >
            {cat}
          </button>
        ))}
      </div>

      {/* 챌린지 리스트 */}
      <div className="space-y-3">
        {challengeFilter === 'joined' ? (
          joinedChallenges.length === 0 ? (
            <div className="bg-gray-50 rounded-xl p-8 text-center">
              <Trophy className="w-12 h-12 text-gray-300 mx-auto mb-3" />
              <p className="text-gray-500">아직 참여 중인 챌린지가 없어요</p>
              <p className="text-sm text-gray-400 mt-1">새로운 챌린지를 시작해보세요!</p>
            </div>
          ) : (
            <div className="grid grid-cols-1 gap-3">
              {joinedChallenges.map((challenge) => (
                <ChallengeCard key={challenge.id} challenge={challenge} joined />
              ))}
            </div>
          )
        ) : (
          <div className="grid grid-cols-1 gap-3">
            {[
              { id: 1, title: '매일 1시간 운동하기', category: '운동', participants: 234, color: colorMap[4] },
              { id: 2, title: '알고리즘 100문제 풀기', category: '코딩', participants: 189, color: colorMap[1] },
              { id: 3, title: '하루 30분 독서', category: '독서', participants: 156, color: colorMap[3] },
              { id: 4, title: '새벽 기상 챌린지', category: '습관', participants: 298, color: colorMap[5] },
            ].map((challenge) => (
              <ChallengeCard key={challenge.id} challenge={challenge} />
            ))}
          </div>
        )}
      </div>
    </div>
  );

  // 캘린더 화면 (준비중)
  const renderCalendar = () => (
    <div className="flex items-center justify-center h-96">
      <div className="text-center">
        <div className="text-6xl mb-4">📅</div>
        <h3 className="text-xl font-bold text-gray-800 mb-2">캘린더 뷰</h3>
        <p className="text-gray-500">곧 만나요!</p>
      </div>
    </div>
  );

  // "내 것" 탭 셀렉터 (내부 서브탭용)
  const MyStuffTabSelector = () => (
    <div className="flex gap-2 mb-6 overflow-x-auto scrollbar-hide">
      {[
        { id: 'today', label: '오늘 할 일', count: todayForCard.length },
        { id: 'projects', label: '내 프로젝트', count: projectsForCard.length },
        { id: 'tasks', label: '할일들', count: tasksForCard.length },
      ].map((tab) => (
        <button
          key={tab.id}
          onClick={() => {
            setMyStuffTab(tab.id);
            if (tab.id !== 'projects') clearSearch();
          }}
          className={`relative px-5 py-2.5 rounded-xl font-medium whitespace-nowrap transition-all ${
            myStuffTab === tab.id
              ? 'bg-gradient-to-r from-blue-600 to-purple-600 text-white shadow-lg'
              : 'bg-white text-gray-700 hover:bg-gray-50'
          }`}
        >
          {tab.label}
          {tab.count > 0 && (
            <span className={`ml-1.5 px-2 py-0.5 rounded-full text-xs ${
              myStuffTab === tab.id ? 'bg-white/20' : 'bg-blue-100 text-blue-600'
            }`}>
              {tab.count}
            </span>
          )}
        </button>
      ))}
    </div>
  );

  // "내 것" 화면 (오늘 할일 + 내 프로젝트 + 할일들)
  const renderMyStuff = () => {
    const isSearching = (searchKeyword ?? "").trim().length > 0;

    return (
      <div className="space-y-4">
        {/* 내 것 탭 네비게이션 */}
        <MyStuffTabSelector />

        {/* 내 것 컨텐츠 */}
        <div>
          {myStuffTab === 'today' && (
            <>
              {todayLoading && <div className="py-6 text-gray-500">오늘 할 일 불러오는 중…</div>}
              {todayError && <div className="py-6 text-red-500">😅오늘 할 일을 불러오지 못했어요...</div>}
              {!todayLoading && !todayError && todayForCard.length === 0 ? (
                <div className="bg-gray-50 rounded-xl p-8 text-center">
                  <div className="text-4xl mb-3">🎉</div>
                  <p className="text-gray-600 font-medium">오늘 할 일이 없어요</p>
                  <p className="text-sm text-gray-400 mt-1">여유롭게 보내세요!</p>
                </div>
              ) : (
                <HorizontalTaskScroll
                  tasks={todayForCard}
                  title="Active Tasks"
                  onUpdate={refetchAllTasks}
                />
              )}
            </>
          )}

          {myStuffTab === 'projects' && (
            <>
              {isSearching ? (
                <>
                  {searchLoading && <div className="py-6 text-gray-500">검색 중…</div>}
                  {searchError && <div className="py-6 text-red-500">😅검색 중 오류가 발생했어요: {searchError}</div>}
                  <HorizontalProjectScroll
                    projects={searchedProjectsForCard}
                    title={`검색 결과 (${searchedProjectsForCard.length})`}
                    onUpdate={refetchProjects}
                  />
                </>
              ) : (
                <>
                  {projectLoading && <div className="py-6 text-gray-500">프로젝트 불러오는 중…</div>}
                  {projectError && <div className="py-6 text-red-500">😅프로젝트를 불러오지 못했어요...</div>}
                  <HorizontalProjectScroll
                    projects={projectsForCard}
                    title="ALL Projects"
                    onUpdate={refetchProjects}
                  />
                </>
              )}
            </>
          )}

          {myStuffTab === 'tasks' && (
            <>
              {taskLoading && <div className="py-6 text-gray-500">태스크 불러오는 중…</div>}
              {taskError && <div className="py-6 text-red-500">😅할 일을 불러오지 못했어요...</div>}
              <HorizontalTaskScroll
                tasks={tasksForCard}
                title="ALL Tasks"
                onUpdate={refetchAllTasks}
              />
            </>
          )}
        </div>
      </div>
    );
  };

  // 메인 콘텐츠 렌더링
  const renderMainContent = () => {
    switch (activeBottomTab) {
      case 'home':
        return renderHomeTab();
      case 'challenge':
        return renderChallengeTab();
      case 'calendar':
        return renderCalendar();
      case 'mystuff':
        return renderMyStuff();
      default:
        return renderHomeTab();
    }
  };

  const navigate = useNavigate();

  const handleBottomAddTask = () => {
    navigate(ROUTES.TASK.CREATE);
  };

  const handleBottomTabChange = (id) => {
    setActiveBottomTab(id);
  };

  return (
    <div className="min-h-screen bg-gray-50">
      <Header />

      <main className="px-6 pb-24">
        <SearchBar placeholder="프로젝트, 챌린지 검색" onSearch={handleSearch} />

        {renderMainContent()}
      </main>

      <BottomNav
        activeTab={activeBottomTab}
        onTabChange={handleBottomTabChange}
        onAddTask={handleBottomAddTask}
      />
    </div>
  );
};

export default Dashboard;