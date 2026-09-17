export interface Team {
  id: number;
  name: string;
  flag: string;
  wins: number;
  draws: number;
  losses: number;
  points: number;
}

export interface Group {
  id: number;
  name: string;
  teams: Team[];
}
