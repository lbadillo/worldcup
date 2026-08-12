import { useAuth } from '../../context/AuthContext';

function Header() {
  const { currentUser, isAuthenticated, logout } = useAuth();

  return (
    <header className="d-flex flex-column flex-md-row justify-content-between align-items-md-center gap-3 mb-4">
      <div>
        <p className="text-uppercase text-primary fw-semibold small mb-1">
          Mi App
        </p>
        <h1 className="h2 mb-0">Panel principal</h1>
      </div>

      <div className="text-md-end">
        <p className="mb-1 text-body-secondary small">
          Usuario autenticado: {isAuthenticated ? currentUser.role : 'N/A'}
        </p>
        <p className="mb-0 fw-semibold">
          {isAuthenticated ? currentUser.name : 'Sin sesion iniciada'}
        </p>
        {isAuthenticated ? (
          <button
            className="btn btn-outline-danger btn-sm mt-2"
            type="button"
            onClick={logout}
          >
            Salir
          </button>
        ) : null}
      </div>
    </header>
  );
}

export default Header;
