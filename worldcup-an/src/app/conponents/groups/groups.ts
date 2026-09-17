import { Component, inject } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { GroupService } from '../../services/group';

@Component({
  standalone: true,
  selector: 'app-groups',
  styleUrl: './groups.css',
  templateUrl: './groups.html',
})
export class GroupsComponent {
  private readonly groupService = inject(GroupService);

  // Exposición directa de la signal httpResource para la plantilla
  readonly groupsResource = this.groupService.groupsResource;

  isUnauthorized(): boolean {
    const error = this.groupsResource.error();
    return error instanceof HttpErrorResponse && error.status === 401;
  }

  redirectToLogin(): void {
    this.groupService.loginWithGoogle();
  }
}
