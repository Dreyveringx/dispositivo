/**
 * Base URLs and paths aligned with backend ApiRoutes.
 * One place to change when backend ports or base path change.
 */
const DEVICE_API_BASE = 'http://localhost:8080/api';
const CATALOG_API_BASE = 'http://localhost:8081/api';
const COMMENT_API_BASE = 'http://localhost:8082/api';

export const ApiRoutes = {
  devices: `${DEVICE_API_BASE}/devices`,
  brands: `${CATALOG_API_BASE}/brands`,
  deviceTypes: `${CATALOG_API_BASE}/device-types`,
  comments: `${COMMENT_API_BASE}/comments`,
} as const;
