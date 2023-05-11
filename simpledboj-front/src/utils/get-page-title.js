import defaultSettings from '@/settings'

const title = defaultSettings.title || 'SDNU SimpleDB OJ'

export default function getPageTitle(pageTitle) {
  if (pageTitle) {
    return `${pageTitle} - ${title}`
  }
  return `${title}`
}
