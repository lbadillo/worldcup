import { NavLink } from 'react-router-dom';
import './Navigation.css';

function Navigation({ tabs = [] }) {
  return (
    <ul
      className="nav nav-pills nav-fill gap-2 p-1 small bg-primary rounded-5 shadow-sm"
      style={{
        '--bs-nav-link-color': 'var(--bs-white)',
        '--bs-nav-pills-link-active-color': 'var(--bs-primary)',
        '--bs-nav-pills-link-active-bg': 'var(--bs-white)',
      }}
    >
      {tabs.map((tab) => (
        <li className="nav-item" key={tab.id}>
          <NavLink
            to={tab.target}
            className={({ isActive }) =>
              `nav-link nav-tab-link rounded-5 ${isActive ? 'active' : ''}`
            }
          >
            <i
              className={
                tab.iconId
                  ? tab.iconId
                  : tab.iconId
                    ? tab.iconId
                    : 'bi bi-alarm'
              }
              aria-hidden="true"
            ></i>
            <span>{tab.label}</span>
          </NavLink>
        </li>
      ))}
    </ul>
  );
}

export default Navigation;
