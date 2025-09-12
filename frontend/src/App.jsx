import {Outlet} from "react-router-dom";

function App() {

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="max-w-md mx-auto bg-white min-h-screen relative">
        <main className="px-6 pb-24">
          <Outlet />
        </main>
      </div>
    </div>
  );
}

export default App
