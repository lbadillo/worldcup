import { useEffect, useState } from 'react';
import { getGroups } from '../../services/groupService';
import { CircleFlag } from 'react-circle-flags';
import './Group.css';

const Group = () => {
  const [selectedTeam, setSelectedTeam] = useState(null);
  const [groups, setGroups] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadGroups();
  }, []);

  const loadGroups = async () => {
    console.log('Fetching groups...');
    try {
      const data = await getGroups();
      setGroups(data);
    } catch (error) {
      console.error('Error fetching groups:', error);
    } finally {
      setLoading(false);
    }
  };
  const handleTeamClick = (team) => {
    setSelectedTeam(selectedTeam?.id === team.id ? null : team);
  };

  if (loading) {
    return (
      <div className="text-center mt-5">
        <div className="spinner-border text-primary" role="status">
          <span className="visually-hidden">Loading...</span>
        </div>
      </div>
    );
  }

  return (
    <div className=" world-cup-bootstrap">
      <div className="text-center mb-4">
        <h1 className="display-6 fw-bold text-primary mb-2">World Cup Teams</h1>
        <p className="lead text-secondary mb-0">
          Groups and teams participating in the World Cup.
        </p>
      </div>

      <div className="row g-4">
        {groups.map((group) => (
          <div key={group.id} className="col-12 col-lg-4">
            <div className="card h-100 border-0 shadow-sm">
              <div className="card-header bg-primary text-white py-3">
                <h2 className="h5 mb-0 text-center">{group.name}</h2>
              </div>
              <div className="card-body p-0">
                <div className="list-group list-group-flush">
                  {group.teams.map((team) => (
                    <button
                      key={team.id}
                      type="button"
                      className={`list-group-item list-group-item-action d-flex align-items-center justify-content-between py-3 px-3 team-button ${selectedTeam?.id === team.id ? 'active' : ''}`}
                      onClick={() => handleTeamClick(team)}
                    >
                      <div className="d-flex align-items-center gap-2">
                        <span
                          className="team-flag-icon"
                          role="img"
                          aria-label={`${team.name} flag`}
                        >
                          {team.countryCode ? (
                            <CircleFlag
                              countryCode={team.countryCode}
                              height="24"
                            />
                          ) : (
                            <i
                              className="bi bi-flag-fill"
                              aria-hidden="true"
                            ></i>
                          )}
                        </span>
                        <span className="fw-semibold">{team.name}</span>
                      </div>
                      <div className="d-flex align-items-center gap-2">
                        <span
                          className="badge rounded-pill text-bg-success"
                          title="Victorias"
                        >
                          <i className="bi bi-check-circle me-1"></i>
                          {team.wins}
                        </span>
                        <span
                          className="badge rounded-pill text-bg-warning"
                          title="Empates"
                        >
                          <i className="bi bi-dash-circle me-1"></i>
                          {team.draws}
                        </span>
                        <span
                          className="badge rounded-pill text-bg-danger"
                          title="Derrotas"
                        >
                          <i className="bi bi-x-circle me-1"></i>
                          {team.losses}
                        </span>
                      </div>
                    </button>
                  ))}
                </div>
              </div>
            </div>
          </div>
        ))}
      </div>

      {selectedTeam && (
        <div
          className="alert alert-light border shadow-sm mt-4 mb-0"
          role="alert"
        >
          <div className="d-flex flex-column flex-md-row align-items-md-center justify-content-between gap-3">
            <div>
              <h3 className="h6 text-uppercase text-primary fw-bold mb-1">
                Equipo seleccionado
              </h3>
              <p className="mb-0 fs-5 fw-semibold">
                {selectedTeam.flag} {selectedTeam.name}
              </p>
            </div>
            <button className="btn btn-primary btn-sm px-4">
              Hacer apuesta
            </button>
          </div>
        </div>
      )}
    </div>
  );
};

export default Group;
