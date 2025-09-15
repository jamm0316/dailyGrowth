import {defineConfig} from 'vite'
import react from '@vitejs/plugin-react'
import tailwind from '@tailwindcss/vite'
export default defineConfig({
  plugins: [react(), tailwind()],
  server: {
    //    host: true, // 0.0.0.0 바인딩 → 외부 접속 허용
    //   allowedHosts: ['0b1715f11995.ngrok-free.app'], // Vite 5+ 외부 호스트 허용
    //   hmr: {
    //     host: '0b1715f11995.ngrok-free.app',
    //     protocol: 'wss',
    //     clientPort: 443,
    //   },
    proxy: {
      // 프론트 1개 ngrok만 사용 → /api는 로컬 백엔드로 프록시
      '/api': {
        target: 'http://127.0.0.1:8080',
        changeOrigin: true,
        secure: false,
        // 백엔드가 /api 없이 받으면 주석 해제:
        // rewrite: (p) => p.replace(/^\/api/, ''),
      },
    },
  },
})
