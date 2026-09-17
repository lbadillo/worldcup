import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { GroupsComponent } from './conponents/groups/groups';

@Component({
  imports: [RouterOutlet, GroupsComponent],
  selector: 'app-root',
  styleUrl: './app.css',
  templateUrl: './app.html',
})
export class App {
  protected readonly title = signal('worldcup-an');
}
