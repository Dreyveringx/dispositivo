export interface Device {
  id?: number;
  name: string;
  description?: string;
  brandId: number;
  deviceTypeId: number;
  releaseDate?: string;
  imageUrl?: string;
  imageUrls?: string[];
  /** Alias para compatibilidad con API que use "images" */
  images?: string[];
}
