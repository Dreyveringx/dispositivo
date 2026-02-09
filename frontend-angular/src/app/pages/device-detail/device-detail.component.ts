import { Component, OnInit, ViewChild, ElementRef, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { DeviceService } from '../../services/device.service';
import { CatalogService } from '../../services/catalog.service';
import { CommentService } from '../../services/comment.service';
import { Device } from '../../models/device.model';
import { Brand } from '../../models/brand.model';
import { DeviceType } from '../../models/device-type.model';
import { Comment } from '../../models/comment.model';

@Component({
  selector: 'app-device-detail',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  template: `
    <a routerLink="/" class="btn btn-outline-secondary mb-3">← Volver al listado</a>

    <div *ngIf="loading" class="text-center py-5">
      <div class="spinner-border text-primary"></div>
    </div>

    <div *ngIf="!loading && device" class="row">
      <div class="col-lg-5 mb-4">
        <!-- Visor principal con zoom y pan -->
        <div #viewerContainer
             class="device-image-viewer border rounded bg-light overflow-hidden position-relative"
             [class.viewer-grab]="!isDragging && zoomLevel > 1"
             [class.viewer-grabbing]="isDragging"
             (wheel)="onViewerWheel($event)"
             (mousedown)="onViewerMouseDown($event)">
          <div class="viewer-inner" [style.transform]="getViewerTransform()">
            <img [src]="selectedImage || placeholderImage"
                 class="device-main-image"
                 [alt]="device.name"
                 (error)="$any($event.target).src = placeholderImage"
                 draggable="false">
          </div>
          <div class="viewer-zoom-controls">
            <button type="button" class="btn btn-light btn-sm shadow-sm" (click)="zoomIn()" title="Acercar">Zoom +</button>
            <button type="button" class="btn btn-light btn-sm shadow-sm" (click)="zoomOut()" title="Alejar">Zoom −</button>
            <button type="button" class="btn btn-light btn-sm shadow-sm" (click)="resetZoom()" title="Restablecer">Reset</button>
          </div>
        </div>
        <!-- Galería de miniaturas -->
        <div class="thumbnails-row mt-2 d-flex flex-wrap gap-2" *ngIf="allImages.length > 0">
          <button type="button"
                  class="thumbnail-btn p-0 border rounded overflow-hidden"
                  [class.thumbnail-active]="selectedImage === url"
                  *ngFor="let url of allImages"
                  (click)="selectImage(url)">
            <img [src]="url" class="thumbnail-img" [alt]="device.name + ' miniatura'" (error)="$any($event.target).src = placeholderImage">
          </button>
        </div>
      </div>
      <div class="col-lg-7">
        <h1 class="h2">{{ device.name }}</h1>
        <p class="text-muted" *ngIf="brandName">Marca: {{ brandName }}</p>
        <p class="text-muted" *ngIf="typeName">Tipo: {{ typeName }}</p>
        <p class="text-muted" *ngIf="device.releaseDate">Fecha de lanzamiento: {{ device.releaseDate | date:'longDate' }}</p>
        <div class="mt-3" *ngIf="device.description">
          <h5>Descripción</h5>
          <p class="text-body">{{ device.description }}</p>
        </div>
      </div>
    </div>

    <hr class="my-5" *ngIf="device">

    <section *ngIf="device">
      <h4 class="mb-3">Comentarios ({{ comments.length }})</h4>

      <div class="card mb-4">
        <div class="card-body">
          <form (ngSubmit)="submitComment()" #f="ngForm">
            <div class="mb-2">
              <input type="text" class="form-control" placeholder="Tu nombre" [(ngModel)]="newComment.author" name="author" required>
            </div>
            <div class="mb-2">
              <textarea class="form-control" rows="3" placeholder="Escribe tu comentario..." [(ngModel)]="newComment.text" name="text" required></textarea>
            </div>
            <button type="submit" class="btn btn-primary" [disabled]="f.invalid || submitting">Enviar comentario</button>
          </form>
        </div>
      </div>

      <div class="comment-card card mb-2" *ngFor="let c of comments">
        <div class="card-body py-3">
          <strong>{{ c.author }}</strong>
          <span class="text-muted small ms-2">{{ c.createdAt | date:'short' }}</span>
          <p class="mb-0 mt-1">{{ c.text }}</p>
        </div>
      </div>
      <p *ngIf="comments.length === 0" class="text-muted">Aún no hay comentarios. ¡Sé el primero!</p>
    </section>
  `,
  styles: [`
    .device-image-viewer {
      aspect-ratio: 4/3;
      max-height: 400px;
      display: flex;
      align-items: center;
      justify-content: center;
      user-select: none;
    }
    .device-image-viewer.viewer-grab {
      cursor: grab;
    }
    .device-image-viewer.viewer-grabbing {
      cursor: grabbing;
    }
    .viewer-inner {
      transform-origin: center center;
      max-width: 100%;
      max-height: 100%;
      display: flex;
      align-items: center;
      justify-content: center;
    }
    .device-main-image {
      max-width: 100%;
      max-height: 400px;
      width: auto;
      height: auto;
      object-fit: contain;
      display: block;
    }
    .viewer-zoom-controls {
      position: absolute;
      bottom: 8px;
      right: 8px;
      display: flex;
      gap: 6px;
    }
    .thumbnails-row .thumbnail-btn {
      width: 64px;
      height: 64px;
      cursor: pointer;
      flex-shrink: 0;
      transition: box-shadow 0.2s, border-color 0.2s;
      background: #fff;
    }
    .thumbnails-row .thumbnail-btn:hover {
      border-color: var(--bs-primary) !important;
      box-shadow: 0 0 0 2px rgba(13, 110, 253, 0.25);
    }
    .thumbnails-row .thumbnail-btn.thumbnail-active {
      border-color: var(--bs-primary) !important;
      border-width: 2px;
      box-shadow: 0 0 0 2px rgba(13, 110, 253, 0.4);
    }
    .thumbnail-img {
      width: 100%;
      height: 100%;
      object-fit: cover;
      display: block;
    }
  `],
})
export class DeviceDetailComponent implements OnInit {
  device: Device | null = null;
  brandName = '';
  typeName = '';
  comments: Comment[] = [];
  newComment: Comment = { deviceId: 0, author: '', text: '' };
  loading = false;
  submitting = false;

  @ViewChild('viewerContainer') viewerContainer?: ElementRef<HTMLElement>;

  readonly placeholderImage = 'https://via.placeholder.com/400x300?text=Sin+imagen';
  selectedImage = '';
  zoomLevel = 1;
  translateX = 0;
  translateY = 0;
  isDragging = false;
  private startX = 0;
  private startY = 0;
  private startTranslateX = 0;
  private startTranslateY = 0;
  private readonly minZoom = 0.5;
  private readonly maxZoom = 3;
  private readonly zoomStep = 0.25;

  get allImages(): string[] {
    if (!this.device) return [];
    const urls = this.device.imageUrls ?? this.device.images ?? [];
    if (urls.length) return urls;
    if (this.device.imageUrl) return [this.device.imageUrl];
    return [];
  }

  constructor(
    private route: ActivatedRoute,
    private deviceService: DeviceService,
    private catalogService: CatalogService,
    private commentService: CommentService,
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (!id) return;
    this.loading = true;
    this.deviceService.getById(id).subscribe({
      next: (d) => {
        this.device = d;
        this.newComment.deviceId = d.id!;
        const images = d.imageUrls ?? d.images ?? (d.imageUrl ? [d.imageUrl] : []);
        this.selectedImage = images.length ? images[0] : '';
        this.zoomLevel = 1;
        this.translateX = 0;
        this.translateY = 0;
        if (d.brandId) {
          this.catalogService.getBrandById(d.brandId).subscribe((b) => (this.brandName = b.name));
        }
        if (d.deviceTypeId) {
          this.catalogService.getDeviceTypeById(d.deviceTypeId).subscribe((t) => (this.typeName = t.name));
        }
        this.loadComments();
        this.loading = false;
      },
      error: () => (this.loading = false),
    });
  }

  loadComments(): void {
    if (!this.device?.id) return;
    this.commentService.getByDeviceId(this.device.id).subscribe((list) => (this.comments = list));
  }

  getViewerTransform(): string {
    return `translate(${this.translateX}px, ${this.translateY}px) scale(${this.zoomLevel})`;
  }

  onViewerWheel(e: WheelEvent): void {
    e.preventDefault();
    e.stopPropagation();
    if (e.deltaY < 0) {
      this.zoomLevel = Math.min(this.maxZoom, this.zoomLevel + this.zoomStep);
    } else {
      this.zoomLevel = Math.max(this.minZoom, this.zoomLevel - this.zoomStep);
    }
    this.applyBoundaries();
  }

  onViewerMouseDown(e: MouseEvent): void {
    if (e.button !== 0 || this.zoomLevel <= 1) return;
    this.isDragging = true;
    this.startX = e.clientX;
    this.startY = e.clientY;
    this.startTranslateX = this.translateX;
    this.startTranslateY = this.translateY;
  }

  @HostListener('document:mousemove', ['$event'])
  onDocumentMouseMove(e: MouseEvent): void {
    if (!this.isDragging) return;
    this.translateX = this.startTranslateX + (e.clientX - this.startX);
    this.translateY = this.startTranslateY + (e.clientY - this.startY);
    this.applyBoundaries();
  }

  @HostListener('document:mouseup')
  onDocumentMouseUp(): void {
    this.isDragging = false;
  }

  private applyBoundaries(): void {
    const el = this.viewerContainer?.nativeElement;
    if (!el) return;
    const w = el.offsetWidth;
    const h = el.offsetHeight;
    const maxX = Math.max(0, (w * (this.zoomLevel - 1)) / 2);
    const maxY = Math.max(0, (h * (this.zoomLevel - 1)) / 2);
    this.translateX = Math.max(-maxX, Math.min(maxX, this.translateX));
    this.translateY = Math.max(-maxY, Math.min(maxY, this.translateY));
  }

  selectImage(imageUrl: string): void {
    this.selectedImage = imageUrl;
    this.zoomLevel = 1;
    this.translateX = 0;
    this.translateY = 0;
  }

  zoomIn(): void {
    this.zoomLevel = Math.min(this.maxZoom, this.zoomLevel + this.zoomStep);
    this.applyBoundaries();
  }

  zoomOut(): void {
    this.zoomLevel = Math.max(this.minZoom, this.zoomLevel - this.zoomStep);
    this.applyBoundaries();
  }

  resetZoom(): void {
    this.zoomLevel = 1;
    this.translateX = 0;
    this.translateY = 0;
  }

  submitComment(): void {
    if (!this.device?.id || !this.newComment.author?.trim() || !this.newComment.text?.trim()) return;
    this.submitting = true;
    this.commentService.create(this.newComment).subscribe({
      next: () => {
        this.newComment = { deviceId: this.device!.id!, author: '', text: '' };
        this.loadComments();
        this.submitting = false;
      },
      error: () => (this.submitting = false),
    });
  }
}
