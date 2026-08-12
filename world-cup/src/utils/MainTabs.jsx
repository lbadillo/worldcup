import Match from '../Features/Match/Match';
import Group from '../Features/Group/Group';
import Home from '../Features/Home/Home';
import Setup from '../Features/Setup/Setup';
import User from '../Features/User/User';
import CreateGroup from '../Features/CreateGroup/CreateGroup';
import CreateMatch from '../Features/CreateMatch/CreateMatch';

export const MainTabs = [
  {
    id: 'home',
    label: 'Home',
    target: '/home',
    component: <Home />,
    iconId: 'bi bi-house-door',
    public: true,
    roles: ['admin', 'user'],
  },
  {
    id: 'setup',
    label: 'Setup',
    target: '/setup',
    component: <Setup />,
    iconId: 'bi bi-gear',
    public: false,
    roles: ['admin'],
  },
  {
    id: 'user',
    label: 'User',
    target: '/user',
    component: <User />,
    iconId: 'bi bi-people-fill',
    public: false,
    roles: ['admin'],
  },
  {
    id: 'teams',
    label: 'Teams',
    target: '/teams',
    component: <Group />,
    iconId: 'bi bi-box',
    public: false,
    roles: ['admin', 'user'],
  },
  {
    id: 'matches',
    label: 'Matches',
    target: '/matches',
    component: <Match />,
    iconId: 'bi bi-calendar3',
    public: false,
    roles: ['admin', 'user'],
  },
  {
    id: 'create-group',
    label: 'Create Group',
    target: '/create-group',
    component: <CreateGroup />,
    iconId: 'bi bi-folder-plus',
    public: false,
    roles: ['admin', 'manager'],
  },
  {
    id: 'create-match',
    label: 'Create Match',
    target: '/create-match',
    component: <CreateMatch />,
    iconId: 'bi bi-calendar-plus',
    public: false,
    roles: ['admin', 'manager'],
  },
];

export const defaultByRole = new Map([
  ['admin', '/setup'],
  ['user', '/teams'],
  ['manager', '/create-group'],
]);
