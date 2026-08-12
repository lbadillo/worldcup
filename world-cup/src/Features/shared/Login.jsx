import { useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { defaultByRole } from '../../utils/MainTabs';

const initialForm = {
  name: '',
  email: '',
  role: '',
};

const ALLOWED_ROLES = ['user', 'admin', 'manager'];

function Login() {
  const { currentUser, isAuthenticated, login, logout } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const [formData, setFormData] = useState(initialForm);
  const [error, setError] = useState('');

  const handleChange = ({ target }) => {
    const { name, value } = target;
    const normalizedValue = name === 'role' ? value.toLowerCase() : value;

    setFormData((previous) => ({
      ...previous,
      [name]: normalizedValue,
    }));
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    const redirectTo =
      location.state?.from?.pathname || defaultByRole.get(formData.role);

    if (
      !formData.name.trim() ||
      !formData.email.trim() ||
      !formData.role.trim()
    ) {
      setError('values required: name, email, role');
      return;
    }

    if (!ALLOWED_ROLES.includes(formData.role.trim().toLowerCase())) {
      setError('Role invalido. Solo se permite: user, admin o manager.');
      return;
    }

    login(formData);
    setFormData(initialForm);
    setError('');
    navigate(redirectTo, { replace: true });
  };

  return (
    <section className="row justify-content-center py-4">
      <div className="col-12 col-md-8 col-lg-5">
        <div className="card border-0 shadow-sm rounded-4">
          <div className="card-body p-4 p-lg-5">
            <p className="text-uppercase text-primary fw-semibold small mb-2">
              Acceso
            </p>
            <h1 className="h3 mb-3">Iniciar sesion</h1>
            <p className="text-body-secondary mb-4">
              Usa este formulario para guardar el usuario autenticado en el
              contexto global.
            </p>

            {isAuthenticated ? (
              <div className="d-grid gap-3">
                <div className="bg-light rounded-4 p-3 border">
                  <h2 className="h5 mb-1">Usuario actual</h2>
                  <p className="mb-1 fw-semibold">{currentUser.name}</p>
                  <p className="mb-0 text-body-secondary">
                    {currentUser.email}
                  </p>
                </div>

                <button
                  className="btn btn-outline-danger"
                  type="button"
                  onClick={logout}
                >
                  Cerrar sesion
                </button>
              </div>
            ) : (
              <form className="d-grid gap-3" onSubmit={handleSubmit}>
                <label className="form-label mb-0">
                  <span className="d-block mb-2">Nombre</span>
                  <input
                    className="form-control"
                    type="text"
                    name="name"
                    value={formData.name}
                    onChange={handleChange}
                    placeholder="Ingresa tu nombre"
                  />
                </label>

                <label className="form-label mb-0">
                  <span className="d-block mb-2">Correo</span>
                  <input
                    className="form-control"
                    type="email"
                    name="email"
                    value={formData.email}
                    onChange={handleChange}
                    placeholder="nombre@correo.com"
                  />
                </label>

                <label className="form-label mb-0">
                  <span className="d-block mb-2">Role</span>
                  <input
                    className="form-control"
                    type="text"
                    name="role"
                    value={formData.role}
                    onChange={handleChange}
                    list="allowed-roles"
                    placeholder="user, manager, admin"
                  />
                  <datalist id="allowed-roles">
                    <option value="user" />
                    <option value="admin" />
                    <option value="manager" />
                  </datalist>
                </label>

                {error ? (
                  <p className="text-danger small mb-0">{error}</p>
                ) : null}

                <button className="btn btn-primary" type="submit">
                  Ingresar
                </button>
              </form>
            )}
          </div>
        </div>
      </div>
    </section>
  );
}

export default Login;
