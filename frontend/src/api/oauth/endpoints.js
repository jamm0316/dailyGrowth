const VERSION = 'v1';
const DOMAIN = 'oauth'
export const ENDPOINTS = {
  LOGIN: (provider) => `${VERSION}/${DOMAIN}/login/${provider}`,
  ME: `${VERSION}/${DOMAIN}/me`,
}