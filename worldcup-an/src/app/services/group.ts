import { Service } from '@angular/core';
import { httpResource } from '@angular/common/http';
import { Group } from '../models/group.model';

@Service()
export class GroupService {
  private readonly apiUrl = 'http://localhost:8080/api/groups';
  readonly groupsResource = httpResource<Group[]>(() => ({
    url: this.apiUrl,
    withCredentials: true,
  }));

  loginWithGoogle(): void {
    window.location.href = 'http://localhost:8080/api/oauth2/authorization/google';
  }
}
