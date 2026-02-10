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
  templateUrl: './device-detail.component.html',
  styleUrls: ['./device-detail.component.css'],
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
